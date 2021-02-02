package com.vedas.spectrocare.BLEConnectionModule;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.spectrochips.spectrumsdk.FRAMEWORK.TestFactors;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.Controllers.ScreeningServerObjectDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.PatientModule.CollectSampleActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MyPatientActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Abhilash on 12/4/2018.
 */

public class HtmltoPdfConversionActivity extends Activity {
    Bitmap bitmap;
    boolean boolean_save;
    WebView webView;
    TextView export, cancel;
    LinearLayout ll_pdflayout;
    RefreshShowingDialog refreshShowingDialog;
    ImageView back;
    TextView toolText;
    String yesno;
    String htmlContentString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmlconverttopdf);
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        refreshShowingDialog= new RefreshShowingDialog(HtmltoPdfConversionActivity.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            yesno = extras.getString("key");
            Log.e("valueeeeeee", "" + yesno);
        }
        init();
        exportpdf();
    }

    private void init() {
        toolText = (TextView) findViewById(R.id.toolbar_text);
        toolText.setText("Test Report");
        back = (ImageView) findViewById(R.id.toolbar_icon);//Spectrum
        back.setBackgroundResource(R.drawable.icon_ionic_ios_arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("backing", "call");
                finish();
            }
        });
        cancel = (TextView) findViewById(R.id.cancel);//Spectrum
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("backing", "call");
                finish();
            }
        });


        LinearLayout ll = (LinearLayout) findViewById(R.id.rl);
        webView = (WebView) findViewById(R.id.wv);
        export = (TextView) findViewById(R.id.export);
        ll_pdflayout = (LinearLayout) findViewById(R.id.ll_pdflayout);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HtmltoPdfConversionActivity.this, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Upload");
                builder.setMessage("Do you want to upload as screening record ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshShowingDialog.showAlert();
                        bitmap = loadBitmapFromView(ll_pdflayout, ll_pdflayout.getWidth(), ll_pdflayout.getHeight());
                        createPdf();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
    public Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PhysicalServerObjectDataController.MessageEvent event) {
        Log.e("sidemenuMessageevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("addScreening")) {
           refreshShowingDialog.dismiss();
           finish();
           startActivity(new Intent(getApplicationContext(), MyPatientActivity.class));
        }
    }
    public void exportpdf() {
        try {
            htmlContentString = "<html>\n" +
                    "    <head>\n" +
                    "        <title>SpectroChip</title>\n" +
                    "    </head>\n" +
                    "    <body style=\"font-family: arial;height: 100%;width: 100%;padding: 0;margin: 0\">\n" +
                    "        <table id=\"customers\" border=\"0\" cellspacing=\"0\" cellpadding=\"0px\" width=\"100%\" style=\"font-family: &quot;Trebuchet MS&quot;, Arial, Helvetica, sans-serif;display: inline-table;border-collapse: collapse;width: 100%;margin-left: 0;margin-top: 0px;margin-right: 0px;margin-bottom: 15px\">\n" +
                    "            <thead style=\"font-family: arial\">\n" +
                    "                <tr bgcolor=\"#0074d9\" style=\"font-family: arial;background-color: #f2f2f2\">\n" +
                    "                    <th colspan=\"0\" cellpadding=\"0\" style=\"padding: 5px;margin-left: 40px;border: none;color: white;font-size: 25px;font-family: arial;padding-top: 22px;padding-bottom: 22px;text-align: center;background-color: #3F51B5\">SpectroCare Test Report</th>\n" +
                    "                </tr>\n" +
                    "            </thead>\n" +
                    "        </table>\n" +
                    "        <div class=\"mainWrapper\" style=\"font-family: arial;width: 800px;margin: 0 auto\">\n" +
                    "            <div class=\"part1\" style=\"font-family: arial;width: 800px;margin: 0 auto;height: 150px\">\n" +
                    "                <div id=\"subDiv1\" style=\"font-family: arial;width: 300px;float: left;height: 150px\">\n" +
                    "                    <figure style=\"font-family: arial\">\n" +
                    "                        <img src=\"ic_slogo.png\" alt=\"\" style=\"font-family: arial;margin-left: 1px;position: relative;height: 100px;width: 200px;top: 1px;left: 2px\"/>\n" +
                    "                    </figure>\n" +
                    "                </div>\n" +
                    "                <div id=\"subDiv2\" style=\"font-family: arial;width: 350px;float: right;text-align: right;line-height: 1.8;height: 150px\">\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\"> SpectroChip, Inc</b></span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\">Phone: 03-552-0892</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\">No.951, Fuxing Rd., Zhubei City,</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\">Hsinchu County 302, Taiwan (R.O.C.)</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\">www.spectrochips.com</span><br style=\"font-family: arial\"/>\n" +
                    "                </div>\n" +
                    "            </div> <!-- Part one Ends here -->\n" +
                    "            <div class=\"clear\" style=\"font-family: arial\"> <hr style=\"font-family: arial\"/> </div>\n" +
                    "            \n" +
                    "            <div class=\"part2\" style=\"font-family: arial;width: 960px;margin-top: 20px\">\n" +
                    "                <div id=\"p2sub1\" style=\"font-family: arial;line-height: 1.5;margin-left: 20px;width: 31%;height: 150px;float: left\">\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Date:</b>#DATE#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Name:</b>#PATIENT_NAME#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Height:</b>#Height#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Fasting:</b>#Fasting#</span><br style=\"font-family: arial\"/>\n" +
                    "                </div>\n" +
                    "                <div id=\"p2sub2\" style=\"font-family: arial;line-height: 1.5;margin-left: 20px;width: 31%;height: 150px;float: left\">\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Time:</b>#TIME#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Gender:</b>#GENDER#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Weight:</b>#Weight#</span><br style=\"font-family: arial\"/>\n" +
                    "                </div>\n" +
                    "                <div id=\"p2sub3\" style=\"font-family: arial;line-height: 1.5;margin-left: 20px;width: 31%;float: right;height: 150px\">\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Blood Type:</b>#Blood#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">Age:</b>#AGE#</span><br style=\"font-family: arial\"/>\n" +
                    "                    <span style=\"font-family: arial\"><b style=\"font-family: arial\">BMI:</b>#Bmi#</span><br style=\"font-family: arial\"/>\n" +
                    "                </div>\n" +
                    "                <div class=\"picGrp\" style=\"font-family: arial;width: 800px;text-align: right\">\n" +
                    "                    <span style=\"font-family: arial\"><img src=\"happy.png\"\" alt=\"\" style=\"font-family: arial;margin-left: 60px;position: relative;height: 30px;width: 30px;top: 6px;left: 2px\"/>:Normal </span>\n" +
                    "                    <span style=\"font-family: arial\"><img src=\"sad.png\" alt=\"\" style=\"font-family: arial;margin-left: 60px;position: relative;height: 30px;width: 30px;top: 6px;left: 2px\"/> :Abnormal</span>\n" +
                    "                </div>\n" +
                    "            </div> <!-- Part 2 ends here -->\n" +
                    "            \n" +
                    "            <div class=\"part3\" style=\"font-family: arial\">\n" +
                    "                <table border=\"1\" cellspacing=\"0\" cellpadding=\"10px\" width=\"800px\" style=\"font-family: arial;margin-top: 20px;margin-left: 40px;float: right;text-align: center;font-weight: 600\">\n" +
                    "                    <thead style=0opuyi8uio0y\"font-family: arial\">\n" +
                    "                        <tr bgcolor=\"#0074d9\" style=\"font-family: arial\">\n" +
                    "                            <th colspan=\"5\" cellpadding=\"10\" style=\"padding: 15px;border: none;color: white;font-size: 25px;font-family: arial\">Urine Test Report</th>\n" +
                    "                        </tr>\n" +
                    "                    </thead>\n" +
                    "                    <tbody style=\"font-family: arial\">\n" +
                    "                        <tr style=\"font-family: arial\">\n" +
                    "                            <th style=\"font-family: arial\">Test Item</th>\n" +
                    "                            <th style=\"font-family: arial\">Value</th>\n" +
                    "                            <th style=\"font-family: arial\">Result</th>\n" +
                    "                            <th style=\"font-family: arial\">Flag</th>\n" +
                    "                            <th style=\"font-family: arial\">Health Reference Ranges</th>\n" +
                    "                        </tr>\n" +
                    "                        #TEST_FACTORS#\n" +
                    "                    </tbody>\n" +
                    "                </table>\n" +
                    "            </div>\n" +
                    "            <h6 style=\"float: right\"> Page 1 of 1</h6>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>\n" +
                    "\n";

            preparePDF();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;
        int convertHighet = (int) hight, convertWidth = (int) width;
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 100, 100, paint);
        document.finishPage(page);

        PatientlProfileModel adminMember = PatientProfileDataController.getInstance().currentPatientlProfile;
        PatientModel adminMember1 = PatientLoginDataController.getInstance().currentPatientlProfile;

        String name = "";
        if(adminMember1 !=null) {
           name= PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName();
        }
        if(adminMember !=null) {
            name= PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName();
        }
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/spectrum4.0/");
        dir.mkdir();
        String filename = name + ".pdf";
        final File file = new File(dir, filename);
        /*FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(enterText.getText().toString().getBytes());
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            document.writeTo(new FileOutputStream(file));
            export.setText("Upload");
            if(file != null) {
                ScreeningServerObjectDataController.getInstance().addScreeningCallForAttchmet(file, file.getName(), "From testing");
            }  /*export.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(String.valueOf(file));
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                }
            });*/
            boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    public void preparePDF() {
        PatientlProfileModel adminMember = PatientProfileDataController.getInstance().currentPatientlProfile;
        PatientModel adminMember1 = PatientLoginDataController.getInstance().currentPatientlProfile;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToStr = format.format(today);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss a");
        String strDate = mdformat.format(calendar.getTime());
        String h = "170 Cm";
        String w = "60 Kg";
        String heightStr = h.substring(0, h.length() - 3);
        String weightStr = w.substring(0, w.length() - 3);
        Log.e("height", "" + heightStr);
        Log.e("weight", "" + weightStr);


        //Get the user values from the fnwidget reference
        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr) / 100;

        //Calculate BMI value
        float bmiValue = calculateBMI(weight, height);

        double angle = bmiValue;
        DecimalFormat df = new DecimalFormat("##.00");
        String bmivalue = df.format(angle);
        Log.e("bmivalue", "" + bmivalue);

        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/htm/");
        dir.mkdir();
        File file = new File(dir, "myTest.html");
        String pathToReportHTMLTemplate = "/htm/myTest.html";
        Log.e("pathToReport", "" + pathToReportHTMLTemplate);

       if(adminMember1 !=null){
           renderTestReport(file,
                   dateToStr,
                   adminMember1.getFirstName(),
                   h,
                   yesno,
                   w,
                   "AB+",
                   bmivalue,
                   strDate,
                   adminMember1.getGender(),
                   adminMember1.getAge());
       }
        if(adminMember !=null){
            renderTestReport(file,
                    dateToStr,
                    adminMember.getFirstName(),
                    h,
                    yesno,
                    w,
                    "AB+",
                    bmivalue,
                    strDate,
                    adminMember.getGender(),
                    adminMember.getAge());
        }
    }

    public String renderTestReport(File htmlPath, String date, String patientName, String height, String fasting, String weight, String bloodtype, String bmi, String time, String gender, String age) {
        try {
            String isFasting = "Yes";
            // Replace all the placeholders with real values except for the items.
            boolean objFasting = true;
            if (objFasting) {
                isFasting = "Yes";
            } else {
                isFasting = "No";
            }
            // Date
            htmlContentString = htmlContentString.replace("#DATE#", date);

            // Patient Name
            htmlContentString = htmlContentString.replace("#PATIENT_NAME#", patientName);

            //Height
            htmlContentString = htmlContentString.replace("#Height#", height);

            //Fasting
            htmlContentString = htmlContentString.replace("#Fasting#", isFasting);


            // Time
            htmlContentString = htmlContentString.replace("#TIME#", time);

            // Gender
            htmlContentString = htmlContentString.replace("#GENDER#", gender);


            //We-ght
            htmlContentString = htmlContentString.replace("#Weight#", weight);

            //Blood type
            htmlContentString = htmlContentString.replace("#Blood#", bloodtype);

            // AGE
            htmlContentString = htmlContentString.replace("#AGE#", age);

            //BMI
            htmlContentString = htmlContentString.replace("#Bmi#", bmi);

            StringBuilder testFactorshtmlString = new StringBuilder();

            for (TestFactors objTestFactor : CollectSampleActivity.testResults) {

                String oneItemsHtmlString = "<tr style=\"font-family: arial\">\n" +
                        "                <td style=\"font-family: arial\">" + "#TEST_NAME#" + "</td>\n" +
                        "                <td style=\"font-family: arial\">" + "#TEST_VALUE#" + "</td>\n" +
                        "                <td style=\"font-family: arial\">" + "#RANGE_LINE#" + "</td>\n" +
                        "                <td style=\"font-family: arial\">#IMAGE_LINK#" +
                        "              </td>\n" +
                        "                <td style=\"font-family: arial\"> " + "#REFERENCE_RANGE#" + " mg/dL</td>\n" +
                        "            </tr>";

                oneItemsHtmlString = oneItemsHtmlString.replace("#TEST_NAME#", objTestFactor.getTestname());
                oneItemsHtmlString = oneItemsHtmlString.replace("#TEST_VALUE#", objTestFactor.getValue() + " " + objTestFactor.getUnits());
                oneItemsHtmlString = oneItemsHtmlString.replace("#RANGE_LINE#", objTestFactor.getResult());
                oneItemsHtmlString = oneItemsHtmlString.replace("#IMAGE_LINK#", "<img src=\"happy.png\">");
               /* if (objTestFactor.ge()){
                    oneItemsHtmlString = oneItemsHtmlString.replace("#IMAGE_LINK#", "<img src=\"ic_happy.png\">");
                }
                else{
                    oneItemsHtmlString = oneItemsHtmlString.replace("#IMAGE_LINK#", "<img src=\"ic_sad.png\">");
                }*/
                oneItemsHtmlString = oneItemsHtmlString.replace("#REFERENCE_RANGE#", objTestFactor.getReferenceRange());
                //  testFactorshtmlString.a(oneItemsHtmlString);
                testFactorshtmlString.append(oneItemsHtmlString);
            }
            htmlContentString = htmlContentString.replace("#TEST_FACTORS#", testFactorshtmlString);
            File file = new File("htm/myTest.html");

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                os.write(htmlContentString.getBytes());
                os.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            webView.loadDataWithBaseURL("file:///android_asset/", htmlContentString, "text/html", "utf-8", "");

            BufferedWriter write = new BufferedWriter(new FileWriter(htmlPath));
            write.write(htmlContentString);
            write.close();
            refreshShowingDialog.dismiss();
            return htmlContentString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Calculate BMI
    private float calculateBMI(float weight, float height) {
        return (float) (weight / (height * height));
    }


    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("dsdsds", "Permission is granted1");
                return true;
            } else {

                Log.v("dsdsds", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("dsdsdsd", "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("dsdssd", "Permission is granted2");
                return true;
            } else {

                Log.v("dsdsd", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("dsdsdsds", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("dsdsds", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("dsdsds", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // downloadPdfFile();
                } else {
                    //  progress.dismiss();
                }
                break;

            case 3:
                Log.d("dsdsdds", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("sddsds", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // SharePdfFile();
                } else {
                    // progress.dismiss();
                }
                break;
        }
    }

    /*private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }*/
}
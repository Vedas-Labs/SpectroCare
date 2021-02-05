
package com.vedas.spectrocare.PatientModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCConnectionHelper;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCTestAnalysis;
import com.spectrochips.spectrumsdk.FRAMEWORK.SpectroDeviceDataController;
import com.spectrochips.spectrumsdk.FRAMEWORK.TestFactors;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.TestFactorDataController;
import com.vedas.spectrocare.DataBase.UrineResultsDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CollectSampleActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_start,btnCalender,btnClock;
    RelativeLayout calLayout,clockLayout;
    TextView txtTime,txtDate;
    Calendar calendar;
    CalendarView calendarView;
    View mView;
    public RefreshShowingDialog testDialogue;
    public static ArrayList<TestFactors> testResults = new ArrayList<>();
    RefreshShowingDialog abortDialogue;
    RadioButton yes , no;
    boolean isFasting =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_sample);
        testDialogue = new RefreshShowingDialog(CollectSampleActivity.this);
        abortDialogue=new RefreshShowingDialog(CollectSampleActivity.this/*,"Aborting..."*/);
        loadIds();
        activateNotification();
        accessInterfaceMethods();
    }
    public void activateNotification() {
        SCConnectionHelper.getInstance().activateScanNotification(new SCConnectionHelper.ScanDeviceInterface() {
            @Override
            public void onSuccessForConnection(String msg) {
                Log.e("onSuccessForConnection", "call");
            }
            @Override
            public void onSuccessForScanning(final ArrayList<BluetoothDevice> devcies, boolean msg) {
                Log.e("onSuccessForScanning", "call");
            }

            @Override
            public void onFailureForConnection(String error) {
                Log.e("onFailureForConnection", "call");
            }
            @Override
            public void uartServiceClose(String error) {
            }
        });
    }
    private void loadIds(){
        txtTime = findViewById(R.id.txt_time);
        txtDate = findViewById(R.id.txt_cal);
        rl_back=findViewById(R.id.back);
        btnCalender = findViewById(R.id.btn_calender);
        btnClock = findViewById(R.id.btn_clock);
        clockLayout = findViewById(R.id.layout_time);
        calLayout = findViewById(R.id.rl_one);
        btn_start=findViewById(R.id.start);
        yes=findViewById(R.id.radio_yes);
        no=findViewById(R.id.radio_no);

        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isFasting=true;
                }else{
                    isFasting=false;
                }
                Log.e("isFasting","call"+isFasting);
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isFasting=false;
                    Log.e("isFasting","call"+isFasting);
                }else{
                    isFasting=true;
                }
            }
        });

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnalyzealert();
            }
        });
        getCurrentDateTime();
    }
    private void getCurrentDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Calendar calobj = Calendar.getInstance();
        System.out.println(df.format(calobj.getTime()));
        String dateTime=df.format(calobj.getTime());
        String array[]=dateTime.split(" ");
        txtDate.setText(array[0]);
        txtTime.setText(array[1]+" "+array[2]);
    }
    public void showAbortDialog(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        RelativeLayout main=(RelativeLayout)dialog.findViewById(R.id.rl_main);
        RelativeLayout main1=(RelativeLayout)dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abortDialogue.showAlert();
                SCTestAnalysis.getInstance().abortTesting(new SCTestAnalysis.AbortInterface() {
                    @Override
                    public void onAbortForTesting(boolean isabort) {
                        if (isabort) {
                            dialog.dismiss();
                            abortDialogue.hideRefreshDialog();
                            finish();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void showAnalyzealert(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_analyze);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnAbort = (Button) dialog.findViewById(R.id.btn_abort);
        GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorOrange));

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SCConnectionHelper.getInstance().isConnected) {
                    activateNotification();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// for stopping screen off while in testing
                    // testDialogue.showAlert();
                    SCTestAnalysis.getInstance().startTestAnalysis(new SCTestAnalysis.TeststaResultInterface() {
                        @Override
                        public void onSuccessForTestComplete(final ArrayList<TestFactors> results, String msg) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    testResults=results;
                                    Log.e("testdata", "call" + testResults.size());
                                    // testDialogue.hideRefreshDialog();
                                    dialog.dismiss();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            testDialogue.showAlert();
                                            loadAddUrineResultsApi(testResults);
                                        }
                                    }, 2000 * 1);
                                }
                            });
                        }

                        @Override
                        public void onFailureForTesting(String error) {
                            Log.e("onFailureForTesting", "call");
                            testDialogue.hideRefreshDialog();
                            performTestFailedFunction();
                        }
                    });
                } else {
                    // Toast.makeText(getApplicationContext(), "Device not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        },1000);
        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAbortDialog();
            }
        });
        dialog.show();
    }
    private void performTestFailedFunction() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CollectSampleActivity.this);
        alertDialogBuilder.setMessage("No strip holder was detected. Please try again")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activateNotification();
                        // testDialogue.showAlert();
                        SCTestAnalysis.getInstance().performTryAgainFunction();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SCTestAnalysis.getInstance().performTestCancelFunction();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));

        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#FF0012"));
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            loadResultsDataTODb(testResults,jsonObject.getString("testReportNumber"));
                        }else{
                            testDialogue.hideRefreshDialog();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                testDialogue.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    String currentTime = String.valueOf(System.currentTimeMillis() / 1000L);
    String testID="test"+String.valueOf(getRandomInteger(1000,10));
    public  int getRandomInteger(int maximum, int minimum)
    {
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }
    private void loadAddUrineResultsApi(ArrayList<TestFactors> testResults) {
       PatientModel objModel= PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject urineParams = new JSONObject();
        JSONArray testITemsArray = new JSONArray();
        try {
            if(testResults.size()>0){
                for(int i = 0; i<testResults.size(); i++){
                    TestFactors obj=testResults.get(i);
                    JSONObject testItem = new JSONObject();
                    testItem.put("testName", obj.getTestname());
                    testItem.put("unit",obj.getUnits() );
                    testItem.put("value", obj.getValue());
                    testItem.put("flag",obj.isFlag() );
                    testItem.put("healthReferenceRanges",obj.getReferenceRange() );
                    testItem.put("result", obj.getResult());
                    testITemsArray.put(testItem);
                }
            }
            Log.e("testitemObjectentire", "call" + testITemsArray.toString());
            Log.e("isFasting", "call" +isFasting);
            Log.e("testid", "call" +testID);

            urineParams.put("testID",testID);
            urineParams.put("categoryID", "category"+String.valueOf(getRandomInteger(100,1)));
            urineParams.put("latitude", "0.0");
            urineParams.put("longitude", "0.0");
            urineParams.put("isFasting", isFasting);
            urineParams.put("sampleCollectedOn", currentTime);
            urineParams.put("testedTime", currentTime);
            urineParams.put("testedPersonName", objModel.getFirstName()+" "+objModel.getLastName());

            urineParams.put("byWhom", "patient");
            urineParams.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            urineParams.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            urineParams.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            urineParams.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            urineParams.put("testFactors", testITemsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(urineParams.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addUriineResultApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "add");
    }

    public void loadResultsDataTODb(ArrayList<TestFactors> testResults,String testReportNumber) {
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        DecimalFormat df = new DecimalFormat("#.##");

        UrineresultsModel objResult=new UrineresultsModel();
        objResult.setTestReportNumber(testReportNumber);
        objResult.setTestType("Urine");
        objResult.setLatitude("0.0");
        objResult.setLongitude("0.0");
        objResult.setTestedTime(currentTime);
        Log.e("dbfasting", "call" +String.valueOf(isFasting));
        objResult.setIsFasting(String.valueOf(isFasting));
        objResult.setRelationtype("Patient");
        objResult.setTest_id(testID);
        objResult.setPatientId(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        objResult.setPatientModel(PatientLoginDataController.getInstance().currentPatientlProfile);

        if (UrineResultsDataController.getInstance().insertUrineResultsForMember(objResult)) {
            for (int index = 0; index < testResults.size(); index++) {
                TestFactors object =testResults.get(index);
                com.vedas.spectrocare.DataBaseModels.TestFactors objTest = new com.vedas.spectrocare.DataBaseModels.TestFactors();
                objTest.setFlag(object.isFlag());
                objTest.setUnit(object.getUnits());
                objTest.setHealthReferenceRanges(object.getReferenceRange());
                objTest.setTestName(object.getTestname());
                objTest.setResult(object.getResult());
                objTest.setValue(object.getValue());
                objTest.setUrineresultsModel(UrineResultsDataController.getInstance().currenturineresultsModel);
                if (TestFactorDataController.getInstance().insertTestFactorResults(objTest)) {

                }
            }
            testDialogue.hideRefreshDialog();
            startActivity(new Intent(getApplicationContext(), ResultPageViewActivity.class));
        }
    }
}

/*
package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import java.util.Calendar;

public class CollectSampleActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_start,btnCalender,btnClock;
    RelativeLayout calLayout,clockLayout;
    TextView txtTime,txtCal;
    Button btnOk;
    int i,k,j;
    Calendar calendar;
    TextView txtHours,txtMin,txtAm,txtPm;
    CalendarView calendarView;
    String hours,minits;
    PopupWindow popUp;
    View mView;
    ImageView imgHrUp,imgHrDown,imgMinUp,imgMinDwn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_sample);
        loadIds();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              startActivity(new Intent(getApplicationContext(),ResultPageViewActivity.class));
            }
        }, 5000);
    }
    private void loadIds(){
        txtTime = findViewById(R.id.txt_time);
        txtCal = findViewById(R.id.txt_cal);
        rl_back=findViewById(R.id.back);
        btnCalender = findViewById(R.id.btn_calender);
        btnClock = findViewById(R.id.btn_clock);
        clockLayout = findViewById(R.id.layout_time);
        calLayout = findViewById(R.id.rl_one);
        btn_start=findViewById(R.id.start);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnalyzealert();
            }
        });
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = LayoutInflater.from(CollectSampleActivity.this).inflate(R.layout.clock_poup_up_layout, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(clockLayout);
                imgHrUp = mView.findViewById(R.id.img_arrow_up);
                imgHrDown = mView.findViewById(R.id.img_arrow_down);
                imgMinUp = mView.findViewById(R.id.img_up_arrow);
                imgMinDwn = mView.findViewById(R.id.img_dwn_arrow);
                txtHours = mView.findViewById(R.id.txt_hour);
                txtMin = mView.findViewById(R.id.txt_minit);
                btnOk = mView.findViewById(R.id.btn_ok);
                txtAm = mView.findViewById(R.id.txt_am);
                txtPm = mView.findViewById(R.id.txt_pm);

                clockItemsClickListners();

            }
        });
        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                mView = LayoutInflater.from(CollectSampleActivity.this).inflate(R.layout.popup_calender_view, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(calLayout);
                calendarView = mView.findViewById(R.id.calendar_view);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.YEAR, 1);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                        txtCal.setText(msg);
                        popUp.dismiss();
                    }
                });


            }
        });
    }
    public void clockItemsClickListners(){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clockView.setVisibility(View.GONE);
                String hour = txtHours.getText().toString();
                String minit = txtMin.getText().toString();
                String time = hour+" : "+minit;
                txtTime.setText(time);
                popUp.dismiss();

            }
        });
        txtAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);

            }
        });
        txtPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);
            }
        });
        imgMinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(minits);

                if (k == 55){
                    k=0;
                    txtMin.setText(String.valueOf(k));
                }else{
                    i = k+5;
                    txtMin.setText(String.valueOf(i));
                }

            }
        });
        imgMinDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(hours);

                if (k==0){
                    k=55;
                    txtMin.setText(String.valueOf(k));
                }else{
                    i = k-5;
                    txtMin.setText(String.valueOf(i));
                }
            }
        });

        imgHrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                j = Integer.parseInt(minits);

                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k == 12){
                    k=0;
                    i = k+1;
                    txtHours.setText(String.valueOf(i));
                }else{
                    i = k+1;
                    txtHours.setText(String.valueOf(i));
                }

            }
        });
        imgHrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k==1){
                    k=12;
                    txtHours.setText(String.valueOf(k));
                }else{
                    i = k-1;
                    txtHours.setText(String.valueOf(i));
                }
            }
        });

    }

    public void showAbortDialog(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        RelativeLayout main=(RelativeLayout)dialog.findViewById(R.id.rl_main);
        RelativeLayout main1=(RelativeLayout)dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void showAnalyzealert(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_analyze);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnAbort = (Button) dialog.findViewById(R.id.btn_abort);
        GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorOrange));

        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAbortDialog();
            }
        });
        dialog.show();


    }
}
*/

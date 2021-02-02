package com.vedas.spectrocare.BLEConnectionModule;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spectrochips.spectrumsdk.FRAMEWORK.SCConnectionHelper;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCTestAnalysis;
import com.spectrochips.spectrumsdk.FRAMEWORK.SpectroDeviceDataController;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.PatientModule.CollectSampleActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FileViewControllerActivity extends AppCompatActivity {
    private ArrayList<SCFile> filesArray = new ArrayList<SCFile>();
    int selectedPosition = -1;
    devicesAdapter adapter;
    RecyclerView recyclerView;
    TextView tool_text;
    RefreshShowingDialog refreshShowingDialog,syncingdialogue;
    public static boolean isFromFile=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        tool_text= findViewById(R.id.tool_txt);
        tool_text.setText("Json Files");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        SpectroDeviceDataController.getInstance().fillContext(getApplicationContext());
        ApiCallDataController.getInstance().fillContent(getApplicationContext());
        syncingdialogue = new RefreshShowingDialog(FileViewControllerActivity.this,"Syncing..");
        refreshShowingDialog=new RefreshShowingDialog(FileViewControllerActivity.this,"Configuring..");
       /* refreshShowingDialog.showAlert();
        DoctorInfoServerObjectDataController.getInstance().loadJson();*/
        isFromFile=true;

        SCFile scFile=new SCFile();
        scFile.setFilename("no5_3518_urineTest.json");
        scFile.setAddedDate("");
        scFile.setCategory("Urine");
        filesArray.add(scFile);
        accessInterfaceMethods();
        loadRecyclerView();
    }
    private void accessInterfaceMethods() {
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
               // Toast.makeText(getApplicationContext(),"Device DisConnected",Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            @Override
            public void uartServiceClose(String error) {
            }
        });
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetchDoctor")) {
                    refreshShowingDialog.dismiss();
                    Log.e("fetchDoctor", "call");
                    try {
                        String res=jsonObject.getString("response");
                        JSONArray array=jsonObject.getJSONArray("files");
                        Log.e("filesarray", "" + array.length());
                        filesArray.clear();
                        for (int i=0;i<array.length();i++){
                            JSONObject obj= (JSONObject) array.get(i);
                            SCFile scFile=new SCFile();
                            scFile.setAddedDate(obj.getString("addedDate"));
                            scFile.setFilename(obj.getString("filename"));
                            scFile.setId(obj.getString("id"));
                            scFile.setCategory(obj.getString("category"));
                            filesArray.add(scFile);
                        }
                        isFromFile=false;
                        refreshShowingDialog.dismissDialogue();
                        loadRecyclerView();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.dismiss();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadRecyclerView() {
        adapter = new devicesAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        RelativeLayout back =  findViewById(R.id.home);
        ImageView refresh = (ImageView) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(true);
                adapter.notifyDataSetChanged();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(false);
                isFromFile=false;
                finish();
            }
        });
    }
    public class devicesAdapter extends RecyclerView.Adapter<devicesAdapter.ViewHolder> {
        Context ctx;
        public devicesAdapter(Context ctx) {
            this.ctx = ctx;
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            ImageView image;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_spinner);
               // image = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
            }
        }
        @Override
        public devicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_text, parent, false);
            return new devicesAdapter.ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final devicesAdapter.ViewHolder holder, final int position) {
            SCFile device=filesArray.get(position);
            holder.name.setText(device.getFilename());
            holder.name.setTypeface(Typeface.DEFAULT_BOLD);
            holder.name.setTextColor(Color.parseColor("#53B9c6"));
            if(selectedPosition==position){
                showAlert(filesArray.get(selectedPosition).getFilename(), filesArray.get(selectedPosition).getCategory(), filesArray.get(selectedPosition).getAddedDate());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            if (filesArray.size()>0) {
                return filesArray.size();
            }else {
                return 0;
            }
        }
    }
    private void showAlert(final String jsonFiles, final String category, final String date) {
        String msg="Would you like to configure the 'XXXXXXXX' into the device?";
        String str1 = msg.replaceFirst("'XXXXXXXX'", jsonFiles);
        System.out.println(str1);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Configure File");
        alertDialogBuilder.setMessage(str1)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      /*  refreshShowingDialog.showAlert();
                        SCTestAnalysis.getInstance().fillContext(getApplicationContext());
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(SCTestAnalysis.getInstance().spectroDeviceObject);
                        try {
                            JSONObject request = new JSONObject(jsonString);
                            Log.e("totalobj","call"+request.toString());
                            SpectroDeviceDataController.getInstance().processJsonData(request);
                            refreshShowingDialog.hideRefreshDialog();
                            loadTestprocess();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e("ioexception","call");
                            e.printStackTrace();
                        }*/
                        refreshShowingDialog.dialogue();
                        SCTestAnalysis.getInstance().fillContext(getApplicationContext());
                        getDeviceSettings(jsonFiles, category, date, new SCTestAnalysis.JsonFileInterface() {
                           @Override
                           public void onSuccessForConfigureJson() {
                               refreshShowingDialog.dismissDialogue();
                               loadTestprocess();
                           }

                           @Override
                           public void onFailureForConfigureJson(String bitmapList) {
                               Log.e("onFailureForJson", "call");

                           }
                       });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadTestprocess() {
     runOnUiThread(new Runnable() {
            public void run() {
                syncingdialogue.dialogue();
                SCTestAnalysis.getInstance().startTestProcess(new SCTestAnalysis.SyncingInterface() {
                    @Override
                    public void isSyncingCompleted(boolean isSync) {
                        if (isSync) {
                            SCTestAnalysis.getInstance().showMessage("Syncing Done.");
                        } else {
                            SCTestAnalysis.getInstance().showMessage("Syncing Failed.");
                        }
                    }
                    @Override
                    public void gettingDarkSpectrum(boolean isgetting) {
                        if (isgetting){
                            syncingdialogue.dismissDialogue();
                            isFromFile=false;
                            Log.e("gettingDarkSpectrum", "calll");
                            Intent i = new Intent(getApplicationContext(), CollectSampleActivity.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });

    }
    public void getDeviceSettings(String testName, String category, String date, SCTestAnalysis.JsonFileInterface jsonFileInterface1) {
       SCTestAnalysis.getInstance().jsonFileInterface=jsonFileInterface1;
       setupTestParameters();
    }
    public void setupTestParameters() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String val[]= PatientMedicalRecordsController.getInstance().selectedTestItem.getConfigFile().split("/");
                    String v=val[val.length-1];
                    Log.e("asdadda","call"+v);//http://34.231.177.197:3000/api/
                    String url="http://34.231.177.197:3000/TestItemFiles/"+v;
                    String response =   sendGET(url);
                    if(response != null){
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            SpectroDeviceDataController.getInstance(). processJsonData(jsonObject);
                            Log.e("setupTestParameters", "calll"+jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (SCTestAnalysis.getInstance().jsonFileInterface!=null){
                                SCTestAnalysis.getInstance().jsonFileInterface.onFailureForConfigureJson("failed");
                            }
                        }
                    }
                    return "Success";
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return "";
                }
            };

        }.execute("");
    }
    private String USER_AGENT = "Mozilla/5.0";
    private String sendGET(String url) throws IOException {
        StringBuffer response = null;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
             response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
            SCTestAnalysis.getInstance().jsonFileInterface.onFailureForConfigureJson("Fail to Configure");

        }
        return response.toString();
    }
    public void openFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        Intent i = Intent.createChooser(intent, "File");
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    if (path.contains(".json")) {
                        Log.e("path and file name", "call" + path + "ffff" + uri.getPath());
                        ReadFile(uri);
                    } else {
                        Toast.makeText(getApplicationContext(), "PLEASE_SELECT_CORRECT_JSON_FILE_KEY", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public void ReadFile(Uri uri) {
        BufferedReader reader = null;
        try {
            // open the user-picked file for reading:
            InputStream in = getContentResolver().openInputStream(uri);
            // now read the content:
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            // Do something with the content in
            final JSONObject jsonObj = new JSONObject(builder.toString());
            processJsonDataFromLOcalFIle(jsonObj, new SCTestAnalysis.JsonFileInterface() {
                @Override
                public void onSuccessForConfigureJson() {
                    Log.e("dataobject", "call" + jsonObj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncingdialogue.dismiss();
                            SCTestAnalysis.getInstance().startTestProcess(new SCTestAnalysis.SyncingInterface() {
                                @Override
                                public void isSyncingCompleted(boolean isSync) {
                                }
                                @Override
                                public void gettingDarkSpectrum(boolean isgetting) {
                                    if (isgetting){
                                        Log.e("isgetting", "call"+isgetting);
                                        syncingdialogue.dismissDialogue();
                                    }
                                }
                            });
                        }
                    });
                }
                @Override
                public void onFailureForConfigureJson(String bitmapList) {
                    Log.e("onFailure", "call" + bitmapList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshShowingDialog.dismissDialogue();
                            Toast.makeText(getApplicationContext(), "Fail to Configure Json.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
           /* Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), InstructionpageViewController.class));
                }
            }, 3000);*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void processJsonDataFromLOcalFIle(JSONObject json, SCTestAnalysis.JsonFileInterface jsonFileInterface1){
        if (json != null) {
            try {
                SCTestAnalysis.getInstance().jsonFileInterface=jsonFileInterface1;
                SpectroDeviceDataController.getInstance().processJsonData(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFromFile=false;
    }
}

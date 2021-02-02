package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientServerApiModel.ChatRoomMessageModel;
import com.vedas.spectrocare.PatientServerApiModel.ChatRoomParticipantModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.patientModuleAdapter.DoctorMessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientSearchDoctorActivity extends AppCompatActivity {
    RecyclerView doctorsListView;
    DoctorMessageAdapter doctorMessageAdapter;
    ImageView imgBack;
    RefreshShowingDialog refreshShowingDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText ed_search;
    public ArrayList<ArrayList<ChatRoomMessageModel>> sortedRoomMessageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_search_doctor);
        refreshShowingDialog = new RefreshShowingDialog(getApplicationContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorOrange);
        ed_search =findViewById(R.id.edt_search);
        casting();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                accessInterfaceMethod();
                fetchChatRooomMessagesDetails();
            }
        });
        loadEdittext();
    }

    public void casting() {
        imgBack = findViewById(R.id.img_back_arrow);
        doctorsListView = findViewById(R.id.doctors_list_view);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        doctorMessageAdapter = new DoctorMessageAdapter(PatientSearchDoctorActivity.this,PatientMedicalRecordsController.getInstance().chatRoomMessageList);
        doctorsListView.setAdapter(doctorMessageAdapter);
        doctorsListView.setLayoutManager(new LinearLayoutManager(PatientSearchDoctorActivity.this));
    }
    private void loadEdittext() {
        ed_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    //  filterArray(s.toString().toLowerCase());
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().length() > 0) {
                    filterArray(s.toString().toLowerCase());
                }else {
                    Log.e("0length", "call" );
                    doctorMessageAdapter = new DoctorMessageAdapter(PatientSearchDoctorActivity.this,PatientMedicalRecordsController.getInstance().chatRoomMessageList);
                    doctorsListView.setAdapter(doctorMessageAdapter);
                    doctorMessageAdapter.notifyDataSetChanged();
                }

            }
        });
    }
    private void filterArray(String val) {
        ArrayList<ChatRoomMessageModel> tempdsList = new ArrayList<>();
        sortedRoomMessageList.clear();
        Log.e("zzzz", "call"+val+PatientMedicalRecordsController.getInstance().chatRoomMessageList.size() );

        for (int i = 0; i <PatientMedicalRecordsController.getInstance().chatRoomMessageList.size(); i++) {
            ArrayList<ChatRoomMessageModel> model = PatientMedicalRecordsController.getInstance().chatRoomMessageList.get(i);
            //for(int k=0;k<model.size();k++){
            Log.e("matchcalled", "call"+model.get(model.size()-1).getDoctorName()+"xxx"+val );
            String name=model.get(model.size()-1).getDoctorName().trim();
            String name1=name.replace("Dr. ","");
                if (name1.trim().toLowerCase().startsWith(val)) {
                    Log.e("match", "call" );
                    tempdsList.add(model.get(model.size()-1));
                    sortedRoomMessageList.add(tempdsList);
                }
            //}
        }
        Log.e("filterArray", "call" +tempdsList.size());
        doctorMessageAdapter = new DoctorMessageAdapter(PatientSearchDoctorActivity.this,sortedRoomMessageList);
        doctorsListView.setAdapter(doctorMessageAdapter);
        doctorMessageAdapter.notifyDataSetChanged();
    }
    private AppointmentArrayModel loadDoctorsDetails(String roomID) {
        Log.e("zzzzz","cll"+roomID);
        if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
            for (int i = 0; i < PatientAppointmentsDataController.getInstance().allappointmentsList.size(); i++) {
                AppointmentArrayModel model = PatientAppointmentsDataController.getInstance().allappointmentsList.get(i);
                Log.e("model","cll"+model.getAppointmentDetails().getAppointmentID());
                if (roomID.equals(model.getAppointmentDetails().getAppointmentID())) {
                    return model;
                }
            }
        }
        return null;
    }
    private void fetchChatRooomMessagesDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("userID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.getChatRooms(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchRooms");
    }
    public void accessInterfaceMethod() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
               // refreshShowingDialog.hideRefreshDialog();
                try {
                    if (jsonObject.getString("response").equals("3")) {
                        PatientMedicalRecordsController.getInstance().chatRoomMessageList.clear();
                        if (opetation.equals("fetchRooms")) {
                            Log.e("fetchRooms", "length" + jsonObject.toString());
                            JSONArray chatList = jsonObject.getJSONArray("chatList");
                            Log.e("chatList", "length" + chatList.length());
                            mSwipeRefreshLayout.setRefreshing(false);
                            for (int i = 0; i < chatList.length(); i++) {

                                JSONObject object = chatList.getJSONObject(i);
                                String roomID=object.getString("roomID");
                                Log.e("roomID", "length" + roomID);

                                Gson gson = new Gson();
                                String doctorName = null, doctorProfile = null;

                                JSONObject jsonString = object.getJSONObject("participant");
                                ChatRoomParticipantModel chatRoomParticipantModel = new ChatRoomParticipantModel();
                                chatRoomParticipantModel = gson.fromJson(jsonString.toString(), ChatRoomParticipantModel.class);
                                doctorName = chatRoomParticipantModel.getName();
                                doctorProfile = ServerApi.img_home_url + chatRoomParticipantModel.getProfilePic();

                                JSONArray messageList = object.getJSONArray("messages");

                                ArrayList<ChatRoomMessageModel> tempList = new ArrayList<>();

                                for(int k=0;k<messageList.length();k++){
                                    JSONObject jsonString1 = messageList.getJSONObject(k);
                                    ChatRoomMessageModel chatRoomMessageModel=new ChatRoomMessageModel();
                                    chatRoomMessageModel = gson.fromJson(jsonString1.toString(), ChatRoomMessageModel.class);
                                    chatRoomMessageModel.setRoomID(roomID);

                                    chatRoomMessageModel.setDoctorName("Dr. " +doctorName );
                                    chatRoomMessageModel.setProfile(doctorProfile);
                                    /*AppointmentArrayModel appointmentArrayModel=loadDoctorsDetails(chatRoomMessageModel.getRoomID());
                                    chatRoomMessageModel.setDoctorName("Dr. "+appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                                            appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getLastName());
                                    chatRoomMessageModel.setProfile(ServerApi.img_home_url + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic());
*/
                                    tempList.add(chatRoomMessageModel);
                                    Log.e("xxxxxxxx", "length" + chatRoomMessageModel.getRoomID());
                                }
                                Log.e("tempList", "length" + i+tempList.size());
                                PatientMedicalRecordsController.getInstance().chatRoomMessageList.add(i,tempList);
                            }
                            doctorMessageAdapter = new DoctorMessageAdapter(PatientSearchDoctorActivity.this,PatientMedicalRecordsController.getInstance().chatRoomMessageList);
                            doctorsListView.setAdapter(doctorMessageAdapter);
                            doctorMessageAdapter.notifyDataSetChanged();
                            Log.e("all", "length" + PatientMedicalRecordsController.getInstance().chatRoomMessageList.size());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
              //  refreshShowingDialog.hideRefreshDialog();
            }
        });
    }

}

package com.vedas.spectrocare.PatientNotificationModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.MessageModel;
import com.vedas.spectrocare.PatientChat.MessagesListModel;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.PatientServerApiModel.InboxNotificationModel;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.SelectUserActivity;
import com.vedas.spectrocare.patientModuleAdapter.PatientNotificationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientNotificationFragment extends Fragment {
    View notificationView;
    TextView txtToday;
    ImageView deleteAll;
    ArrayList<MessageModel> messageList;
    RecyclerView todayNotification, yesterdayNotification;
    PatientNotificationAdapter notificationAdapter;
    RefreshShowingDialog refreshShowingDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean isRefresh = true;
    ArrayList<InboxNotificationModel> listOfChat;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Socket mSocket;
    public PatientNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientNotificationFragment newInstance(String param1, String param2) {
        PatientNotificationFragment fragment = new PatientNotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notificationView = inflater.inflate(R.layout.fragment_patient_notification, container, false);
        notificationAdapter = new PatientNotificationAdapter(getContext());
        refreshShowingDialog = new RefreshShowingDialog(getContext());
        messageList = new ArrayList<>();
        mSocket = SocketIOHelper.getInstance().socket;
        SocketIOHelper.getInstance().listenEvents();
        mSocket.on("getRoomMessages", onNewMessage);
        accessInterfaceMethods();
        fetchAppointmentDetails();
        inboxMessagesFetchApi();
        casting();
        todayNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        todayNotification.setAdapter(notificationAdapter);

        SwipeHelper mySwipehelper = new SwipeHelper(getContext(), todayNotification, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List buffer) {
                buffer.add(new MyButton(getContext(), R.drawable.delete_sweep, Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                isRefresh=false;
                                Log.e("onClickInvoice", "dd" + isRefresh);
                                if(isRefresh==false) {
                                    showDeleteDialog(pos, "Single");
                                }
                            }
                        }));
            }
        };
        return notificationView;
    }

    private void fetchAppointmentDetails() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchAppointmentDetaisl(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchAppointment");
    }

    public void casting() {
        listOfChat = new ArrayList<>();
        todayNotification = notificationView.findViewById(R.id.today_notification_view);
        yesterdayNotification = notificationView.findViewById(R.id.yesterday_notification_view);
        txtToday = notificationView.findViewById(R.id.txt_today);
        deleteAll = notificationView.findViewById(R.id.delete);

        showDeleteAllButton();
        txtToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().startActivity(new Intent(getContext(), PatientNotificationTimeActivity.class));
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(0, "All");

            }
        });
    }

    private void showDeleteAllButton() {
        if (PatientMedicalRecordsController.getInstance().inboxNotificationList.size() > 0) {
            deleteAll.setVisibility(View.VISIBLE);
        } else {
            deleteAll.setVisibility(View.GONE);
        }
    }

    private void inboxDeleteAllApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        inboxDeleteAllApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteAll");
    }

    private void inboxMessagesFetchApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        getInboxFetchApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "invoiceFetch");
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                if (curdOpetaton.equals("deleteInvoice")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    Log.e("zxcvb", "dd" + isRefresh+jsonObject.toString());
                                    PatientMedicalRecordsController.getInstance().inboxNotificationList.remove(PatientMedicalRecordsController.getInstance().inboxNotificationModel);
                                    isRefresh=true;
                                    Log.e("deleteInvoice", "dd" + isRefresh+jsonObject.toString());
                                    notificationAdapter.notifyDataSetChanged();
                                    showDeleteAllButton();
                                    refreshShowingDialog.hideRefreshDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("invoiceFetch")) {
                    try {
                        Log.e("invoiceFetch", "call" + jsonObject.toString());
                        JSONArray resultArray = jsonObject.getJSONArray("result");
                        if (resultArray.length() > 0) {
                            PatientMedicalRecordsController.getInstance().inboxNotificationList.clear();
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject obj = resultArray.getJSONObject(i);
                                Gson gson = new Gson();
                                InboxNotificationModel inboxNotificationModel = gson.fromJson(obj.toString(), InboxNotificationModel.class);
                                PatientMedicalRecordsController.getInstance().inboxNotificationList.add(inboxNotificationModel);
                            }
                            refreshShowingDialog.hideRefreshDialog();
                            showDeleteAllButton();
                            Gson gson = new Gson();
                            String strMsg = gson.toJson(PatientMedicalRecordsController.getInstance().inboxNotificationList);
                            Log.e("inboxlist", "call" + strMsg);
                            notificationAdapter.notifyDataSetChanged();
                            for (int m = 0; m < resultArray.length(); m++) {
                                JSONObject obj = resultArray.getJSONObject(m);
                                Gson gson1 = new Gson();
                                if (obj.getJSONObject("messageBody").getJSONObject("data").get("messageType").equals("ChatMessage")){
                                    InboxNotificationModel inboxNotificationModel = gson.fromJson(obj.toString(), InboxNotificationModel.class);
                                    listOfChat.add(inboxNotificationModel);


                                }
                                String listOf = gson1.toJson(listOfChat);
                                Log.e("listOfChat",":: "+listOf);

                            }
                            int countA= Collections.frequency(listOfChat, "APNTIDlgvudPnb");
                            Log.e("count","is:: "+countA);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("deleteAll")) {
                    try {
                        Log.e("deleteAll", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    PatientMedicalRecordsController.getInstance().inboxNotificationList.clear();
                                    isRefresh=true;
                                    notificationAdapter.notifyDataSetChanged();
                                    deleteAll.setVisibility(View.GONE);
                                    refreshShowingDialog.hideRefreshDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("fetchAppointment")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientAppointmentsDataController.getInstance().allappointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().pastAppointmentsList.clear();
                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment", "length" + appointmentArray.length());

                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                PatientAppointmentsDataController.getInstance().allappointmentsList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(PatientAppointmentsDataController.getInstance().allappointmentsList);
                            Log.e("appointmentsize", "dd" + PatientAppointmentsDataController.getInstance().allappointmentsList.size());
                            showDeleteAllButton();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // removeDuplicatesFromList(PatientMedicalRecordsController.getInstance().inboxNotificationList);

            }

            @Override
            public void failureCallBack(String failureMsg) {

            }
        });
    }

/*
    public ArrayList<InboxNotificationModel> removeDuplicatesFromList(ArrayList<InboxNotificationModel> appointmentList){
        Map<String, InboxNotificationModel> map = new HashMap<String, InboxNotificationModel>();
        ArrayList<InboxNotificationModel> newPersonList = new ArrayList<>();
        for(InboxNotificationModel p:appointmentList){
            map.put(p.getMessageBody().getData().getAppointmentID(),p);
        }
        Iterator itr=map.keySet().iterator();
        while (itr.hasNext()) {
            String id =  itr.next().toString();
            newPersonList.add((InboxNotificationModel)map.get(id));
        }
        return newPersonList;
    }
*/

    private void deleteInboxApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            fetchObject.put("messageID", PatientMedicalRecordsController.getInstance().inboxNotificationModel.getMessageBody().getData().getMessageID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("deleteInboxApi", "data" + PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken());
        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        inboxDeleteApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteInvoice");
    }

    public void showDeleteDialog(int pos, String all) {
        Log.e("logggg", "out");
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        dialog.show();
        btnNo.setText("Cancel");
        btnYes.setText("Delete");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);


        txt_msg.setText("Are you sure you want to ");
        if (all.contains("All")) {
            txt_title.setText("Delete All");
            txt_msg1.setText("delete all notifications ?");
        } else {
            txt_title.setText("Delete");
            txt_msg1.setText("delete this notification ?");
        }
        RelativeLayout main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        RelativeLayout main1 = (RelativeLayout) dialog.findViewById(R.id.rl_main1);

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
                if (isNetworkConnected()) {
                    refreshShowingDialog.showAlert();
                    if (all.contains("All")) {
                        inboxDeleteAllApi();
                    } else {
                        PatientMedicalRecordsController.getInstance().inboxNotificationModel = PatientMedicalRecordsController.getInstance().inboxNotificationList.get(pos);
                        Log.e("onClick", "dd" + pos + PatientMedicalRecordsController.getInstance().inboxNotificationModel.getMessageBody().getData().getMessageID());
                        deleteInboxApi();
                    }
                } else {
                    refreshShowingDialog.hideRefreshDialog();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.e("caaalll", "cc :: "+data.toString());
                    Log.e("response for socket", " chat message" + data.toString());
                    String response;
                    String message = null;
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject(data.toString());
                        response = jsonObj.getString("response");
                        message = jsonObj.getString("message");
                        Log.e("rreeess", "ponse" + message);
                        if (Integer.parseInt(response) == 3) {
                            if (message.equals("Room fetched successfully")) {

                                Log.e("length of", "room" + jsonObj.getJSONArray("room").length());
                                JSONArray responseArray = jsonObj.getJSONArray("room");
                                for (int l = 0; l < responseArray.length(); l++) {
                                    Gson gson = new Gson();
                                    String jsonString = jsonObj.getJSONArray("room").getJSONObject(l).toString();
                                    MessageModel messageModel = gson.fromJson(jsonString, MessageModel.class);
                                    String kk = gson.toJson(messageModel);
                                    Log.e("logaa", "dafaa" + messageModel.getMessages().size());
                                    if (messageList.size() > 0) {
                                        Log.e("sixxxxx", "vvv" + messageList.size());
                                        messageList.set(0, messageModel);

                                        Log.e("sixxxxx", "dd" + messageList.get(0).getMessages().size());
                                        Log.e("sixxxxx", "vvv" + messageList.size());
                                    } else {
                                        messageList.add(messageModel);
                                    }
                                      Log.e("sixxxxx", "ee " + messageList.get(0).getMessages().size());
                                }

                            }
                            Log.e("calling", "call");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(Chating_activity.this,message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

}

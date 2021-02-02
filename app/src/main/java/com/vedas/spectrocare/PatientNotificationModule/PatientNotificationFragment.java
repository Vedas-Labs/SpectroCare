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
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientNotificationFragment extends Fragment {
    View notificationView;
    TextView txtToday;
    ImageView deleteAll;
    RecyclerView todayNotification, yesterdayNotification;
    PatientNotificationAdapter notificationAdapter;
    RefreshShowingDialog refreshShowingDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
                                showDeleteDialog(pos, "Single");
                            }
                        }));
            }
        };
        /*MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(), todayNotification, 180) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(getContext(), "", 0, R.drawable.delete_sweep,
                        Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                if (isNetworkConnected()) {
                                    refreshShowingDialog.showAlert();
                                    PatientMedicalRecordsController.getInstance().inboxNotificationModel = PatientMedicalRecordsController.getInstance().inboxNotificationList.get(pos);
                                    Log.e("onClick", "dd" + pos+PatientMedicalRecordsController.getInstance().inboxNotificationModel.getMessageBody().getData().getMessageID());
                                    deleteInboxApi();
                                } else {
                                    refreshShowingDialog.hideRefreshDialog();
                                }
                            }
                        }));
            }
        };*/
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
                        Log.e("deleteInvoice", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    PatientMedicalRecordsController.getInstance().inboxNotificationList.remove(PatientMedicalRecordsController.getInstance().inboxNotificationModel);
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
                            Log.e("inboxlist", "call" + PatientMedicalRecordsController.getInstance().inboxNotificationList.size());
                            notificationAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("deleteAll")) {
                    try {
                        Log.e("deleteInvoice", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    PatientMedicalRecordsController.getInstance().inboxNotificationList.clear();
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

            }

            @Override
            public void failureCallBack(String failureMsg) {

            }
        });
    }
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
        ApiCallDataController.getInstance().loadjsonApiCall(
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
            }
        });
        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}

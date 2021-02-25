package com.vedas.spectrocare.patient_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientModule.InvoiceDetailsActivity;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.BillingAndPaymentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingAndPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingAndPaymentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BillingAndPaymentAdapter billingAdapter;
    RecyclerView billingRecycle;
    TextView next_amount, next_duedate,txt_cash;
    Button btn_pay;
    RelativeLayout rl_nextpay;
    RefreshShowingDialog refreshShowingDialog;

    public BillingAndPaymentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BillingAndPaymentFragment newInstance(String param1, String param2) {
        BillingAndPaymentFragment fragment = new BillingAndPaymentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View paymentView = inflater.inflate(R.layout.fragment_billing_and_payment, container, false);
        refreshShowingDialog=new RefreshShowingDialog(getContext());
        billingRecycle = paymentView.findViewById(R.id.billing_recycle);
        next_amount = paymentView.findViewById(R.id.next_amt);
        next_duedate = paymentView.findViewById(R.id.next_duedate);
        btn_pay = paymentView.findViewById(R.id.btn_pay);
        rl_nextpay=paymentView.findViewById(R.id.nextPayLayout);
        txt_cash=paymentView.findViewById(R.id.txt_cash);

        PatientMedicalRecordsController.getInstance().invoiceObject = null;
        billingAdapter = new BillingAndPaymentAdapter(getContext());
        billingRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        billingRecycle.setAdapter(billingAdapter);
        accessInterfaceMethods();
        if (isNetworkConnected()) {
            refreshShowingDialog.showAlert();
            fetchInvoiceApi();
        } else {
            refreshShowingDialog.hideRefreshDialog();
        }
        return paymentView;
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetchInvoice")) {
                    try {
                        Log.e("fetchInvoice", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    PatientMedicalRecordsController.getInstance().invoiceList.clear();
                                    JSONArray appointmentArray = jsonObject.getJSONArray("invoices");
                                    Log.e("invoicesize", "length" + appointmentArray.length());
                                    for (int l = 0; l < appointmentArray.length(); l++) {
                                        Gson gson = new Gson();
                                        String jsonString = appointmentArray.getJSONObject(l).toString();
                                        InvoiceModel obj = gson.fromJson(jsonString, InvoiceModel.class);
                                        PatientMedicalRecordsController.getInstance().invoiceList.add(obj);
                                    }
                                    refreshShowingDialog.hideRefreshDialog();
                                    billingAdapter.notifyDataSetChanged();
                                    loadUnpaidInvoices(PatientMedicalRecordsController.getInstance().invoiceList);
                                    loadPaymentAction();
                                    loadNextPAymentUiLables();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    ArrayList<InvoiceModel> unpaidList = new ArrayList<>();

    private void loadPaymentAction() {
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unpaidList.size() > 0) {
                    PatientMedicalRecordsController.getInstance().invoiceObject =unpaidList.get(0);
                    startActivity(new Intent(getContext(), InvoiceDetailsActivity.class));
                }
            }
        });
    }

    private void loadNextPAymentUiLables() {
        if (unpaidList.size() > 0) {
            next_amount.setText(unpaidList.get(0).getTotalAmount());
            try {
                String date = PersonalInfoController.getInstance().invoiceTimestampToDate(unpaidList.get(0).getInvoicePaymentDueDate());
                String a[] = date.split(" ");
                next_duedate.setText("Due on " + a[0] + " " + a[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void loadUnpaidInvoices(ArrayList<InvoiceModel> list) {
        for (InvoiceModel model : list) {
            if (model.getPaymentStatus().toLowerCase().equals("unpaid")) {
                unpaidList.add(model);
            }
        }
        Log.e("unpaidlist", "call" + unpaidList.size());
        if(unpaidList.size()>0){
         float cost=   calculateUnpaidInvociesCost();
            Log.e("totalamount","call"+cost);

            txt_cash.setText(String.valueOf(cost)+"$");
            sortUrineResultsBasedOnTime(unpaidList);
        }else {
            rl_nextpay.setVisibility(View.GONE);
        }
      //  Collections.reverse(unpaidList);
    }

    private float calculateUnpaidInvociesCost(){
        float cost=0.0f;
        for (InvoiceModel model : unpaidList) {
            cost= cost+Float.parseFloat(model.getTotalAmount());
        }
        return cost;
    }

    public ArrayList<InvoiceModel> sortUrineResultsBasedOnTime(ArrayList<InvoiceModel> urineResults) {
        Collections.sort(urineResults, new Comparator<InvoiceModel>() {
            @Override
            public int compare(InvoiceModel s1, InvoiceModel s2) {
                return s1.getInvoicePaymentDueDate().compareTo(s2.getInvoicePaymentDueDate());
            }
        });
        return urineResults;
    }

    private void fetchInvoiceApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("byWhom", "admin");
            fetchObject.put("byWhomID", "viswanath3344");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("send", "data" + PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverApi.
                        getInvoiceByHospitalId(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetchInvoice");

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}

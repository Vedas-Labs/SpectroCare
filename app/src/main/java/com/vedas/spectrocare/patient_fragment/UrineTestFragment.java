package com.vedas.spectrocare.patient_fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientFileActivity;
import com.vedas.spectrocare.PatientModule.PatientTestRecordActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.UrineTestAdapter;
//import com.vedas.spectrocare.patientModuleAdapter.UrineTestAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UrineTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UrineTestFragment extends Fragment  {
    boolean c=false;
   public static UrineTestAdapter testAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UrineTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UrinTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UrineTestFragment newInstance(String param1, String param2) {
        UrineTestFragment fragment = new UrineTestFragment();
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

        View allTestRecords = inflater.inflate(R.layout.fragment_urin_test, container, false);
        Button btnDelete = allTestRecords.findViewById(R.id.btn_delete);
        RecyclerView urineTestRecordsView = allTestRecords.findViewById(R.id.urine_test_record_view);
        PatientTestRecordActivity recordActivity = new PatientTestRecordActivity();
        String dd = recordActivity.sendData();
        Log.e("dadaa",""+dd);
        urineTestRecordsView.setLayoutManager(new LinearLayoutManager(getContext()));
        testAdapter = new UrineTestAdapter(getContext(),btnDelete);
        urineTestRecordsView.setAdapter(testAdapter);
        return allTestRecords;
    }

    public void betterTest(){
        testAdapter.testMethod();
    }
    /*public class UrineTestAdapter extends RecyclerView.Adapter<UrineTestAdapter.UrineTestHolder> {
        Context context;
        Button btnEdit;

        public UrineTestAdapter(Context context, Button btnEdit) {
            this.context = context;
            this.btnEdit = btnEdit;
        }

        public UrineTestAdapter(Context context) {
            this.context = context;
        }


        @Override
        public UrineTestAdapter.UrineTestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View urineTestView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
            return new UrineTestHolder(urineTestView);
        }

        @Override
        public void onBindViewHolder( UrineTestAdapter.UrineTestHolder holder, int position) {
            holder.txtNormal.setText("Normal");
            holder.txtTest.setText("Urine Routine");
            holder.imgIcon.setBackgroundResource(R.drawable.testtube);
            holder.txtNormal.setTextColor(Color.parseColor("#2DAF22"));
            holder.txtAbnormal.setVisibility(View.VISIBLE);


        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public  class UrineTestHolder extends RecyclerView.ViewHolder {
            TextView txtNormal,txtAbnormal,txtTest;
            CheckBox checkBox;
            ImageView imgIcon;
            Button viewBtn;
            public UrineTestHolder(@NonNull View itemView) {
                super(itemView);
                imgIcon = itemView.findViewById(R.id.img_doc_pic);
                checkBox = itemView.findViewById(R.id.checkbox);
                txtTest = itemView.findViewById(R.id.txt_doc_name);
                txtAbnormal = itemView.findViewById(R.id.txt_Abnormal);
                txtNormal = itemView.findViewById(R.id.txt_infection_name);
            }
        }
    }*/

}

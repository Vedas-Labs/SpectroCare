package com.vedas.spectrocare.PatientNotificationModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientNotificationAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientNotificationFragment extends Fragment {
    View notificationView;
    TextView txtToday;
    RecyclerView todayNotification,yesterdayNotification;
    PatientNotificationAdapter notificationAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notificationView = inflater.inflate(R.layout.fragment_patient_notification, container, false);
        notificationAdapter = new PatientNotificationAdapter(getContext());
        casting();
        todayNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        todayNotification.setAdapter(notificationAdapter);
        txtToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(),PatientNotificationTimeActivity.class));
            }
        });

        MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(),todayNotification,200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(getContext(),"",0,R.drawable.delete_sweep,
                        Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(getContext(), "deleteClick", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        };

        return notificationView;
    }
    public  void casting(){
        todayNotification = notificationView.findViewById(R.id.today_notification_view);
        yesterdayNotification = notificationView.findViewById(R.id.yesterday_notification_view);
        txtToday = notificationView.findViewById(R.id.txt_today);
    }

}

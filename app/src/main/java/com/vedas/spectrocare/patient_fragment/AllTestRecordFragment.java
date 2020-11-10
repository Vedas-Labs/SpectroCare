package com.vedas.spectrocare.patient_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.AllTestRecordAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllTestRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTestRecordFragment extends Fragment {
    public static AllTestRecordAdapter allTestRecordAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllTestRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTestRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllTestRecordFragment newInstance(String param1, String param2) {
        AllTestRecordFragment fragment = new AllTestRecordFragment();
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
        View viewAll =inflater.inflate(R.layout.fragment_all_test_record, container, false);

        RecyclerView allRecycleTests = viewAll.findViewById(R.id.all_recycle_tests);
        allRecycleTests.setLayoutManager(new LinearLayoutManager(getContext()));
        allTestRecordAdapter = new AllTestRecordAdapter(getContext());
        allRecycleTests.setAdapter(allTestRecordAdapter);
        return viewAll;
    }
    public void betterTest(){
        allTestRecordAdapter.testMethod();
    }
}

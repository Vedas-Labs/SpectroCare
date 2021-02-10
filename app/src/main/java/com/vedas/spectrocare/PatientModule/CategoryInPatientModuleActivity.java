package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.DoctorsItemModel;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsCategoryAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryInPatientModuleActivity extends AppCompatActivity {
    RecyclerView categoryListView;
    ImageView imgBackArrow;
    DoctorsCategoryAdapter doctorsCategoryAdapter;
    ArrayList<CategoryItemModel> sortedList=new ArrayList<>();
    EditText searchByCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_in_ptient_module);
        categoryListView = findViewById(R.id.category_list_view);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        searchByCategory = findViewById(R.id.edt_search);

        sortedList= PatientMedicalRecordsController.getInstance().doctorsCategoryList;
        doctorsCategoryAdapter = new DoctorsCategoryAdapter(CategoryInPatientModuleActivity.this, sortedList);
        categoryListView.setLayoutManager(new LinearLayoutManager(CategoryInPatientModuleActivity.this));
        categoryListView.setNestedScrollingEnabled(false);
        categoryListView.setAdapter(doctorsCategoryAdapter);

        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchByCategory.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().length() > 0) {
                    Log.e("dssssssss", "call" + s.toString());
                    filter(s.toString().toLowerCase());
                }else {
                    doctorsCategoryAdapter.filterList(PatientMedicalRecordsController.getInstance().doctorsCategoryList);
                }

            }
        });

    }
    private void filter(String text) {
        ArrayList<CategoryItemModel> filterdNames = new ArrayList<>();
        for (CategoryItemModel s : PatientMedicalRecordsController.getInstance().doctorsCategoryList) {
            if (s.getCategoryTitle().toLowerCase().startsWith(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        sortedList=filterdNames;
        doctorsCategoryAdapter.filterList(sortedList);
    }


}

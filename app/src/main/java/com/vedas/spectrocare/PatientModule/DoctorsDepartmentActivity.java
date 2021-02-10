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
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.SearchByDepartmentMode;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsDepartmentAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoctorsDepartmentActivity extends AppCompatActivity {
    ArrayList<CategoryItemModel> sorteddepartmentList=new ArrayList<>();
    RecyclerView departmentView;
    DoctorsDepartmentAdapter doctorsDepartmentAdapter;
    ImageView imgArrowback;
    EditText edtSearchByDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_department);
        departmentView = findViewById(R.id.department_list_view);
        imgArrowback = findViewById(R.id.img_back_arrow);
        edtSearchByDepartment = findViewById(R.id.edt_search);

        sorteddepartmentList= PatientMedicalRecordsController.getInstance().doctorsDepartmentList;
        doctorsDepartmentAdapter = new DoctorsDepartmentAdapter(DoctorsDepartmentActivity.this,sorteddepartmentList);
        departmentView.setLayoutManager(new LinearLayoutManager(DoctorsDepartmentActivity.this));
        departmentView.setNestedScrollingEnabled(false);
        departmentView.setAdapter(doctorsDepartmentAdapter);

        imgArrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtSearchByDepartment.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    Log.e("dssssssss", "call" + s.toString());
                    //  filterArray(s.toString().toLowerCase());
                }else{

                }
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
                    doctorsDepartmentAdapter.filterList(PatientMedicalRecordsController.getInstance().doctorsDepartmentList);
                }

            }
        });
    }
    private void filter(String text) {
        ArrayList<CategoryItemModel> filterdNames = new ArrayList<>();
        for (CategoryItemModel s : PatientMedicalRecordsController.getInstance().doctorsDepartmentList) {
            if (s.getCategoryTitle().toLowerCase().startsWith(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        sorteddepartmentList=filterdNames;
        doctorsDepartmentAdapter.filterList(sorteddepartmentList);
    }

}

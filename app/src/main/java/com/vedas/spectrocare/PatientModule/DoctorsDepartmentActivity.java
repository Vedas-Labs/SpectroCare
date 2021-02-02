package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
    ArrayList<CategoryItemModel> departmentList;
    RecyclerView departmentView;
    DoctorsDepartmentAdapter doctorsDepartmentAdapter;
    ImageView imgArrowback;
    EditText edtSearchByDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_department);
        //searchByDepartmentAPI();
        departmentView = findViewById(R.id.department_list_view);
        imgArrowback = findViewById(R.id.img_back_arrow);
        edtSearchByDepartment = findViewById(R.id.edt_search);
        departmentList = new ArrayList();
        departmentList.add(new CategoryItemModel(R.drawable.sample_image, "Elderly Services"));
        departmentList.add(new CategoryItemModel(R.drawable.cardiovascular_1, "Cardiology"));
        departmentList.add(new CategoryItemModel(R.drawable.sample_image, "Endocrinology Care"));
        departmentList.add(new CategoryItemModel(R.drawable.sample_image, "Gastroentrology"));
        departmentList.add(new CategoryItemModel(R.drawable.ent, "ENT"));
        departmentList.add(new CategoryItemModel(R.drawable.sample_image, "Dermatology"));
        departmentList.add(new CategoryItemModel(R.drawable.sample_image, "Family Practice"));
        doctorsDepartmentAdapter = new DoctorsDepartmentAdapter(DoctorsDepartmentActivity.this,departmentList);
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
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<CategoryItemModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (CategoryItemModel s : departmentList) {
            //if the existing elements contains the search input
            if (s.getCategoryTitle().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }

        doctorsDepartmentAdapter.filterList(filterdNames);
    }
/*
    public void searchByDepartmentAPI(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SearchByDepartmentMode departmentMode = new SearchByDepartmentMode("","",
                "","","");
        ServerApi s = retrofit.create(ServerApi.class);

        Call<ResponseBody> departmentDataCall = s.searchByDepart(departmentMode);
        departmentDataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("yem vachindante!","!!!"+response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
*/

}

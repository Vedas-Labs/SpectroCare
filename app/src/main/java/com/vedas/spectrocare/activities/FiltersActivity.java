package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.DoctorInfoServerObjectDataController;
import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.adapter.AppointmentDeptAdapter;
import com.vedas.spectrocare.adapter.AppointmentStatusAdapter;
import com.vedas.spectrocare.adapter.AppointmentVisiteAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FiltersActivity extends AppCompatActivity {
    RelativeLayout backImg;
    public static boolean isSelectFilter=false;
    RecyclerView status_recycle_view,department_recycle_view,visite_recycle_view;
    TextView txtClearFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        loadIds();
        txtClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("daf","dd");
                adapterDepart();
                adapterFilter();
                adapterVisit();
            }
        });

    }
    private void loadIds(){
        backImg = findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnClear = findViewById(R.id.btn_cancel);
        Button btnFilter = findViewById(R.id.btn_filter);
         txtClearFilters = findViewById(R.id.txt_clr_filter);

         status_recycle_view = findViewById(R.id.status_recycle_view);
         department_recycle_view = findViewById(R.id.department_recycle_view);
         visite_recycle_view = findViewById(R.id.visite_recycle_view);
adapterDepart();
adapterFilter();
adapterVisit();

      /*  AppointmentStatusAdapter filtersAdapter = new AppointmentStatusAdapter(FiltersActivity.this,getStatusListData());
        status_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        status_recycle_view.setAdapter(filtersAdapter);
        filtersAdapter.notifyDataSetChanged();

        AppointmentVisiteAdapter visiteAdapter = new AppointmentVisiteAdapter(FiltersActivity.this,getVisiteData());
        visite_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        visite_recycle_view.setAdapter(visiteAdapter);
        visiteAdapter.notifyDataSetChanged();

        AppointmentDeptAdapter deptAdapter = new AppointmentDeptAdapter(FiltersActivity.this,getDeptData());
        department_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        department_recycle_view.setAdapter(deptAdapter);
        deptAdapter.notifyDataSetChanged();
*/
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelectFilter=true;
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    public void adapterFilter(){
        AppointmentStatusAdapter filtersAdapter = new AppointmentStatusAdapter(FiltersActivity.this,getStatusListData());
        status_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        status_recycle_view.setAdapter(filtersAdapter);
        filtersAdapter.notifyDataSetChanged();

    }
    public void adapterVisit(){
        AppointmentVisiteAdapter visiteAdapter = new AppointmentVisiteAdapter(FiltersActivity.this,getVisiteData());
        visite_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        visite_recycle_view.setAdapter(visiteAdapter);
        visiteAdapter.notifyDataSetChanged();

    }
    public void adapterDepart(){
        AppointmentDeptAdapter deptAdapter = new AppointmentDeptAdapter(FiltersActivity.this,getDeptData());
        department_recycle_view.setLayoutManager(new GridLayoutManager(FiltersActivity.this,3));
        department_recycle_view.setAdapter(deptAdapter);
        deptAdapter.notifyDataSetChanged();

    }
    private ArrayList<Model> getStatusListData() {
        ArrayList<Model>   mModelList = new ArrayList<>();
        for (int i = 0; i < DoctorInfoServerObjectDataController.getInstance().statusTypeArray.size(); i++) {
            mModelList.add(new Model(DoctorInfoServerObjectDataController.getInstance().statusTypeArray.get(i)));
        }
        return mModelList;
    }
    private ArrayList<Model> getVisiteData() {
        ArrayList<Model>     mModelList = new ArrayList<>();
        for (int i = 0; i < DoctorInfoServerObjectDataController.getInstance().visiteTypeArray.size(); i++) {
            mModelList.add(new Model(DoctorInfoServerObjectDataController.getInstance().visiteTypeArray.get(i)));
        }
        return mModelList;
    }
    private ArrayList<Model> getDeptData() {
        ArrayList<String>     mModelList = new ArrayList<>();
        ArrayList<Model>     modelList = new ArrayList<>();
        if(DoctorInfoDataController.getInstance().allDoctorInfo.size()>0) {
            for (int i = 0; i < DoctorInfoDataController.getInstance().allDoctorInfo.size(); i++) {
              //  mModelList.add(new Model(DoctorInfoDataController.getInstance().allDoctorInfo.get(i).getDepartment()));
                mModelList.add(DoctorInfoDataController.getInstance().allDoctorInfo.get(i).getDepartment());
            }
        }
        System.out.println(mModelList.toString());
        Set<String> set = new LinkedHashSet<String>(mModelList);
        System.out.println(set);
        modelList.clear();
        for (String dept : set) {
            modelList.add(new Model(dept));
        }
        Log.e("deptaaass","call"+modelList.size());
        return modelList;
    }
   /* private ArrayList<Model> getDeptData() {
        ArrayList<Model>     mModelList = new ArrayList<>();
        for (int i = 0; i < DoctorInfoServerObjectDataController.getInstance().departmentArray.size(); i++) {
            mModelList.add(new Model(DoctorInfoServerObjectDataController.getInstance().departmentArray.get(i)));
        }
        return mModelList;
    }*/
    public class Model {
        private String text;
        private boolean isSelected = false;
        public Model(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }
        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        public boolean isSelected() {
            return isSelected;
        }
    }
}

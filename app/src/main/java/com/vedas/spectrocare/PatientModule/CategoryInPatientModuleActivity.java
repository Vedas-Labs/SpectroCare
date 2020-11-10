package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
ArrayList<CategoryItemModel> categoryListItem;
    EditText searchByCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_in_ptient_module);
        categoryListView = findViewById(R.id.category_list_view);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        searchByCategory = findViewById(R.id.edt_search);
        categoryListItem = new ArrayList();
        categoryListItem.add(new CategoryItemModel(R.drawable.diabetescare_1, "Diabetic Care"));
        categoryListItem.add(new CategoryItemModel(R.drawable.cardiovascular_1, "cardiovascular Care"));

        doctorsCategoryAdapter = new DoctorsCategoryAdapter(CategoryInPatientModuleActivity.this,categoryListItem);
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

      /*  for(int i=0;i<searchedList.size();i++){
            if(searchedList.get(i).contains(text.toLowerCase())){
                filterdNames.add(names.get(i));
            }
        }*/

        //looping through existing elements
        for (CategoryItemModel s : categoryListItem) {
            //if the existing elements contains the search input
            if (s.getCategoryTitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        doctorsCategoryAdapter.filterList(filterdNames);
    }


}

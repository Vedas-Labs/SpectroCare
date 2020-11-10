package com.vedas.spectrocare.PatientModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.MedicalPersonnelController;
import com.vedas.spectrocare.PatientDocResponseModel.DepartmentResponseModel;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.DoctorsItemModel;
import com.vedas.spectrocare.patientModuleAdapter.SearchResultAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {
    ArrayList<DoctorsItemModel> searchedList;
    RelativeLayout btnShowMore;
    RecyclerView searchResultView;
    Button btnMore;
    ImageView imgBack;
    SearchResultAdapter resultAdapter;
    DepartmentResponseModel responseModel;
    EditText searchEdit;
    ArrayList<MedicalPersonnelModel> medicList;

    RefreshShowingDialog showingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        btnShowMore = findViewById(R.id.layout_more);
        imgBack = findViewById(R.id.img_back_arrow);
        btnMore = findViewById(R.id.btn_show_more);
        responseModel = new DepartmentResponseModel();
        searchedList = new ArrayList();
        medicList = new ArrayList<>();
        showingDialog=new RefreshShowingDialog(SearchResultsActivity.this);
        DocDepartmentAPI();
        showingDialog.showAlert();
        searchEdit = findViewById(R.id.edt_search);
        searchResultView = findViewById(R.id.serch_result_view);
       /* searchedList.add(new DoctorsItemModel(R.drawable.profile, "DR Durga Prasad","Cardiology"));
        searchedList.add(new DoctorsItemModel(R.drawable.profile, "DR Muneeswar","Cardiology"));
        resultAdapter = new SearchResultAdapter(SearchResultsActivity.this,searchedList);
        searchResultView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));
        searchResultView.setNestedScrollingEnabled(false);
        searchResultView.setAdapter(resultAdapter);*/
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultsActivity.this,PatientPaymentActivity.class));
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
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
        ArrayList<DoctorsItemModel> filterdNames = new ArrayList<>();

      /*  for(int i=0;i<searchedList.size();i++){
            if(searchedList.get(i).contains(text.toLowerCase())){
                filterdNames.add(names.get(i));
            }
        }*/

        //looping through existing elements
        for (DoctorsItemModel s : searchedList) {
            //if the existing elements contains the search input
            if (s.getDoctorName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        resultAdapter.filterList(filterdNames);
    }
    public void DocDepartmentAPI(){
        Retrofit rFit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi serverDepartmentApi = rFit.create(ServerApi.class);
        JSONObject params = new JSONObject();
        try {
            params.put("byWhomID","viswanath3344@gmail.com");
            params.put("byWhom","patient");
            params.put("department","Elderly services");
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        Call<DepartmentResponseModel> callDepartmentAPI = serverDepartmentApi.getDoctorsByDept(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),gsonObject);
        callDepartmentAPI.enqueue(new Callback<DepartmentResponseModel>() {
            @Override
            public void onResponse(Call<DepartmentResponseModel> call, Response<DepartmentResponseModel> response) {
                Log.e("response","res"+response.body());
                showingDialog.hideRefreshDialog();
                responseModel = response.body();
                Gson gson = new Gson();
                String json = gson.toJson(responseModel);
                Log.e("bodyy","dd"+json);
                MedicalPersonnelController.setNull();
              //  medicalPersonnelController.setMedicalPersonnelModel(responseModel);

                searchResultView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));
                searchResultView.setNestedScrollingEnabled(false);
              //  Gson gson = new Gson();
                medicList = responseModel.getMedicalPersonnels();
             //   String json = gson.toJson(responseModel);
                Log.e("list","size"+responseModel.getMessage()) ;
               // Log.e("jso","size"+json) ;
              Log.e("sixe","da"+medicList.size());

                for (int i=0;i<responseModel.getMedicalPersonnels().size();i++){
                    Log.e("cjcjc","ad"+responseModel.getMedicalPersonnels().get(i).getProfile().getUserProfile().getProfilePic());
                    searchedList.add(new DoctorsItemModel(responseModel.getMedicalPersonnels().get(i).getProfile().getUserProfile().getProfilePic(),
                            responseModel.getMedicalPersonnels().get(i).getProfile().getUserProfile().getFirstName()+
                                    responseModel.getMedicalPersonnels().get(i).getProfile().getUserProfile().getLastName(),
                            responseModel.getMedicalPersonnels().get(i).getProfile().getUserProfile().getDepartment()));
                    resultAdapter = new SearchResultAdapter(SearchResultsActivity.this,searchedList);
                    searchResultView.setAdapter(resultAdapter);

                }
                /*searchedList.add(new DoctorsItemModel(, "DR Durga Prasad","Cardiology"));
                searchedList.add(new DoctorsItemModel(R.drawable.profile, "DR Muneeswar","Cardiology"));*/

            }

            @Override
            public void onFailure(Call<DepartmentResponseModel> call, Throwable t) {
                Log.e("fail","res"+t.getMessage());
                showingDialog.hideRefreshDialog();

            }
        });
    }
    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResulHolder> {
        Context context;
        ArrayList<DoctorsItemModel> doctorsLatestList;

        public SearchResultAdapter(Context context, ArrayList<DoctorsItemModel> doctorsLatestList) {
            this.context = context;
            this.doctorsLatestList = doctorsLatestList;
        }

        @NonNull
        @Override
        public SearchResultAdapter.SearchResulHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View doctorSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_latest_search_item, parent, false);
            return new SearchResulHolder(doctorSearchView);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultAdapter.SearchResulHolder holder, int position) {
            Log.e("ds","ddd");
            //holder.docPic.setImageResource(doctorsLatestList.get(position).getCategoryIcon());
            Log.e("adfaf","a"+ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon());
            //  Picasso.get().load(ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon());
            Picasso.get().load(ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon()).placeholder(R.drawable.profile_1)
                    .into(holder.docPic);
          //  Picasso.get().load(ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon()).into(holder.docPic);
            holder.txtDocName.setText(doctorsLatestList.get(position).getDoctorName());
            holder.txtDocProfession.setText(doctorsLatestList.get(position).getDocProfession());
            holder.btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PatientBookAppointmentActivity.class)
                    .putExtra("docName",doctorsLatestList.get(holder.getAdapterPosition()).getDoctorName())
                    .putExtra("docProfi",doctorsLatestList.get(holder.getAdapterPosition()).getDocProfession())
                    .putExtra("docId",medicList.get(holder.getAdapterPosition()).getProfile().getUserProfile().getMedical_personnel_id()));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicalPersonnelController.setNull();
                    MedicalPersonnelController medicalPersonnelController = MedicalPersonnelController.getInstance();
                    medicalPersonnelController.setMedicalPersonnelModel(responseModel.getMedicalPersonnels().get(holder.getAdapterPosition()));
                    context.startActivity(new Intent(context, DoctorSummeryActivity.class));
                }
            });

        }

        @Override
        public int getItemCount() {
            return doctorsLatestList.size();
        }

        public class SearchResulHolder extends RecyclerView.ViewHolder {
            ImageView docPic;
            Button btnBook;
            TextView txtDocName,txtDocProfession;
            public SearchResulHolder(@NonNull View itemView) {
                super(itemView);
                docPic = itemView.findViewById(R.id.img_doc_pic);
                txtDocName = itemView.findViewById(R.id.txt_doc_name);
                btnBook = itemView.findViewById(R.id.btn_book);
                txtDocProfession = itemView.findViewById(R.id.txt_doc_profession);

            }
        }
        public void filterList(ArrayList<DoctorsItemModel> filterdNames) {
            this.doctorsLatestList = filterdNames;
            notifyDataSetChanged();
        }

    }




}

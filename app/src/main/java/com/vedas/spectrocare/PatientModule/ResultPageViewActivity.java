package com.vedas.spectrocare.PatientModule;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.TestFactorDataController;
import com.vedas.spectrocare.DataBase.UrineResultsDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.DataBaseModels.TestFactors;
import com.vedas.spectrocare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ResultPageViewActivity extends AppCompatActivity {
    int selectedPosition=-1;
    RecyclerView resultRecyclerView;
    ResultsTableViewCell resultsTableViewCell;
    TextView txt_name,txt_gender,txt_patientId,txt_medicalId,txt_routine,txt_date,txt_time;
    TextView txt_normal,txt_abnormal;
    CircularImageView circularImageView;
    ArrayList<TestFactors> testFactorsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page_view);
        setResultRecyclerViewData();
        loadIds();
    }
    private void loadIds(){
        txt_name=findViewById(R.id.txt_name);
        txt_gender=findViewById(R.id.txt_gender);
        txt_patientId=findViewById(R.id.txt_patientid);
        txt_medicalId=findViewById(R.id.txt_medicalid);
      //  txt_routine=findViewById(R.id.txt_rooutine);
        txt_date=findViewById(R.id.txt_date);
        txt_time=findViewById(R.id.txt_time);
        circularImageView=findViewById(R.id.img_profile);
        txt_normal=findViewById(R.id.txt_normal);
        txt_abnormal=findViewById(R.id.txt_abnormal);

        if(PatientLoginDataController.getInstance().currentPatientlProfile != null){
            PatientModel obj= PatientLoginDataController.getInstance().currentPatientlProfile;
            txt_name.setText(obj.getFirstName());
            txt_gender.setText(obj.getGender()+" "+obj.getAge()+" Years");
            txt_patientId.setText("Patient ID:"+obj.getPatientId());
            txt_medicalId.setText("Medical Person ID:"+obj.getMedicalRecordId());
            Picasso.get().load(obj.getProfilePic()).placeholder(R.drawable.image).into(circularImageView);
        }
        //  UrineResultsDataController.getInstance().fetchAllUrineResults();
        if(UrineResultsDataController.getInstance().currenturineresultsModel != null) {
            testFactorsArrayList = TestFactorDataController.getInstance().fetchTestFactorresults(UrineResultsDataController.getInstance().currenturineresultsModel);
            if (testFactorsArrayList.size() > 0) {
                String val[] = calculateNormalAndAbnormalValues().split(" ");
                txt_normal.setText("Normal " + val[0]);
                txt_abnormal.setText("AbNormal " + val[1]);
            }
            getCurrentDateTime(UrineResultsDataController.getInstance().currenturineresultsModel.getTestedTime());
        }
    }
    private void getCurrentDateTime(String time){
        long yourmilliseconds = Long.parseLong(time);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = df.format(resultdate);
        String array[]=weekString.split(" ");
        txt_date.setText(array[0]);
        txt_time.setText(array[1]+" "+array[2]);
    }
    public void setResultRecyclerViewData() {
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        resultRecyclerView.setNestedScrollingEnabled(false);
        resultsTableViewCell = new ResultsTableViewCell();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(horizontalLayoutManager);
        resultRecyclerView.setAdapter(resultsTableViewCell);
        resultRecyclerView.setMotionEventSplittingEnabled(false);
        resultsTableViewCell.notifyDataSetChanged();
        resultsTableViewCell.notifyItemChanged(selectedPosition);
    }
    public class ResultsTableViewCell extends RecyclerView.Adapter<ResultsTableViewCell.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result_items, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            TestFactors objTestFactors = testFactorsArrayList.get(position);
            holder.testName.setText(objTestFactors.getTestName());
            holder.testValue.setText(objTestFactors.getResult());
            holder.unitsName.setText(objTestFactors.getUnit());
            holder.testrsult.setText(objTestFactors.getValue());
            if (objTestFactors.isFlag()) {
                holder.testCondition.setText("Normal");
                holder.testCondition.setTextColor(Color.parseColor("#2DAF22"));
                holder.imageView.setBackgroundResource(R.drawable.happiness);
                holder.txt_bg.setBackgroundColor(Color.parseColor("#2DAF22"));
                holder.txt_bg1.setBackgroundColor(Color.parseColor("#2DAF22"));
            } else {
                holder.imageView.setBackgroundResource(R.drawable.sad);
                holder.testCondition.setText("AbNormal");
                holder.testCondition.setTextColor(Color.parseColor("#FF0000"));
                holder.txt_bg.setBackgroundColor(Color.parseColor("#F93225"));
                holder.txt_bg1.setBackgroundColor(Color.parseColor("#F93225"));
            }
            if (selectedPosition == position) {
                holder.rlScrollMeg.setVisibility(View.VISIBLE);
                holder.down.setBackgroundResource(R.drawable.ic_arrow_drop_up);
                holder.txt_discription = (TextView) holder.itemView.findViewById(R.id.test_discription);
              //   holder.txt_discription.setText(getTheDescriptionForTestName(holder.testName.getText().toString()));
            } else {
                holder.rlScrollMeg.setVisibility(View.GONE);
                holder.down.setBackgroundResource(R.drawable.dropdown);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                    } else {
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(testFactorsArrayList.size()>0){
                return testFactorsArrayList.size();
            }else{
                return 0;
            }
        }
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView testName, testValue, testCondition, txt_discription, unitsName, testrsult;
            Context ctx;
            ImageView imageView;
            Button down;
            TextView txt_bg, txt_bg1;
            RelativeLayout rlScrollMeg;

            public ViewHolder(View itemView) {
                super(itemView);
                testName = (TextView) itemView.findViewById(R.id.testName);
                unitsName = (TextView) itemView.findViewById(R.id.unitsName);
                testValue = (TextView) itemView.findViewById(R.id.testVal);
                testrsult = (TextView) itemView.findViewById(R.id.testrsult);
                testCondition = (TextView) itemView.findViewById(R.id.testCondition);
                imageView = (ImageView) itemView.findViewById(R.id.img_icon);
                down = (Button) itemView.findViewById(R.id.btn_down);
                txt_bg = (TextView) itemView.findViewById(R.id.txt1);
                txt_bg1 = (TextView) itemView.findViewById(R.id.txt2);
                rlScrollMeg = (RelativeLayout) itemView.findViewById(R.id.rl_msg);
                txt_discription = (TextView) itemView.findViewById(R.id.test_discription);
            }
        }
    }
    private String calculateNormalAndAbnormalValues(){
        int normal=0 ,abnormal=0;
        if(testFactorsArrayList.size()>0){
            for(int i = 0; i< testFactorsArrayList.size(); i++){
                if(testFactorsArrayList.get(i).isFlag()){
                    normal=normal+1;
                }else {
                    abnormal=abnormal+1;
                }
            }
        }
        return String.valueOf(normal)+" "+String.valueOf(abnormal);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (PatientTestRecordActivity.isFromTestRecord) {
            PatientTestRecordActivity.isFromTestRecord=false;
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}

package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

public class ResultPageViewActivity extends AppCompatActivity {
    int selectedPosition=-1;
    RecyclerView resultRecyclerView;
    ResultsTableViewCell resultsTableViewCell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page_view);
        setResultRecyclerViewData();
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
        public ResultsTableViewCell() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result_items, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
              /*TestFactors objTestFactors = TestFactorDataController.getInstance().testfactorlist.get(position);
                 holder.testName.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(objTestFactors.getTestName()));

                holder.testValue.setText(objTestFactors.getResult());
                holder.unitsName.setText(objTestFactors.getUnit());
                holder.testrsult.setText(objTestFactors.getValue());


                holder.txt_bg.setBackgroundColor(changeLableColor(holder.testrsult.getText().toString()));
                holder.txt_bg1.setBackgroundColor(changeLableColor(holder.testrsult.getText().toString()));
                if (objTestFactors.getFlag()) {
                    holder.testCondition.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(LanguagesKeys.NORMAL_VALUE_KEY));
                    holder.testCondition.setTextColor(Color.parseColor("#090A0B"));
                    holder.imageView.setBackgroundResource(R.drawable.ic_happyold);
                } else {
                    holder.imageView.setBackgroundResource(R.drawable.ic_sadold);
                    holder.testCondition.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(LanguagesKeys.ABNORMAL_VALUE_KEY));
                    holder.testCondition.setTextColor(Color.parseColor("#FF0000"));

                }*/


            if (selectedPosition == position) {
                holder.rlScrollMeg.setVisibility(View.VISIBLE);
                holder.down.setBackgroundResource(R.drawable.ic_arrow_drop_up);
                holder.txt_discription = (TextView) holder.itemView.findViewById(R.id.test_discription);
               // holder.txt_discription.setText(getTheDescriptionForTestName(holder.testName.getText().toString()));
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
           return 5;
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

}

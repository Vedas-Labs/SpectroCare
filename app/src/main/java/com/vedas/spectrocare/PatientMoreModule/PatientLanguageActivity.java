package com.vedas.spectrocare.PatientMoreModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
import com.vedas.spectrocare.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientLanguageActivity extends AppCompatActivity {
    ArrayList<String> languages = new ArrayList<>();
    ArrayList<String> languages1 = new ArrayList<>();

    RecyclerView addRecyclerView;
    ImageView imgBack;
    Language languageAdapter;
    int selectedPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_language);
        imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
    }
    private void init() {
        languages.add("English (India)");
        languages.add("English(United States)");
        languages.add("Chinese Simplified");
        languages.add("Chinese Traditional");

        addRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        languageAdapter = new Language(getApplication());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        addRecyclerView.setLayoutManager(horizontalLayoutManager);
        addRecyclerView.setAdapter(languageAdapter);
    }
    public class Language extends RecyclerView.Adapter<Language.ViewHolder> {
        Context ctx;

        public Language(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_language_item, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ImageView image = (ImageView) holder.itemView.findViewById(R.id.tick);

            holder.userName.setText(languages.get(position));
           // holder.userName1.setText(objLanguage.get(LanguagesKeys.originalLanguageKey));

            if (selectedPosition == position) {
                image.setVisibility(View.VISIBLE);
              //  holder.layout.setBackgroundColor(Color.parseColor("#d6eef8"));//#5CEBD3
                SettingsActivity.editor.putString("language",languages.get(position));
                SettingsActivity.editor.commit();
            }else {
                image.setVisibility(View.INVISIBLE);
                holder.layout.setBackgroundColor(Color.parseColor("#fcfcfc"));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(selectedPosition != position){
                       selectedPosition=position;
                       notifyDataSetChanged();
                   }else{
                       selectedPosition= -1;
                       notifyDataSetChanged();
                   }
                }
            });
        }
        @Override
        public int getItemCount() {
            return languages.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView userName, userName1;
            ArrayList<String> arrayList = new ArrayList<String>();
            Context ctx;
            CardView layout;
            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                this.ctx = ctx;
                userName = (TextView) itemView.findViewById(R.id.devicename);
                userName1 = (TextView) itemView.findViewById(R.id.device);
                layout = (CardView) itemView.findViewById(R.id.relative_language);
                itemView.setOnClickListener(this);

            }
            @Override
            public void onClick(View v) {


            }
        }
    }

}

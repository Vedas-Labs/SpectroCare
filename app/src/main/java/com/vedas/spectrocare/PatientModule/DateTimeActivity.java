package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
import com.vedas.spectrocare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateTimeActivity extends AppCompatActivity {
    ToggleButton toggle;
    EditText myTextBox;
    Spinner spinner;
    TextView txt_datetime;
    List<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        ButterKnife.bind(this);
        loadSpinner();
        loadIds();
        readData();
    }

    @OnClick(R.id.back)
    void backAction() {
        String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(getApplicationContext(),String.valueOf(System.currentTimeMillis()));
        Log.e("value", "call" + value + String.valueOf(System.currentTimeMillis()));
        onBackPressed();
    }

    private void readData() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean formate = sharedPreferences.getBoolean("is24Hour", false);
        String region = sharedPreferences.getString("region", "India");
        String dateFormat = sharedPreferences.getString("dateFormat", "YYYY/MM/dd");
        myTextBox.setText(region);
        spinner.setSelection(categories.indexOf(dateFormat));
        if (formate) {
            toggle.setChecked(true);
            loadtimestamp(true);
        } else {
            toggle.setChecked(false);
            loadtimestamp(false);
        }
    }

    private void loadSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        categories.add("YYYY/MM/DD");
        categories.add("YYYY/MM/dd");
        categories.add("yyyy-MM-dd");
        categories.add("dd.MM.yyyy");
        categories.add("yyyy.MM.dd");
        categories.add("d/M/yyyy");
        categories.add("yyyy.M.d");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (toggle.isChecked()) {
                    loadtimestamp(true);
                } else {
                    loadtimestamp(false);
                }
                SettingsActivity.editor.putString("dateFormat", item);
                SettingsActivity.editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadIds() {
        myTextBox = (EditText) findViewById(R.id.region);
        txt_datetime = (TextView) findViewById(R.id.txt_datetime);

        toggle = findViewById(R.id.all);

        myTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SettingsActivity.editor.putString("region", s.toString());
                SettingsActivity.editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SettingsActivity.editor.putBoolean("is24Hour", true);
                    SettingsActivity.editor.commit();
                    loadtimestamp(true);
                } else {
                    SettingsActivity.editor.putBoolean("is24Hour", false);
                    SettingsActivity.editor.commit();
                    loadtimestamp(false);
                }
            }
        });
    }

    private void loadtimestamp(boolean is24Hour) {
        try {
            String val = convertTimestampTodate(spinner.getSelectedItem().toString(), is24Hour);
            txt_datetime.setText(val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String convertTimestampTodate(String formater, boolean is24Hour) throws ParseException {
        SimpleDateFormat weekFormatter;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        if (is24Hour) {
            String formate = formater + " HH:mm";
            weekFormatter = new SimpleDateFormat(formate, Locale.ENGLISH);
        } else {
            String formate = formater + " hh:mm aa";
            weekFormatter = new SimpleDateFormat(formate, Locale.ENGLISH);
        }
        String weekString = weekFormatter.format(date);
        return weekString;
    }

}
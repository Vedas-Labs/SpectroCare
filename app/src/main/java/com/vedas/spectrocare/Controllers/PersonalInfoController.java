package com.vedas.spectrocare.Controllers;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParsePosition;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vedas.spectrocare.LoginResponseModel.AppSettingsModel;
import com.vedas.spectrocare.ServerApiModel.MedicalRecordModel;
import com.vedas.spectrocare.model.PatientList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class PersonalInfoController {

    public static PersonalInfoController myObj;
    Context context;
    public MedicalRecordModel currectProfileData;
    public AppSettingsModel appSettingsModel;
    public List<String> feetArray;
    public List<String> waistArray;
    public List<String> ageValuesArray;
    public List<String> dayValuesArray;
    public List<String> cmArray;
    public List<String> lbsArray;
    public List<String> kgArray;
    public String[] weightunitsArray;
    public String[] heightUnitsArray;
    public String[] waistUnitsArray;
    public PatientList currentPatient;
    public String[] ageUnitsArray;
    public String[] dayUnitsArray;
    public ArrayList<String> yearArray;

    public static PersonalInfoController getInstance() {
        if (myObj == null) {
            myObj = new PersonalInfoController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
        currectProfileData = new MedicalRecordModel();
        currentPatient = new PatientList();
    }

    public void loadYearArray() {
        yearArray = new ArrayList<>();

        for (int i = 1900; i <= 2050; i++) {
            yearArray.add(String.valueOf(i));
        }


    }

    public void loadAgeUnitaArray() {
        ageUnitsArray = new String[1];
        ageUnitsArray[0] = "YEARS";

    }

    public void loadDaysUnitArray() {
        dayUnitsArray = new String[1];
        dayUnitsArray[0] = "Days";

    }

    public void loadAllUnitsArrays() {
        loadHeightUnitsArray();
        loadWeightUnitsArray();
        loadWaistUnitaArray();
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        // profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + imageInByte);
        return imageInByte;
    }

    public Bitmap convertByteArrayTOBitmap(byte[] profilePic) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(profilePic);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public void loadWaistUnitaArray() {
        waistUnitsArray = new String[1];
        waistUnitsArray[0] = "CM";
        //  waistUnitsArray[1]="Lbs";
    }

    public void loadWeightUnitsArray() {
        weightunitsArray = new String[2];
        weightunitsArray[0] = "Kgs";
        weightunitsArray[1] = "Lbs";
    }

    public void loadHeightUnitsArray() {
        heightUnitsArray = new String[2];
        heightUnitsArray[0] = "CM";
        heightUnitsArray[1] = "Feet";
    }

    public void loadHeightValuesArray() {
        cmArray = new ArrayList<>();
        waistArray = new ArrayList<>();
        ageValuesArray = new ArrayList<>();
        dayValuesArray = new ArrayList<>();


        for (int i = 46; i <= 243; i++) {
            cmArray.add(String.valueOf(i));
        }

        for (int i = 18; i <= 70; i++) {
            waistArray.add(String.valueOf(i));
        }

        for (int i = 10; i <= 100; i++) {
            ageValuesArray.add(String.valueOf(i));
        }
        for (int i = 0; i <= 100; i++) {
            dayValuesArray.add(String.valueOf(i));
        }


        loadFeetArrayInEnglish();
    }

    public void loadFeetArrayInEnglish() {
        feetArray = new ArrayList<String>();
        Log.e("ft", "call");
        for (int i = 1; i <= 8; i++) {
            if (i == 8) {
                String s = i + " " + "ft" + " " + "0" + " " + "in";
                feetArray.add(s);
            } else {
                if (i == 1) {
                    for (int j = 6; j <= 11; j++) {
                        String inchString = String.valueOf(j);
                        String s = i + " " + "ft" + " " + inchString + " " + "in";
                        feetArray.add(s);
                    }
                } else {
                    for (int j = 0; j <= 11; j++) {
                        String inchString = String.valueOf(j);
                        String s = i + " " + "ft" + " " + inchString + " " + "in";
                        feetArray.add(s);
                    }
                }
            }
        }
    }

    public void loadWeightValuesArray() {

        kgArray = new ArrayList<String>();
        lbsArray = new ArrayList<String>();

        Log.e("kg", "call");
        for (int i = 3; i <= 350; i++) {//29
            kgArray.add(String.valueOf(i));
        }

        for (int j = 6; j <= 772; j++) {
            lbsArray.add(String.valueOf(j));
        }
    }

    public String convertCmToFeet(String cmValue) {

        String[] cmValueArray = cmValue.split(" ");

        Double cmVal = Double.parseDouble(cmValueArray[0]);
        Log.e("cm", "call" + cmVal);
        double inchesValue = cmVal * 0.39370;
        Log.e("cm inches", "call" + cmVal / 12);


        int feetValue = (int) (inchesValue / 12);
        Log.e("fetvalu", "call" + (feetValue % 12));


        //   int remainInchesValue = (int) (inchesValue % 12);
        double remainInchesValue = (inchesValue % 12);

        Log.e("qqqqqqqq", "" + remainInchesValue + "round" + Math.round(remainInchesValue));


      /*  String remainInchString = String.valueOf(remainInchesValue);

        if (remainInchesValue>0 && remainInchesValue<10) {
            remainInchString = "0" + remainInchesValue;
        }*/

        String requiredString = feetValue + " " + "ft" + " " + Math.round(remainInchesValue) + " " + "in";

        Log.e("requiredFeetString", requiredString);
        return requiredString;

    }

    public String convertFeetToCm(String feetValue) {
        Log.e("convertFeetToCm", "" + feetValue);
        String requiredString = feetValue.replace("ft", ".");
        requiredString = requiredString.replace("in", "");
        requiredString = requiredString.replace(" ", "");

        String[] feetStrArray = requiredString.split("\\.");

        double totalInches = 0.0;
        double feetInches = Double.parseDouble(feetStrArray[0]);
        double inches = Double.parseDouble(feetStrArray[1]);

        Log.e("feetInches", "" + feetInches + "" + inches);
        totalInches = feetInches * 12 + inches;
        Log.e("totalInches", "" + totalInches);
        Double feetDouble = new Double(totalInches * 2.54);
        Log.e("feetDouble", "" + feetDouble);
        int feetint = (int) Math.round(feetDouble);
        if (feetint < 17) {
            feetint = 17;
        }
        Log.e("feetint", "call" + feetint);
        String strValue = String.valueOf(feetint);

        return strValue;

    }

    public String convertKgToLbs(String kgValue) {
        int kg = Integer.parseInt(kgValue);
        int kgVal = (int) ((kg) * 2.2046);
        String kgStr = String.valueOf(kgVal);
        return kgStr;
    }


    public String convertLbsToKg(String lbsValue) {
        Log.e("lbsValue", "call" + lbsValue);
        int kg = Integer.parseInt(lbsValue);
        double kgVal = kg * 0.453592;
        String kgStr = String.valueOf(Math.round(kgVal));
        Log.e("kgStr", "call" + kgStr);
        return kgStr;

    }

    public String calculatingAge(int year, int month, int day) {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }

    public String convertTimestampTodate(String stringData)
            throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        Log.e("timeStamp", "ss" + resultdate);
        String weekString = weekFormatter.format(resultdate);
        return weekString;
    }

    public String convertTimestampTodate1(String stringData)
            throws ParseException {
        Date date1 = null;
        date1 = new Date(Long.parseLong(stringData));
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");
        jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date1);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = null;
        String withdrawnTime = null;
        try {

            date = inputFormat.parse(java_date);
            withdrawnTime = sdf.format(date);
            return withdrawnTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return withdrawnTime;
    }

    public Date stringToDate(String aDate) {
        if (aDate == null) return null;
        // ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date stringDate = null;
        try {
            stringDate = weekFormatter.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;

    }

    public String convertDateToString(Date stringData) throws ParseException {
        SimpleDateFormat weekFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String weekString = weekFormatter.format(stringData);
        return weekString;
    }

    public String convertTimestampToMinutes(String stringData) throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        Log.e("yourmilliseconds", "" + yourmilliseconds);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = weekFormatter.format(resultdate);
        return weekString;
    }

    public String[] convertTimestampToslashFormate(String stringData) throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        Log.e("yourmilliseconds", "" + yourmilliseconds);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = weekFormatter.format(resultdate);
        String array[] = weekString.split(" ");
        Log.e("weeekarray", "" + array[0] + array[1] + array[2]);
        return array;
    }

    public String[] invoiceTimestampToslashFormate(String stringData) throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        Log.e("yourmilliseconds", "" + yourmilliseconds);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("MMM dd hh:mm a", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = weekFormatter.format(resultdate);
        String array[] = weekString.split(" ");
        Log.e("weeekarray", "" + array[0] + array[1] + array[2] + array[3]);
        return array;
    }

    public String invoiceTimestampToDate(String stringData) throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        Log.e("yourmilliseconds", "" + yourmilliseconds);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = weekFormatter.format(resultdate);
        //  String array[]=weekString.split(" ");
        //  Log.e("weeekarray", "" + array[0]+array[1]+array[2]+array[3]);
        return weekString;
    }

    public int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public String loadSettingsDataFormateToEntireApp(Context context,String timeStmap) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
      /*  boolean notification = sharedPreferences.getBoolean("allnotification", false);
        boolean formate = sharedPreferences.getBoolean("is24Hour", false);
        String units = sharedPreferences.getString("units", " ");
        String language = sharedPreferences.getString("language", "English");
*/
        Date date1 = null;
        date1 = new Date(Long.parseLong(timeStmap));

        String dateFormat = sharedPreferences.getString("dateFormat", "YYYY/MM/dd");
        Log.e("dateFormat", "call" + dateFormat);

        SimpleDateFormat jdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date1);

        return java_date;
    }
    public boolean loadHourFormateToEntireApp(){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean hourFormat = sharedPreferences.getBoolean("is24Hour", false);
        return hourFormat;
    }
}

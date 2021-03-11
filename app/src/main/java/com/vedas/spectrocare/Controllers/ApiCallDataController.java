package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.vedas.spectrocare.ServerApi;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCallDataController {
  public static ApiCallDataController myObj;
  ServerResponseInterface serverResponseInterface;
  public ServerApi  serverApi;
  public ServerApi  serverJsonApi;

  Context context;

  public static ApiCallDataController getInstance() {
    if (myObj == null) {
      myObj = new ApiCallDataController();
    }
    return myObj;
  }

  public void fillContent(Context context1) {
    context = context1;
    createRetrofotInstance();
    createRetrofotInstance1();
  }

  public void initializeServerInterface(ServerResponseInterface serverResponseInterface1) {
    if(serverResponseInterface !=null){
      serverResponseInterface=null;
    }
    serverResponseInterface = serverResponseInterface1;
  }
  public void createRetrofotInstance(){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServerApi.home_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    serverApi = retrofit.create(ServerApi.class);
  }
  public void loadServerApiCall(Call<JsonObject> apiCall, final String opetation) {
    Call<JsonObject> callable = apiCall;
    callable.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        Log.e("ffff",""+response.body());
        Log.e("gggg",""+response.message());
        if (response.body() != null) {
          String responseString = response.body().toString();
          JSONObject jsonObject = null;
          try {
            jsonObject = new JSONObject(responseString);
            Log.e("aaaaaaaaaaaa", "onResponse: " + jsonObject.toString());
            if (jsonObject.getString("message").equals("Inbox messages has been fetched succesfully")) {
              serverResponseInterface.successCallBack(jsonObject,opetation);
            } else if (jsonObject.getString("response").equals("3")) {
              serverResponseInterface.successCallBack(jsonObject,opetation);
            }  else {
              Log.e("response ","messsage");
              serverResponseInterface.failureCallBack(jsonObject.getString("message"));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        // Toast.makeText(context, "please check your network connection", Toast.LENGTH_SHORT).show();

        serverResponseInterface.failureCallBack(t.getMessage());
      }
    });
  }
  public void createRetrofotInstance1(){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServerApi.home_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    serverJsonApi = retrofit.create(ServerApi.class);
  }
  public void loadjsonApiCall(Call<JsonObject> apiCall, final String opetation) {
    Call<JsonObject> callable = apiCall;
    callable.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        Log.e("ffff",""+response.code());
        if (response.body() != null) {
          String responseString = response.body().toString();
          JSONObject jsonObject = null;
          try {
            jsonObject = new JSONObject(responseString);
            Log.e("aaaaaaaaaaaa", "onResponse: " + jsonObject.toString());
            if (jsonObject.getString("response").equals("3")) {
              serverResponseInterface.successCallBack(jsonObject,opetation);
            }  else {
              Log.e("response","message");
              //serverResponseInterface.failureCallBack(jsonObject.getString("message"));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        serverResponseInterface.failureCallBack(t.getMessage());
      }
    });
  }
  public interface ServerResponseInterface {
    void successCallBack(JSONObject jsonObject, String opetation);
    void failureCallBack(String failureMsg);
  }
}

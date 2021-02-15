package com.vedas.spectrocare.PatientChat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.ImageCompresser.IImageCompressTaskListener;
import com.vedas.spectrocare.ImageCompresser.ImageCompressTask;
import com.vedas.spectrocare.ImageCompresser.ScalingUtilities;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.patientModuleAdapter.InboxChatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatientChatActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    InboxChatAdapter chatAdapter;
    Socket socketOnline;
    private String mCurrentPhotoPath;
    boolean fetch=true;
    boolean scrollTo,isUserOnline;
    Uri photoURI;
    String strDocName,strUrl,strCharecters;
    ArrayList<MessagesListModel> listModel = new ArrayList<>();
    ImageView imgBack, imgSend, imgAttach, imgSelected;
    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;
    private static final int FILE_PICKER_REQUEST_CODE = 8001;
    String filenmae, profileBase64Obj,strName,strText1;
    File file;
    Intent picIntent;
    MessagesListModel msgModel;
    String replyMsg="",replayMessage,replayMsgID,strUniqueMsgId;
    CircularImageView imgPic;
    TextView txtUri, txtDocName,txtDoc,txtMsg,txtTyping,txtOnline;
    Button btnStatus;
    EditText edtSendingTxt;
    ImageView imgPlay,imgCancel,imgReplay;
    private Bitmap bitmap;
    String urlStr = "", docID = "", appointmentID = "", patientID;
    AlertDialog dialog;
    VideoView video;
    ChatModel chatModel;
    WifiReceiver wifiReceiver;
    LinearLayout layoutPhoto;
    boolean isImg,isBottom;
    Socket mSocket;
    private ImageCompressTask imageCompressTask;
    ArrayList<ChatModel> chatList;
    ArrayList<MessageModel> messageList;
    ArrayList<MessagesListModel> messagesListModels;
    RelativeLayout relaVideo,selectedLayout;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    ImageView imgDeleteAll;
    String extenction;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    Uri uri;
    File photoFile;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);

        Log.e("chhcakfdfads","asdfdsf"+fetch);
       /* ChatApplication app = (ChatApplication) getApplicationContext();
        mSocket = app.getSocket();

*/
        mSocket = SocketIOHelper.getInstance().socket;
        SocketIOHelper.getInstance().listenEvents();

        mSocket.on("getRoomMessages", onNewMessage);
        mSocket.on("sendMessage", onNewMessage);
       // mSocket.on("join", onNewMessage);
        mSocket.on("updateChatMessageIsRead", onReplayMessage);
        mSocket.on("unsubscribe",onReplayMessage);
        mSocket.on("deleteChatHostory",onReplayMessage);
        // mSocket.on("sendMessage", onReplayMessage);
        mSocket.on("typing", onReplayMessage);

        castingAndClicks();
        loadResponse();
        Log.e("wifiREciver","::"+wifiReceiver.isOnline());

        mSocket.on("userConnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        txtOnline.setText("Online");
                        //  btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                        btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                        btnStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorGreen)));
                        for (int i =0;i< PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
                            if (PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentID().equals(appointmentID)){
                                PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getDoctorDetails().getProfile().getUserProfile().setOnline(true);
                            }
                        }
                    }
                });
                JSONObject data = (JSONObject) args[0];
                Log.e("afafffewdsfsd","d"+data.toString());
               // joinChat(patientID,appointmentID);

            }
        });
        mSocket.on("userDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.e("afafffewdsfsd","df"+data.toString());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        txtOnline.setText("Offline");
                        btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                        btnStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorOrange)));
                        for (int i =0;i< PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
                            if (PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentID().equals(appointmentID)){
                                PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getDoctorDetails().getProfile().getUserProfile().setOnline(false);
                            }
                        }
                    }
                });

            }
        });

        ItemTouchHelper.SimpleCallback touchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                replyMsg = "reply";
                imgAttach.setVisibility(View.GONE);
                Gson gson = new Gson();
                Log.e("dddfda","da"+ChatDataController.getInstance().getMessageModelArrayList()
                        .get(0).getMessages().get(viewHolder.getAdapterPosition()));
                selectedLayout.setVisibility(View.VISIBLE);
                if (ChatDataController.getInstance().getMessageModelArrayList()
                        .get(0).getMessages().get(viewHolder.getAdapterPosition()).getUserID().equals(patientID)){
                    strName = PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName();
                    txtDoc.setText(strName);
                }else{
                    strName =strDocName ;
                    txtDoc.setText(strName);
                }
                if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(viewHolder.getAdapterPosition()).getAttachments()
                        !=null){
                    strUrl = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(viewHolder.getAdapterPosition()).getAttachments()
                            .get(0).getFilePath();
                    txtMsg.setVisibility(View.GONE);
                    // imgReplay.setImageURI(Uri.parse(ServerApi.img_home_url+strUrl));
                    imgReplay.setVisibility(View.VISIBLE);
                    layoutPhoto.setVisibility(View.VISIBLE);

                    // Picasso.get().load(Uri.parse(ServerApi.img_home_url+strUrl)).placeholder(R.drawable.profile_1).into(imgReplay);
                  if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(viewHolder.getAdapterPosition()).getType().equals("loadId")){
                      Glide.with(PatientChatActivity.this)
                              .load(Uri.parse(strUrl))
                              .placeholder(R.drawable.profile_1)
                              .into(imgReplay);

                  }else{
                      Glide.with(PatientChatActivity.this)
                              .load(Uri.parse(ServerApi.img_home_url+strUrl))
                              .placeholder(R.drawable.profile_1)
                              .into(imgReplay);
                  }
                  Log.e("chateeee","tutiii"+strUrl);
                  if (strUrl.contains("ChatFiles"))
                    replayMessage=ServerApi.img_home_url+strUrl;
                  else
                      replayMessage = strUrl;

                }
                else{

                    layoutPhoto.setVisibility(View.GONE);
                    txtMsg.setVisibility(View.VISIBLE);
                    imgReplay.setVisibility(View.GONE);
                    imgReplay.setVisibility(View.GONE);
                    replayMessage=ChatDataController.getInstance().getMessageModelArrayList()
                            .get(0).getMessages().get(viewHolder.getAdapterPosition()).getMessage();

                }
                txtMsg.setText(ChatDataController.getInstance().getMessageModelArrayList()
                        .get(0).getMessages().get(viewHolder.getAdapterPosition()).getMessage());
                replayMsgID = ChatDataController.getInstance().getMessageModelArrayList()
                        .get(0).getMessages().get(viewHolder.getAdapterPosition()).getMessageID();
                chatAdapter.notifyDataSetChanged();
              //  chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());

               /* edtSendingTxt.requestFocus();
                edtSendingTxt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                edtSendingTxt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));*/
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(chatRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("dfasf","dddddddddf");
        // mSocket.disconnect();
        mSocket.off("sendMessage", onNewMessage);
        mSocket.on("userDisconnected",onReplayMessage);
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReciver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReciver);
    }

    private BroadcastReceiver wifiStateReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra){
                case WifiManager.WIFI_STATE_ENABLED:
                    isUserOnline=true;
                    if (isBottom) {
                        isBottom=false;
                       // Log.e("checkkk","on");
                        Log.e("isWifi","On");


                        joinChat(patientID,appointmentID);
                        for (int bm=0;bm<ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();bm++){
                            if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getType().equals("dummy")){
                                Log.e("valuesOF","::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessage()+" ,"+
                                        ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessageID());
                                String idd =ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessageID();
                               String msg = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessage();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getRepliedMessageID().isEmpty()){
                                    sendMessages(patientID,appointmentID,idd,msg);
                                }else{
                                    String rplIdd =ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessageID();
                                    String rplID = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getRepliedMessageID();
                                    String rplMsg = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessage1();
                                    String rplName = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getRecipientRepliedName();
                                   String rplmessage = ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(bm).getMessage();
                                    Log.e("messageeads","ond "+rplmessage);
                                    replayToMsg(patientID,appointmentID,rplmessage,rplIdd,rplID,rplMsg,rplName);
                                }
                            }
                        }
                       // ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages()
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fetchChat(patientID,appointmentID);

                    }
/*
                    if (isBottom){
                        isBottom = false;
                        for (int b=0;b<ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();b++){
                            if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(b).getType().equals("dummy")){
                                strUniqueMsgId = "MSG_Android_"+getSaltString();
                                sendMessages(patientID,appointmentID,strUniqueMsgId,
                                        ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(b).getMessage());
                            }
                        }

                    }
*/
                    break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        isUserOnline = false;
                        isBottom = true;
                        Log.e("isWifi","off");
                        if (isBottom){
                            Log.e("checkkk","off");                        }
                        break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void castingAndClicks() {

       /* for (int i = 0; i < 8; i++) {
            strText1 += strCharecters.charAt(Math.floor(Math.random() * strCharecters.length()));
        }*/
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_STATE_ALWAYS_HIDDEN );
         msgModel = new MessagesListModel();
         messagesListModels = new ArrayList<>();
         isBottom = false;
       Log.e("strRandom","dd: "+getSaltString());
        Log.e("tex","variable: "+strText1);
        wifiReceiver = new WifiReceiver();
        imgBack = findViewById(R.id.img_back_arrow);
        imgAttach = findViewById(R.id.img_attach);
        imgSelected = findViewById(R.id.img_selected);
        video = findViewById(R.id.vv1);
        imgPic = findViewById(R.id.circular_image);
        imgCancel = findViewById(R.id.img_cancel);
        imgDeleteAll = findViewById(R.id.delete_all_chat);
        messageList = new ArrayList<>();
        layoutPhoto = findViewById(R.id.layout_photo);
        progressBar = findViewById(R.id.progressbar);
        txtOnline = findViewById(R.id.txt_doc_online);
        btnStatus = findViewById(R.id.btn_status);
        patientID = PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId();
        chatList = new ArrayList<>();
        imgReplay = findViewById(R.id.img_replay);
        selectedLayout = findViewById(R.id.selected_msg_layout);
        txtDoc = findViewById(R.id.txt_selected_doc);
        txtMsg = findViewById(R.id.txt_selected_msg);
        txtDocName = findViewById(R.id.txt_doc_name);
        txtTyping = findViewById(R.id.txt_typing);
        relaVideo = findViewById(R.id.rl_video);
        imgSend = findViewById(R.id.img_send);
        edtSendingTxt = findViewById(R.id.edt_sending_txt);
        // txtUri = findViewById(R.id.txt_url);
        chatRecyclerView = findViewById(R.id.chat_recycleView);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setItemViewCacheSize(20);
        chatRecyclerView.setItemAnimator(null);
        chatRecyclerView.setDrawingCacheEnabled(true);
        chatRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        docOnline();
        chatRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ( bottom < oldBottom) {
                    chatRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("checking","daf"+isBottom);

                                chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());

                        }
                    }, 100);
                }
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyMsg = "";
                selectedLayout.setVisibility(View.GONE);
                imgAttach.setVisibility(View.VISIBLE);
            }
        });
        Intent docIntent = getIntent();
        if (docIntent.hasExtra("docName")) {
            docID = docIntent.getStringExtra("docId");
            appointmentID = docIntent.getStringExtra("appointmentID");
            Log.e("docId", "is :" + docID + " ," + PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id()
                    + " ," + PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId()
                    + " ," + PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId() + "," + appointmentID);
            if (!docIntent.getStringExtra("docPic").isEmpty())
                Picasso.get().load(docIntent.getStringExtra("docPic")).placeholder(R.drawable.image).into(imgPic);
            if (docIntent.hasExtra("docName")) {
                strDocName = docIntent.getStringExtra("docName");
                txtDocName.setText(strDocName);
            }
            boolean isOnline = docIntent.getBooleanExtra("isOnline",false);
            Log.e("booleanValue","is : "+isOnline);

            if (isOnline){
                txtOnline.setText("Online");
                //  btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                btnStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorGreen)));
            }else{
                txtOnline.setText("Offline");
                btnStatus.setBackgroundResource(R.drawable.btn_yellow_backgroound);
                btnStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorOrange)));
            }

            fetchChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), appointmentID);
        }

        chatAdapter = new InboxChatAdapter(PatientChatActivity.this,edtSendingTxt,appointmentID);
        layoutManager =new LinearLayoutManager(PatientChatActivity.this);
        chatRecyclerView.setLayoutManager(layoutManager);
       // chatRecyclerView.setAdapter(chatAdapter);

        patientID = PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId();

       /* if (!ChatDataController.isNull()) {
            if (ChatDataController.getInstance().getMessageModelArrayList()!=null)
           imgDeleteAll.setVisibility(View.VISIBLE);
        }else{
            imgDeleteAll.setVisibility(View.GONE);
        }*/
        RecyclerView.ItemAnimator animator = chatRecyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgPlay = findViewById(R.id.img_play);

        edtSendingTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                imgSelected.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                isTyping(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

                isTyping(false);
            }
        });

        chatRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isLastVisible()){
                    Log.e("dfs","st"+isBottom);

                  //  isBottom=true;
                     isReadMsgs();
                }
            }
        });

        edtSendingTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                Log.e("isssss",":"+isLastVisible());

            }
        });
/*
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUniqueMsgId = "MSG_Android_"+getSaltString();
                Log.e("idMessage","mes:: "+strUniqueMsgId);
                selectedLayout.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.VISIBLE);
                imgSelected.setVisibility(View.GONE);
                imgAttach.setVisibility(View.VISIBLE);
                relaVideo.setVisibility(View.GONE);
                isTyping(false);

                if (replyMsg.equals("reply")){
                    replyMsg = "";
                    replayToMsg(patientID,appointmentID,strUniqueMsgId);
                    edtSendingTxt.getText().clear();
                    Log.e("replay","is on");
                }else{
                    if (chatAdapter.isEdit()){
                        Log.e("isUpppdd",": "+chatAdapter.isEdit());
                        if (!edtSendingTxt.getText().toString().isEmpty()) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtSendingTxt.getWindowToken(), 0);
                            edtSendingTxt.getText().clear();
                        }

                    }else{
                        Log.e("isUpppdd",":: "+chatAdapter.isEdit());
                        if (!edtSendingTxt.getText().toString().isEmpty()){
                            Log.e("ettext","::"+edtSendingTxt.getText().toString());
                            chatAdapter.editChatMessage(edtSendingTxt.getText().toString());
                        }
                    }
                }
                //  chatAdapter.editChatMessage("text");
                Log.e("appointment", "ID :" + appointmentID);

            }
        });
*/
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUniqueMsgId = "MSG_Android_"+getSaltString();
                imgAttach.setVisibility(View.VISIBLE);
                Log.e("idMessage","mes:: "+strUniqueMsgId);
                selectedLayout.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.VISIBLE);
                imgSelected.setVisibility(View.GONE);
                imgAttach.setVisibility(View.VISIBLE);
                relaVideo.setVisibility(View.GONE);
                isTyping(false);


                InputMethodManager immm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                immm.hideSoftInputFromWindow(edtSendingTxt.getWindowToken(), 0);


                if (replyMsg.equals("reply")){
                    replyMsg = "";
                    Log.e("messasgesaae","re "+replayMessage);
                    MessagesListModel modelMsg = new MessagesListModel();
                    modelMsg.setLiked(false);
                    modelMsg.setRecipientRepliedName(strName);
                    modelMsg.setRepliedMessageID(replayMsgID);
                    modelMsg.setMessage1(replayMessage);
                    modelMsg.setMessage(edtSendingTxt.getText().toString());
                    modelMsg.setMessageID(strUniqueMsgId);
                    modelMsg.setEdited(false);
                    modelMsg.setUserID(patientID);
                    modelMsg.setType("dummy");
                    modelMsg.setRead(false);
                    modelMsg.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));

                    ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(modelMsg);

                    chatAdapter.notifyItemRangeInserted(chatRecyclerView.getAdapter().getItemCount()-1,chatRecyclerView.getAdapter().getItemCount());
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
                    if (isUserOnline){
                        replayToMsg(patientID,appointmentID,edtSendingTxt.getText().toString(),strUniqueMsgId,replayMsgID,replayMessage,strName);
                    }
                    edtSendingTxt.getText().clear();
                    Log.e("replay","is on");
                }else{
                    if (chatAdapter.isEdit()){
                        Log.e("isUpppdd",": "+chatAdapter.isEdit());
                        if (!edtSendingTxt.getText().toString().isEmpty()) {

                            MessagesListModel modelMsg = new MessagesListModel();
                            modelMsg.setLiked(false);
                            modelMsg.setRecipientRepliedName("");
                            modelMsg.setRepliedMessageID("");
                            modelMsg.setMessage1("");
                            modelMsg.setMessage(edtSendingTxt.getText().toString());
                            modelMsg.setMessageID(strUniqueMsgId);
                            modelMsg.setEdited(false);
                            modelMsg.setUserID(patientID);
                            modelMsg.setType("dummy");
                            modelMsg.setRead(false);
                            modelMsg.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));

                            if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()>0) {
                                Log.e("arraySize", "arraySize ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(modelMsg);
                                Log.e("arraySize", "arraySiz ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                // chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
                                chatAdapter.notifyItemInserted(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
                                //  chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                Log.e("idOf","doc:: "+modelMsg.getUserID());

                            } else {
                                listModel.clear();
                                Log.e("arraySize", "arraySizeO ::");
                                listModel.add(modelMsg);
                                ChatDataController.getInstance().getMessageModelArrayList().get(0).setMessages(listModel);
                                Log.e("arrayy","sizO"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                chatAdapter.notifyDataSetChanged();
                            }
                            if (isUserOnline){
                                Log.e("isUserFellow","online");
                                sendMessages(patientID, appointmentID,strUniqueMsgId,edtSendingTxt.getText().toString());
                            }else{
                                Log.e("isUserFellow","offline");
                            }
                            //  replayToMsg(patientID,appointmentID);
/*
                        if (!ChatDataController.isNull()) {
                            chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());


*/
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtSendingTxt.getWindowToken(), 0);
                            edtSendingTxt.getText().clear();
                        }

                    }else{
                        Log.e("isUpppdd",":: "+chatAdapter.isEdit());
                        if (!edtSendingTxt.getText().toString().isEmpty()){
                            Log.e("ettext","::"+edtSendingTxt.getText().toString());
                            chatAdapter.editChatMessage(edtSendingTxt.getText().toString());
                        }
                    }
                }
                //  chatAdapter.editChatMessage("text");
                Log.e("appointment", "ID :" + appointmentID);

            }
        });

        edtSendingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheet();
            }
        });
        imgDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager =((LinearLayoutManager) chatRecyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems =  chatAdapter.getItemCount();
        return (pos >= numItems - 1);
    }


    public void docOnline(){
        JsonObject object = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID",patientID);
            jsonObject.put("userType","Patient");
            JsonParser jsonParser = new JsonParser();
            object = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("userConnected", jsonObject);

    }

    public void unSubscribe(){
        JsonObject object = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID",appointmentID);
            JsonParser jsonParser = new JsonParser();
            object = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("unsubscribe", jsonObject);

    }

    public void deleteAllChatHistory(){
        JsonObject object = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID",patientID);
            jsonObject.put("roomID",appointmentID);
            JsonParser jsonParser = new JsonParser();
            object = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("deleteChatHostory", jsonObject);
        Log.e("idSocket:", " " + mSocket.id());
/*
        if (mSocket.id()!=null){
            ChatDataController.getInstance().getMessageModelArrayList().clear();
            chatAdapter.notifyDataSetChanged();
        }
*/
    }
    private void joinChat(String userID, String roomid) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomid);
            jsonObject.put("userID", userID);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket11", "message" + mSocket.id());
        mSocket.emit("subscribe", jsonObject);

    }
    private void isReadMsgs() {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", appointmentID);
            jsonObject.put("userID", docID);
            jsonObject.put("isDoctor", false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // boolean isread = true;
        Log.e("socket16", " is Read" + mSocket.id());
        mSocket.emit("updateChatMessageIsRead", jsonObject);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet() {
        edtSendingTxt.setText("");
        TextView cam, gal, canc, file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(PatientChatActivity.this), R.style.BottomSheetDialogTheme);
        cameraBottomSheetDialog.setContentView(dialogView);
        cam = cameraBottomSheetDialog.findViewById(R.id.camera);
        gal = cameraBottomSheetDialog.findViewById(R.id.gallery);
        file = cameraBottomSheetDialog.findViewById(R.id.file);
        canc = cameraBottomSheetDialog.findViewById(R.id.cancel);
        FrameLayout bottomSheet = (FrameLayout) cameraBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        cameraBottomSheetDialog.show();
        canc.setTextColor(Color.parseColor("#53B9c6"));

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    LoadCaptureImageScreen();
                } else {
                    cameraBottomSheetDialog.cancel();
                    // dialog.dismiss();
                    requestCameraPermission();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    showFileChooser();
                } else {
                    cameraBottomSheetDialog.cancel();
                    requestStoragePermission();
                }

            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    LoadImageFromGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheetDialog.cancel();
            }
        });

    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Camera Permission is Needed for Adding your Profile Image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PatientChatActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(PatientChatActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);

    }

    public void alertDialog() {
       /* LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.alert_abort, null);
*/
        final Dialog dialog = new Dialog(PatientChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        dialog.show();
        btnNo.setText("No");
        btnYes.setText("Yes");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete");
        txt_msg.setText("Are you sure you");
        txt_msg1.setText("want to delete all messages ?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllChatHistory();
                dialog.dismiss();
            }
        });

    }
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
           /* Bitmap bitmap1 = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap1);
            bitmap = rotatedBitmap;
            urlStr = mCurrentPhotoPath;*/
            Log.e("uuuuuf", "afad" + getRealPathFromURI(photoURI));
            Log.e("uuuuuf", "afda" + photoURI);
           // imgSelected.setVisibility(View.VISIBLE);
          //  imgAttach.setVisibility(View.GONE);
          //  edtSendingTxt.setVisibility(View.GONE);
          //  imgSelected.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
          //  loadEncoded64ImageStringFromBitmap(bitmap);

            strUniqueMsgId = "MSG_Android_"+getSaltString();
            Log.e("asadfadsfsafdafdaf", "uri " + photoURI);
            urlStr = String.valueOf(photoURI);
            Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), photoURI, new String[]{MediaStore.Images.Media.DATA});

            if (urlStr.contains("Pictures")) {
                Log.e("urii", "image " + photoURI);
                imgSelected.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                imgAttach.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.VISIBLE);
                imgSelected.setImageURI(photoURI);
                ChatAttachmentModel attachmentModel = new ChatAttachmentModel();
                msgModel.setRead(false);
                msgModel.setType("SingleImage");
                msgModel.setMessageID("id");
                msgModel.setMessage("File Attachment");
                msgModel.setUserID(patientID);
                msgModel.setEdited(false);
                msgModel.setMessage1("");
                msgModel.setEdited(false);
                msgModel.setLiked(false);
                msgModel.setRepliedMessageID("");
                msgModel.setRecipientRepliedName("");
                msgModel.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                attachmentModel.setFilePath(String.valueOf(photoURI));
                ArrayList<ChatAttachmentModel> attachList = new ArrayList<>();
                attachList.add(attachmentModel);
                msgModel.setAttachments(attachList);
                ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(msgModel);
                // chatAdapter.notifyDataSetChanged();
                chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
                int mmm =ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();
                chatRecyclerView.scrollToPosition(mmm-1);

                if(cursor != null && cursor.moveToFirst()) {

                    String path = getRealPathFromURI(photoURI);
                    Log.e("paaathi","coming:: "+path);
                    File file = new File(path);
                    Log.e("asadfadsfsafdafdaf","kdkddk"+path);
                    extenction = path.substring(path.lastIndexOf(".") + 1);
                    //Create ImageCompressTask and execute with Executor.
                    imageCompressTask = new ImageCompressTask(this, path, iImageCompressTaskListener);
                    // String path = getRealPathFromURI(photoURI);
                    mExecutorService.execute(imageCompressTask);
                    // String path = getRealPathFromURI(photoURI);

                }

            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientChatActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            strUniqueMsgId = "MSG_Android_"+getSaltString();
            uri = imageReturnedIntent.getData();
            Log.e("checkURI", "uri " + uri);
            urlStr = String.valueOf(uri);
            Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), uri, new String[]{MediaStore.Images.Media.DATA});

            if (urlStr.contains("image")) {
                Log.e("urii", "image " + uri);
                imgSelected.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                imgAttach.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.VISIBLE);
                imgSelected.setImageURI(uri);
                ChatAttachmentModel attachmentModel = new ChatAttachmentModel();
                msgModel.setRead(false);
                msgModel.setType("SingleImage");
                msgModel.setMessageID("id");
                msgModel.setMessage("File Attachment");
                msgModel.setUserID(patientID);
                msgModel.setEdited(false);
                msgModel.setMessage1("");
                msgModel.setEdited(false);
                msgModel.setLiked(false);
                msgModel.setRepliedMessageID("");
                msgModel.setRecipientRepliedName("");
                msgModel.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                attachmentModel.setFilePath(String.valueOf(uri));
                ArrayList<ChatAttachmentModel> attachList = new ArrayList<>();
                attachList.add(attachmentModel);
                msgModel.setAttachments(attachList);
                ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(msgModel);
               // chatAdapter.notifyDataSetChanged();
                chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
                int mmm =ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();
                chatRecyclerView.scrollToPosition(mmm-1);

                try {
                    if(cursor != null && cursor.moveToFirst()) {
                        String path = getFilePath(getApplicationContext(), uri);
                        File file = new File(path);
                        Log.e("asadfadsfsafdafdaf","kdkddk");

                        extenction = path.substring(path.lastIndexOf(".") + 1);

                        //Create ImageCompressTask and execute with Executor.
                        imageCompressTask = new ImageCompressTask(this, path, iImageCompressTaskListener);

                        mExecutorService.execute(imageCompressTask);
                    }
                } catch (URISyntaxException e) {
                    Log.e("URISyntaxException", "call" + e.getMessage());
                }

            } else if (urlStr.contains("video")) {
                relaVideo.setVisibility(View.VISIBLE);
                imgSelected.setVisibility(View.GONE);
                imgAttach.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.GONE);
                Log.e("urii", "video " + uri);
                video.setVisibility(View.VISIBLE);
                video.setVideoURI(uri);
                imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgPlay.setVisibility(View.GONE);
                        video.start();
                    }
                });
                // video.start();
            }

        } else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = imageReturnedIntent.getData();
            urlStr = String.valueOf(fileUri);
            Log.e("fileName", "path" + urlStr);

            imgSelected.setImageResource(R.drawable.pdf);
            imgSelected.setVisibility(View.VISIBLE);
            edtSendingTxt.setVisibility(View.GONE);
            imgAttach.setVisibility(View.GONE);


            Uri uri = imageReturnedIntent.getData();
            File myFile = new File(uri.toString());
            mCurrentPhotoPath = myFile.getPath();
            Log.e("noooo", "call" + mCurrentPhotoPath);
           /* try {
                mCurrentPhotoPath = getFilePath(getApplicationContext(), uri);
                Log.e("mpath", "call" + getFilePath(getApplicationContext(), uri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
*/


            Log.e("uri", "iru" + fileUri);
        } else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientChatActivity.this, "File Selection Cancelled", Toast.LENGTH_SHORT).show();
        } else {
           // dialog.dismiss();
            Toast.makeText(this, "Attachment selection cancelled", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean isImgSend(){
        return isImg;
    }
    private IImageCompressTaskListener iImageCompressTaskListener = new IImageCompressTaskListener() {
        @Override
        public void onComplete(List<File> compressed) {
            //photo compressed. Yay!

            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)

            File file = compressed.get(0);
            byte[] fileContent = new byte[0];
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e("asadfadsfsafdafdaf","vvvvvvvv");
                    fileContent = Files.readAllBytes(file.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("asadfadsfsafdafdaf","adadsfaf");
            }
            Log.e("path", "call" + file.getName() + extenction+fileContent.length);

            isImg =true;

            //  chatAdapter.notifyDataSetChanged();
            ViewController.getInstance().uploadFileAction(file, extenction,appointmentID,patientID,strUniqueMsgId,fileContent);


            Log.d("ImageCompressor", "New photo size ==> " + file.length()); //log new file size.

           // imgAttach.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        @Override
        public void onError(Throwable error) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error);
        }
    };

    private void showFileChooser() {
        Intent intent;
        if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            Log.e("callSam", "hello samsung");
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {

            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_PICKER_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        Log.e("inUri","adf"+uri);
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            Log.e("thsi","da");
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private Bitmap rotatedImageBitmap(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(PatientChatActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(PatientChatActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void LoadCaptureImageScreen() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(PatientChatActivity.this,
                        "com.vedas.spectrocare.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private String decodeFile(String path) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {

            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 50, 50, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 50, 50, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/myTmpDir");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unSubscribe();
        // mSocket.disconnect();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("currentpath", "call" + mCurrentPhotoPath);
        return image;
    }
    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Gallery Permission is needed for adding your Profile Image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(PatientChatActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(PatientChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

    private  Emitter.Listener onReplayMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.e("caaalll", "cc");
            Log.e("onine", " chat message" + data.toString());
            try {
                Log.e("musususud", " chat message" + data.toString());
                Log.e("messageeee",":: "+data.get("isDoctor"));
                if (data.get("message").equals("Messages status updated successfully")){
                    boolean b= Boolean.parseBoolean(String.valueOf(data.get("isDoctor")));
                    Log.e("obobob","dd:: "+b);
                    if (b){
                        Log.e("dadf","adf");
                        for (int m=0;m<ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();m++){
                            if (!ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(m).isRead){
                                ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(m).setRead(true);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.notifyDataSetChanged();
                               // chatAdapter.notifyItemRangeInserted(chatRecyclerView.getAdapter().getItemCount()-1,chatRecyclerView.getAdapter().getItemCount());
                            }
                        });

                    }
                }else if (data.get("message").equals("Chat history deleted successfully")){
                    Log.e("adfsadfdafds","adfasfafsdfas");
                    imgDeleteAll.setVisibility(View.GONE);
                    ChatDataController.setNull();
                    chatAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Log.e("isType",":: "+data.get("isTyping"));
                if (data.get("isTyping").equals(true)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isBottom=false;
                            String firstName[] = strDocName.split(" ");
                            txtTyping.setText(firstName[0]+" is typing....");
                            txtTyping.setVisibility(View.VISIBLE);

                        }
                    });

                }else if(data.get("isTyping").equals(false)){
                    Log.e("falsee","dff");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtTyping.setVisibility(View.GONE);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    ///response on this function.

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.e("caaalll", "cc");
                    Log.e("response for socket", " chat message" + data.toString());
                    String response;
                    String message = null;
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject(data.toString());
                        response = jsonObj.getString("response");
                        message = jsonObj.getString("message");
                        Log.e("rreeess", "ponse" + message);
                        if (Integer.parseInt(response) == 3) {
                            if (message.equals("Room fetched successfully")) {

                                    Log.e("length of", "room" + jsonObj.getJSONArray("room").length());
                                    JSONArray responseArray = jsonObj.getJSONArray("room");
                                    for (int l = 0; l < responseArray.length(); l++) {
                                        Gson gson = new Gson();
                                        String jsonString = jsonObj.getJSONArray("room").getJSONObject(l).toString();
                                        MessageModel messageModel = gson.fromJson(jsonString, MessageModel.class);
                                        String kk = gson.toJson(messageModel);
                                        Log.e("logaa", "dafaa" + messageModel.getMessages().size());
                                        if (messageList.size() > 0) {
                                            Log.e("sixxxxx", "vvv" + messageList.size());
                                            messageList.set(0, messageModel);

                                            Log.e("sixxxxx", "dd" + messageList.get(0).getMessages().size());
                                            Log.e("sixxxxx", "vvv" + messageList.size());
                                        } else {
                                            messageList.add(messageModel);
                                        }

                                      //  Log.e("sixxxxx", "ee " + messageList.get(0).getMessages().size());
                                    }
                                   // Log.e("sixxxxx", "ff " + messageList.get(0).getMessages().size());
                                   // Log.e("listu", "sizu" + messageList.get(0).getMessages().size());
                                    ChatDataController.getInstance().setMessageModelArrayList(messageList);

                                    chatRecyclerView.setAdapter(chatAdapter);

                                    //chatAdapter.notifyDataSetChanged();
                                   // layoutManager.scrollToPosition(chatRecyclerView.getAdapter().getItemCount());
                                   // chatRecyclerView.setItemAnimator(null);
                                  //  chatAdapter.notifyItemRangeInserted(0,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());

                                    if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()>0){
                                        Log.e("sssssssssssize", "sizu" + ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                      // chatRecyclerView.scrollToPosition(chatRecyclerView.getAdapter().getItemCount()-1);
                                        final int lastItemPosition = chatAdapter.getItemCount() - 1;

                                        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
                                        chatRecyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // then scroll to specific offset
                                                View target = layoutManager.findViewByPosition(lastItemPosition);
                                                if (target != null) {
                                                    int offset = chatRecyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                                                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);
                                                }
                                            }
                                        });
                                       // chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
                                    }

                                    // mSocket.off("getRoomMessages", onNewMessage);

                            } else {
                                Log.e("not", "happening");
                                if (Integer.parseInt(response) == 3) {
                                    txtTyping.setVisibility(View.GONE);
                                    Gson gson = new Gson();
                                    String object = data.getJSONObject("messageData").toString();
                                    MessagesListModel messageModel = gson.fromJson(object, MessagesListModel.class);
                                    Log.e("object", "msgObject" + object);
                                    Log.e("arrayy","size"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                    if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()>0) {
                                        Log.e("arraySize", "arraySize ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());

                                        for (int i=0; i<ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size();i++){
                                            if (messageModel.getMessageID().equals(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(i).getMessageID())){
                                                Log.e("arrayID","is :: "+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(i).getMessageID());
                                                ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().set(i,messageModel);
                                            }/*else {
                                                Log.e("notequal","is :: "+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(i).getMessageID());
                                            }*/

                                        }


                                        chatAdapter.notifyDataSetChanged();
                                       // chatAdapter.notifyItemInserted(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
                                        Log.e("checking", "arraySiz ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());

                                        // chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
                                        chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);

                                        Log.e("idOf","doc:: "+messageModel.getUserID());
                                        if (!messageModel.getUserID().equals(patientID)){
                                           // isReadMsgs();
                                            Log.e("idOf","doc:: "+messageModel.getUserID());
                                            for (int m=0;m<ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1;m++){
                                                if (!ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(m).isRead){
                                                    ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(m).setRead(true);
                                                }
                                            }
                                            ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(messageModel);
                                            chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1
                                                    ,ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                            chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
                                           // chatAdapter.setHasStableIds(true);
                                        }
                                    } else {
                                        listModel.clear();
                                        Log.e("arraySize", "arraySizeO ::");
                                        listModel.add(messageModel);
                                        ChatDataController.getInstance().getMessageModelArrayList().get(0).setMessages(listModel);
                                        Log.e("arrayy","sizO"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                                        chatAdapter.notifyDataSetChanged();

                                    }


                                }
                                // fetchChat(patientID,appointmentID);
                            }
                            Log.e("calling", "call");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(Chating_activity.this,message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void sendMessages(String userID, String roomID,String messageId,String message) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("userID", userID);
            jsonObject.put("message", message);
            jsonObject.put("message1", "");
            jsonObject.put("messageID", messageId);
            jsonObject.put("repliedMessageID", "");
            jsonObject.put("audioDuration", "");
            jsonObject.put("recipientRepliedName", "");
            jsonObject.put("type", "regular");

            //jsonObject.put("isDoctor",false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* MessagesListModel modelMsg = new MessagesListModel();
        modelMsg.setLiked(false);
        modelMsg.setRecipientRepliedName("");
        modelMsg.setRepliedMessageID("");
        modelMsg.setMessage1("");
        modelMsg.setMessage(message);
        modelMsg.setMessageID(messageId);
        modelMsg.setEdited(false);
        modelMsg.setUserID(userID);
        modelMsg.setType("dummy");
        modelMsg.setRead(false);
        modelMsg.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()>0) {
            Log.e("arraySize", "arraySize ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
            ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(modelMsg);
            Log.e("arraySize", "arraySiz ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
           // chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
           chatAdapter.notifyItemInserted(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
          //  chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
             chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
            Log.e("idOf","doc:: "+modelMsg.getUserID());

        } else {
            listModel.clear();
            Log.e("arraySize", "arraySizeO ::");
            listModel.add(modelMsg);
            ChatDataController.getInstance().getMessageModelArrayList().get(0).setMessages(listModel);
            Log.e("arrayy","sizO"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
            chatAdapter.notifyDataSetChanged();
        }*/

        mSocket.emit("sendMessage", jsonObject);
    }

    public void scrollToSelectedPosition(int position){
        chatRecyclerView.scrollToPosition(position);
    }

    public void isTyping(boolean isTyping){
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", appointmentID);
            jsonObject.put("username", PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName());
            jsonObject.put("isTyping", isTyping);

            //jsonObject.put("isDoctor",false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket15","message" + mSocket.id());
        mSocket.emit("typing",jsonObject);
    }

    private void replayToMsg(String userID, String roomID,String message,String messageId,String rplId,String msg1,String name) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        Log.e("messageesd","ffd "+msg1);
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("userID", userID);
            jsonObject.put("message",message);
            jsonObject.put("message1", msg1);
            jsonObject.put("messageID", messageId);
            jsonObject.put("repliedMessageID", rplId);
            jsonObject.put("audioDuration", "");
            jsonObject.put("recipientRepliedName",name );
            jsonObject.put("type", "reply");

            //jsonObject.put("isDoctor",false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*MessagesListModel modelMsg = new MessagesListModel();
        modelMsg.setLiked(false);
        modelMsg.setRecipientRepliedName(strName);
        modelMsg.setRepliedMessageID(replayMsgID);
        modelMsg.setMessage1(replayMessage);
        modelMsg.setMessage(edtSendingTxt.getText().toString());
        modelMsg.setMessageID(messageId);
        modelMsg.setEdited(false);
        modelMsg.setUserID(userID);
        modelMsg.setType("dummy");
        modelMsg.setRead(false);
        modelMsg.setTimeStamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));

        ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().add(modelMsg);

        chatAdapter.notifyItemRangeInserted(chatRecyclerView.getAdapter().getItemCount()-1,chatRecyclerView.getAdapter().getItemCount());
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
*/
        Log.e("socket18", "message" + mSocket.id());
        mSocket.emit("sendMessage", jsonObject);

    }


    private void fetchChat(String userID, String roomID) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("userID", userID);
            jsonObject.put("isDoctor", false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        Log.e("socket12", "message" + mSocket.id());
        mSocket.emit("getRoomMessages", jsonObject);
    }
    private void loadResponse(){
        SocketIOHelper.getInstance().attachResponse(new SocketIOHelper.ednResponseInterface() {
            @Override
            public void endFileResponse(JSONObject endFileResponse) {
                Log.e("sdfsafsaaeeeedd","dfa");

                try {
                    if (endFileResponse.get("response").equals(3)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                imgAttach.setVisibility(View.VISIBLE);

                            }
                        });


                        Log.e("comeIn","yess");
                        Gson gson = new Gson();
                        String object = null;
                        try {
                            object = endFileResponse.getJSONObject("messageData").toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MessagesListModel messageModel = gson.fromJson(object, MessagesListModel.class);

                        Log.e("fileFrom",":: "+endFileResponse.toString());
                        if (ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()>0) {

                            Log.e("arraySize", "arraySize ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                            ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().set(
                                    ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1,messageModel);
/*
                           if (uri!=null){
                               ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1)
                                       .getAttachments().get(0).setFilePath(String.valueOf(uri));
                               ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().get(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1).setType("loadId");
                           }
*/

                            Log.e("arraySize", "arraySiz ::"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                            // chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                            Log.e("idOf","doc:: "+messageModel.getUserID());

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    chatAdapter.notifyItemInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1);
                                  //  chatAdapter.notifyItemRangeInserted(ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size()-1,ChatDataController.getInstance().messageModelArrayList.get(0).getMessages().size());
                                  // chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);
                                    chatRecyclerView.scrollToPosition(ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size()-1);

                                }
                            });

                            //recyclerToBottom(chatRecyclerView);
                        }else {
                            listModel.clear();
                            Log.e("arraySize", "arraySizeO ::");
                            listModel.add(messageModel);
                            ChatDataController.getInstance().getMessageModelArrayList().get(0).setMessages(listModel);
                            Log.e("arrayy","sizO"+ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().size());
                            chatAdapter.notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("KGF","Chapter 2");
        joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(),
                appointmentID);
    }
}

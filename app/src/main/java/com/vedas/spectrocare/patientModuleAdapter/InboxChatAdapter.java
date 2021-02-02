package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import retrofit2.http.Header;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.FullImageActivity;
import com.vedas.spectrocare.PatientChat.MessageModel;
import com.vedas.spectrocare.PatientChat.MessagesListModel;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InboxChatAdapter extends RecyclerView.Adapter<InboxChatAdapter.InboxChatHolder> {

    Context context;
    String formattedDate, hrsNdMints;
    Bitmap bitmap;
    String pic;
    boolean m;
    ArrayList<MessagesListModel> getMessageList;
    ArrayList<MessagesListModel> messagesSendArray = new ArrayList<>();
    Uri fileUrl;
    ArrayList<MessageModel> messageList = new ArrayList<>();
    int currentPos, bm, getPosition;
    Socket mSocket;
    EditText edtText;
    String jsonString;
    String imgBlur;
    boolean isUpdate = true;
    int deletePosition,b;
    String roomID, messageID, message1;
    private static final int ID_Edit = 1;
    private static final int ID_Delete = 2;
    private QuickAction quickAction;
    private QuickAction quickIntent;
    PatientChatActivity patientChatActivity = new PatientChatActivity();

    public InboxChatAdapter(Context context, EditText edtText, String roomID) {
        this.context = context;
        this.edtText = edtText;
        this.roomID = roomID;
        mSocket = SocketIOHelper.getInstance().socket;
        mSocket.connect();
        mSocket.on("deleteMessageForMe", onNewMessage);
        mSocket.on("deleteMessageForEveryOne", deleteForBoth);
        mSocket.on("messageEdit", onNewMessage);

    }

    public InboxChatAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public InboxChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_view, null);
        InboxChatHolder myholder = new InboxChatHolder(layout);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull InboxChatHolder holder, int position) {
//        Log.e("yaandi","ee"+ChatDataController.getInstance().getChatModelArrayList().size());

     //   holder.itemView.setBackgroundColor(0xff000000 | (position * 0x611de));
        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = null;
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return null;
            }
        };

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.setColorFilter(position, PorterDuff.Mode.LIGHTEN);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.profile_1);
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        QuickAction.setDefaultColor(ResourcesCompat.getColor(context.getResources(), R.color.colorOrange, null));
        QuickAction.setDefaultTextColor(Color.BLACK);

        ActionItem nextItem = new ActionItem(ID_Edit, "Edit", R.drawable.ic_edit_white);
        ActionItem prevItem = new ActionItem(ID_Delete, "Delete", R.drawable.ic_delete_in_white);
        quickAction = new QuickAction(context, QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.colorOrange);
        quickAction.setDividerColor(R.color.colorOrange);
        quickAction.setTextColorRes(R.color.colorWhite);
        quickAction.addActionItem(nextItem, prevItem);
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionItem item) {
                //here we can filter which action item was clicked with pos or actionId parameter
                String title = item.getTitle();
                // Toast.makeText(context, title + " selected", Toast.LENGTH_SHORT).show();
                if (title.equals("Delete")) {
                    // alertDailog();
                    deleteDialog("patient");
                } else {
                    isUpdate = false;
                    edtText.setText(getMessageList.get(currentPos).getMessage());
                    messageID = getMessageList.get(currentPos).getMessageID();
                    message1 = getMessageList.get(currentPos).getMessage();
                }
            }
        });

        //  if (!ChatDataController.isNull()) {
            Log.e("isNull", "true");
           /* for (int i = 0; i < ChatDataController.getInstance().getMessageModelArrayList().size(); i++) {
                getMessageList = ChatDataController.getInstance().getMessageModelArrayList().get(i).getMessages();
            }*/
           /* Gson fffd= new Gson();
            String fffff = fffd.toJson(getMessageList);
            Log.e("dsfaffafew","dsafaf"+fffff);
*/
            long time = Long.parseLong(getMessageList.get(position).getTimeStamp());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm aa");
            String dateString = format.format(new Date(time));
            Log.e("timeee", "tiem" + dateString);
            String timee[] = dateString.split(" ");
            Log.e("timein", "hr" + timee[1] + " " + timee[2]);
            hrsNdMints = timee[1] + " " + timee[2];
            Log.e("listS", "zz? " + getMessageList.size());
            if (!holder.txtDate.getText().toString().isEmpty()) {
                Log.e("isText", "::11 ");
            }

            Log.e("progresssss", "ad:: " + ((PatientChatActivity) context).isImgSend());
            m = ((PatientChatActivity) context).isImgSend();

          /*  Log.e("daadaa","ad "+getMyPrettyDate(time));
            formattedDate = getMyPrettyDate(time);

*/
            Glide.with(context)
                    .load(Uri.parse(ServerApi.img_home_url + ChatDataController.getInstance().getMessageModelArrayList().get(0)
                            .getParticipant().getProfilePic()))
                    .placeholder(R.drawable.profile_1)
                    .into(holder.imgNonuserPic);
            if (getMessageList.get(position).getUserID().equals(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId())) {
              //  Log.e("listSize", "?" + getMessageList.size());

                holder.nonUserLay.setVisibility(View.GONE);
                holder.userLay.setVisibility(View.VISIBLE);
                holder.txtUserMsz.setVisibility(View.VISIBLE);
                holder.txtUserTime.setText(hrsNdMints);
                holder.txtUserMsz.setText(getMessageList.get(position).getMessage());
               /* Picasso.get().load(ServerApi.img_home_url+ChatDataController.getInstance().getMessageModelArrayList().get(0)
                .getParticipant().getProfilePic()).placeholder(R.drawable.image).into(holder.imgNonuserPic);*/
              /*  Picasso.get().load(ChatDataController.getInstance().getMessageModelArrayList().get(0)
                        .getSender().getProfilePic()).placeholder(R.drawable.profile_1).into(holder.imgUserPic);*/
                Glide.with(context)
                        .load(Uri.parse(ServerApi.img_home_url + ChatDataController.getInstance().getMessageModelArrayList().get(0)
                                .getSender().getProfilePic()))
                        .placeholder(R.drawable.profile_1)
                        .into(holder.imgUserPic);


                if (getMessageList.get(position).getRepliedMessageID() != null) {
                    if (!getMessageList.get(position).getRepliedMessageID().isEmpty()) {

                        holder.rlReplayLayout.setVisibility(View.VISIBLE);
                        if (getMessageList.get(position).getMessage1().contains("/")) {
                            holder.replaiedImg.setVisibility(View.VISIBLE);
                            holder.replayLayout.setVisibility(View.VISIBLE);
                            holder.txtReplayMsg.setVisibility(View.GONE);
                            Log.e("replayImg", ": " + getMessageList.get(position).getMessage1());
                            //  Picasso.get().load(getMessageList.get(position).getMessage1()).placeholder(R.drawable.profile_1).into(holder.replaiedImg);
                            if (getMessageList.get(position).getMessage1().contains("http")){
                                Glide.with(context)
                                        .load(Uri.parse(getMessageList.get(position).getMessage1()))
                                        .placeholder(R.drawable.profile_1)
                                        .into(holder.replaiedImg);

                            }else {
                                Glide.with(context)
                                        .load(getMessageList.get(position).getMessage1())
                                        .placeholder(R.drawable.profile_1)
                                        .into(holder.replaiedImg);

                            }

                        } else {
                            holder.replaiedImg.setVisibility(View.GONE);
                            holder.replayLayout.setVisibility(View.GONE);
                            holder.txtReplayMsg.setVisibility(View.VISIBLE);
                        }
                        holder.txtReplayMsg.setText(getMessageList.get(position).getMessage1());
                        holder.txtReplayName.setText(getMessageList.get(position).getRecipientRepliedName());
                    } else {
                        Log.e("log", "bba");
                        holder.rlReplayLayout.setVisibility(View.GONE);
                    }
                }
                if (getMessageList.get(position).getAttachments() != null) {
                    holder.txtUserMsz.setVisibility(View.GONE);
                    holder.imgUserMsz.setVisibility(View.VISIBLE);
                    if (getMessageList.get(position).getMessageID().equals("id")) {

                        holder.progressBar.setVisibility(View.VISIBLE);

                        //  holder.imgUserMsz.setImageURI(getMessageList.get(position).getAttachments().get(0).getFilePath());
                        Picasso.get()
                                .load(Uri.parse(getMessageList.get(position).getAttachments().get(0).getFilePath()))
                                .transform(new BlurTransformation(context, 30, 1))
                                .fit()
                                .into(holder.imgUserMsz);
                        imgBlur = getMessageList.get(position).getAttachments().get(0).getFilePath();
/*
                        Glide.with(context)
                                .load(Uri.parse(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath()))
                                .into(holder.imgUserMsz);
*/

                    } else {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.rlReplayLayout.setVisibility(View.GONE);
                        Log.e("imagePathh", ":: " + getMessageList.get(position).getAttachments().get(0).getFilePath());
                        // Picasso.get().load(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath()).placeholder(R.drawable.profile_1).into(holder.imgUserMsz);
                        // Log.e("imagePath",":: "+ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath());
                        if (getMessageList.get(position).getType().equals("loadId")) {
                            Log.e("imgaga", "aad");
                            Picasso.get()
                                    .load(Uri.parse(getMessageList.get(position).getAttachments().get(0).getFilePath()))
                                    .fit()
                                    .into(holder.imgUserMsz);

                        } else {
                            holder.progressBar.setVisibility(View.GONE);
                            Glide.with(context)
                                    .load(Uri.parse(ServerApi.img_home_url + getMessageList.get(position).getAttachments().get(0).getFilePath()))
                                    .placeholder(R.drawable.profile_1)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontTransform()
                                    /*.apply(requestOptions)*/
                                    .into(holder.imgUserMsz);

/*
                                    Picasso.get().load(Uri.parse(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath()))
                                            .placeholder(R.drawable.profile_1)
                                            .resize(100, 120)
                                            .transform(blurTransformation)
                                            .into(holder.imgUserMsz, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    Picasso.get()
                                                            .load(Uri.parse(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath())) // image url goes here
                                                            .resize(100, 120)
                                                            .placeholder(R.drawable.profile_1)
                                                            .into(holder.imgUserMsz);
                                                }

                                                @Override
                                                public void onError(Exception e) {

                                                }
                                            });
*/


                        }

                    }
/*
                    if (m){
                        Log.e("log","bba");
                        m = false;
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }
*/

                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.txtUserMsz.setVisibility(View.VISIBLE);
                    holder.imgUserMsz.setVisibility(View.GONE);
                }

            } else {
                holder.nonUserLay.setVisibility(View.VISIBLE);
                holder.userLay.setVisibility(View.GONE);
                holder.txtNonUserTime.setText(hrsNdMints);
                holder.txtNonUserMsz.setText(getMessageList.get(position).getMessage());
                if (getMessageList.get(position).getAttachments() != null) {
                    holder.txtNonUserMsz.setVisibility(View.GONE);
                    holder.imgNonUserMsz.setVisibility(View.VISIBLE);


                    Log.e("imagePath", ":: " + ServerApi.img_home_url + getMessageList.get(position).getAttachments().get(0).getFilePath());
                   /* Picasso.get().load(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath()).placeholder(R.drawable.profile_1)
                            .fit().into(holder.imgNonUserMsz);*/
                    Glide.with(context)
                            .load(Uri.parse(ServerApi.img_home_url + getMessageList.get(position).getAttachments().get(0).getFilePath()))
                            .placeholder(R.drawable.profile_1)
                            /*.apply(requestOptions)*/
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontTransform()
                            .into(holder.imgNonUserMsz);


                    // Picasso.get().load(ServerApi.img_home_url+getMessageList.get(position).getAttachments().get(0).getFilePath()).into(holder.imgNonUserMsz);

                } else {
                    holder.txtNonUserMsz.setVisibility(View.VISIBLE);
                    holder.imgNonUserMsz.setVisibility(View.GONE);
                }
                if (getMessageList.get(position).getRepliedMessageID() != null) {
                    if (!getMessageList.get(position).getRepliedMessageID().isEmpty()) {
                        if (getMessageList.get(position).getMessage1() != null) {
                            if (getMessageList.get(position).getMessage1().contains("/")) {
                                holder.replyDocImg.setVisibility(View.VISIBLE);
                                holder.linearDocLayout.setVisibility(View.VISIBLE);
                                holder.replyDocLayout.setVisibility(View.VISIBLE);
                                holder.docMsg.setVisibility(View.GONE);
                                // Picasso.get().load(getMessageList.get(position).getMessage1()).placeholder(R.drawable.profile_1).into(holder.replyDocImg);
                                if (getMessageList.get(position).getMessage1().contains("http")){
                                    Glide.with(context)
                                            .load(Uri.parse(getMessageList.get(position).getMessage1()))
                                            .placeholder(R.drawable.profile_1)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .dontTransform()
                                            .into(holder.replyDocImg);

                                }else {
                                    Glide.with(context)
                                            .load(getMessageList.get(position).getMessage1())
                                            .placeholder(R.drawable.profile_1)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .dontTransform()
                                            .into(holder.replyDocImg);

                                }

                            } else {
                                holder.replyDocImg.setVisibility(View.GONE);
                                holder.replyDocLayout.setVisibility(View.VISIBLE);
                                holder.linearDocLayout.setVisibility(View.GONE);
                                holder.docMsg.setVisibility(View.VISIBLE);
                                holder.docMsg.setText(getMessageList.get(position).getMessage1());
                            }
                        }
                        /*holder.replyDocLayout.setVisibility(View.VISIBLE);
                        holder.docMsg.setText(getMessageList.get(position).getMessage1());*/
                        holder.docTxt.setText(getMessageList.get(position).getRecipientRepliedName());

                    } else {
                        Log.e("log", "bba");
                        holder.replyDocLayout.setVisibility(View.GONE);
                    }
                }

            }
             if (getMessageList.get(position).isRead()) {
                    holder.imgRead.setImageResource(R.drawable.ic_seen);
                } else {
                    if (getMessageList.get(position).getType().equals("dummy")) {
                        Log.e("dummycalled", "arraySiz ::");
                        holder.imgRead.setImageResource(R.drawable.ic_check);
                    }else if (getMessageList.get(position).getType().equals("regular")) {
                        Log.e("regularcalled", "arraySiz ::");
                        holder.imgRead.setImageResource(R.drawable.ic_unread);
                    } else if (getMessageList.get(position).getType().equals("reply")) {
                        holder.imgRead.setImageResource(R.drawable.ic_unread);
                    }
                }
/*
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    quickAction.show(v);
                }
            });
*/
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    currentPos = holder.getAdapterPosition();
                    if (getMessageList.get(currentPos).getUserID().equals(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId())) {
                        quickAction.show(v);
                    } else {
                        deleteDialog("doctor");
                        // alertDailog();
                    }
                    return false;
                }
            });
            holder.imgNonUserMsz.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    deleteDialog("doctor");
                    return false;
                }
            });
            holder.imgUserMsz.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    quickAction.show(v);
                    return false;
                }
            });

            holder.imgNonUserMsz.setTransitionName("photoTransition");
            holder.imgUserMsz.setTransitionName("photoTransition");

            holder.imgNonUserMsz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    Log.e("typeCheck", ": " + getMessageList.get(currentPos).getType());
                    String imgUrl = getMessageList.get(currentPos).getAttachments().get(0).getFilePath();
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("type", getMessageList.get(currentPos).getType());
                    intent.putExtra("imgUrl", imgUrl);
                    // context.startActivity(intent);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((PatientChatActivity) context, holder.imgNonUserMsz, holder.imgNonUserMsz.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
            holder.imgUserMsz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    String imgUrl = getMessageList.get(currentPos).getAttachments().get(0).getFilePath();
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("type", getMessageList.get(currentPos).getType());
                    intent.putExtra("imgUrl", imgUrl);
                    // context.startActivity(intent);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((PatientChatActivity) context, holder.imgUserMsz, holder.imgUserMsz.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    currentPos = holder.getAdapterPosition();
                    if (getMessageList.get(currentPos).getType().equals("reply")) {
                        Log.e("replayID",":: "+getMessageList.get(currentPos).getRepliedMessageID());
                        String selectedMsg = getMessageList.get(currentPos).getRepliedMessageID();
                        Log.e("message", "selected:: " + selectedMsg);
                        for (bm = 0; bm < getMessageList.size(); bm++) {
                            if (selectedMsg.equals(getMessageList.get(bm).getMessageID())) {
                                ((PatientChatActivity) context).scrollToSelectedPosition(bm);

                            }
                        }
                    } else if (getMessageList.get(currentPos).getAttachments() != null) {
                        String imgUrl = getMessageList.get(currentPos).getAttachments().get(0).getFilePath();
                       /* Intent intent =new Intent(context, FullImageActivity.class);
                        intent.putExtra("imgUrl",imgUrl);
                       // context.startActivity(intent);

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((PatientChatActivity)context,holder.imgUserMsz, holder.nonUserLay.getTransitionName());
                        context.startActivity(intent, optionsCompat.toBundle());*/
                    }
                }
            });

      //  }

    }

    public boolean isEdit() {
        return isUpdate;
    }

    @Override
    public int getItemCount() {
        if (ChatDataController.isNull()) {
            return 0;
        } else {
            getMessageList=ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages();
            return getMessageList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    public void addDate(TextView txt, boolean b) {
        b = false;
        txt.setVisibility(View.GONE);
    }

    public static class InboxChatHolder extends RecyclerView.ViewHolder {
        RelativeLayout userLay, nonUserLay, rlReplayLayout;
        TextView txtUserMsz, txtNonUserMsz, txtReplayName, txtReplayMsg, txtUserTime, docTxt, docMsg, txtNonUserTime, txtDate;
        VideoView sendVideo, receivedVideo;
        CircularImageView docImg, patientImage;
        LinearLayout replayLayout, linearDocLayout;
        ImageView replaiedImg, replyDocImg;
        ImageView imgReceive, imgSend, imgUserMsz, imgPlay, imgNonUserMsz, imgReceivePlay, imgRead, imgUserPic, imgNonuserPic;
        RelativeLayout rlReceiveVideo, rlSendVideo, replyDocLayout;
        ProgressBar progressBar;

        public InboxChatHolder(View view) {
            super(view);
            imgReceive = view.findViewById(R.id.img_receive);
            rlSendVideo = view.findViewById(R.id.rela_send_video);
            rlReceiveVideo = view.findViewById(R.id.rel_receive_video);
            replaiedImg = view.findViewById(R.id.replied_img);
            replayLayout = view.findViewById(R.id.layout_photo);
            imgUserPic = view.findViewById(R.id.userPic);
            imgNonuserPic = view.findViewById(R.id.notUserPic);
            replyDocImg = view.findViewById(R.id.replied_doc_img);
            linearDocLayout = view.findViewById(R.id.layout_doc_photo);
            imgUserMsz = view.findViewById(R.id.img_user_msz);
            progressBar = view.findViewById(R.id.progress);
            imgNonUserMsz = view.findViewById(R.id.img_nonuser_msz);
            txtReplayName = view.findViewById(R.id.txt_selected_doc);
            txtReplayMsg = view.findViewById(R.id.txt_selected_msg);
            replyDocLayout = view.findViewById(R.id.replay_doc_layout);
            docMsg = view.findViewById(R.id.txt_selected);
            docTxt = view.findViewById(R.id.selected_doc);
            imgSend = view.findViewById(R.id.img_send);
            imgPlay = view.findViewById(R.id.img_play);
            txtDate = view.findViewById(R.id.txt_date);
            txtNonUserTime = view.findViewById(R.id.txt_non_user_time);
            txtUserTime = view.findViewById(R.id.txt_user_time);
            docImg = view.findViewById(R.id.notUserPic);
            patientImage = view.findViewById(R.id.userPic);
            imgRead = view.findViewById(R.id.img_read);
            imgReceivePlay = view.findViewById(R.id.img_receive_play);
            txtNonUserMsz = view.findViewById(R.id.nonUserMsz);
            txtUserMsz = view.findViewById(R.id.userMsz);
            userLay = view.findViewById(R.id.userLay);
            sendVideo = view.findViewById(R.id.send_video);
            receivedVideo = view.findViewById(R.id.receive_video);
            nonUserLay = view.findViewById(R.id.notUserLay);
            rlReplayLayout = view.findViewById(R.id.replay_msg_layout);

        }
    }

    private void deleteChatMessage(String userID, String roomID) {
        Log.e("posotionnn", "aadapter" + currentPos);
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        String messageID = getMessageList.get(currentPos).getMessageID();
        String[] separated = messageID.split("_");
        // this will contain "Fruit"
       /* Log.e("message", "Idd :" + getMessageList.get(currentPos).getMessageID());
        Log.e("message", "Id" + separated[1]);*/
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getMessageList.get(currentPos).getMessageID());
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("userID", userID);
            jsonObject.put("messageIDs", jsonArray);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket12", "message" + mSocket.id());
        mSocket.emit("deleteMessageForMe", jsonObject);

/*
        RetrofitCallbacks.getInstace().ApiCallbacksChatMessage(Chating_activity.this, accesstoken, "Room/chatMessage", feedObj, files, li_gallery,
                frame_reply,
                rl_send_refresh_msg,
                rl_send_msg,
                rl_voice);
*/
    }

    private Emitter.Listener deleteForBoth = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
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
                Log.e("efdsggg", "dgd");
                if (message.equals("Message deleted successfully")) {
                    Log.e("efdsggg", "dgdgg");
                    Log.e("length of", "room" + jsonObj.getJSONArray("messageIDs").length());
                    JSONArray responseArray = jsonObj.getJSONArray("messageIDs");
                    for (int l = 0; l < responseArray.length(); l++) {
                        Gson gson = new Gson();
                        jsonString = jsonObj.getJSONArray("messageIDs").getString(0);
                        Log.e("messageIddd", "message:: " + jsonString);

                    }
                    for (int k = 0; k < getMessageList.size(); k++) {
                        Log.e("message", "deleted : " + jsonString);
                        if (jsonString.equals(getMessageList.get(k).getMessageID())) {
                            Log.e("message", "list : " + getMessageList.get(k).getMessageID());
                            deletePosition = k;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("notifying", "dataa");
                                    getMessageList.remove(deletePosition);
                                    notifyItemRemoved(deletePosition);
                                    // notifyItemRangeChanged(getMessageList.size()-1,getMessageList.size());
                                    //   notifyDataSetChanged();
                                }
                            });

                        }
                    }
                    Log.e("position", "deleted" + deletePosition);
                    // getMessageList.remove(deletePosition);

                    // notifyDataSetChanged();

                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                Log.e("messageError", "error" + ex.getMessage());
            }
            Log.e("rreeess", "ponse" + message);

            //  mSocket.off("deleteMessageForEveryOne",deleteForBoth);
          /*  Log.e("possition","is :"+currentPos);
            Log.e("sizeOf","array "+getMessageList.size());
            // ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().remove(currentPos);
            getMessageList.remove(currentPos);
            Log.e("possition","is :"+currentPos);
            Log.e("sizeOf","arrayO "+getMessageList.size());
            mSocket.off("deleteMessageForMe",deleteForBoth);
            notifyDataSetChanged();
*/
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
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

                        if (message.contains("Message deleted successfully")) {
                            Log.e("possition", "is :" + currentPos);
                            Log.e("sizeOf", "array " + getMessageList.size());
                            // ChatDataController.getInstance().getMessageModelArrayList().get(0).getMessages().remove(currentPos);
                            getMessageList.remove(currentPos);
                            Log.e("possition", "is :" + currentPos);
                            Log.e("sizeOf", "arrayO " + getMessageList.size());
                            notifyItemRemoved(currentPos);
                            //  mSocket.off("deleteMessageForMe",onNewMessage);
                            // notifyItemRangeChanged(getMessageList.size()-1,getMessageList.size());
                            //notifyDataSetChanged();
                           /* fetchChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), roomID);
                            mSocket.on("getRoomMessages", onNewMessage);*/
                        } else if (message.equals("Message updated successfully")) {
                            Log.e("editdsfa","xall");
                            String messageId = data.getString("messageID");
                            String edtMessage = data.getString("editedMessage");
                            MessagesListModel model = new MessagesListModel();

                            Log.e("editedText", ":: " + edtMessage + ", " + messageId);
                            for (int m = 0; m < getMessageList.size(); m++) {
                                if (messageId.equals(getMessageList.get(m).getMessageID())) {
                                    model.setEdited(true);
                                    model.setLiked(false);
                                    model.setMessage(edtMessage);
                                    model.setUserID(getMessageList.get(m).getUserID());
                                    model.setMessage1(getMessageList.get(m).getMessage());
                                    model.setRead(false);
                                    model.setRecipientRepliedName(getMessageList.get(m).getRecipientRepliedName());
                                    model.setRepliedMessageID(getMessageList.get(m).getRepliedMessageID());
                                    model.setTimeStamp(getMessageList.get(m).getTimeStamp());
                                    model.setMessageID(getMessageList.get(currentPos).getMessageID());
                                    model.setType(getMessageList.get(m).getType());
                                    getMessageList.set(m, model);
                                    b=m;
                                }
                            }
                            edtText.setText("");
                           // notifyItemRangeChanged(m, getMessageList.size());
                            notifyItemChanged(b);

                           /* fetchChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), roomID);
                            mSocket.on("getRoomMessages", onNewMessage);*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(Chating_activity.this,message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

/*
    public static String getMyPrettyDate(long neededTimeMilis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(neededTimeMilis);

        if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {

            if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {

                if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                    //here return like "Tomorrow at 12:00"
                   // return "Tomorrow at " + DateFormat.format("HH:mm", neededTime);
                    return "Tomorrow";

                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    //here return like "Today at 12:00"

                  //  return "Today at " + DateFormat.format("HH:mm", neededTime);
                    return " Today ";


                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    //here return like "Yesterday at 12:00"

                    return " Yesterday ";

                } else {
                    //here return like "May 31, 12:00"
                    return DateFormat.format("dd MMMM yyyy ", neededTime).toString();
                }

            } else {
                //here return like "May 31, 12:00"
                return DateFormat.format("dd MMMM yyyy ", neededTime).toString();
            }

        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            return DateFormat.format("dd MMMM yyyy ", neededTime).toString();
        }

    }
*/

    public void alertDailog() {
       /* LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.alert_abort, null);
*/
        final Dialog dialog = new Dialog(context);
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
        txt_msg1.setText("want to delete ?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("idd", "room" + roomID);
                deleteChatMessage(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), roomID);
                dialog.dismiss();
            }
        });

    }

    public void editChatMessage(String message) {
        isUpdate = true;
        Log.e("stringgg", "edd" + message);
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("userID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            jsonObject.put("messageID", messageID);
            jsonObject.put("message", message);
            jsonObject.put("message1", message1);
            jsonObject.put("isEdited", true);
            jsonObject.put("type", "regular");

            //jsonObject.put("isDoctor",false);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket12", "message" + mSocket.id());
        mSocket.emit("messageEdit", jsonObject);
    }

    public void deleteForEveryOne() {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getMessageList.get(currentPos).getMessageID());
        try {
            jsonObject.put("roomID", roomID);
            jsonObject.put("messageIDs", jsonArray);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            Log.e("ChatJSON:", " " + jsonObject);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        Log.e("socket13", "message" + mSocket.id());
        mSocket.emit("deleteMessageForEveryOne", jsonObject);
    }

    public void deleteDialog(String str) {
       /* LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.alert_abort, null);
*/
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        TextView txtCancel = dialog.findViewById(R.id.delcancel);
        TextView txtDeleteEveryOne = dialog.findViewById(R.id.delforevery);
        TextView txtDeleteOne = dialog.findViewById(R.id.delforme);
        dialog.show();

        if (str.equals("doctor")) {
            txtDeleteEveryOne.setVisibility(View.GONE);
            txtDeleteOne.setText("Delete");
        }
        txtDeleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChatMessage(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), roomID);
                dialog.dismiss();

            }
        });
        txtDeleteEveryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteForEveryOne();
                dialog.dismiss();

            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}


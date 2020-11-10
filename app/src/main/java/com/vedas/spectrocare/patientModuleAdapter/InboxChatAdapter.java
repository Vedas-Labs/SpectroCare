package com.vedas.spectrocare.patientModuleAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.FullImageActivity;
import com.vedas.spectrocare.R;
/*import com.star.startasker.AuthorMszs;
import com.star.startasker.PostTaskController;
import com.star.startasker.ReportTask;
import com.star.startasker.TimeAgo;
import com.vedas.spectrocare.R;*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.paypal.android.sdk.df.e;

public class InboxChatAdapter extends RecyclerView.Adapter<InboxChatAdapter.InboxChatHolder> {

    Context context;
    String formattedDate;
    Bitmap bitmap;
    String pic;
    Uri fileUrl;
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

    @Override
    public void onBindViewHolder(@NonNull InboxChatHolder holder, int position) {
        Log.e("yaandi","ee"+ChatDataController.getInstance().getChatModelArrayList().size());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        // Log.e("idd","userID"+ChatDataController.getInstance().getChatModelArrayList().get(position).getiD());
        if (ChatDataController.getInstance().getChatModelArrayList().get(position).getiD().equals("NonLocal")){
            holder.nonUserLay.setVisibility(View.VISIBLE);
            holder.userLay.setVisibility(View.GONE);
            holder.txtNonUserMsz.setText(ChatDataController.getInstance().getChatModelArrayList().get(position).getText());
        }else{
            holder.userLay.setVisibility(View.VISIBLE);
            holder.nonUserLay.setVisibility(View.GONE);
            if (!ChatDataController.getInstance().getChatModelArrayList().get(position).getText().isEmpty()){
                holder.imgSend.setVisibility(View.GONE);
                holder.sendVideo.setVisibility(View.GONE);
                holder.txtUserMsz.setVisibility(View.VISIBLE);
                holder.txtUserMsz.setText(ChatDataController.getInstance().getChatModelArrayList().get(position).getText());

            }
                if (!ChatDataController.getInstance().getChatModelArrayList().get(position).getImage().isEmpty()){
                   // holder.imgSend.setVisibility(View.VISIBLE);
                    holder.txtUserMsz.setVisibility(View.GONE);
                    holder.imgSend.setVisibility(View.VISIBLE);
                    holder.sendVideo.setVisibility(View.GONE);
                    if (ChatDataController.getInstance().getChatModelArrayList().get(position).getImage().contains("jpg")){
                        String path =ChatDataController.getInstance().getChatModelArrayList().get(position).getImage();
                        Bitmap bitmap1 = BitmapFactory.decodeFile(path, options);
                        Bitmap rotatedBitmap = rotatedImageBitmap(path, bitmap1);
                        bitmap = rotatedBitmap;
                        Log.e("uuuuuf","afad"+path);
                        holder.imgSend.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
                    }else
                    holder.imgSend.setImageURI(Uri.parse(ChatDataController.getInstance().getChatModelArrayList().get(position).getImage()));
                } if (!ChatDataController.getInstance().getChatModelArrayList().get(position).getVideo().isEmpty()){
                    holder.txtUserMsz.setVisibility(View.GONE);
                    Log.e("kfkfkfk","ffff"+ChatDataController.getInstance().getChatModelArrayList().get(position).getVideo());
                    holder.rlSendVideo.setVisibility(View.VISIBLE);
                holder.imgSend.setVisibility(View.GONE);
                holder.sendVideo.setVideoURI(Uri.parse(ChatDataController.getInstance().getChatModelArrayList().get(position).getVideo()));
                    holder.imgPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.imgPlay.setVisibility(View.GONE);
                            holder.sendVideo.start();
                        }
                    });
                }if (!ChatDataController.getInstance().getChatModelArrayList().get(position).getFile().isEmpty()){
                    holder.imgSend.setVisibility(View.VISIBLE);
                holder.imgSend.setImageResource(R.drawable.pdf);
                holder.txtUserMsz.setVisibility(View.GONE);

/*
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("imageIntn","fdd");

                        }
                    });
*/
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ChatDataController.getInstance().getChatModelArrayList().get(holder.getAdapterPosition()).getFile().isEmpty()) {
                    File file = new File(ChatDataController.getInstance()
                            .getChatModelArrayList().get(position).getFile());
                    fileUrl = Uri.fromFile(file);
                    Log.e("filee","url"+fileUrl);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    try {
                        if (fileUrl.toString().contains(".doc") || fileUrl.toString().contains(".docx")) {
                            // Word document
                            Log.e("filee","doccc");
                            intent.setDataAndType(fileUrl, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        } else if (fileUrl.toString().contains(".pdf")) {
                            // PDF file
                            Log.e("filee","pdf");

                            intent.setDataAndType(fileUrl, "application/pdf");
                        } else if (fileUrl.toString().contains(".ppt") || fileUrl.toString().contains(".pptx")) {
                            // Powerpoint file
                            intent.setDataAndType(fileUrl, "application/vnd.ms-powerpoint");
                        } else if (fileUrl.toString().contains(".xls") || fileUrl.toString().contains(".xlsx")) {
                            // Excel file
                            intent.setDataAndType(fileUrl, "application/vnd.ms-excel");
                        } else if (fileUrl.toString().contains(".zip")) {
                            // ZIP file
                            intent.setDataAndType(fileUrl, "application/zip");
                        } else if (fileUrl.toString().contains(".rar")) {
                            // RAR file
                            intent.setDataAndType(fileUrl, "application/x-rar-compressed");
                        } else if (fileUrl.toString().contains(".rtf")) {
                            // RTF file
                            intent.setDataAndType(fileUrl, "application/rtf");
                        } else if (fileUrl.toString().contains(".wav") || fileUrl.toString().contains(".mp3")) {
                            // WAV audio file
                            intent.setDataAndType(fileUrl, "audio/x-wav");
                        } else if (fileUrl.toString().contains(".gif")) {
                            // GIF file
                            intent.setDataAndType(fileUrl, "image/gif");
                        } else if (fileUrl.toString().contains(".jpg") || fileUrl.toString().contains(".jpeg") || fileUrl.toString().contains(".png")) {
                            // JPG file
                            intent.setDataAndType(fileUrl, "image/jpeg");
                        } else if (fileUrl.toString().contains(".txt")) {
                            // Text file
                            intent.setDataAndType(fileUrl, "text/plain");
                        } else if (fileUrl.toString().contains(".3gp") || fileUrl.toString().contains(".mpg") ||
                                fileUrl.toString().contains(".mpeg") || fileUrl.toString().contains(".mpe") || fileUrl.toString().contains(".mp4") || fileUrl.toString().contains(".avi")) {
                            // Video files
                            intent.setDataAndType(fileUrl, "video/*");
                        } else {
                            intent.setDataAndType(fileUrl, "*/*");
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
                    }

                }

                if (!ChatDataController.getInstance().getChatModelArrayList().get(holder.getAdapterPosition()).getImage().isEmpty()){
                    Log.e("imageIntn","fddd");
                    context.startActivity(new Intent(context, FullImageActivity.class)
                            .putExtra("proPic",ChatDataController.getInstance().getChatModelArrayList().get(position).getImage()));
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        if (ChatDataController.isNull()){
            return 0;
        }else{
            return ChatDataController.getInstance().getChatModelArrayList().size();
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

    public static class InboxChatHolder extends RecyclerView.ViewHolder {
        RelativeLayout userLay,nonUserLay;
        TextView txtUserMsz,txtNonUserMsz;
        VideoView sendVideo,receivedVideo;
        ImageView imgReceive,imgSend,imgPlay,imgReceivePlay;
        RelativeLayout rlReceiveVideo,rlSendVideo;
        public InboxChatHolder(View view) {
            super(view);
            imgReceive = view.findViewById(R.id.img_receive);
            rlSendVideo = view.findViewById(R.id.rela_send_video);
            rlReceiveVideo = view.findViewById(R.id.rel_receive_video);
            imgSend = view.findViewById(R.id.img_send);
            imgPlay = view.findViewById(R.id.img_play);
            imgReceivePlay = view.findViewById(R.id.img_receive_play);
            txtNonUserMsz = view.findViewById(R.id.nonUserMsz);
            txtUserMsz = view.findViewById(R.id.userMsz);
            userLay=view.findViewById(R.id.userLay);
            sendVideo = view.findViewById(R.id.send_video);
            receivedVideo = view.findViewById(R.id.receive_video);
            nonUserLay=view.findViewById(R.id.notUserLay);

        }
    }

}

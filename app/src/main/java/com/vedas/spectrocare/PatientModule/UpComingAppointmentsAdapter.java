package com.vedas.spectrocare.PatientModule;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientModule.UpcomingAppointmentFragment.OnListFragmentInteractionListener;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.AppointmetModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UpComingAppointmentsAdapter extends RecyclerView.Adapter<UpComingAppointmentsAdapter.ViewHolder> {
    public Context context1;
    TextView txtName, txtDate, timeTxt;
    CircularImageView imgAlertPic;
    AppointmetModel appointmetModel;
    AppointmentArrayModel arrayModel;
    Button btnAbort;
    int pos;
    ArrayList<AppointmetModel> appointmentList = new ArrayList<>();
    ArrayList<AppointmentArrayModel> appointmentDataList = new ArrayList<>();

    //private final List<DummyItem> mValues;
    //private final OnListFragmentInteractionListener mListener;

    public UpComingAppointmentsAdapter(Context context1, ArrayList<AppointmentArrayModel> appointmentDataList) {
        this.context1 = context1;
        this.appointmentDataList = appointmentDataList;
    }

    public UpComingAppointmentsAdapter(Context context/*List<DummyItem> items,*/ /*nListFragmentInteractionListener listener*/) {
        // mValues = items;
        //  mListener = listener;69
        this.context1 = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upcoming_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.e("uuusus", "dadu" +appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile()
        .getFirstName());

        if (!appointmentDataList.isEmpty()) {
            //   appointmentList = PatientAppointmentController.getInstance().getAppointmentList();
            Log.e("caaaaa", "dsfdf"+appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getFirstName());
            holder.txtName.setText(appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+
                    appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getLastName());
            holder.txtSpecial.setText(appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getDepartment());
           /* holder.txtName.setText(appointmentList.get(position).getDocName());
            holder.txtSpecial.setText(appointmentList.get(position).getSpecialization());*/
           if (appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate().contains("/")){
               holder.txtAppointmentDate.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate());
           }else{
               long ll = Long.parseLong(appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate());
               Date currentDate = new Date(ll);
               SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
               jdff.setTimeZone(TimeZone.getDefault());
               String java_date = jdff.format(currentDate);
               holder.txtAppointmentDate.setText(java_date);
           }
           /* long ll = Long.parseLong(appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getRegisterTime());
            Date currentDate = new Date(ll);
            SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
            jdff.setTimeZone(TimeZone.getDefault());
            String java_date = jdff.format(currentDate);
            holder.txtAppointmentDate.setText(java_date);*/
           holder.txtTime.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentTime());
            holder.txtStatus.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentStatus());
            /*holder.txtTime.setText(appointmentList.get(position).getTime());
            holder.txtStatus.setText(appointmentList.get(position).getApprove());
*/        }

        /*holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        if (!appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
        Picasso.get().load(ServerApi.img_home_url+appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(holder.imgPic);
        Log.e("piccc","pdd"+ServerApi.img_home_url+appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getProfilePic());
        holder.mainLayout.setBackgroundDrawable(gd);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                alertDailog();
                // context1.startActivity(new Intent(context1,AddPaymentMethodAactivity.class));
                /*if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(*//*holder.mItem*//*);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        if (appointmentDataList.size()>0) {
            return appointmentDataList.size();
        } else
            return 0;/*mValues.size();*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView txtName, txtSpecial, txtAppointmentDate, txtStatus, txtTime;
        /*public final TextView mIdView;
        public final TextView mContentView;*/
        // public DummyItem mItem;
        CircularImageView imgPic;
        RelativeLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = view.findViewById(R.id.doc_name);
            txtStatus = view.findViewById(R.id.appointment_approval);
            txtSpecial = view.findViewById(R.id.doc_spe);
            imgPic = view.findViewById(R.id.doc_profile);
            txtTime = view.findViewById(R.id.appointment_time);
            txtAppointmentDate = view.findViewById(R.id.appointment_date);
            mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
            /*mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);*/
        }
    }

    public void alertDailog() {
        LayoutInflater li = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.addappotment_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(context1), R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        btnAbort = (Button) dialog.findViewById(R.id.status);
        txtName = dialog.findViewById(R.id.title);
        txtDate = dialog.findViewById(R.id.txt_date);
        timeTxt = dialog.findViewById(R.id.time);
        imgAlertPic = dialog.findViewById(R.id.icon_play);
        if (!appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
            Picasso.get().load(ServerApi.img_home_url+appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(imgAlertPic);
        long ll = Long.parseLong(appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getRegisterTime());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        txtName.setText(appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+
                appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getLastName());
        txtDate.setText(java_date);
        timeTxt.setText(appointmentDataList.get(pos).getAppointmentDetails().getAppointmentTime());
        final GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(context1.getResources().getColor(R.color.colorGreen));
        CardView cancelView = dialog.findViewById(R.id.cancel);
        CardView detailsView = dialog.findViewById(R.id.details);
        CardView messageView = dialog.findViewById(R.id.card_message_view);
        CardView callView = dialog.findViewById(R.id.card_call_view);

        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("appointmentID","Id"+appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
                context1.startActivity(new Intent(context1, VideoActivity.class)
                        .putExtra("appointmentId",appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID()));
            }
        });
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context1.startActivity(new Intent(context1,PatientChatActivity.class)
                        .putExtra("docName",txtName.getText().toString())
                        .putExtra("docPic",ServerApi.img_home_url+appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(context1.getResources().getColor(R.color.colorAccent));
                context1.startActivity(new Intent(context1, AppointmentCancelActivity.class)
                        .putExtra("appointmentDetails",appointmentDataList.get(pos))
                        .putExtra("docName",appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+
                                appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getLastName()));
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               // appointmetModel = new AppointmetModel();
             //   appointmetModel = appointmentList.get(pos);
                arrayModel = appointmentDataList.get(pos);
                drawable.setColor(context1.getResources().getColor(R.color.colorAccent));
                context1.startActivity(new Intent(context1, AppointmentDetailsActivity.class)
                        .putExtra("sampleObject",arrayModel)
                        .putExtra("docPic",ServerApi.img_home_url+appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
                Gson gson = new Gson();
                String json = gson.toJson(arrayModel);
                Log.e("dadd","d"+json);
            }
        });
       /* RelativeLayout rlPSW=dialog.findViewById(R.id.psw);
        RelativeLayout rlProfile=dialog.findViewById(R.id.profile);
        RelativeLayout rlLogout=dialog.findViewById(R.id.logout);
        RelativeLayout rlsettings=dialog.findViewById(R.id.settings);
      */
    }

}

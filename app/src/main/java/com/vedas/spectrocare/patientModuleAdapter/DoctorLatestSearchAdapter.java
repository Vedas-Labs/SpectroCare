package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientModule.DoctorSummeryActivity;
import com.vedas.spectrocare.PatientModule.PatientBookAppointmentActivity;
import com.vedas.spectrocare.PatientMoreModule.CalenderNew;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.DoctorsItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorLatestSearchAdapter extends RecyclerView.Adapter<DoctorLatestSearchAdapter.DoctorSearchHolder> {
    Context context;

    public DoctorLatestSearchAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorLatestSearchAdapter.DoctorSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctoresLatestSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_latest_search_item, parent, false);
        return new DoctorSearchHolder(doctoresLatestSearchView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorLatestSearchAdapter.DoctorSearchHolder holder, int position) {
        MedicalPersonnelModel doctorDetailsModel=PatientMedicalRecordsController.getInstance().doctorProfleList.get(position);
        Picasso.get().load(ServerApi.img_home_url +doctorDetailsModel.getProfile().getUserProfile().getProfilePic()).into(holder.docPic);
        holder.txtDocName.setText("Dr " + doctorDetailsModel.getProfile().getUserProfile().getFirstName()/*+" "+doctorsLatestList.get(position).getProfile().getUserProfile().getLastName()*/);
        holder.txtDocProfession.setText(doctorDetailsModel.getProfile().getUserProfile().getDepartment());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().medicalPersonnelModel=PatientMedicalRecordsController.getInstance().doctorProfleList.get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, DoctorSummeryActivity.class));

               /* PatientMedicalRecordsController.getInstance().currentdepartmentModel = responseModel.getMedicalPersonnels().get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, DoctorSummeryActivity.class));*/
                   }
        });
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicalPersonnelModel obj=PatientMedicalRecordsController.getInstance().doctorProfleList.get(holder.getAdapterPosition());
                Log.e("name","call"+obj.getProfile().getUserProfile().getFirstName());
                context.startActivity(new Intent(context, PatientBookAppointmentActivity.class)
                        .putExtra("docName", obj.getProfile().getUserProfile().getFirstName()+" "+ obj.getProfile().getUserProfile().getLastName())
                        .putExtra("docProfi", obj.getProfile().getUserProfile().getDepartment())
                        .putExtra("docId", obj.getProfile().getUserProfile().getMedical_personnel_id())
                        .putExtra("docProfile", obj.getProfile().getUserProfile().getProfilePic()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (PatientMedicalRecordsController.getInstance().doctorProfleList.size() > 0) {
            return PatientMedicalRecordsController.getInstance().doctorProfleList.size();
        } else {
            return 0;
        }
    }

    public class DoctorSearchHolder extends RecyclerView.ViewHolder {
        CircleImageView docPic;
        TextView txtDocName, txtDocProfession;
        Button btnBook;

        public DoctorSearchHolder(@NonNull View itemView) {
            super(itemView);
            docPic = itemView.findViewById(R.id.img_doc_pic);
            btnBook = itemView.findViewById(R.id.btn_book);
            txtDocName = itemView.findViewById(R.id.txt_doc_name);
            txtDocProfession = itemView.findViewById(R.id.txt_doc_profession);
        }
    }
}

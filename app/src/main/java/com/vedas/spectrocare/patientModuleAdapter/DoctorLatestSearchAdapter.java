package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vedas.spectrocare.PatientModule.PatientBookAppointmentActivity;
import com.vedas.spectrocare.PatientMoreModule.CalenderNew;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.DoctorsItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorLatestSearchAdapter extends RecyclerView.Adapter<DoctorLatestSearchAdapter.DoctorSearchHolder> {
    Context context;
    ArrayList<DoctorsItemModel> doctorsLatestList;

    public DoctorLatestSearchAdapter(Context context, ArrayList<DoctorsItemModel> doctorsLatestList) {
        this.context = context;
        this.doctorsLatestList = doctorsLatestList;
    }

    @NonNull
    @Override
    public DoctorLatestSearchAdapter.DoctorSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctoresLatestSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_latest_search_item, parent, false);
        return new DoctorSearchHolder(doctoresLatestSearchView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorLatestSearchAdapter.DoctorSearchHolder holder, int position) {
       // holder.docPic.setImageResource(doctorsLatestList.get(position).getCategoryIcon());
        holder.txtDocName.setText(doctorsLatestList.get(position).getDoctorName());
        holder.txtDocProfession.setText(doctorsLatestList.get(position).getDocProfession());
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PatientBookAppointmentActivity.class)
                        .putExtra("docName",doctorsLatestList.get(holder.getAdapterPosition()).getDoctorName())
                .putExtra("docProfi",doctorsLatestList.get(holder.getAdapterPosition()).getDocProfession()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsLatestList.size();
    }

    public class DoctorSearchHolder extends RecyclerView.ViewHolder {
        CircleImageView docPic;
        TextView  txtDocName,txtDocProfession;
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

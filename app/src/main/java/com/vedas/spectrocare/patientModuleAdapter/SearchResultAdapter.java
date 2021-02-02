/*
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
import com.vedas.spectrocare.PatientModule.DoctorSummeryActivity;
import com.vedas.spectrocare.PatientModule.PatientBookAppointmentActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.DoctorsItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResulHolder> {
    Context context;
    ArrayList<DoctorsItemModel> doctorsLatestList;

    public SearchResultAdapter(Context context, ArrayList<DoctorsItemModel> doctorsLatestList) {
        this.context = context;
        this.doctorsLatestList = doctorsLatestList;
    }

    @NonNull
    @Override
    public SearchResultAdapter.SearchResulHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_latest_search_item, parent, false);
        return new SearchResulHolder(doctorSearchView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.SearchResulHolder holder, int position) {
        Log.e("ds","ddd");
        //holder.docPic.setImageResource(doctorsLatestList.get(position).getCategoryIcon());
        Log.e("adfaf","a"+ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon());
      //  Picasso.get().load(ServerApi.img_home_url+doctorsLatestList.get(position).getCategoryIcon());
        Picasso.get().load(ServerApi.img_home_url+doctorsLatestList.get(position).
                getCategoryIcon()).placeholder(R.drawable.profile_1)
                .into(holder.docPic);
        holder.txtDocName.setText(doctorsLatestList.get(position).getDoctorName());
        holder.txtDocProfession.setText(doctorsLatestList.get(position).getDocProfession());
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PatientBookAppointmentActivity.class));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().currentdepartmentModel = responseModel.getMedicalPersonnels().get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, DoctorSummeryActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorsLatestList.size();
    }

    public class SearchResulHolder extends RecyclerView.ViewHolder {
        CircleImageView docPic;
        Button btnBook;
        TextView txtDocName,txtDocProfession;
        public SearchResulHolder(@NonNull View itemView) {
            super(itemView);
            docPic = itemView.findViewById(R.id.img_doc_pic);
            txtDocName = itemView.findViewById(R.id.txt_doc_name);
            btnBook = itemView.findViewById(R.id.btn_book);
            txtDocProfession = itemView.findViewById(R.id.txt_doc_profession);

        }
    }
    public void filterList(ArrayList<DoctorsItemModel> filterdNames) {
        this.doctorsLatestList = filterdNames;
        notifyDataSetChanged();
    }

}
*/

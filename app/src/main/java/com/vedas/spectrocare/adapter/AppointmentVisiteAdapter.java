package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.vedas.spectrocare.BLEConnectionModule.Constants;
import com.vedas.spectrocare.Controllers.DoctorInfoServerObjectDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AppointmentsActivity;
import com.vedas.spectrocare.activities.FiltersActivity;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;
public class AppointmentVisiteAdapter extends RecyclerView.Adapter<AppointmentVisiteAdapter.FilterHolder> {
    Context context;
    TextView textView;
    private ArrayList<FiltersActivity.Model> mModelList;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    public AppointmentVisiteAdapter(Context context, ArrayList<FiltersActivity.Model> list) {
        this.context = context;
        this.mModelList = list;
    }


    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters_recycle_item, parent, false);
        return new FilterHolder(filterView);
    }
    @Override
    public void onBindViewHolder(@NonNull final FilterHolder holder, int position) {
        final FiltersActivity.Model model = mModelList.get(position);
        String status = model.getText();
        holder.txtFilterItem.setText(status);
        loadData(holder,model);
        if (model.isSelected()){
            holder.txtFilterItem.setBackgroundResource(R.drawable.new_login_boarder);
            holder.txtFilterItem.setTextColor(parseColor("#3E454C"));

        }
        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setSelected(!model.isSelected());
                holder.txtFilterItem.setBackgroundResource(model.isSelected() ? R.drawable.btn_bck_color : R.drawable.new_login_boarder);
                holder.txtFilterItem.setTextColor(model.isSelected() ? parseColor("#ffffff") : parseColor("#3E454C"));
                if (model.isSelected()) {
                    stringArrayList.add(model.getText());
                } else {
                    stringArrayList.remove(model.getText());
                }
                Log.e("stutuslist", "call" + stringArrayList.toString());
                DoctorInfoServerObjectDataController.removeDuplicates(stringArrayList);
                DoctorInfoServerObjectDataController.getInstance().selectedVisiteTypeArray = stringArrayList;
            }
        });

    }
    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }
    public class FilterHolder extends RecyclerView.ViewHolder {
        TextView txtFilterItem;

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.item_filter);
        }
    }
    private void loadData(FilterHolder holder,FiltersActivity.Model model){
        if (DoctorInfoServerObjectDataController.getInstance().selectedVisiteTypeArray.size() > 0) {
            for (String element:  DoctorInfoServerObjectDataController.getInstance().selectedVisiteTypeArray) {
                if(model.getText().equals(element)){
                    model.setSelected(true);
                    stringArrayList.add(element);
                    holder.txtFilterItem.setBackgroundResource(model.isSelected() ? R.drawable.btn_bck_color : R.drawable.new_login_boarder);
                    holder.txtFilterItem.setTextColor(model.isSelected() ? parseColor("#ffffff") : parseColor("#3E454C"));
                }
            }
        }
    }
}

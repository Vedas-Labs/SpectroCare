package com.vedas.spectrocare.PatientModule;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PastAppointmentFragment.OnListFragmentInteractionListener;
import com.vedas.spectrocare.R;

import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PastAppointmentViewAdapter extends RecyclerView.Adapter<PastAppointmentViewAdapter.ViewHolder> {
    public PastAppointmentViewAdapter() {
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upcoming_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        holder.mainLayout.setBackgroundDrawable(gd);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        RelativeLayout mainLayout;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mainLayout= (RelativeLayout)view. findViewById(R.id.mainLayout);

        }
    }
}

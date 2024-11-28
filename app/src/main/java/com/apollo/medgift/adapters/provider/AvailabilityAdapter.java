package com.apollo.medgift.adapters.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.models.Availability;

import java.util.List;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.AvailabilityViewHolder> {
    private List<Availability> availabilityList;
    private Context context;

    public AvailabilityAdapter(List<Availability> availabilityList, Context context) {
        this.availabilityList = availabilityList;
        this.context = context;
    }

    @Override
    public AvailabilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.availability_item, parent, false);
        return new AvailabilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailabilityViewHolder holder, int position) {
        Availability availability = availabilityList.get(position);
        holder.bind(availability);
    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    public static class AvailabilityViewHolder extends RecyclerView.ViewHolder {
        // Declare the views
        private TextView dayTextView;
        private TextView startTimeTextView;
        private TextView endTimeTextView;

        public AvailabilityViewHolder(View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.txtDay);
            startTimeTextView = itemView.findViewById(R.id.edtStartTime);
            endTimeTextView = itemView.findViewById(R.id.edtEndTime);
        }

        public void bind(Availability availability) {

            dayTextView.setText(availability.getDay());
            startTimeTextView.setText(availability.getStart());
            endTimeTextView.setText(availability.getEnd());
        }
    }
}
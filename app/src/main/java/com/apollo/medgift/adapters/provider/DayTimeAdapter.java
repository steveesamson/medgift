package com.apollo.medgift.adapters.provider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.OnModelManageCallback;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.DaytimeItemBinding;
import com.apollo.medgift.databinding.RecipientItemBinding;
import com.apollo.medgift.models.DateTimeValue;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.views.gifter.AddRecipientActivity;

import java.util.ArrayList;
import java.util.List;

public class DayTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<DayTime> times = new ArrayList<>();

    public DayTimeAdapter(List<DayTime> times, Context context) {
        this.context = context;
        this.times = times;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                DaytimeItemBinding itemBinding = DaytimeItemBinding.inflate(layoutInflater, parent, false);
                return new DayTimeHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayTimeHolder recipientHolder = (DayTimeHolder) holder;
        recipientHolder.bindData(this.times.get(position));
    }


    @Override
    public int getItemCount() {
        return times.size();
    }

    class DayTimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final DaytimeItemBinding itemBinding;
        private DayTime dayTime;

        public DayTimeHolder(DaytimeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemBinding.btnDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void bindData(DayTime dayTime) {
            this.dayTime = dayTime;
            itemBinding.txtDay.setText(dayTime.getDay());
            itemBinding.txtStart.setText(new DateTimeValue(dayTime.getStartTime()).toAMPM());
            itemBinding.txtEnd.setText(new DateTimeValue(dayTime.getEndTime()).toAMPM());
        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.btnDelete) {
                // Delete here
                Util.showConfirm(context, "Delete", "Do your really want to delete this availability?", (dialog, which) -> {
                    // Implement delete
                    ((OnModelManageCallback)context).onDeleting(this.dayTime);
                    dialog.dismiss();
                });
            }
            else{
                ((OnModelManageCallback)context).onEdit(this.dayTime);
            }
        }
    }
}

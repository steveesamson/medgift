package com.apollo.medgift.adapters.gifters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.OnModelSelectCallback;
import com.apollo.medgift.databinding.ScheduleItemBinding;
import com.apollo.medgift.models.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Schedule> schedules;
    private static Schedule activeSchedule;

    public ScheduleAdapter(Context context, List<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ScheduleItemBinding scheduleItemBinding = ScheduleItemBinding.inflate(layoutInflater, parent, false);
        return new ScheduleHolder(scheduleItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleHolder contributorHolder = (ScheduleHolder) holder;
        contributorHolder.bindData(schedules.get(position));
    }

    public void clearActiveSchedule(){
        activeSchedule = null;
    }
    @Override
    public int getItemCount() {

        return schedules.size();
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {
        private final ScheduleItemBinding itemBinding;
        private Schedule schedule;
        public ScheduleHolder(ScheduleItemBinding _itemBinding) {
            super(_itemBinding.getRoot());
            itemBinding = _itemBinding;
            itemView.setOnClickListener((v) ->{
                    activeSchedule = this.schedule;
                    ((OnModelSelectCallback)context).onScheduleSelected(this.schedule);
                ScheduleAdapter.this.notifyDataSetChanged();
            });

        }

        public void bindData(Schedule schedule){
            this.schedule = schedule;
            itemBinding.txtSchedule.setText(schedule.toAMPM());

            if(activeSchedule != null){
                itemBinding.scheduleCheck.setVisibility(schedule.getSchedule().equals(activeSchedule.getSchedule())? View.VISIBLE : View.GONE);
            }
        }
    }
}
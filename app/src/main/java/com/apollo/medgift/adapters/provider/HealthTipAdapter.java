package com.apollo.medgift.adapters.provider;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.ReadHealthTipActivity;
import com.apollo.medgift.databinding.HealthtipItemBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.views.provider.AddHealthTipActivity;

import java.util.List;

public class HealthTipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<HealthTip> healthtips;

    public HealthTipAdapter(List<HealthTip> healthTips, Context context) {
        this.healthtips = healthTips;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        HealthtipItemBinding binding = HealthtipItemBinding.inflate(layoutInflater, parent, false);
        return new HealthTipHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HealthTipHolder healthTipHolder = (HealthTipHolder) holder;
        healthTipHolder.bindData(healthtips.get(position));
    }

    @Override
    public int getItemCount() {
        return healthtips.size();
    }

    class HealthTipHolder extends RecyclerView.ViewHolder {
        private final HealthtipItemBinding itemBinding;
        private HealthTip healthtip;

        public HealthTipHolder(HealthtipItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Role.GIFTER.equals(Firebase.currentUser().getUserRole())) {
                        Intent intent = new Intent(context, ReadHealthTipActivity.class);
                        intent.putExtra(HealthTip.STORE, HealthTipHolder.this.healthtip);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, AddHealthTipActivity.class);
                         intent.putExtra(HealthTip.STORE, HealthTipHolder.this.healthtip);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bindData(HealthTip healthTip) {
            this.healthtip = healthTip;
            itemBinding.txtTitle.setText(healthTip.getTitle());
            itemBinding.createdBy.setText(healthTip.getCreatedByName());
        }

    }

}


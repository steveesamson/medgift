package com.apollo.medgift.adapters.provider;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.HealthtipItemBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.User;
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

    class HealthTipHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final HealthtipItemBinding itemBinding;
        private HealthTip healthtip;

        public HealthTipHolder(HealthtipItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemBinding.btnDelete.setOnClickListener(this);
            itemBinding.txtTitle.setOnClickListener(this);
            if (User.Role.GIFTER.name().equals(Firebase.currentUser().getUserRole())) {
                // hide button for "Gifter"
                itemBinding.btnDelete.setVisibility(View.GONE);
            } else {
                // enable buttons for other roles
                itemBinding.btnDelete.setVisibility(View.VISIBLE);
            }

        }

        public void bindData(HealthTip healthTip) {
            this.healthtip = healthTip;
            itemBinding.txtTitle.setText(healthTip.getTitle());
            itemBinding.txtDescription.setText(truncateString(healthTip.getContent()));
            itemBinding.txtRole.setText(healthTip.getCreatedByName());
        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.btnDelete) {
                // Delete here
                Util.showConfirm(context, "Delete", "Do your really want to delete this health tip?", (dialog, which) -> {
                    // Implement delete
                    Firebase.delete(healthtip, HealthTip.STORE, (task) -> {
                        if (task.isSuccessful()) {
                            Util.notify(context, "Health Tip deleted!");
                        }
                    });
                    dialog.dismiss();
                });
            } else if (view == itemBinding.txtTitle) { // Display health tip form for details
                Intent intent = new Intent(context, AddHealthTipActivity.class);
                intent.putExtra(HealthTip.STORE, HealthTipHolder.this.healthtip);
                context.startActivity(intent);
            }
        }
    }

    public static String truncateString(String input) {
        return input.length() > 9 ? input.substring(0, 9) + "..." : input;
    }

}


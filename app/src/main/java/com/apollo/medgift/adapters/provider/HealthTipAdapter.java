package com.apollo.medgift.adapters.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.databinding.HealthtipItemBinding;
import com.apollo.medgift.models.HealthTip;

import java.util.List;

public class HealthTipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HealthTip> healthtips;
    public HealthTipAdapter(List<HealthTip> healthTips, Context context){
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
//            itemBinding.btnDelete.setOnClickListener(this);
//            itemBinding.txtName.setOnClickListener(this);

        }

        public void bindData(HealthTip healthTip) {
            this.healthtip = healthTip;
//            itemBinding.txtName.setText(String.format("%s %s", recipient.getFirstName(), recipient.getLastName()));
        }

        @Override
        public void onClick(View view) {
//            if (view == itemBinding.btnDelete) {
//                // Delete here
//                Util.showConfirm(context, "Delete", "Do your really want to delete this recipient?", (dialog, which) -> {
//                    // Implement delete
//                    Firebase.delete(healthtip, Recipient.STORE,(task) -> {
//                        if(task.isSuccessful()){
//                            Util.notify(context, "Recipient deleted!");
//                        }
//                    });
//                    dialog.dismiss();
//                });
//            } else if(view == itemBinding.txtName){ // Display recipient form for details
//                Intent intent = new Intent(context, AddRecipientActivity.class);
//                intent.putExtra(Recipient.STORE, RecipientAdapter.RecipientHolder.this.recipient);
//                context.startActivity(intent);
//            }
        }
    }
}


package com.apollo.medgift.adapters.gifters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.RecipientItemBinding;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.views.gifter.AddRecipientActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<Recipient> recipients = new ArrayList<>();

    public RecipientAdapter(List<Recipient> recipients, Context context) {
        this.context = context;
        this.recipients = recipients;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                RecipientItemBinding itemBinding = RecipientItemBinding.inflate(layoutInflater, parent, false);
                return new RecipientHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipientHolder recipientHolder = (RecipientHolder) holder;
        recipientHolder.bindData(this.recipients.get(position));
    }


    @Override
    public int getItemCount() {
        return recipients.size();
    }

    class RecipientHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final RecipientItemBinding itemBinding;
        private Recipient recipient;

        public RecipientHolder(RecipientItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemBinding.btnDelete.setOnClickListener(this);
            itemBinding.txtName.setOnClickListener(this);

        }

        public void bindData(Recipient recipient) {
            this.recipient = recipient;
            itemBinding.txtName.setText(String.format("%s %s <%s>", recipient.getFirstName(), recipient.getLastName(), recipient.getPhone()));
        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.btnDelete) {
                // Delete here
                Util.showConfirm(context, "Delete", "Do your really want to delete this recipient?", (dialog, which) -> {
                    // Implement delete
                    Firebase.delete(recipient, Recipient.STORE,(task) -> {
                        if(task.isSuccessful()){
                            Util.notify(context, "Recipient deleted!");
                        }
                    });
                    dialog.dismiss();
                });
            } else if(view == itemBinding.txtName){ // Display recipient form for details
                Intent intent = new Intent(context, AddRecipientActivity.class);
                intent.putExtra(Recipient.STORE, RecipientHolder.this.recipient);
                context.startActivity(intent);
            }
        }
    }
}
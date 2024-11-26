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
import com.apollo.medgift.databinding.GiftItemBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.views.gifter.AddGiftActivity;

import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<Gift> gifts = new ArrayList<>();

    public GiftAdapter(List<Gift> gifts, Context context) {
        this.context = context;
        this.gifts = gifts;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GiftItemBinding itemBinding = GiftItemBinding.inflate(layoutInflater, parent, false);
        return new GiftHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GiftHolder giftHolder = (GiftHolder) holder;
        giftHolder.bindData(this.gifts.get(position));
    }


    @Override
    public int getItemCount() {
        return gifts.size();
    }

    class GiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final GiftItemBinding itemBinding;
        private Gift gift;

        public GiftHolder(GiftItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemBinding.manageGiftView.setOnClickListener(this);
            itemBinding.addServiceView.setOnClickListener(this);
            itemBinding.btnDelete.setOnClickListener(this);

        }

        public void bindData(Gift gift) {
            this.gift = gift;
            itemBinding.giftTitle.setText(String.format("%s", gift.getName()));
            itemBinding.giftDescription.setText(String.format("%s", gift.getDescription()));
        }

        @Override
        public void onClick(View view)
        {
            if (view == itemBinding.btnDelete)
            {
                // Delete here
                Util.showConfirm(context, "Delete", "Do your really want to delete this Gift?", (dialog, which) -> {
                    // Implement delete
                    Firebase.delete(gift, Gift.STORE,(task) -> {
                        if(task.isSuccessful()){
                            Util.notify(context, "Gift deleted!");
                        }
                    });
                    dialog.dismiss();
                });
            }
            if(view == itemBinding.manageGiftView.getRootView()) { // Display gift form for details
                Intent intent = new Intent(context, AddGiftActivity.class);
                intent.putExtra(Gift.STORE, GiftHolder.this.gift);
                context.startActivity(intent);
            }
        }
    }
}

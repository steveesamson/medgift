package com.apollo.medgift.adapters.gifters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.databinding.ContributorItemBinding;
import com.apollo.medgift.models.GiftService;


import java.util.List;

public class ContributorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<GiftService> contributorList;


    public ContributorAdapter(Context context, List<GiftService> contributorList) {
        this.context = context;
        this.contributorList = contributorList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ContributorItemBinding contributorItemBinding = ContributorItemBinding.inflate(layoutInflater, parent, false);
        return new ContributorHolder(contributorItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContributorHolder contributorHolder = (ContributorHolder) holder;
        contributorHolder.bindData(contributorList.get(position));
    }

    @Override
    public int getItemCount() {

        return contributorList.size();
    }

    public static class ContributorHolder extends RecyclerView.ViewHolder {
        private ContributorItemBinding contributorItemBinding;
        public ContributorHolder(ContributorItemBinding contributorItemBinding) {
            super(contributorItemBinding.getRoot());
            this.contributorItemBinding = contributorItemBinding;
        }

        public void bindData(GiftService giftService){
            contributorItemBinding.txtName.setText(giftService.toString());
        }
    }
}
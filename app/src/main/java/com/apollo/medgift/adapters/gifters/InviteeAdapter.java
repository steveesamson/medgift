package com.apollo.medgift.adapters.gifters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.databinding.ContributorItemBinding;
import com.apollo.medgift.models.GiftInvite;

import java.util.List;

public class InviteeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<GiftInvite> invitees;


    public InviteeAdapter(Context context, List<GiftInvite> contributorList) {
        this.context = context;
        this.invitees = contributorList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ContributorItemBinding contributorItemBinding = ContributorItemBinding.inflate(layoutInflater, parent, false);
        return new InviteeHolder(contributorItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InviteeHolder contributorHolder = (InviteeHolder) holder;
        contributorHolder.bindData(invitees.get(position));
    }

    @Override
    public int getItemCount() {

        return invitees.size();
    }

    public static class InviteeHolder extends RecyclerView.ViewHolder {
        private ContributorItemBinding contributorItemBinding;
        public InviteeHolder(ContributorItemBinding contributorItemBinding) {
            super(contributorItemBinding.getRoot());
            this.contributorItemBinding = contributorItemBinding;
        }

        public void bindData(GiftInvite invite){

        }
    }
}
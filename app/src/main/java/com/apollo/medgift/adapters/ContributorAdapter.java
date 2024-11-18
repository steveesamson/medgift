package com.apollo.medgift.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.models.Contributor;


import java.util.List;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ViewHolder> {

    private final Context context;
    private final List<Contributor> contributorList;


    public ContributorAdapter(Context context, List<Contributor> contributorList) {
        this.context = context;
        this.contributorList = contributorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contributor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Contributor contributor = contributorList.get(position);
        holder.name.setText(contributor.getFirstName() + " " + contributor.getLastName());
        holder.email.setText(contributor.getEmail());
    }

    @Override
    public int getItemCount() {

        return contributorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contributorName);
            email = itemView.findViewById(R.id.contributorEmail);
        }
    }
}
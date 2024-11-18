package com.apollo.medgift.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.models.Gift;

import java.util.List;

public class GiftRecyclerViewAdapter extends RecyclerView.Adapter<GiftRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Gift> giftList;

    public GiftRecyclerViewAdapter(Context context, List<Gift> giftList) {
        this.context = context;
        this.giftList = giftList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gift gift = giftList.get(position);


    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
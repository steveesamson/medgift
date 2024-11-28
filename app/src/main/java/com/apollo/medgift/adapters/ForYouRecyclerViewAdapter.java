package com.apollo.medgift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;

public class ForYouRecyclerViewAdapter extends RecyclerView.Adapter<ForYouRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final int[] images;
    private final String[] titles;
    private final String[] providers;
    private final String[] locations;
    private final String[] descriptions;
    private final String[] prices;

    public ForYouRecyclerViewAdapter(Context context, int[] images, String[] titles, String[] providers, String[] locations, String[] descriptions, String[] prices) {
        this.context = context;
        this.images = images;
        this.titles = titles;
        this.providers = providers;
        this.locations = locations;
        this.descriptions = descriptions;
        this.prices = prices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foryourecyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.giftImage.setImageResource(images[position]);
        holder.giftTitle.setText(titles[position]);
        holder.giftProvider.setText(providers[position]);
        holder.giftDescription.setText(descriptions[position]);
        holder.giftPrice.setText(prices[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView giftImage;
        TextView giftTitle, giftProvider, giftLocation, giftDescription, giftPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            giftImage = itemView.findViewById(R.id.giftImage);
            giftTitle = itemView.findViewById(R.id.giftTitle);
            giftDescription = itemView.findViewById(R.id.giftDescription);
            giftPrice = itemView.findViewById(R.id.giftPrice);
        }
    }
}

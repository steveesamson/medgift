package com.apollo.medgift.adapters.provider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.R;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ForyourecyclerviewBinding;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.User;
import com.apollo.medgift.views.gifter.ViewServiceActivity;
import com.apollo.medgift.views.provider.CreateServiceActivity;

import java.util.List;
import java.util.Random;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<HealthcareService> healthcareServices;

    public ServiceAdapter(List<HealthcareService> healthcareServices, Context context) {
        this.context = context;
        this.healthcareServices = healthcareServices;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ForyourecyclerviewBinding binding = ForyourecyclerviewBinding.inflate(layoutInflater, parent, false);
        return new ServiceHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ServiceHolder serviceHolder = (ServiceHolder) holder;
        serviceHolder.bindData(healthcareServices.get(position));
    }

    @Override
    public int getItemCount() {
        return healthcareServices.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ForyourecyclerviewBinding itemBinding;
        private HealthcareService healthcareService;

        public ServiceHolder(ForyourecyclerviewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {
            itemBinding.serviceItem.setOnClickListener(this);
        }

        public void bindData(HealthcareService healthcareService) {
            this.healthcareService = healthcareService;
            itemBinding.giftTitle.setText(healthcareService.getServiceName());
            itemBinding.giftDescription.setText(healthcareService.getDescription());
            itemBinding.giftPrice.setText("$ " + healthcareService.getPrice());
            if (healthcareService.getBannerUrl() != null && !healthcareService.getBannerUrl().isEmpty()) {
                Util.loadImageUri(itemBinding.giftImage, healthcareService.getBannerUrl(), context);
            } else {
                itemBinding.giftImage.setImageResource(R.drawable.default_service_image);
            }

            itemBinding.ratingBar.setRating(getRandomNumber("star"));
            itemBinding.ratingCount.setText(String.valueOf(getRandomNumber("count")));
        }

        public int getRandomNumber(String type) {
            Random random = new Random();
            if (type == "star") {
                return random.nextInt(5) + 1;
            }
            if (type == "count") {
                return random.nextInt(800) + 1;
            }
            return 0;
        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.serviceItem) {
                if (Firebase.currentUser().getUserRole().equals(Role.GIFTER)) {
                    Intent intent = new Intent(context, ViewServiceActivity.class);
                    intent.putExtra(HealthcareService.STORE, ServiceHolder.this.healthcareService);
                    context.startActivity(intent);
                }
                if (Firebase.currentUser().getUserRole().equals(Role.PROVIDER)) {
                    Intent intent = new Intent(context, CreateServiceActivity.class);
                    intent.putExtra(HealthcareService.STORE, ServiceHolder.this.healthcareService);
                    context.startActivity(intent);
                }
            }
        }
    }

}

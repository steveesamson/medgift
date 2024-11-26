package com.apollo.medgift.adapters.provider;

import static com.apollo.medgift.adapters.provider.HealthTipAdapter.truncateString;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.databinding.ForyourecyclerviewBinding;
import com.apollo.medgift.databinding.ServicetypeItemBinding;
import com.apollo.medgift.models.HealthTip;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Service;
import com.apollo.medgift.views.provider.AddHealthTipActivity;
import com.apollo.medgift.views.provider.CreateServiceActivity;

import java.util.List;

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
        }

        public void bindData(HealthcareService healthcareService) {
            this.healthcareService = healthcareService;
            itemBinding.giftTitle.setText(healthcareService.getServiceName());
            itemBinding.giftProvider.setText(healthcareService.getProviderId());
            itemBinding.giftDescription.setText(truncateString(healthcareService.getDescription()));
            itemBinding.giftPrice.setText(String.valueOf(healthcareService.getPrice()));
//            itemBinding.giftImage.setImageBitmap();

        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.serviceItem) {
                Intent intent = new Intent(context, CreateServiceActivity.class);
                intent.putExtra(Service.STORE, ServiceHolder.this.healthcareService);
                context.startActivity(intent);
            }
        }
    }

    public static String truncateString(String input) {
        return input.length() > 20 ? input.substring(0, 20) + "..." : input;
    }

}

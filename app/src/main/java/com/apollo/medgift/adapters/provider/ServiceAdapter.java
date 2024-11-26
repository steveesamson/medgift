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
import com.apollo.medgift.models.Service;
import com.apollo.medgift.views.provider.AddHealthTipActivity;
import com.apollo.medgift.views.provider.CreateServiceActivity;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Service> services;

    public ServiceAdapter(List<Service> services, Context context) {
        this.context = context;
        this.services = services;
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
        serviceHolder.bindData(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ForyourecyclerviewBinding itemBinding;
        private Service service;

        public ServiceHolder(ForyourecyclerviewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindData(Service service) {
            this.service = service;
            itemBinding.giftTitle.setText(service.getServiceName());
            itemBinding.giftProvider.setText(service.getServiceAssignee());
            itemBinding.giftLocation.setText(service.getHospital());
            itemBinding.giftLocation.setText(service.getHospital());
            itemBinding.giftDescription.setText(truncateString(service.getDescription()));
            itemBinding.giftPrice.setText(String.valueOf(service.getPrice()));
//            itemBinding.giftImage.setImageBitmap();

        }

        @Override
        public void onClick(View view) {
            if (view == itemBinding.serviceItem) {
                Intent intent = new Intent(context, CreateServiceActivity.class);
                intent.putExtra(Service.STORE, ServiceHolder.this.service);
                context.startActivity(intent);
            }
        }
    }

    public static String truncateString(String input) {
        return input.length() > 20 ? input.substring(0, 20) + "..." : input;
    }

}

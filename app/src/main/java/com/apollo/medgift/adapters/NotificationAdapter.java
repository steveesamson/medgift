package com.apollo.medgift.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.databinding.NotificationItemBinding;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Notification;
import com.apollo.medgift.models.NotificationType;
import com.apollo.medgift.views.InviteInfoActivity;
import com.apollo.medgift.views.ServiceInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<Notification> notifications = new ArrayList<>();

    public NotificationAdapter(List<Notification> notifications, Context context) {
        this.context = context;
        this.notifications = notifications;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                NotificationItemBinding itemBinding = NotificationItemBinding.inflate(layoutInflater, parent, false);
                return new NotificationHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationHolder recipientHolder = (NotificationHolder) holder;
        recipientHolder.bindData(this.notifications.get(position));
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder {
        private final NotificationItemBinding itemBinding;
        private Notification notification;

        public NotificationHolder(NotificationItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            setupListeners();
        }

        private void setupListeners() {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationType type = NotificationType.valueOf(notification.getModelName());
                    switch (type){
                        case GiftInvite:
                            Firebase.getModelBy(GiftInvite.STORE,"key", notification.getModelKey(), GiftInvite.class, (giftInvite) ->{
                                Intent intent = new Intent(context, InviteInfoActivity.class);
                                intent.putExtra(GiftInvite.STORE, giftInvite);
                                context.startActivity(intent);
                            });
                            break;
                        case GiftService:
                            Firebase.getModelBy(GiftService.STORE,"key", notification.getModelKey(), GiftService.class, (giftService) ->{
                                Intent intent = new Intent(context, ServiceInfoActivity.class);
                                intent.putExtra(GiftService.STORE, giftService);
                                context.startActivity(intent);
                            });
                            break;
                    }
                }
            });

        }

        public void bindData(Notification notification) {
            this.notification = notification;
            itemBinding.txtTitle.setText(notification.getTitle());
            itemBinding.txtBody.setText(notification.getBody());
        }

    }
}

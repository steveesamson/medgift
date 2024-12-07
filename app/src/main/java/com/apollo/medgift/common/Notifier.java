package com.apollo.medgift.common;

import android.content.Context;
import android.util.Log;

import com.apollo.medgift.jobs.JobUtil;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.InviteStatus;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.Notification;
import com.apollo.medgift.models.NotificationStatus;
import com.apollo.medgift.models.NotificationType;
import com.apollo.medgift.models.Role;
import com.apollo.medgift.models.ServiceStatus;
import com.apollo.medgift.models.SessionUser;

import java.util.ArrayList;
import java.util.List;

public class Notifier implements Closeable {
    private static Closeable giftServiceChildEvents;
    private static Closeable notificationChildEvents;
    private boolean watchIsStarted = false;
    private SessionUser sessionUser;
    private static Notifier instance = null;

    private Notifier(){
            this.sessionUser = Firebase.currentUser();
    }
    public static Notifier getInstance(){
        if(instance == null){
           instance = new Notifier();
        }
        return instance;
    }


    public void notifyInvite(Context context, GiftInvite added){
        if(added.getStatus().equals(InviteStatus.NOTIFIED)){
            return;
        }
        String title = "Group Gift Invitation";
        String body = String.format("Hey %s, you have been invited to contribute to a group gift.", sessionUser.getUserName());
        Message message = new Message();

        message.setTitle(title);
        message.setBody(body);
        message.setButtonLabel("Contribute now");
        message.setPayLoad(added);
        message.setNotificationType(NotificationType.GiftInvite);
        NotificationUtil.sendNotification(context, message);
        added.setStatus(InviteStatus.NOTIFIED);
        NotificationUtil.saveNotification(message, added.getInviteeId());

        Firebase.save(added, GiftInvite.STORE,(task, key) ->{});
    }
    public void beginWatches(Context context) {
        // GiftService
        // GiftInvite
        //

        if(watchIsStarted){
            return;
        }
        watchIsStarted = true;
        Log.i("DBUG: ", "beginWatches");
        SessionUser sessionUser = Firebase.currentUser();
        assert  sessionUser != null;

        notificationChildEvents = new ChildEvents<>(Notification.STORE,"createdFor", sessionUser.getUserId(), Notification.class, (added) ->{
            if(added != null && added.getStatus().equals(NotificationStatus.PENDING)){
               BaseActivity base = (BaseActivity) context;
               added.setStatus(NotificationStatus.VIEWED);
               Firebase.save(added, Notification.STORE,(task, key) ->{
                   base.onNotified(true);
               });

            }
        }, (updated) ->{});

        if(sessionUser.getUserRole().equals(Role.GIFTER)){

             Firebase.getModelsBy(GiftService.STORE,"giftOwner", sessionUser.getUserId(), GiftService.class, (giftServices) ->{
                if(giftServices != null){
                    List<GiftService> srvs = new ArrayList<>();
                    for(GiftService gs: giftServices){
                        if(gs.getStatus().equals(ServiceStatus.SCHEDULED)){
                            srvs.add(gs);
                        }
                    }
                    JobUtil.scheduleJobs(srvs, sessionUser.getUserId(), context);
                }
            });

            // Invitee
            Firebase.getModelsBy(GiftInvite.STORE,"inviteeId", sessionUser.getUserId(), GiftInvite.class, (giftInvites) ->{
                if(giftInvites != null){
                    for(GiftInvite gi: giftInvites){
                        if(gi.getStatus().equals(InviteStatus.PENDING)){
                            notifyInvite(context, gi);
                        }
                    }
                }
            });

            //My Gifts
            giftServiceChildEvents = new ChildEvents<>(GiftService.STORE,"giftOwner", sessionUser.getUserId(), GiftService.class, (added) ->{
                if(added != null && !added.getGifterEmail().equals(sessionUser.getEmail())){
                    // notify new service for gift
                    Message message = new Message();
                    String title = "New Contributor";
                    String msg = String.format("Congrats! %s just contributed '%s' to your group gift for %s. Click details to see.", added.getGifterName(), added.getServiceName(), added.getRecipientName());
                    message.setTitle(title);
                    message.setBody(msg);
                    message.setButtonLabel("Service Details");
                    message.setPayLoad(added);
                    message.setNotificationType(NotificationType.GiftService);
                    NotificationUtil.sendNotification(context, message);
                    NotificationUtil.saveNotification(message, sessionUser.getUserId());
                    JobUtil.scheduleJob(added, sessionUser.getUserId(), context);
                }
            }, (updated) ->{
                if(updated != null && !(updated.getStatus().equals(ServiceStatus.SCHEDULED) || updated.getStatus().equals(ServiceStatus.NOTIFIED))){
                    // notify service update for gift
                    String title = "";
                    String body = "";
                    if(updated.getStatus().equals(ServiceStatus.DELIVERED)){
                        title = "Gift Service Delivered!";
                        body = String.format("Hey %s, %s has been delivered to one of your recipients: %s", sessionUser.getUserName(), updated.getServiceName(), updated.getRecipientName());
                    }
                    if(updated.getStatus().equals(ServiceStatus.CONFIRMED)){
                        title = "Gift Service Completed!";
                        body = String.format("Hey %s, %s service gifted to %s is now completed.", sessionUser.getUserName(), updated.getServiceName(), updated.getRecipientName());

                    }
                    Message message = new Message();

                    message.setTitle(title);
                    message.setBody(body);
                    message.setButtonLabel("Service Details");
                    message.setPayLoad(updated);
                    message.setNotificationType(NotificationType.GiftService);
                    NotificationUtil.saveNotification(message, sessionUser.getUserId());
                    NotificationUtil.sendNotification(context, message);
                }
            } );



        }else if(sessionUser.getUserRole().equals(Role.PROVIDER)){
            Firebase.getModelsBy(GiftService.STORE,"serviceOwner", sessionUser.getUserId(), GiftService.class, (giftServices) ->{
                if(giftServices != null){
                    List<GiftService> srvs = new ArrayList<>();
                    for(GiftService gs: giftServices){
                        if(gs.getStatus().equals(ServiceStatus.SCHEDULED)){
                            srvs.add(gs);
                        }
                    }
                    JobUtil.scheduleJobs(srvs, sessionUser.getUserId(), context);
                }
            });
            giftServiceChildEvents = new ChildEvents<>(GiftService.STORE,"serviceOwner", sessionUser.getUserId(), GiftService.class, (added) ->{
                if(added != null){
                    // Notify of a service schedule
                    Message message = new Message();
                    String title = "New Service Request";
                    String msg = String.format("Hello! Your service, %s has been purchased for delivery to '%s' on %s. Click details to see.", added.getServiceName(), added.getRecipientName(), added.getDeliveryDate());
                    message.setTitle(title);
                    message.setBody(msg);
                    message.setButtonLabel("Service Details");
                    message.setPayLoad(added);

                    message.setNotificationType(NotificationType.GiftService);
                    NotificationUtil.sendNotification(context, message);
                    JobUtil.scheduleJob(added, sessionUser.getUserId(), context);
                }
            }, (updated) ->{
                if(updated != null && updated.getStatus().equals(ServiceStatus.CONFIRMED)){
                    // Notify of a service delivery confirmation

                    String title = "Gift Service Completed!";
                    String body = String.format("Hey %s, %s service gifted to %s is now completed.", sessionUser.getUserName(), updated.getServiceName(), updated.getRecipientName());
                    Message message = new Message();

                    message.setTitle(title);
                    message.setBody(body);
                    message.setButtonLabel("Service Details");
                    message.setPayLoad(updated);
                    message.setNotificationType(NotificationType.GiftService);
                    NotificationUtil.sendNotification(context, message);

                }
            } );
        }


    }


    @Override
    public void release() {
        giftServiceChildEvents.release();
        notificationChildEvents.release();
        this.sessionUser = null;
        this.watchIsStarted = false;
        instance = null;
    }
}

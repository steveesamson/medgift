package com.apollo.medgift.jobs;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.apollo.medgift.models.GiftService;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JobUtil {
    private static final String TAG = JobUtil.class.getSimpleName();
    // This tracks what tasks have been queued
    private static Set<String> cache;

    // Android handler that runs runnable tasks
    private static Handler handler;


    public static void init(Handler _handler) {
        handler = _handler;
        cache = new TreeSet<>();
    }




    //Schedule a single task
    public static void scheduleJob(GiftService giftService, Context context){
        //If it contain an existing tasker id, skip enqueue
        if(cache.contains(giftService.getKey())){
            return;
        }

        // Schedule it only when due time has not passed
        if(giftService.isPending()){

            Runnable task = new Delivery(context, giftService);
            handler.postDelayed(task, giftService.dueIntervalInMillis());
            Log.i(TAG, String.format("%s scheduled for run.", giftService.toString()));
            // Schedule it only when due time minus 3 hours has not passed
            if(giftService.isPendingWatch()){
                Runnable watch = new WatchDelivery(context, giftService);
                handler.postDelayed(watch, giftService.watchIntervalInMillis());
                Log.i(TAG, String.format("%s scheduled for notify.", giftService.toString()));
            }
            // Add this tasker  to cache
            cache.add(giftService.getKey());
        }
    }

    //Enqueue multiple tasks
    public static void scheduleJobs(List<GiftService> giftServices, Context context) {
        // Loop over tasks and schedule them
        for(GiftService g : giftServices){
            scheduleJob(g, context);
        }
    }
}

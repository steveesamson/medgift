package com.apollo.medgift.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ValueEvents<T extends BaseModel> {

    public  ValueEventListener registerListener(DatabaseReference db, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, BaseViewModel<T> viewModel, List<T> modelList, Class<T> modelClass, OnCallBack<T> onComplete){

        viewModel.getModel().observe((LifecycleOwner) context, list -> {
            // Update the selected filters UI.
            modelList.clear();
            modelList.addAll(list);
            onComplete.onComplete(list);
            adapter.notifyDataSetChanged();
        });


        ValueEventListener listener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                List<T> list = new ArrayList<>();
//                GenericTypeIndicator<T> ts = new GenericTypeIndicator<T>() {};
                for (DataSnapshot snapshot : snapshots.getChildren()) {
                    T r = snapshot.getValue(modelClass);
                    if (r != null) {
                        r.setKey(snapshot.getKey());
                        list.add(r);
                    }
                }
                viewModel.setModel(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        // Start listening
        db.addValueEventListener(listener);
        return listener;
    }
}

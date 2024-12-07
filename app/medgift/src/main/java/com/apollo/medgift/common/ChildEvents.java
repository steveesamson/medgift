package com.apollo.medgift.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

public class ChildEvents<T extends BaseModel> implements Closeable {
    private final OnModel<T> onChildAdded;
    private final OnModel<T> onChildUpdated;
    private final Class<T> modelClass;
    private final Query query;
    private ChildEventListener childEventListener;

    public ChildEvents(String storeName, String key, String value, Class<T> modelClass, OnModel<T> onChildAdded, OnModel<T> onChildUpdated){
        this.onChildAdded = onChildAdded;
        this.onChildUpdated = onChildUpdated;
        this.modelClass = modelClass;
        this.query = Firebase.database(storeName).orderByChild(key).equalTo(value);
        this.listen();
    }

    private void listen(){

        this.childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                T r = snapshot.getValue(ChildEvents.this.modelClass);
                if (r != null && ChildEvents.this.onChildAdded != null) {
                    r.setKey(snapshot.getKey());
                    ChildEvents.this.onChildAdded.onComplete(r);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                T r = snapshot.getValue(ChildEvents.this.modelClass);
                if (r != null && ChildEvents.this.onChildUpdated != null) {
                    r.setKey(snapshot.getKey());
                    ChildEvents.this.onChildUpdated.onComplete(r);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        };
        this.query.addChildEventListener(this.childEventListener);

    }

    public void release(){
        this.query.removeEventListener(this.childEventListener);
    }

}

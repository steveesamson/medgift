package com.apollo.medgift.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.medgift.models.BaseModel;
import com.apollo.medgift.views.models.BaseViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility to manage all google Firebase services
 */
public class Firebase {

    // Get a reference to Firebase DatabaseReference
    public static DatabaseReference database(String root){
        return FirebaseDatabase.getInstance().getReference().child(root);
    }

    // Get a reference to Firebase StorageReference
    public static StorageReference store(String name){
        String PICTURE = name.startsWith("medgifts/") ? name:  "medgifts/" + name;
        return FirebaseStorage.getInstance().getReference().child(PICTURE);
    }

    // Get a reference to FirebaseAuth
    public static FirebaseAuth auth(){
        return FirebaseAuth.getInstance();
    }

    // Create and bind listener for db
    public static <T extends BaseModel> ValueEventListener registerListener(DatabaseReference db, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, ViewModel viewModel, List<T> collection){

        BaseViewModel vModel = (BaseViewModel<T>)viewModel;

        vModel.getModel().observe((LifecycleOwner) context, list -> {
            // Update the selected filters UI.
            collection.clear();
            collection.addAll((Collection<? extends T>) list);
            adapter.notifyDataSetChanged();
        });


        ValueEventListener listener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                List<BaseModel> list = new ArrayList<>();
                GenericTypeIndicator<T> ts = new GenericTypeIndicator<T>() {};
                for (DataSnapshot snapshot : snapshots.getChildren()) {
                    T r = snapshot.getValue(ts);
                    if (r != null) {
                        r.setKey(snapshot.getKey());
                        list.add(r);
                    }
                }
                vModel.setModel(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        // Start listening
        db.addValueEventListener(listener);
        return listener;
    }


    // Remove storage resource
    private static void removeStorage(String imageUri) {
        // Get resource name from Uri
        String name = Util.uriToName(imageUri);
        if(name == null) return;

        // Delete from storage
        Firebase.store(name).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

//    // On Room deletion, remove all related
//    // inspections and images
//    public  static void removeInspectionsForRoom(Room room){
//        Firebase.removeStorage(room.getImageUri());
//        Firebase.database(Inspection.STORE).orderByChild("room").equalTo(room.getKey()).getRef().removeValue(new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//            }
//        });
//    }
}


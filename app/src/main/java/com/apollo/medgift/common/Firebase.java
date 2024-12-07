package com.apollo.medgift.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility to manage all google Firebase services
 */
public class Firebase {

    private static final String TAG = Firebase.class.getSimpleName();

    // Get a reference to Firebase DatabaseReference
    public static DatabaseReference database(String root) {
        return FirebaseDatabase.getInstance().getReference().child(root);
    }

    // Get a reference to Firebase StorageReference
    public static StorageReference store(String name) {
        String PICTURE = name.startsWith("medgifts/") ? name : "medgifts/" + name;
        return FirebaseStorage.getInstance().getReference().child(PICTURE);
    }

    // Get a reference to FirebaseAuth
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static <T extends BaseModel> void getModelBy(String storeName, String key, String value, Class<T> modelClass, OnModel<T> onComplete) {

        boolean isKey = key.equals("key");
        if (isKey) {
            Query query = Firebase.database(storeName).child(value);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    query.removeEventListener(this);
                    if (snapshot.exists()) {

                        // Get the value
                        T r = snapshot.getValue(modelClass);
                        onComplete.onComplete(r);
                        // If you know the specific type, you can cast
                        // For example, if it's a User object
                        // User user = dataSnapshot.getValue(User.class);
                    } else {
                        onComplete.onComplete(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    query.removeEventListener(this);
                    System.err.println("Error reading data: " + databaseError.getMessage());
                    onComplete.onComplete(null);
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);

        } else {
             Firebase.getModelsBy(storeName, key, value, modelClass, (list) -> {
                if (!list.isEmpty()) {
                    onComplete.onComplete(list.get(0));
                } else {
                    onComplete.onComplete(null);
                }
            });
        }
    }


    public static <T extends BaseModel> void getModelsBy(String storeName, String key, String value, Class<T> modelClass, OnModel<List<T>> onComplete) {


        Query query = Firebase.database(storeName).orderByChild(key).equalTo(value);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                query.removeEventListener(this);

                List<T> list = new ArrayList<>();
                for (DataSnapshot snapshot : snapshots.getChildren()) {
                    T r = snapshot.getValue(modelClass);
                    if (r != null) {
                        r.setKey(snapshot.getKey());
                        list.add(r);
                    }
                }
                // Get Post object and use the values to update the UI
                onComplete.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                query.removeEventListener(this);
                // Getting Post failed, log a message
                Log.w("getModelsBy", ":onCancelled", databaseError.toException());
                onComplete.onComplete(null);
            }
        };
        query.addValueEventListener(postListener);
    }

    // Get current user
    public static SessionUser currentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return new SessionUser(user);
    }


    // Remove storage resource
    private static void removeStorage(String imageUri) {
        // Get resource name from Uri
        String name = Util.uriToName(imageUri);
        if (name == null) return;

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

    public static void createUser(String email, String password, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onComplete);
    }

    public static void updateProfile(User user, OnCompleteListener<Void> onComplete) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(String.format("%s %s|%s", user.getFirstName(), user.getLastName(), user.getRole()))
                .build();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setKey(firebaseUser.getUid());
        Firebase.save(user, User.STORE, (tsk, key) -> {});
        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(onComplete);
    }

    public static void login(String email, String password, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onComplete);
    }

    public static void save(BaseModel model, String storeName, StoreCompleteListener onComplete) {

        String key = model.getKey();

        DatabaseReference db = Firebase.database(storeName);

        // If this is a fresh model,
        // Get and assign a key
        if (key == null || key.isEmpty()) {
            key = db.push().getKey();
        }
        if (key != null) {
            // Save to Firebase
            String finalKey = key;
            model.setKey(finalKey);
            db.child(key).setValue(model).addOnCompleteListener((task) -> {
                onComplete.onComplete(task, finalKey);
            });
        }
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }


    public static void delete(BaseModel model, String storeName, OnCompleteListener<Void> onComplete) {
        Firebase.database(storeName).child(model.getKey()).removeValue().addOnCompleteListener(onComplete);
    }

    // Extract file extension from Uri
    private static String getFileExt(Uri contentUri, Activity activity) {
        ContentResolver c = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public static void uploadImageToFirebase(Uri contentUri, Activity activity, OnSuccessListener<Uri> onUpload, OnFailureListener onFail) {
        // Extract the Uri from room's transient field
        // Get a timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Concoct a name
        String name = "JPEG_" + timeStamp + "." + getFileExt(contentUri, activity);

        // Get a storage ref
        final StorageReference image = Firebase.store(name);
        // Store image
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(onUpload);
            }
        }).addOnFailureListener(onFail);

    }

}


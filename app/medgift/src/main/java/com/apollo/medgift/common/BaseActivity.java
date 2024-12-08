package com.apollo.medgift.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.jobs.JobUtil;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.views.LogInActivity;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.Node;

import java.util.Set;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String CAPABILITY_MOBILE_APP = "medgift_remote_intent_capability";
    private static final String DATA_PATH = "/medgift_remote_intent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Setup Tasker with android handler
        JobUtil.init(new Handler(Looper.getMainLooper()));
        NotificationUtil.createNotificationChannel(this, getString(R.string.channel_name), getString(R.string.channel_description));

    }

    protected void applyWindowInsetsListenerTo(ComponentActivity activity, ViewGroup view) {
        EdgeToEdge.enable(activity);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void onNotified(boolean isActive){
    }

    public void sendRemoteIntent(Message message) {
        // Find connected nodes with the specific capability
        Wearable.getCapabilityClient(this)
                .getCapability(CAPABILITY_MOBILE_APP, CapabilityClient.FILTER_REACHABLE)
                .addOnSuccessListener(capabilityInfo -> {
                    // Get the connected nodes
                    Set<Node> connectedNodes = capabilityInfo.getNodes();

                    if (!connectedNodes.isEmpty()) {
                        // Select the first connected node (or implement logic to choose specific node)
                        Node targetNode = connectedNodes.iterator().next();

                        // Check for nearby node
                        if(targetNode.isNearby()){
                            // Prepare the remote intent data
                            Bundle intentData = new Bundle();
                            intentData.putString("type", message.getNotificationType().name());
                            intentData.putString("key", message.getPayloadKey());

                            // Send the intent via MessageClient
                            sendIntentToNode(targetNode, intentData);
                        }

                    } else {
                        Log.d(TAG, "No connected nodes found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get capability", e);
                });
    }

    private void sendIntentToNode(Node node, Bundle intentData) {
        // Serialize the intent data
        byte[] intentBytes = bundleToByteArray(intentData);

        // Send message to mobile app
        Wearable.getMessageClient(this)
                .sendMessage(node.getId(), DATA_PATH, intentBytes)
                .addOnSuccessListener(result -> {
                    Log.d(TAG, "Intent sent successfully to " + node.getDisplayName());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to send intent", e);
                });
    }

    // Utility method to convert Bundle to byte array
    private byte[] bundleToByteArray(Bundle bundle) {
        // Serialize bundle to byte array
        // This is a simplified example - in production, use more robust serialization
        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objectOutputStream = new java.io.ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(bundle);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (java.io.IOException e) {
            Log.e(TAG, "Error serializing bundle", e);
            return new byte[0];
        }
    }

    // Handle session
    @Override
    protected void onStart() {
        super.onStart();
        Notifier.getInstance().beginWatches(this);
    }


    // LogOut on click
    protected void logout() {
        Firebase.logout();
        Notifier.getInstance().release();
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

package com.apollo.medgift.common;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

public interface StoreCompleteListener {
    void onComplete(@NonNull Task<Void> task, String key);
}

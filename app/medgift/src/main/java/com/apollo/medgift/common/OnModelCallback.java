package com.apollo.medgift.common;
import java.util.List;

public interface OnModelCallback<T extends BaseModel> {
    void onComplete(List<T> list);
}
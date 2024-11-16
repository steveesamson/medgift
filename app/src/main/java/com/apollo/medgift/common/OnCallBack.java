package com.apollo.medgift.common;
import java.util.List;

public interface OnCallBack<T extends BaseModel> {
    void onComplete(List<T> list);
}
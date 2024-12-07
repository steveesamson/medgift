package com.apollo.medgift.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BaseViewModel<T> extends ViewModel {

    public  void setModel(List<T> rms) {
    }

    public  LiveData<List<T>> getModel() {
        return null;
    }

    public void addModel(T model){

    }
}

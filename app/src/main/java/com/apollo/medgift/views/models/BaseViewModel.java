package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.apollo.medgift.models.BaseModel;

import java.util.List;

public class BaseViewModel<T extends  BaseModel> extends ViewModel {

    public  void setModel(List<T> rms) {
    }

    public  LiveData<List<T>> getModel() {
        return null;
    }
}

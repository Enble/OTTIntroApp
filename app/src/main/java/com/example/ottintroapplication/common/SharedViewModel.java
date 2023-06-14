package com.example.ottintroapplication.common;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Bundle> result = new MutableLiveData<>();

    public void setData(Bundle bundle) {
        result.setValue(bundle);
    }

    public MutableLiveData<Bundle> getData() {
        return result;
    }
}

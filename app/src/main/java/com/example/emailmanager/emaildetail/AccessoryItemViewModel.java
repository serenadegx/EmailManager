package com.example.emailmanager.emaildetail;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

public class AccessoryItemViewModel {
    public final ObservableField<String> fileName = new ObservableField<>();
    public final ObservableField<String> size = new ObservableField<>();
    public final ObservableBoolean isDownload = new ObservableBoolean();


    public void downloadOrOpen(View view){}

}

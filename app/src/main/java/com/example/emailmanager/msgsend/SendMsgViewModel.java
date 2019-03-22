package com.example.emailmanager.msgsend;

import android.databinding.ObservableField;
import android.view.View;

public class SendMsgViewModel {
    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> secret = new ObservableField<>();
    public final ObservableField<String> send = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();

    public void addReceiver(View view){}
    public void addCopy(View view){}
    public void addSecret(View view){}
}

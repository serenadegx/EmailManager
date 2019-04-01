package com.example.emailmanager.msgsend;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;

import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailRepository;

public class SendMsgViewModel {
    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> secret = new ObservableField<>();
    public final ObservableField<String> send = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    private final Context mContext;
    private final EmailRepository mEmailRepository;

    public SendMsgViewModel(Context mContext, EmailRepository mEmailRepository) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
    }



    public void addReceiver(View view){}
    public void addCopy(View view){}
    public void addSecret(View view){}

    public void sendMsg() {
        mEmailRepository.sendMsg(new EmailDetail());
    }
}

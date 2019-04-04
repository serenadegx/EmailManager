package com.example.emailmanager.msgsend;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;

import java.util.ArrayList;
import java.util.List;

public class SendMsgViewModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;

    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> secret = new ObservableField<>();
    public final ObservableField<String> send = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    private final Context mContext;
    private final EmailRepository mEmailRepository;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String hint;
            if (msg.what == SUCCESS) {
                hint = "发送成功";
                ((Activity) mContext).finish();
            } else {
                hint = (String) msg.obj;
            }
            Toast.makeText(mContext, hint, Toast.LENGTH_SHORT).show();
        }
    };

    public SendMsgViewModel(Context mContext, EmailRepository mEmailRepository) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
        receiver.set("guoxinrui_qd@caxins.com");
        send.set("1099805713@qq.com");
    }


    public void addReceiver(View view) {
    }

    public void addCopy(View view) {
    }

    public void addSecret(View view) {
    }

    public void sendMsg() {

        final EmailDetail data = new EmailDetail();
        data.setFrom(TextUtils.isEmpty(send.get()) ? null : send.get());
        data.setTo(TextUtils.isEmpty(receiver.get()) ? null : receiver.get());
        data.setCc(TextUtils.isEmpty(copy.get()) ? null : copy.get());
        data.setBcc(TextUtils.isEmpty(secret.get()) ? null : secret.get());
        data.setSubject(subject.get());
        data.setContent(content.get());
        List<AccessoryDetail> attach = new ArrayList<>();
        attach.add(new AccessoryDetail(Environment.getExternalStorageDirectory() + "/example.jpg"));
        data.setAccessoryList(attach);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.sendMsg(data, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = ERROR;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();

    }
}

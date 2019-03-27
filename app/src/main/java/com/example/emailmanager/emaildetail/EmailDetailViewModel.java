package com.example.emailmanager.emaildetail;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;

import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

import java.io.File;
import java.io.FileOutputStream;

public class EmailDetailViewModel {
    public final ObservableField<String> receivers = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> accessory = new ObservableField<>();
    public final ObservableBoolean isAttach = new ObservableBoolean();

    private Context mContext;
    private EmailRepository mEmailRepository;

    private AccessoryListAdapter adapter;
    private WebView webview;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                EmailDetail emailDetail = (EmailDetail) msg.obj;
                receivers.set(emailDetail.getTo());
                subject.set(emailDetail.getSubject());
                date.set(emailDetail.getDate());
                isAttach.set(emailDetail.getAccessoryList().size() > 0);
                accessory.set(emailDetail.getAccessoryList().size() + "个附件");
                adapter.refreshData(emailDetail.getAccessoryList());
//                webview.loadData(emailDetail.getContent(), "text/html", "utf-8");
                webview.loadDataWithBaseURL(null, emailDetail.getContent(), "text/html", "utf-8", null);

            }
        }
    };

    public EmailDetailViewModel(Context mContext, EmailRepository mEmailRepository) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
    }

    public void loadDataById(final int msgnum) {
        new Thread() {
            @Override
            public void run() {
                EmailDetail emailDetail = mEmailRepository.loadRemoteDataById(msgnum);
                Message message = Message.obtain();
                message.what = 1000;
                message.obj = emailDetail;
                mHandler.sendMessage(message);
//                saveHtml(emailDetail);
            }
        }.start();


    }

    public void setWebView(WebView webView) {
        this.webview = webView;
    }


    public void setAdapter(AccessoryListAdapter listAdapter) {
        this.adapter = listAdapter;
    }

    private void saveHtml(EmailDetail emailDetail) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), emailDetail.getSubject() + emailDetail.getId() + ".html"));
            fos.write(emailDetail.getContent().getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message message = Message.obtain();
        message.what = 1001;
        message.obj = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), emailDetail.getSubject() + emailDetail.getId() + ".html").getAbsolutePath();
        mHandler.sendMessage(message);
    }

    public void reply(View v) {
    }

    public void replyAll(View v) {
    }

    public void forward(View v) {
    }

    public void delete(View v) {
    }
}

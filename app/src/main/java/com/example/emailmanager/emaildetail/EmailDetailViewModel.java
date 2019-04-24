package com.example.emailmanager.emaildetail;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;
import com.example.emailmanager.msgsend.SendMsgActivity;
import com.example.xrwebviewlibrary.XRWebView;

import java.io.File;
import java.io.FileOutputStream;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public class EmailDetailViewModel {
    private static final int DELETE_SUCCESS = 1;
    private static final int DELETE_ERROR = 2;
    private static final int SUCCESS = 3;
    private static final int READ_SUCCESS = 4;
    private static final int READ_ERROR = 5;
    public final ObservableField<String> receivers = new ObservableField<>();
    public final ObservableField<String> copy = new ObservableField<>();
    public final ObservableField<String> subject = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> accessory = new ObservableField<>();
    public final ObservableBoolean isAttach = new ObservableBoolean();
    private final int msgNum;

    private Context mContext;
    private EmailRepository mEmailRepository;

    private AccessoryListAdapter adapter;
    private WebView webview;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS) {
                EmailDetail emailDetail = (EmailDetail) msg.obj;
                receivers.set(emailDetail.getTo());
                subject.set(emailDetail.getSubject());
                date.set(emailDetail.getDate());
                isAttach.set(emailDetail.getAccessoryList().size() > 0);
                accessory.set(emailDetail.getAccessoryList().size() + "个附件");
                adapter.refreshData(emailDetail.getAccessoryList());
                XRWebView.with(webview).simple().build().loadHtml(emailDetail.getContent(), "text/html", "utf-8");
            } else if (msg.what == DELETE_SUCCESS) {
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                ((Activity) mContext).finish();
            } else if (msg.what == DELETE_ERROR) {
                Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public EmailDetailViewModel(Context mContext, EmailRepository mEmailRepository, int msgnum) {
        this.mContext = mContext;
        this.mEmailRepository = mEmailRepository;
        this.msgNum = msgnum;
    }

    public void loadDataById() {
        new Thread() {
            @Override
            public void run() {
                EmailDetail emailDetail = mEmailRepository.loadRemoteDataById(EMApplication.getAccount(), msgNum);
                Message message = Message.obtain();
                message.what = SUCCESS;
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
        SendMsgActivity.start2SendMsgActivity(mContext, msgNum, SendMsgActivity.REPLY);
    }

    public void replyAll(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, msgNum, SendMsgActivity.REPLY_ALL);
    }

    public void forward(View v) {
        SendMsgActivity.start2SendMsgActivity(mContext, msgNum, SendMsgActivity.FORWARD);
    }

    public void delete(View v) {
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.deleteById(EMApplication.getAccount(), msgNum, new EmailDataSource.GetResultCallBack() {
                    @Override
                    public void onSuccess() {
                        mHandler.sendEmptyMessage(DELETE_SUCCESS);
                    }

                    @Override
                    public void onError(String ex) {
                        Message message = Message.obtain();
                        message.what = SUCCESS;
                        message.obj = ex;
                        mHandler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

}

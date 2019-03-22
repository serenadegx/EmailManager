package com.example.emailmanager.emails;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.emails.adapter.EmailListAdapter;

import java.util.ArrayList;
import java.util.List;

public class EmailsViewModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private final EmailRepository mEmailRepository;
    private final Context mContext;
    private List<EmailDetail> mData = new ArrayList<>();
    private EmailListAdapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                if (adapter != null)
                    adapter.refreshData((List<EmailDetail>) msg.obj);
            } else {
                Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public EmailsViewModel(EmailRepository mEmailRepository, Context context) {
        this.mEmailRepository = mEmailRepository;
        this.mContext = context;
    }


    public void loadEmails() {
//        for (int i = 0; i < 10; i++) {
//            EmailDetail emailDetail = new EmailDetail(0, "主题", "19:30", "1099805713@163.com");
//            mData.add(emailDetail);
//        }
//        adapter.refreshData(mData);
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.loadData(new EmailDataSource.GetEmailsCallBack() {
                    @Override
                    public void onEmailsLoaded(List<EmailDetail> emails) {

                        Message message = Message.obtain();
                        message.what = SUCCESS;
                        message.obj = emails;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        mHandler.sendEmptyMessage(ERROR);
                    }
                });
            }
        }.start();

    }

    public void setAdapter(EmailListAdapter listAdapter) {
        this.adapter = listAdapter;
    }
}

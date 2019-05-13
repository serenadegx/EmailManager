package com.example.emailmanager.emails;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailDataSource;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.emails.adapter.EmailListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class EmailsViewModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private final EmailDataRepository mEmailRepository;
    private final Context mContext;
    private final SwipeRefreshLayout srl;
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
            if (srl.isRefreshing())
                srl.setRefreshing(false);
        }
    };

    public EmailsViewModel(EmailDataRepository mEmailRepository, Context context, SwipeRefreshLayout srl) {
        this.mEmailRepository = mEmailRepository;
        this.mContext = context;
        this.srl = srl;
    }


    public void setAdapter(EmailListAdapter listAdapter) {
        this.adapter = listAdapter;
    }

    public void refresh() {
        mEmailRepository.refreshEmails();
        loadEmails();
    }

    public void loadEmails() {
        showLoading();
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.getEmails(EMApplication.getAccount(), new EmailDataSource.GetEmailsCallBack() {
                    @Override
                    public void onEmailsLoaded(List<EmailDetail> emails) {
                        List<Contacts> contacts = new ArrayList<>();
                        for (EmailDetail detail : emails) {
                            Contacts contact = new Contacts(detail.getPersonal(), detail.getFrom());
                            if (!contacts.contains(contact))
                                contacts.add(contact);
                        }
                        EMApplication.setContacts(contacts);
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

    public void loadEmailsFromSent() {
        showLoading();
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.loadSentMessage(EMApplication.getAccount(), new EmailDataSource.GetEmailsCallBack() {
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

    public void loadEmailsFromDraft() {
        showLoading();
        new Thread() {
            @Override
            public void run() {
                mEmailRepository.loadDrafts(EMApplication.getAccount(), new EmailDataSource.GetEmailsCallBack() {
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

    public void loadEmailsFromDelete() {

    }

    private void showLoading() {
        if (!srl.isRefreshing()) {
            srl.setRefreshing(true);
        }
    }
}

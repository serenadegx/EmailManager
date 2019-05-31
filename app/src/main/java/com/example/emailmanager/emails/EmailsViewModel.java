package com.example.emailmanager.emails;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.emailmanager.BR;
import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailDataSource;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;

import static com.example.emailmanager.emails.InboxFragment.DELETE;
import static com.example.emailmanager.emails.InboxFragment.DRAFTS;
import static com.example.emailmanager.emails.InboxFragment.INBOX;
import static com.example.emailmanager.emails.InboxFragment.SENT_MESSAGES;

public class EmailsViewModel extends BaseObservable {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;

    public final ObservableList<EmailDetail> mItems = new ObservableArrayList<>();
    public final ObservableBoolean isLoading = new ObservableBoolean();
    private final EmailDataRepository mEmailRepository;
    private final Context mContext;
    private int loadTYpe;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                mItems.clear();
                mItems.addAll((List<EmailDetail>) msg.obj);
                notifyPropertyChanged(BR.empty);
            } else {
                Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
            }
            isLoading.set(false);
        }
    };

    public EmailsViewModel(EmailDataRepository mEmailRepository, Context context) {
        this.mEmailRepository = mEmailRepository;
        this.mContext = context;
    }

    public void refresh() {
        isLoading.set(true);
        mEmailRepository.refreshEmails(false);
        switch (loadTYpe) {
            case INBOX:
                loadEmailFromInbox();
                break;
            case SENT_MESSAGES:
                loadEmailsFromSent();
                break;
            case DRAFTS:
                loadEmailsFromDraft();
                break;
            case DELETE:
                loadEmailsFromDelete();
                break;
            default:
                break;
        }
    }

    @Bindable
    public boolean isEmpty() {
        return mItems.isEmpty();
    }


    public void loadEmails() {
        isLoading.set(true);
        switch (loadTYpe) {
            case INBOX:
                loadEmailFromInbox();
                break;
            case SENT_MESSAGES:
                loadEmailsFromSent();
                break;
            case DRAFTS:
                loadEmailsFromDraft();
                break;
            case DELETE:
                loadEmailsFromDelete();
                break;
            default:
                break;
        }


    }

    public void loadEmailFromInbox() {
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
        isLoading.set(true);
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

    public void setLoadType(int type) {
        this.loadTYpe = type;
    }

    public void setRefresh(boolean isRefresh) {
        mEmailRepository.refreshEmails(isRefresh);
    }
}

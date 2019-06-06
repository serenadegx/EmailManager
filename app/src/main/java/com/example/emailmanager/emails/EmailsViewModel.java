package com.example.emailmanager.emails;

import android.content.Context;
import android.database.Observable;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.example.emailmanager.BR;
import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.Contacts;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataRepository;
import com.example.emailmanager.data.source.EmailDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.example.emailmanager.emails.InboxFragment.DELETE;
import static com.example.emailmanager.emails.InboxFragment.DRAFTS;
import static com.example.emailmanager.emails.InboxFragment.INBOX;
import static com.example.emailmanager.emails.InboxFragment.SENT_MESSAGES;

public class EmailsViewModel extends BaseObservable {

    public final ObservableList<EmailDetail> mItems = new ObservableArrayList<>();
    public final ObservableField<String> snackBarText = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean();
    private final EmailDataRepository mEmailRepository;
    private final Context mContext;
    private int loadTYpe;

    public EmailsViewModel(EmailDataRepository mEmailRepository, Context context) {
        this.mEmailRepository = mEmailRepository;
        this.mContext = context;
    }

    @Bindable
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Nullable
    public String getSnackBarText() {
        return snackBarText.get();
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
                        isLoading.set(false);
                        mItems.clear();
                        mItems.addAll(emails);
                        notifyPropertyChanged(BR.empty);
                        snackBarText.set("加载成功");
                    }

                    @Override
                    public void onDataNotAvailable() {
                        isLoading.set(false);
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
                        isLoading.set(false);
                        mItems.clear();
                        mItems.addAll(emails);
                        notifyPropertyChanged(BR.empty);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        isLoading.set(false);
                        Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
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
                        isLoading.set(false);
                        mItems.clear();
                        mItems.addAll(emails);
                        notifyPropertyChanged(BR.empty);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        isLoading.set(false);
                        Toast.makeText(mContext, "获取失败", Toast.LENGTH_SHORT).show();
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

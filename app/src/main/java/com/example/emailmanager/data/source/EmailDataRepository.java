package com.example.emailmanager.data.source;

import android.util.Log;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDao;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.EmailDetailDao;
import com.example.emailmanager.data.source.local.EmailLocalDataSource;
import com.example.emailmanager.data.source.remote.EmailRemoteDataSource;

import java.util.List;

public class EmailDataRepository implements EmailDataSource {
    private static EmailDataRepository INSTANCE;

    private boolean isCache = true;

    private EmailDataSource mEmailLocalDataSource;

    private EmailDataSource mEmailRemoteDataSource;

    private EmailDataRepository(EmailDataSource mEmailLocalDataSource, EmailDataSource mEmailRemoteDataSource) {
        this.mEmailLocalDataSource = mEmailLocalDataSource;
        this.mEmailRemoteDataSource = mEmailRemoteDataSource;
    }

    public static EmailDataRepository provideRepository() {
        if (INSTANCE == null) {
            EmailDetailDao emailDao = EMApplication.getDaoSession().getEmailDetailDao();
            INSTANCE = new EmailDataRepository(EmailLocalDataSource.getInstance(emailDao),
                    EmailRemoteDataSource.getInstance());
        }
        return INSTANCE;
    }

    public void refreshEmails(boolean cache) {
        isCache = cache;
    }

    @Override
    public void getEmails(final AccountDetail detail, final GetEmailsCallBack callBack) {
        if (!isCache) {
            getEmailsFromRemoteDataSource(detail, callBack);
        } else {
            mEmailLocalDataSource.getEmails(detail, new GetEmailsCallBack() {
                @Override
                public void onEmailsLoaded(List<EmailDetail> emails) {
                    callBack.onEmailsLoaded(emails);
                }

                @Override
                public void onDataNotAvailable() {
                    getEmailsFromRemoteDataSource(detail, callBack);
                }
            });
        }
    }

    @Override
    public void getEmail(final AccountDetail detail, long id, final GetEmailCallBack callBack) {
        mEmailRemoteDataSource.getEmail(detail, id, new GetEmailCallBack() {
            @Override
            public void onEmailLoaded(EmailDetail email) {
                signRead(detail, email);
                callBack.onEmailLoaded(email);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void sendEmail(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {
        mEmailRemoteDataSource.sendEmail(detail, email, callBack);
    }

    @Override
    public void reply(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {
        mEmailRemoteDataSource.reply(detail, email, callBack);
    }

    @Override
    public void forward(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {
        mEmailRemoteDataSource.forward(detail, email, callBack);
    }

    @Override
    public void save2Drafts(AccountDetail detail, EmailDetail data, GetResultCallBack callBack) {
        mEmailRemoteDataSource.save2Drafts(detail, data, callBack);
    }


    @Override
    public void deleteEmail(final AccountDetail detail, final long id, final GetResultCallBack callBack) {
        mEmailRemoteDataSource.deleteEmail(detail, id, new GetResultCallBack() {
            @Override
            public void onSuccess() {
                mEmailLocalDataSource.deleteEmail(detail, id, callBack);
            }

            @Override
            public void onError(String ex) {
                callBack.onError(ex);
            }
        });

    }

    @Override
    public void signRead(AccountDetail detail, EmailDetail email) {
        mEmailLocalDataSource.signRead(detail, email);
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void saveAll(List<EmailDetail> emails) {

    }

    public void loadSentMessage(AccountDetail account, GetEmailsCallBack callBack) {
        ((EmailRemoteDataSource) mEmailRemoteDataSource).loadSentMessage(account, callBack);
    }

    public void loadDrafts(AccountDetail account, GetEmailsCallBack callBack) {
        ((EmailRemoteDataSource) mEmailRemoteDataSource).loadDrafts(account, callBack);
    }

    private void getEmailsFromRemoteDataSource(AccountDetail detail, final GetEmailsCallBack callBack) {
        mEmailRemoteDataSource.getEmails(detail, new GetEmailsCallBack() {
            @Override
            public void onEmailsLoaded(List<EmailDetail> emails) {
                Log.i("mango", "etEmailsFromRemoteDataSource");
                refreshLocalDataSource(emails);
                callBack.onEmailsLoaded(emails);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<EmailDetail> emails) {
        mEmailLocalDataSource.deleteAll();
        mEmailLocalDataSource.saveAll(emails);
        isCache = true;
    }
}

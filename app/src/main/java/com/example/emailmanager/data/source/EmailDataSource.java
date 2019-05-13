package com.example.emailmanager.data.source;

import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDetail;

import java.util.List;

public interface EmailDataSource {
    void deleteAll();

    void saveAll(List<EmailDetail> emails);

    interface GetEmailsCallBack {

        void onEmailsLoaded(List<EmailDetail> emails);

        void onDataNotAvailable();
    }

    interface GetEmailCallBack {

        void onEmailLoaded(EmailDetail email);

        void onDataNotAvailable();
    }

    interface GetResultCallBack {
        void onSuccess();

        void onError(String ex);
    }

    void getEmails(AccountDetail detail, GetEmailsCallBack callBack);

    void getEmail(AccountDetail detail, long id, GetEmailCallBack callBack);

    void sendEmail(AccountDetail detail, EmailDetail email, EmailDataSource.GetResultCallBack callBack);

    void reply(AccountDetail detail, EmailDetail email, EmailDataSource.GetResultCallBack callBack);

    void signRead(AccountDetail detail, EmailDetail email);

    void forward(AccountDetail detail, EmailDetail email, EmailDataSource.GetResultCallBack callBack);

    void save2Drafts(AccountDetail detail, EmailDetail data, EmailDataSource.GetResultCallBack callBack);

    void deleteEmail(AccountDetail detail, long id, GetResultCallBack callBack);
}

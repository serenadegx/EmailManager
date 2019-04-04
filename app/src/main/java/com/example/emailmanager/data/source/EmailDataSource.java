package com.example.emailmanager.data.source;

import com.example.emailmanager.data.EmailDetail;

import java.util.List;

public interface EmailDataSource {
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

    void getEmails(GetEmailsCallBack callBack);

    void getEmail(String id, GetEmailCallBack callBack);

    void sendEmail(EmailDetail email);

    void deleteEmail(String id);

    void reply(EmailDetail emailDetail);

    void signRead(EmailDetail emailDetail);

    void forward(EmailDetail emailDetail);
}

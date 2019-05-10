package com.example.emailmanager.data.source;

import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDetail;

public class EmailDataRepository implements EmailDataSource {

    private boolean isCache;
    @Override
    public void getEmails(AccountDetail detail, GetEmailsCallBack callBack) {

    }

    @Override
    public void getEmail(AccountDetail detail, long id, GetEmailCallBack callBack) {

    }

    @Override
    public void sendEmail(EmailDetail email) {

    }

    @Override
    public void deleteEmail(String id) {

    }

    @Override
    public void reply(EmailDetail emailDetail) {

    }

    @Override
    public void signRead(EmailDetail emailDetail) {

    }

    @Override
    public void forward(EmailDetail emailDetail) {

    }

}

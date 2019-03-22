package com.example.emailmanager.data.source.remote;

import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.source.EmailDataSource;

public class EmailRemoteDataSource implements EmailDataSource {
    @Override
    public void getEmails(GetEmailsCallBack callBack) {

    }

    @Override
    public void getEmail(String id, GetEmailCallBack callBack) {

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

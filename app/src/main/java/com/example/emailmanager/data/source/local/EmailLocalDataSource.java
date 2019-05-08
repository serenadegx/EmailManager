package com.example.emailmanager.data.source.local;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.EmailDetailDao;
import com.example.emailmanager.data.source.EmailDataSource;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class EmailLocalDataSource implements EmailDataSource {
    @Override
    public void getEmails(GetEmailsCallBack callBack) {
        List<EmailDetail> local = EMApplication.getDaoSession().getEmailDetailDao().loadAll();
        if (local != null && local.size() < 1) {
            callBack.onEmailsLoaded(local);
        } else {
            callBack.onDataNotAvailable();
        }
    }

    @Override
    public void getEmail(String id, GetEmailCallBack callBack) {
        QueryBuilder<EmailDetail> qb = EMApplication.getDaoSession().getEmailDetailDao().queryBuilder();
        List<EmailDetail> details = qb.where(EmailDetailDao.Properties.Id.eq(id)).list();
        if (details != null && details.size() == 1) {
            callBack.onEmailLoaded(details.get(0));
        } else {
            callBack.onDataNotAvailable();
        }
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

    public void insert(List<EmailDetail> emails) {
        List<EmailDetail> local = EMApplication.getDaoSession().getEmailDetailDao().loadAll();
        if (local != null && local.size() > 0) {
            for (EmailDetail emailDetail : emails) {
                if (!local.contains(emailDetail))
                    EMApplication.getDaoSession().getEmailDetailDao().insert(emailDetail);
            }
        } else {
            EMApplication.getDaoSession().getEmailDetailDao().insertInTx(emails);
        }
    }
}

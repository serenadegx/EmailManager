package com.example.emailmanager.data.source.local;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.EmailDetailDao;
import com.example.emailmanager.data.source.EmailDataSource;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

public class EmailLocalDataSource implements EmailDataSource {
    public static EmailLocalDataSource INSTANCE;

    private EmailDetailDao mEmailDetailDao;

    private EmailLocalDataSource(EmailDetailDao emailDetailDao) {
        this.mEmailDetailDao = emailDetailDao;
    }

    public static EmailLocalDataSource getInstance(EmailDetailDao emailDetailDao) {
        if (INSTANCE == null) {
            synchronized (EmailLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EmailLocalDataSource(emailDetailDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getEmails(AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> local = EMApplication.getDaoSession().getEmailDetailDao()
                .queryBuilder()
//                .where(EmailDetailDao.Properties.From.eq(detail.getAccount()))
                .list();
        if (local != null && local.size() > 0) {
            Collections.reverse(local);
            callBack.onEmailsLoaded(local);
        } else {
            callBack.onDataNotAvailable();
        }
    }

    @Override
    public void getEmail(AccountDetail detail, long id, GetEmailCallBack callBack) {
        QueryBuilder<EmailDetail> qb = EMApplication.getDaoSession().getEmailDetailDao().queryBuilder();
        qb.where(EmailDetailDao.Properties.Id.eq(id));
//        qb.and(EmailDetailDao.Properties.From.eq(detail.getAccount()), EmailDetailDao.Properties.Id.eq(id));
        List<EmailDetail> details = qb.list();
        if (details != null && details.size() == 1) {
            callBack.onEmailLoaded(details.get(0));
        } else {
            callBack.onDataNotAvailable();
        }
    }

    @Override
    public void deleteEmail(AccountDetail detail, long id, GetResultCallBack callBack) {
        EMApplication.getDaoSession().getEmailDetailDao().deleteByKey(id);
        callBack.onSuccess();
    }

    @Override
    public void signRead(AccountDetail detail, EmailDetail email) {
        QueryBuilder<EmailDetail> qb = EMApplication.getDaoSession().getEmailDetailDao().queryBuilder();
        List<EmailDetail> list = qb.where(EmailDetailDao.Properties.Id.eq(email.getId())).list();
        EmailDetail emailDetail = list.get(0);
        emailDetail.setRead(true);
        EMApplication.getDaoSession().getEmailDetailDao().update(emailDetail);
    }

    @Override
    public void sendEmail(AccountDetail detail, EmailDetail email, EmailDataSource.GetResultCallBack callBack) {

    }

    @Override
    public void reply(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {

    }

    @Override
    public void forward(AccountDetail detail, EmailDetail email, GetResultCallBack callBack) {

    }

    @Override
    public void save2Drafts(AccountDetail detail, EmailDetail data, GetResultCallBack callBack) {

    }


    @Override
    public void deleteAll() {
        EMApplication.getDaoSession().getAccessoryDetailDao().deleteAll();
        EMApplication.getDaoSession().getEmailDetailDao().deleteAll();
    }

    @Override
    public void saveAll(List<EmailDetail> emails) {
        EMApplication.getDaoSession().getEmailDetailDao().insertInTx(emails);
        for (EmailDetail emailDetail : emails) {
            EMApplication.getDaoSession().getAccessoryDetailDao().insertInTx(emailDetail.getAccessoryList());
        }
    }

    public void insert(List<EmailDetail> emails) {
        List<EmailDetail> localEmailDetail = EMApplication.getDaoSession().getEmailDetailDao().loadAll();
        List<AccessoryDetail> accessoryDetails = EMApplication.getDaoSession().getAccessoryDetailDao().loadAll();
        if (localEmailDetail != null && localEmailDetail.size() > 0) {
            for (EmailDetail emailDetail : emails) {
                if (!localEmailDetail.contains(emailDetail))
                    EMApplication.getDaoSession().getEmailDetailDao().insert(emailDetail);
                if (accessoryDetails != null && accessoryDetails.size() > 0) {
                    for (AccessoryDetail accessoryDetail : emailDetail.getAccessoryList()) {
                        if (!accessoryDetails.contains(accessoryDetail))
                            EMApplication.getDaoSession().getAccessoryDetailDao().insert(accessoryDetail);
                    }
                } else {
                    EMApplication.getDaoSession().getAccessoryDetailDao().insertInTx(emailDetail.getAccessoryList());
                }
            }
        } else {
            EMApplication.getDaoSession().getEmailDetailDao().insertInTx(emails);
        }
    }
}

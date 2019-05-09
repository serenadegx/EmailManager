package com.example.emailmanager.data.source.local;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.data.AccessoryDetail;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.EmailDetail;
import com.example.emailmanager.data.EmailDetailDao;
import com.example.emailmanager.data.source.EmailDataSource;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class EmailLocalDataSource implements EmailDataSource {
    @Override
    public void getEmails(AccountDetail detail, GetEmailsCallBack callBack) {
        List<EmailDetail> local = EMApplication.getDaoSession().getEmailDetailDao()
                .queryBuilder()
                .where(EmailDetailDao.Properties.From.eq(detail.getAccount()))
                .list();
        if (local != null && local.size() < 1) {
            callBack.onEmailsLoaded(local);
        } else {
            callBack.onDataNotAvailable();
        }
    }

    @Override
    public void getEmail(AccountDetail detail, long id, GetEmailCallBack callBack) {
        QueryBuilder<EmailDetail> qb = EMApplication.getDaoSession().getEmailDetailDao().queryBuilder();
        qb.and(EmailDetailDao.Properties.From.eq(detail.getAccount()),EmailDetailDao.Properties.Id.eq(id));
        List<EmailDetail> details = qb.list();
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

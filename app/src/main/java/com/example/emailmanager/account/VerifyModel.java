package com.example.emailmanager.account;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.emailmanager.EMApplication;
import com.example.emailmanager.MainActivity;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.AccountDetailDao;
import com.example.emailmanager.data.Email;
import com.example.emailmanager.data.EmailDao;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

public class VerifyModel {
    public final ObservableField<String> account = new ObservableField<>();
    public final ObservableField<String> pwd = new ObservableField<>();
    public final ObservableField<String> snackBarText = new ObservableField<>();
    private final long category;
    private VerityNavigator mNavigator;

    public VerifyModel(long category) {
        this.category = category;
        initData();
    }

    @Nullable
    public String getSnackBarText() {
        return snackBarText.get();
    }

    public void addAccount(View v) {
        List<AccountDetail> accounts = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.Account.eq(account.get())).list();
        List<Email> emails = EMApplication.getDaoSession().getEmailDao().queryBuilder()
                .where(EmailDao.Properties.CategoryId.eq(category))
                .list();
        if (!(accounts != null && accounts.size() > 0)) {
            if (emails != null && emails.size() == 1) {
                final Email email = emails.get(0);
                new Thread() {
                    @Override
                    public void run() {
                        Properties props = System.getProperties();
                        props.put(email.getReceiveHostKey(), email.getReceiveHostValue());
                        props.put(email.getReceivePortKey(), email.getReceivePortValue());
                        props.put(email.getReceiveEncryptKey(), email.getReceiveEncryptValue());
                        Session session = Session.getInstance(props, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        account.get(), pwd.get());
                            }
                        });
                        session.setDebug(true);
                        Store store = null;
                        try {
                            store = session.getStore("imap");
                            store.connect();
                            Folder[] folders = store.getDefaultFolder().list();
                            for (Folder folder : folders) {
                                Log.i("Mango", "folderName" + folder.getName());
                            }
                            //储存账号
                            saveAccount();
                            snackBarText.set("登录成功");
                            SystemClock.sleep(1000);
                            mNavigator.onAccountVerify();
                        } catch (NoSuchProviderException e) {
                            snackBarText.set("验证失败");
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            snackBarText.set("验证失败");
                            e.printStackTrace();
                        } finally {
                            try {
                                store.close();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            } else {
                snackBarText.set("账号重复");
            }
        }

    }

    public void onActivityCreated(VerityNavigator navigator) {
        this.mNavigator = navigator;
    }

    private void initData() {
        account.set("guoxinrui@fantaike.ai");
        pwd.set("1993Gxr");
    }

    private void saveAccount() {
        //清除当前账号状态
        QueryBuilder<AccountDetail> queryBuilder = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.IsCur.eq("true"));
        List<AccountDetail> list = queryBuilder.list();
        if (list != null && list.size() > 0) {
            for (AccountDetail accountDetail : list) {
                accountDetail.setCur(false);
            }
            EMApplication.getDaoSession().getAccountDetailDao().updateInTx(list);
        }
        //存储账号并设置为当前账号
        AccountDetail data = new AccountDetail();
        data.setAccount(account.get());
        data.setPwd(pwd.get());
        data.setEmailId(category);
        data.setCur(true);
        EMApplication.getDaoSession().getAccountDetailDao().insert(data);
        //存储账号到全局
        QueryBuilder<AccountDetail> qb = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.IsCur.eq("true"));
        List<AccountDetail> accounts = qb.list();
        if (accounts != null && accounts.size() > 0) {
            AccountDetail accountDetail = accounts.get(0);
            Log.i("mango", "account:" + accountDetail.getAccount() + "    isCurrent:" + accountDetail.isCur() + "  ReceiveProtocol:"
                    + accountDetail.getEmail().getReceiveProtocol() + "  ReceiveHost:" + accountDetail.getEmail().getReceiveHostValue());
            EMApplication.setAccount(accountDetail);
        }
    }

}

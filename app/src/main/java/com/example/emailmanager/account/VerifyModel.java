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

import androidx.databinding.ObservableField;

public class VerifyModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    public final ObservableField<String> account = new ObservableField<>();
    public final ObservableField<String> pwd = new ObservableField<>();
    private final long category;
    private String host;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    MainActivity.start2MainActivity(mContext);
                    ((Activity) mContext).finish();
                    break;
                case ERROR:
                    String hint = (String) msg.obj;
                    Toast.makeText(mContext, hint, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public VerifyModel(String host, Context mContext, long category) {
        this.host = host;
        this.mContext = mContext;
        this.category = category;
        initData();
    }

    private void initData() {
        account.set("1099805713@qq.com");
        pwd.set("pfujejqwrezxgbjj");
    }

    public void addAccount(View v) {
        List<AccountDetail> accounts = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.Account.eq(account.get())).list();
        if (!(accounts != null && accounts.size() > 0)) {
            Log.i("Mango", "addAccount");
            new Thread() {
                @Override
                public void run() {
                    Properties props = System.getProperties();
                    props.put("mail.imap.host", host);
                    props.put("mail.imap.port", "993");
                    props.put("mail.imap.ssl.enable", true);
                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    account.get(), pwd.get());
                        }
                    });
                    session.setDebug(true);
                    String msg;
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
                        SystemClock.sleep(1000);
                        msg = "验证成功";
                        mHandler.sendEmptyMessage(SUCCESS);
                    } catch (NoSuchProviderException e) {
                        Message message = Message.obtain();
                        message.what = ERROR;
                        message.obj = "验证失败";
                        mHandler.sendMessage(message);
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        Message message = Message.obtain();
                        message.what = ERROR;
                        message.obj = "验证失败";
                        mHandler.sendMessage(message);
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
            Message message = Message.obtain();
            message.what = ERROR;
            message.obj = "账号重复";
            mHandler.sendMessage(message);
        }


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

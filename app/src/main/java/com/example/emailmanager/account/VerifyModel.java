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
                    Toast.makeText(mContext, "邮箱或密码有误", Toast.LENGTH_SHORT).show();
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
//        List<AccountDetail> accounts = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.Account.eq(email.get())).list();
//        if (!(accounts != null && accounts.size() > 0)) {
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
                    msg = "验证失败";
                    mHandler.sendEmptyMessage(ERROR);
                    e.printStackTrace();
                } catch (MessagingException e) {
                    msg = "验证失败";
                    mHandler.sendEmptyMessage(ERROR);
                    e.printStackTrace();
                } finally {
                    try {
                        store.close();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Mango", msg);

            }
        }.start();
//        } else {
//            mHandler.sendEmptyMessage(SUCCESS);
//        }


    }

    private void saveAccount() {
        AccountDetail data = new AccountDetail();
        data.setAccount(account.get());
        data.setPwd(pwd.get());
        data.setEmailId(category);
        data.setCur(true);
        EMApplication.getDaoSession().getAccountDetailDao().insert(data);
    }


}

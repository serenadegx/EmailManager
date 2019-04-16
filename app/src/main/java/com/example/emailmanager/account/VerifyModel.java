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
import com.example.emailmanager.data.Receiver;
import com.example.emailmanager.data.Send;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import androidx.databinding.ObservableField;

public class VerifyModel {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> pwd = new ObservableField<>();

    private String host;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    MainActivity.start2MainActivity(mContext);
                    ((Activity)mContext).finish();
                    break;
                case ERROR:
                    Toast.makeText(mContext, "邮箱或密码有误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public VerifyModel(String host, Context mContext) {
        this.host = host;
        this.mContext = mContext;
        initData();
    }

    private void initData() {
        email.set("1099805713@qq.com");
        pwd.set("pfujejqwrezxgbjj");
    }

    public void addAccount(View v) {
        List<AccountDetail> accounts = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.Account.eq(email.get())).list();
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
                                    email.get(), pwd.get());
                        }
                    });
                    session.setDebug(true);
                    String msg;
                    Store store = null;
                    try {
                        store = session.getStore("imap");
                        store.connect();
                        add();
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
        }else {
            mHandler.sendEmptyMessage(SUCCESS);
        }



    }

    private void add() {
        List<AccountDetail> accounts = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().where(AccountDetailDao.Properties.Account.eq(email.get())).list();
        if (!(accounts != null && accounts.size() > 0)) {
            long customId = SystemClock.currentThreadTimeMillis();
            AccountDetail account = new AccountDetail();
            account.setAccount(email.get());
            account.setPwd(pwd.get());
            account.setEnable(true);
            account.setCustomId(customId);
            Receiver receiver = new Receiver();
            receiver.setId(customId);
            receiver.setProtocol("imap");
            receiver.setHostKey("mail.imap.host");
            receiver.setHostValue("imap.qq.com");
            receiver.setPortKey("mail.imap.port");
            receiver.setPortValue("993");
            receiver.setEncryptKey("mail.imap.ssl.enable");
            receiver.setEncryptValue(true);
            account.setReceiver(receiver);
            EMApplication.getDaoSession().getReceiverDao().insert(receiver);
            Send send = new Send();
            send.setId(customId);
            send.setProtocol("smtp");
            send.setHostKey("mail.smtp.host");
            send.setHostValue("smtp.qq.com");
            send.setPortKey("mail.smtp.port");
            send.setPortValue("465");
            send.setEncryptKey("mail.smtp.ssl.enable");
            send.setEncryptValue(true);
            account.setSend(send);
            EMApplication.getDaoSession().getSendDao().insert(send);
            EMApplication.getDaoSession().getAccountDetailDao().insert(account);
        }
    }

}

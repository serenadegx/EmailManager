package com.example.emailmanager;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.example.emailmanager.account.EmailCategoryActivity;
import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.Email;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initData();
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<AccountDetail> list = EMApplication.getDaoSession().getAccountDetailDao().queryBuilder().list();
                        if (list != null && list.size() > 0) {
                            EMApplication.setAccount(list.get(0).getAccount());
                            MainActivity.start2MainActivity(SplashActivity.this);
                        } else {
                            EmailCategoryActivity.start2EmailCategoryActivity(SplashActivity.this);
                        }
                        finish();
                    }
                });
            }
        }.start();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                List<Email> emails = EMApplication.getDaoSession().getEmailDao().loadAll();
                if (emails == null || emails.size() < 2) {
                    Email email1 = new Email();
                    String[] array = getResources().getStringArray(R.array.qq_email);
                    email1.setCategoryId(1);//主键不能重复
                    email1.setName(array[0]);
                    email1.setReceiveProtocol(array[1]);
                    email1.setReceiveHostKey(array[2]);
                    email1.setReceiveHostValue(array[3]);
                    email1.setReceivePortKey(array[4]);
                    email1.setReceivePortValue(array[5]);
                    email1.setReceiveEncryptKey(array[6]);
                    email1.setReceiveEncryptValue("1".equals(array[7]));
                    email1.setSendProtocol(array[8]);
                    email1.setSendHostKey(array[9]);
                    email1.setReceiveHostValue(array[10]);
                    email1.setSendPortKey(array[11]);
                    email1.setSendPortValue(array[12]);
                    email1.setSendEncryptKey(array[13]);
                    email1.setSendEncryptValue("1".equals(array[14]));
                    email1.setAuthKey(array[15]);
                    email1.setAuthValue("1".equals(array[16]));
                    EMApplication.getDaoSession().getEmailDao().insert(email1);
                    String[] array2 = getResources().getStringArray(R.array.qq_exmail);
                    Email email2 = new Email();
                    email2.setCategoryId(2);
                    email2.setName(array2[0]);
                    email2.setReceiveProtocol(array2[1]);
                    email2.setReceiveHostKey(array2[2]);
                    email2.setReceiveHostValue(array2[3]);
                    email2.setReceivePortKey(array2[4]);
                    email2.setReceivePortValue(array2[5]);
                    email2.setReceiveEncryptKey(array2[6]);
                    email2.setReceiveEncryptValue("1".equals(array2[7]));
                    email2.setSendProtocol(array2[8]);
                    email2.setSendHostKey(array2[9]);
                    email2.setReceiveHostValue(array2[10]);
                    email2.setSendPortKey(array2[11]);
                    email2.setSendPortValue(array2[12]);
                    email2.setSendEncryptKey(array2[13]);
                    email2.setSendEncryptValue("1".equals(array2[14]));
                    email2.setAuthKey(array2[15]);
                    email2.setAuthValue("1".equals(array2[16]));
                    EMApplication.getDaoSession().getEmailDao().insert(email2);
                }
            }
        }.start();
    }
}

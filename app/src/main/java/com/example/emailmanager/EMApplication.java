package com.example.emailmanager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.emailmanager.data.AccountDetail;
import com.example.emailmanager.data.DaoMaster;
import com.example.emailmanager.data.DaoSession;

public class EMApplication extends Application {
    private static AccountDetail mAccount;

    public static void setAccount(AccountDetail account) {
        mAccount = account;
    }

    public static AccountDetail getAccount() {
        return mAccount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "aserbao.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}

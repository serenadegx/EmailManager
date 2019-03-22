package com.example.emailmanager.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class AccountDetail {
    @Id(autoincrement = true)
    private long id;
    private String account;
    private String pwd;
    private int emailCategoryId;
    private String emailCategory;
    private boolean enable;
    private long customId;
    /**
     * 接收邮件服务器
     */
    @ToOne(joinProperty = "customId")
    private Receiver receiver;
    /**
     * 发送邮件服务器
     */
    @ToOne(joinProperty = "customId")
    private Send send;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1975204085)
    private transient AccountDetailDao myDao;

    @Generated(hash = 1891738362)
    public AccountDetail(long id, String account, String pwd, int emailCategoryId,
            String emailCategory, boolean enable, long customId) {
        this.id = id;
        this.account = account;
        this.pwd = pwd;
        this.emailCategoryId = emailCategoryId;
        this.emailCategory = emailCategory;
        this.enable = enable;
        this.customId = customId;
    }

    @Generated(hash = 200705118)
    public AccountDetail() {
    }

    @Generated(hash = 118553546)
    private transient Long receiver__resolvedKey;
    @Generated(hash = 138548586)
    private transient Long send__resolvedKey;

    public void setId(long id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmailCategoryId(int emailCategoryId) {
        this.emailCategoryId = emailCategoryId;
    }

    public void setEmailCategory(String emailCategory) {
        this.emailCategory = emailCategory;
    }

    public void setCustomId(long customId) {
        this.customId = customId;
    }

    @Keep
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Keep
    public void setSend(Send send) {
        this.send = send;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPwd() {
        return pwd;
    }

    public int getEmailCategoryId() {
        return emailCategoryId;
    }

    public String getEmailCategory() {
        return emailCategory;
    }

    public long getCustomId() {
        return customId;
    }

    @Keep
    public Receiver getReceiver() {
        return receiver;
    }

    @Keep
    public Send getSend() {
        return send;
    }

    public boolean isEnable() {
        return enable;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1905580256)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountDetailDao() : null;
    }

    public boolean getEnable() {
        return this.enable;
    }
}

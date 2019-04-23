package com.example.emailmanager.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class AccountDetail {
    @Id(autoincrement = true)
    private long id;
    @NotNull
    private String account;
    @NotNull
    private String pwd;
    @NotNull
    private long emailId;
    @ToOne(joinProperty = "emailId")
    private Email email;
    private boolean isCur;
    private String remark;
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

    @Generated(hash = 392936856)
    public AccountDetail(long id, @NotNull String account, @NotNull String pwd, long emailId,
            boolean isCur, String remark) {
        this.id = id;
        this.account = account;
        this.pwd = pwd;
        this.emailId = emailId;
        this.isCur = isCur;
        this.remark = remark;
    }

    @Generated(hash = 200705118)
    public AccountDetail() {
    }

    @Generated(hash = 759071072)
    private transient Long email__resolvedKey;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setCur(boolean cur) {
        isCur = cur;
    }

    public boolean isCur() {
        return isCur;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmailId(long emailId) {
        this.emailId = emailId;
    }

    @Keep
    public void setEmail(Email email) {
        this.email = email;
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

    public long getEmailId() {
        return emailId;
    }

    @Keep
    public Email getEmail() {
        return email;
    }

    public boolean getIsCur() {
        return this.isCur;
    }

    public void setIsCur(boolean isCur) {
        this.isCur = isCur;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1905580256)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountDetailDao() : null;
    }
}

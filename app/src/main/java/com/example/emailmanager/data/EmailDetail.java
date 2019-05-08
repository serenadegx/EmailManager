package com.example.emailmanager.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class EmailDetail extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 6201378234876550715L;
    @Id
    private Long id;
    private boolean isRead;
    private String subject;
    private String date;
    private String from;
    private String personal;
    @Transient
    private String to;
    @Transient
    private String cc;
    @Transient
    private String bcc;
    @Transient
    private String content;
    @ToMany(referencedJoinProperty = "emailId")
    private List<AccessoryDetail> accessoryList = new ArrayList<>();
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 174579630)
    private transient EmailDetailDao myDao;

    public EmailDetail() {
    }

    public EmailDetail(Long id, String subject, String date, String from) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.from = from;
    }

    @Generated(hash = 560960926)
    public EmailDetail(Long id, boolean isRead, String subject, String date, String from,
            String personal) {
        this.id = id;
        this.isRead = isRead;
        this.subject = subject;
        this.date = date;
        this.from = from;
        this.personal = personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getPersonal() {
        return personal;
    }

    @Bindable
    public Long getId() {
        return id;
    }

    @Bindable
    public String getSubject() {
        return subject;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    @Bindable
    public String getFrom() {
        return from;
    }

    @Bindable
    public String getTo() {
        return to;
    }

    @Bindable
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setAccessoryList(List<AccessoryDetail> accessoryList) {
        this.accessoryList = accessoryList;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1926107967)
    public List<AccessoryDetail> getAccessoryList() {
        if (accessoryList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccessoryDetailDao targetDao = daoSession.getAccessoryDetailDao();
            List<AccessoryDetail> accessoryListNew = targetDao
                    ._queryEmailDetail_AccessoryList(id);
            synchronized (this) {
                if (accessoryList == null) {
                    accessoryList = accessoryListNew;
                }
            }
        }
        return accessoryList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof EmailDetail && this.getId() == ((EmailDetail) obj).getId());
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 748983842)
    public synchronized void resetAccessoryList() {
        accessoryList = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 261910362)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEmailDetailDao() : null;
    }
}

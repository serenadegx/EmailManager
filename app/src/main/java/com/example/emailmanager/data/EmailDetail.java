package com.example.emailmanager.data;


import android.os.Parcel;
import android.os.Parcelable;

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
public class EmailDetail extends BaseObservable implements Parcelable{
    @Id
    private Long id;
    private boolean isRead;
    private String subject;
    private String date;
    private String from;
    private String personal;
    private String to;
    private String cc;
    private String bcc;
    private String content;
    @ToMany(referencedJoinProperty = "emailId")
    public List<AccessoryDetail> accessoryList = new ArrayList<>();

    public EmailDetail() {
    }

    public EmailDetail(Long id, String subject, String date, String from) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.from = from;
    }


    protected EmailDetail(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        isRead = in.readByte() != 0;
        subject = in.readString();
        date = in.readString();
        from = in.readString();
        personal = in.readString();
        to = in.readString();
        cc = in.readString();
        bcc = in.readString();
        content = in.readString();
        accessoryList = in.createTypedArrayList(AccessoryDetail.CREATOR);
    }

    @Generated(hash = 2132571833)
    public EmailDetail(Long id, boolean isRead, String subject, String date, String from,
            String personal, String to, String cc, String bcc, String content) {
        this.id = id;
        this.isRead = isRead;
        this.subject = subject;
        this.date = date;
        this.from = from;
        this.personal = personal;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
    }

    public static final Creator<EmailDetail> CREATOR = new Creator<EmailDetail>() {
        @Override
        public EmailDetail createFromParcel(Parcel in) {
            return new EmailDetail(in);
        }

        @Override
        public EmailDetail[] newArray(int size) {
            return new EmailDetail[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 174579630)
    private transient EmailDetailDao myDao;

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

    public void setAccessoryList(List<AccessoryDetail> accessoryList) {
        this.accessoryList =  accessoryList;
    }
    

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof EmailDetail && this.getId() == ((EmailDetail) obj).getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeByte((byte) (isRead ? 1 : 0));
        dest.writeString(subject);
        dest.writeString(date);
        dest.writeString(from);
        dest.writeString(personal);
        dest.writeString(to);
        dest.writeString(cc);
        dest.writeString(bcc);
        dest.writeString(content);
        dest.writeTypedList(accessoryList);
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

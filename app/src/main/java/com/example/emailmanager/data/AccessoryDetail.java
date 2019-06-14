package com.example.emailmanager.data;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AccessoryDetail extends BaseObservable implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String fileName;
    private String path;
    private String size;
    private long total;
    private boolean isDownload;
    private Long emailId;
    @Transient
    private InputStream is;

    public AccessoryDetail(String fileName) {
        this.fileName = fileName;
    }

    public AccessoryDetail(String fileName, String path, String size, boolean isDownload) {
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.isDownload = isDownload;
    }

    public AccessoryDetail(String fileName, String size, long total, InputStream is) {
        this.fileName = fileName;
        this.size = size;
        this.total = total;
        this.is = is;
    }

    public AccessoryDetail(String fileName, String path, String size) {
        this.fileName = fileName;
        this.path = path;
        this.size = size;
    }

    public AccessoryDetail(Long emailId, String fileName, String size) {
        this.fileName = fileName;
        this.size = size;
        this.emailId = emailId;
    }

    @Generated(hash = 1796029192)
    public AccessoryDetail(Long id, String fileName, String path, String size, long total, boolean isDownload,
                           Long emailId) {
        this.id = id;
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.total = total;
        this.isDownload = isDownload;
        this.emailId = emailId;
    }

    @Generated(hash = 797569814)
    public AccessoryDetail() {
    }


    protected AccessoryDetail(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        fileName = in.readString();
        path = in.readString();
        size = in.readString();
        total = in.readLong();
        isDownload = in.readByte() != 0;
        if (in.readByte() == 0) {
            emailId = null;
        } else {
            emailId = in.readLong();
        }
    }

    public static final Creator<AccessoryDetail> CREATOR = new Creator<AccessoryDetail>() {
        @Override
        public AccessoryDetail createFromParcel(Parcel in) {
            return new AccessoryDetail(in);
        }

        @Override
        public AccessoryDetail[] newArray(int size) {
            return new AccessoryDetail[size];
        }
    };

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public InputStream getIs() {
        return is;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    @Bindable
    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    @Bindable
    public String getSize() {
        return size;
    }

    @Bindable
    public boolean isDownload() {
        return isDownload;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof AccessoryDetail && this.getFileName().equals(((AccessoryDetail) obj)
                .getFileName()) && this.getEmailId() == ((AccessoryDetail) obj).getEmailId();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsDownload() {
        return this.isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
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
        dest.writeString(fileName);
        dest.writeString(path);
        dest.writeString(size);
        dest.writeLong(total);
        dest.writeByte((byte) (isDownload ? 1 : 0));
        if (emailId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(emailId);
        }
    }
}

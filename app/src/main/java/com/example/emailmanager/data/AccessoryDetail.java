package com.example.emailmanager.data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AccessoryDetail extends BaseObservable {
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
}

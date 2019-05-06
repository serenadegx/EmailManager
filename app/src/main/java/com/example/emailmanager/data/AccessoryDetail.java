package com.example.emailmanager.data;


import java.io.InputStream;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AccessoryDetail extends BaseObservable {
    private String fileName;
    private String path;
    private String size;
    private long total;
    private boolean isDownload;
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
}

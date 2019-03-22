package com.example.emailmanager.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class AccessoryDetail extends BaseObservable {
    private String fileName;
    private String downloadUrl;
    private String size;
    private boolean isDownload;

    public AccessoryDetail(String fileName, String downloadUrl, String size, boolean isDownload) {
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.size = size;
        this.isDownload = isDownload;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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

    public String getDownloadUrl() {
        return downloadUrl;
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

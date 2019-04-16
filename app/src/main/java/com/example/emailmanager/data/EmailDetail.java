package com.example.emailmanager.data;


import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class EmailDetail extends BaseObservable {
    private int id;
    private boolean isRead;
    private String subject;
    private String date;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String content;
    private List<AccessoryDetail> accessoryList = new ArrayList<>();

    public EmailDetail() {
    }

    public EmailDetail(int id, String subject, String date, String from) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.from = from;
    }

    @Bindable
    public int getId() {
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

    public void setId(int id) {
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

    public List<AccessoryDetail> getAccessoryList() {
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
}

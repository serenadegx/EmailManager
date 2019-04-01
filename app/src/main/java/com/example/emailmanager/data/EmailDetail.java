package com.example.emailmanager.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

public class EmailDetail extends BaseObservable {
    private int id;
    private String subject;
    private String date;
    private String from;
    private String to;
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

    public String getTo() {
        return to;
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
}

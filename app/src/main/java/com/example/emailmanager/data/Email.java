package com.example.emailmanager.data;

import androidx.databinding.Bindable;

public class Email {
    private int categoryId;
    private String name;

    private String receiveProtocol;
    private String receiveHostKey;
    private String receiveHostValue;
    private String receivePortKey;
    private String receivePortValue;
    private String receiveEncryptKey;
    private boolean receiveEncryptValue;

    private String sendProtocol;
    private String sendHostKey;
    private String sendHostValue;
    private String sendPortKey;
    private String sendPortValue;
    private String sendEncryptKey;
    private boolean sendEncryptValue;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReceiveProtocol(String receiveProtocol) {
        this.receiveProtocol = receiveProtocol;
    }

    public void setReceiveHostKey(String receiveHostKey) {
        this.receiveHostKey = receiveHostKey;
    }

    public void setReceiveHostValue(String receiveHostValue) {
        this.receiveHostValue = receiveHostValue;
    }

    public void setReceivePortKey(String receivePortKey) {
        this.receivePortKey = receivePortKey;
    }

    public void setReceivePortValue(String receivePortValue) {
        this.receivePortValue = receivePortValue;
    }

    public void setReceiveEncryptKey(String receiveEncryptKey) {
        this.receiveEncryptKey = receiveEncryptKey;
    }

    public void setReceiveEncryptValue(boolean receiveEncryptValue) {
        this.receiveEncryptValue = receiveEncryptValue;
    }

    public void setSendProtocol(String sendProtocol) {
        this.sendProtocol = sendProtocol;
    }

    public void setSendHostKey(String sendHostKey) {
        this.sendHostKey = sendHostKey;
    }

    public void setSendHostValue(String sendHostValue) {
        this.sendHostValue = sendHostValue;
    }

    public void setSendPortKey(String sendPortKey) {
        this.sendPortKey = sendPortKey;
    }

    public void setSendPortValue(String sendPortValue) {
        this.sendPortValue = sendPortValue;
    }

    public void setSendEncryptKey(String sendEncryptKey) {
        this.sendEncryptKey = sendEncryptKey;
    }

    public void setSendEncryptValue(boolean sendEncryptValue) {
        this.sendEncryptValue = sendEncryptValue;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public String getReceiveProtocol() {
        return receiveProtocol;
    }

    public String getReceiveHostKey() {
        return receiveHostKey;
    }

    public String getReceiveHostValue() {
        return receiveHostValue;
    }

    public String getReceivePortKey() {
        return receivePortKey;
    }

    public String getReceivePortValue() {
        return receivePortValue;
    }

    public String getReceiveEncryptKey() {
        return receiveEncryptKey;
    }

    public boolean isReceiveEncryptValue() {
        return receiveEncryptValue;
    }

    public String getSendProtocol() {
        return sendProtocol;
    }

    public String getSendHostKey() {
        return sendHostKey;
    }

    public String getSendHostValue() {
        return sendHostValue;
    }

    public String getSendPortKey() {
        return sendPortKey;
    }

    public String getSendPortValue() {
        return sendPortValue;
    }

    public String getSendEncryptKey() {
        return sendEncryptKey;
    }

    public boolean isSendEncryptValue() {
        return sendEncryptValue;
    }
}

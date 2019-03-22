package com.example.emailmanager.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Receiver {
    @Id
    private long id;
    private String protocol;
    private String hostKey;
    private String hostValue;
    private String portKey;
    private String portValue;
    private String encryptKey;
    private boolean encryptValue;

    @Generated(hash = 214941433)
    public Receiver(long id, String protocol, String hostKey, String hostValue,
            String portKey, String portValue, String encryptKey,
            boolean encryptValue) {
        this.id = id;
        this.protocol = protocol;
        this.hostKey = hostKey;
        this.hostValue = hostValue;
        this.portKey = portKey;
        this.portValue = portValue;
        this.encryptKey = encryptKey;
        this.encryptValue = encryptValue;
    }

    @Generated(hash = 1850575795)
    public Receiver() {
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public void setHostValue(String hostValue) {
        this.hostValue = hostValue;
    }

    public void setPortKey(String portKey) {
        this.portKey = portKey;
    }

    public void setPortValue(String portValue) {
        this.portValue = portValue;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public void setEncryptValue(boolean encryptValue) {
        this.encryptValue = encryptValue;
    }

    public String getHostKey() {
        return hostKey;
    }

    public String getHostValue() {
        return hostValue;
    }

    public String getPortKey() {
        return portKey;
    }

    public String getPortValue() {
        return portValue;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public boolean isEncryptValue() {
        return encryptValue;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getEncryptValue() {
        return this.encryptValue;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}

package com.example.emailmanager.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Contacts {
    private String personal;
    private String address;

    public Contacts(String personal, String address) {
        this.personal = personal;
        this.address = address;
    }

    public String getPersonal() {
        return personal;
    }

    public String getAddress() {
        return address;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return personal;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Contacts) && this.getAddress().equals(((Contacts) obj).getAddress());
    }
}

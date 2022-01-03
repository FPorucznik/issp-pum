package com.example.studentcrimeapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String picture;

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean getSolved(){ return this.mSolved; }

    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id){ this.mId = id; }

    public String getTitle() { return mTitle; }

    public Date getDate() { return mDate; }

    public void setDate(Date date){ this.mDate = date; }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }
}

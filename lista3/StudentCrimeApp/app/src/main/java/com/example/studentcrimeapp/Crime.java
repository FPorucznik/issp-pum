package com.example.studentcrimeapp;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }
}

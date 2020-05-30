package com.example.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Family implements Parcelable {
    private String familyId;
    private String familyName;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.familyId);
        dest.writeString(this.familyName);
    }

    public Family() {
    }

    protected Family(Parcel in) {
        this.familyId = in.readString();
        this.familyName = in.readString();
    }

    public static final Parcelable.Creator<Family> CREATOR = new Parcelable.Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel source) {
            return new Family(source);
        }

        @Override
        public Family[] newArray(int size) {
            return new Family[size];
        }
    };
}

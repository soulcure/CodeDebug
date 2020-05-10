package com.example.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Book implements Parcelable {
    public String bookName;
    public int price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookName);
        dest.writeInt(price);
    }

    public static Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in) {
        readFromParcel(in);
    }

    public Book(String name, int price) {
        this.bookName = name;
        this.price = price;
    }

    public Book() {

    }

    public void readFromParcel(Parcel in) {
        bookName = in.readString();
        price = in.readInt();
    }

    @NonNull
    public String toString() {
        return "[bookName=" + bookName + ", bookPrice=" + price + "]";
    }
}
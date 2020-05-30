package com.example.sdk;

import com.example.sdk.entity.Family;

import java.util.List;

public interface FamiliesListener {
    void onSuccess(List<Family> list);
}
// IFamilyManager.aidl
package com.example.sdk;
import com.example.sdk.entity.Family;
// Declare any non-default types here with import statements

interface IFamilyManager {
    List<Family>  getFamilyList(in String key);
}

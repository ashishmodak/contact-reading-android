package com.example.contacts.contactsdemo;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by muffin on 30/06/17.
 */
public class ContactObj implements Serializable {
    public String accountType;
    public String id;
    public String displayName;
    public String storedNumber;
    public String version;
    public String e164Number;
    public String internationalNumber;
    public int hasPhoneNumber;
    public String photoURI;
    public PhoneNumberUtil.PhoneNumberType phoneNumberType;
    private long contactId;
    private ArrayList<String> emailList;
    public ContactObj() {
        emailList = new ArrayList<String>();
        internationalNumber = e164Number = "NA";
        version = "0";
    }
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStoredNumber() {
        return storedNumber;
    }

    public void setStoredNumber(String storedNumber) {
        this.storedNumber = storedNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getE164Number() {
        return e164Number;
    }

    public void setE164Number(String e164Number) {
        this.e164Number = e164Number;
    }

    public String getInternationalNumber() {
        return internationalNumber;
    }

    public void setInternationalNumber(String internationalNumber) {
        this.internationalNumber = internationalNumber;
    }

    public int getHasPhoneNumber() {
        return hasPhoneNumber;
    }

    public void setHasPhoneNumber(int hasPhoneNumber) {
        this.hasPhoneNumber = hasPhoneNumber;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public ArrayList<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }

    public void addEmail(String email) {
        this.emailList.add(email);
    }

    public PhoneNumberUtil.PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(PhoneNumberUtil.PhoneNumberType phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }
}

package com.nicobbp.mybusinesscard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Profile implements Serializable {

    private String id;
    private String fullName;
    private String headline;
    private String mail;
    private String location;
    private String pictureUrl;
    private List<Profile> contactList;

    Profile() {
        this.contactList = new ArrayList<>();
    }

    void getProfileData(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getString("id"));
        setFullName(jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));
        setHeadline(jsonObject.getString("headline"));
        setMail(jsonObject.getString("emailAddress"));
        setLocation(jsonObject.getJSONObject("location").getString("name"));
        setPictureUrl(jsonObject.getJSONObject("pictureUrls").getJSONArray("values").getString(0));
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getFullName() {
        return fullName;
    }

    void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String getHeadline() {
        return headline;
    }

    void setHeadline(String headline) {
        this.headline = headline;
    }

    String getMail() {
        return mail;
    }

    void setMail(String mail) {
        this.mail = mail;
    }

    String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    String getPictureUrl() {
        return pictureUrl;
    }

    void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    List<Profile> getContactList() {
        return contactList;
    }

    void setContactList(List<Profile> contactList) {
        this.contactList = contactList;
    }
}

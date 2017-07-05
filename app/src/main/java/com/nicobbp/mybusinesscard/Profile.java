package com.nicobbp.mybusinesscard;

import org.json.JSONException;
import org.json.JSONObject;

class Profile {

    private String id;
    private String fullName;
    private String headline;
    private String mail;
    private String location;
    private String pictureUrl;

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

    private void setId(String id) {
        this.id = id;
    }

    String getFullName() {
        return fullName;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String getHeadline() {
        return headline;
    }

    private void setHeadline(String headline) {
        this.headline = headline;
    }

    String getMail() {
        return mail;
    }

    private void setMail(String mail) {
        this.mail = mail;
    }

    String getLocation() {
        return location;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    String getPictureUrl() {
        return pictureUrl;
    }

    private void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}

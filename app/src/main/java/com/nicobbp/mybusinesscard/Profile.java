package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.EncodeHintType;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private transient Bitmap qrCode;
    private List<Profile> contactList;

    public Profile() {
        this.contactList = new ArrayList<>();
    }

    public void getProfileDataFromJSON(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getString("id"));
        setFullName(jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));
        setHeadline(jsonObject.getString("headline"));
        setMail(jsonObject.getString("emailAddress"));
        setLocation(jsonObject.getJSONObject("location").getString("name"));
        setPictureUrl(jsonObject.getJSONObject("pictureUrls").getJSONArray("values").getString(0));
    }

    public String generateProfileString() {
        return "ValidLinkedInProfile__" +
                getId() + "__" +
                getFullName() + "__" +
                getHeadline() + "__" +
                getMail() + "__" +
                getLocation() + "__" +
                getPictureUrl();
    }

    public void generateQR() {
        setQrCode(QRCode.from(generateProfileString()).withSize(512, 512)
                .withHint(EncodeHintType.MARGIN, "1").bitmap());
    }

    public Profile createContactFromQRCode(String code) {
        String DELIMITER = "__";
        String[] strTemp = code.split(DELIMITER);

        Profile newContact = new Profile();
        newContact.setId(strTemp[1]);
        newContact.setFullName(strTemp[2]);
        newContact.setHeadline(strTemp[3]);
        newContact.setMail(strTemp[4]);
        newContact.setLocation(strTemp[5]);
        newContact.setPictureUrl(strTemp[6]);
        return newContact;
    }

    public Profile readSavedProfile(Activity activity) throws IOException, ClassNotFoundException {
        FileInputStream fis = activity.openFileInput("profileSave.txt");
        ObjectInputStream is = new ObjectInputStream(fis);
        Profile myProfile = (Profile) is.readObject();
        is.close();
        fis.close();
        return myProfile;
    }

    public void writeSavedProfile(Profile myProfile, Activity activity) throws IOException {
        FileOutputStream fos = activity.openFileOutput("profileSave.txt", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(myProfile);
        os.close();
        fos.close();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public List<Profile> getContactList() {
        return contactList;
    }

    public void setContactList(List<Profile> contactList) {
        this.contactList = contactList;
    }

}

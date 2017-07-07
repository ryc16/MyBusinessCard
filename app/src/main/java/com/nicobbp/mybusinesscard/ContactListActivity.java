package com.nicobbp.mybusinesscard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.nicobbp.mybusinesscard.MainActivity.userProfile;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        try {
            userProfile = read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateContactList();
    }

    public void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        View linearLayout = findViewById(R.id.contact_list_layout);
        if (scanResult != null) {
            userProfile.getContactList().add(createContactFromQRCode(scanResult.getContents()));
            try {
                write(userProfile);
                addNewContact((LinearLayout) linearLayout, userProfile.getContactList().size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateContactList() {
        View linearLayout = findViewById(R.id.contact_list_layout);
        for (int i = 0; i < userProfile.getContactList().size(); i++) {
            addNewContact((LinearLayout) linearLayout, i);
        }
    }

    public void addNewContact(LinearLayout linearLayout, int i) {
        TextView textView = new TextView(this);
        textView.setText(userProfile.getContactList().get(i).getFullName());
        linearLayout.addView(textView);
    }

    public Profile createContactFromQRCode(String code) {
        String DELIMITER = "__";
        String[] strTemp = code.split(DELIMITER);

        Profile newContact = new Profile();
        newContact.setId(strTemp[0]);
        newContact.setFullName(strTemp[1]);
        newContact.setHeadline(strTemp[2]);
        newContact.setMail(strTemp[3]);
        newContact.setLocation(strTemp[4]);
        newContact.setPictureUrl(strTemp[5]);
        return newContact;
    }

    public Profile read() throws IOException, ClassNotFoundException {
        FileInputStream fis = this.openFileInput("profileSave.txt");
        ObjectInputStream is = new ObjectInputStream(fis);
        Profile myProfile = (Profile) is.readObject();
        is.close();
        fis.close();
        return myProfile;
    }

    public void write(Profile myProfile) throws IOException {
        FileOutputStream fos = this.openFileOutput("profileSave.txt", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(myProfile);
        os.close();
        fos.close();
    }


}

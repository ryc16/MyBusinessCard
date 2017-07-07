package com.nicobbp.mybusinesscard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.nicobbp.mybusinesscard.MainActivity.userProfile;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        try {
            userProfile = userProfile.readSavedProfile(this);
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

        if (scanResult.getContents().contains("ValidLinkedInProfile")) {
            userProfile.getContactList().add(userProfile.createContactFromQRCode(scanResult.getContents()));
            try {
                userProfile.writeSavedProfile(userProfile, this);

                View linearLayout = findViewById(R.id.contact_list_layout);
                addNewContact((LinearLayout) linearLayout, userProfile.getContactList().size() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateContactList() {
        View linearLayout = findViewById(R.id.contact_list_layout);
        for (int i = 0; i < userProfile.getContactList().size(); i++) {
            addNewContact((LinearLayout) linearLayout, i);
        }
    }

    public void addNewContact(LinearLayout linearLayout, int i) {
        TextView textView = new TextView(this, null, 0, R.style.ProfileText);
        textView.setText(userProfile.getContactList().get(i).getFullName());
        linearLayout.addView(textView);
    }

}

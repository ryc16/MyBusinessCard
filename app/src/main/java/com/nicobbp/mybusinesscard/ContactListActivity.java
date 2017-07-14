package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nicobbp.mybusinesscard.Classes.Profile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Iterator;

import static com.nicobbp.mybusinesscard.LoginActivity.userProfile;

public class ContactListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        try {
            userProfile = userProfile.readSavedProfile(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateContactList(userProfile.getContactList().iterator());
    }

    public void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult.getContents() != null) {
            if (scanResult.getContents().contains("ValidLinkedInProfile")) {
                try {
                    userProfile.getContactList().add(userProfile.createContactFromQRCode(scanResult.getContents()));
                    userProfile.writeSavedProfile(userProfile, this);
                    generateContactList(userProfile.getContactList().iterator());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void generateContactList(Iterator contacts) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contact_list_layout);
        linearLayout.removeAllViews();
        final Activity thisActivity = this;

        while (contacts.hasNext()) {
            final Profile newContact = (Profile) contacts.next();
            LinearLayout textAndIcon = createNewContactLayout(newContact.getId(), newContact.getFullName(), newContact.getPictureUrl());
            textAndIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent contactProfile = new Intent(thisActivity, ContactProfileActivity.class);

                    contactProfile.putExtra("ID", newContact.getId());
                    contactProfile.putExtra("FULL_NAME", newContact.getFullName());
                    contactProfile.putExtra("HEADLINE", newContact.getHeadline());
                    contactProfile.putExtra("MAIL", newContact.getMail());
                    contactProfile.putExtra("LOCATION", newContact.getLocation());
                    contactProfile.putExtra("PICTURE_URL", newContact.getPictureUrl());

                    startActivity(contactProfile);
                }
            });

            linearLayout.addView(textAndIcon);
        }
    }

    public LinearLayout createNewContactLayout(final String contactId, String fullName, String pictureUrl) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.contact_layout, null);
        ImageView contactPicture = (ImageView) getLayoutInflater().inflate(R.layout.contact_image, linearLayout, false);
        TextView contactFullName = (TextView) getLayoutInflater().inflate(R.layout.contact_text, linearLayout, false);
        Button buttonDelete = (Button) getLayoutInflater().inflate(R.layout.contact_button, linearLayout, false);

        Picasso.with(this).load(pictureUrl).into(contactPicture);
        contactFullName.setText(fullName);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteContact(contactId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        linearLayout.addView(contactPicture);
        linearLayout.addView(contactFullName);
        linearLayout.addView(buttonDelete);
        return linearLayout;
    }

    public void deleteContact(String contactId) throws IOException {
        Profile contact = new Profile(contactId);
        userProfile.getContactList().remove(contact);
        userProfile.writeSavedProfile(userProfile, this);
        generateContactList(userProfile.getContactList().iterator());
    }
}

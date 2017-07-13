package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

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
        generateContactList();
    }

    public void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult.getContents() != null) {
            if (scanResult.getContents().contains("ValidLinkedInProfile")) {
                userProfile.getContactList().add(userProfile.createContactFromQRCode(scanResult.getContents()));
                try {
                    userProfile.writeSavedProfile(userProfile, this);

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contact_list_layout);
                    addNewContact(linearLayout, userProfile.getContactList().size() - 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void generateContactList() {
        View linearLayout = findViewById(R.id.contact_list_layout);
        for (int i = 0; i < userProfile.getContactList().size(); i++) {
            addNewContact((LinearLayout) linearLayout, i);
        }
    }

    public void addNewContact(LinearLayout linearLayout, final int i) {
        final Activity thisActivity = this;
        final Profile newContact = userProfile.getContactList().get(i);

        LinearLayout textAndIcon = createNewContactLayout(newContact.getFullName(), newContact.getPictureUrl());
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

    public LinearLayout createNewContactLayout(String fullName, String pictureUrl) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.contact_layout, null);
        ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.contact_image, null);
        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.contact_text, null);

        Picasso.with(this).load(pictureUrl).resize(150, 150).into(imageView);
        textView.setText(fullName);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        return linearLayout;
    }

}

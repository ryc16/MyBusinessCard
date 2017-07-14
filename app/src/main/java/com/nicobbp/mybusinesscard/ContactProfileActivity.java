package com.nicobbp.mybusinesscard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;
import com.nicobbp.mybusinesscard.Classes.Profile;
import com.squareup.picasso.Picasso;


public class ContactProfileActivity extends AppCompatActivity {
    Profile contactProfile = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        getContactData();
        setUpProfileView(contactProfile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this, requestCode, resultCode, data);
    }

    public void getContactData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactProfile.setId(extras.getString("ID"));
            contactProfile.setFullName(extras.getString("FULL_NAME"));
            contactProfile.setHeadline(extras.getString("HEADLINE"));
            contactProfile.setMail(extras.getString("MAIL"));
            contactProfile.setLocation(extras.getString("LOCATION"));
            contactProfile.setPictureUrl(extras.getString("PICTURE_URL"));
        }
    }

    public void setUpProfileView(Profile profile) {
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        TextView profileHeadline = (TextView) findViewById(R.id.profile_headline);
        TextView profileMail = (TextView) findViewById(R.id.profile_mail);
        TextView profileLocation = (TextView) findViewById(R.id.profile_location);
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);

        profileName.setText(profile.getFullName());
        profileHeadline.setText(profile.getHeadline());
        profileMail.setText(profile.getMail());
        profileLocation.setText(profile.getLocation());
        Picasso.with(this).load(profile.getPictureUrl()).into(profilePicture);
    }

    public void goToProfile(View view) {

        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
        final String targetID = contactProfile.getId();

        deepLinkHelper.openOtherProfile(this, targetID, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                // Successfully sent user to LinkedIn app
            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {
                // Error sending user to LinkedIn app
            }
        });
    }
}

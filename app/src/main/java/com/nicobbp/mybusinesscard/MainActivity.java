package com.nicobbp.mybusinesscard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nicobbp.mybusinesscard.Classes.Profile;
import com.squareup.picasso.Picasso;

import eu.davidea.flipview.FlipView;

import static com.nicobbp.mybusinesscard.LoginActivity.userProfile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpProfileView(userProfile);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void setUpProfileView(Profile profile) {
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        TextView profileHeadline = (TextView) findViewById(R.id.profile_headline);
        TextView profileMail = (TextView) findViewById(R.id.profile_mail);
        TextView profileLocation = (TextView) findViewById(R.id.profile_location);

        profileName.setText(profile.getFullName());
        profileHeadline.setText(profile.getHeadline());
        profileMail.setText(profile.getMail());
        profileLocation.setText(profile.getLocation());
        setProfilePicture();
    }

    public void setProfilePicture() {
        FlipView profilePicture = (FlipView) findViewById(R.id.profile_picture);
        Picasso.with(this).load(userProfile.getPictureUrl()).into(profilePicture.getFrontImageView());

        userProfile.generateQR();
        profilePicture.setRearImageBitmap(userProfile.getQrCode());
    }

    public void switchToContacts(View view) {
        Intent contacts = new Intent(this, ContactListActivity.class);
        startActivity(contacts);
    }
}

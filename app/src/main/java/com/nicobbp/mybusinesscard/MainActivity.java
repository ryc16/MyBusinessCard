package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicobbp.mybusinesscard.Classes.Profile;
import com.squareup.picasso.Picasso;

import static com.nicobbp.mybusinesscard.LoginActivity.userProfile;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpProfileView(userProfile);
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
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipImage(v);
            }
        });

        setProfilePicture();
        profile.generateQR();
    }

    public void setProfilePicture() {
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        Picasso.with(this).load(userProfile.getPictureUrl()).into(profilePicture);
        profilePicture.setTag(1);
    }

    public void flipImage(View view) {
        ImageView myImage = (ImageView) findViewById(R.id.profile_picture);

        if (Integer.parseInt(myImage.getTag().toString()) == 1) {
            if (userProfile.getQrCode() == null)
                userProfile.generateQR();
            myImage.setImageBitmap(userProfile.getQrCode());
            myImage.setTag(2);
        } else {
            setProfilePicture();
        }
    }

    public void switchToContacts(View view) {
        Intent contacts = new Intent(this, ContactListActivity.class);
        startActivity(contacts);
    }
}

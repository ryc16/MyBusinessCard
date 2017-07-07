package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.EncodeHintType;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import static com.linkedin.platform.utils.Scope.build;

public class MainActivity extends Activity {

    static Profile userProfile = new Profile();
    static Bitmap userQRCode;

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity thisActivity = this;

        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do other calls with the SDK.
                String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name," +
                        "headline,location,email-address,picture-urls::(original))?format=json";
                final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

                apiHelper.getRequest(thisActivity, url, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) throws JSONException {
                        // Success!
                        JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                        userProfile.getProfileData(jsonObject);
                        setUpProfile(userProfile);
                    }

                    @Override
                    public void onApiError(LIApiError liApiError) {
                        // Error making GET request!
                    }
                });
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this, requestCode, resultCode, data);
    }

    public void setUpProfile(Profile profile) throws JSONException {
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        TextView profileHeadline = (TextView) findViewById(R.id.profile_headline);
        TextView profileMail = (TextView) findViewById(R.id.profile_mail);
        TextView profileLocation = (TextView) findViewById(R.id.profile_location);

        profileName.setText(profile.getFullName());
        profileHeadline.setText(profile.getHeadline());
        profileMail.setText(profile.getMail());
        profileLocation.setText(profile.getLocation());

        setProfilePicture();
        generateQR();
    }

    public String generateProfileString() {
        return userProfile.getId() + "__" +
                userProfile.getFullName() + "__" +
                userProfile.getHeadline() + "__" +
                userProfile.getMail() + "__" +
                userProfile.getLocation() + "__" +
                userProfile.getPictureUrl();
    }

    public void generateQR() {
        userQRCode = QRCode.from(generateProfileString()).withSize(512, 512)
                .withHint(EncodeHintType.MARGIN, "1").bitmap();
    }

    public void setProfilePicture() {
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        Picasso.with(this).load(userProfile.getPictureUrl()).into(profilePicture);
        profilePicture.setTag(1);
    }

    public void flipImage(View view) {
        ImageView myImage = (ImageView) findViewById(R.id.profile_picture);

        if (Integer.parseInt(myImage.getTag().toString()) == 1) {
            myImage.setImageBitmap(userQRCode);
            myImage.setTag(2);
        } else {
            setProfilePicture();
        }
    }

    public void switchToContacts(View view) {
        Intent someName = new Intent(this, ContactListActivity.class);
        startActivity(someName);
    }
}



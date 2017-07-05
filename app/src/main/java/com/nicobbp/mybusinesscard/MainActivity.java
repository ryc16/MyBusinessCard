package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Store a reference to the current activity
        final Activity thisActivity = this;

        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name," +
                        "headline,location,email-address,picture-urls::(original))?format=json";
                final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

                apiHelper.getRequest(thisActivity, url, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) throws JSONException {
                        // Success!
                        JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                        fillProfileData(jsonObject, thisActivity);
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

    public void fillProfileData(JSONObject jsonObject, Activity activity) throws JSONException {

        profileId = jsonObject.getString("id");

        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        Picasso.with(activity).load(jsonObject.getJSONObject("pictureUrls")
                .getJSONArray("values").getString(0)).into(profilePicture);

        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));

        TextView profileHeadline = (TextView) findViewById(R.id.profile_headline);
        profileHeadline.setText(jsonObject.getString("headline"));

        TextView profileMail = (TextView) findViewById(R.id.profile_mail);
        profileMail.setText(jsonObject.getString("emailAddress"));

        TextView profileLocation = (TextView) findViewById(R.id.profile_location);
        profileLocation.setText(jsonObject.getJSONObject("location").getString("name"));
    }

    public void share(View view) {
        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();

        if (detector.isOperational()) {
            Bitmap myBitmap = generateQR("MyBusinessCard-" + profileId);

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodeArray = detector.detect(frame);

            Barcode thisCode = barcodeArray.valueAt(0);
            TextView txtView = (TextView) findViewById(R.id.profile_id);
            txtView.setText(thisCode.rawValue);
        }
    }

    public Bitmap generateQR(String content) {
        Bitmap myBitmap = QRCode.from(content).withSize(256, 256).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.profile_picture);
        myImage.setImageBitmap(myBitmap);
        return myBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
}



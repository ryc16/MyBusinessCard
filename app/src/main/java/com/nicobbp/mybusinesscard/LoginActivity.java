package com.nicobbp.mybusinesscard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.nicobbp.mybusinesscard.Classes.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import static com.linkedin.platform.utils.Scope.build;

public class LoginActivity extends Activity {

    static Profile userProfile = new Profile();

    private static Scope buildScope() {
        return build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this, requestCode, resultCode, data);
    }

    public void login(View view) {
        final Activity thisActivity = this;

        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name," +
                        "headline,location,email-address,picture-urls::(original))?format=json";
                final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

                apiHelper.getRequest(thisActivity, url, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) throws JSONException {
                        JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                        userProfile.getProfileDataFromJSON(jsonObject);
                        Intent main = new Intent(thisActivity, MainActivity.class);
                        startActivity(main);
                        thisActivity.finish();
                    }

                    @Override
                    public void onApiError(LIApiError liApiError) {
                        Toast.makeText(thisActivity, "Error connecting with LinkedIn, please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Toast.makeText(thisActivity, "Error connecting with LinkedIn, please try again", Toast.LENGTH_LONG).show();
            }
        }, true);
    }
}

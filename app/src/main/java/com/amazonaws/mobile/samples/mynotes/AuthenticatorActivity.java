package com.amazonaws.mobile.samples.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInActivity;

/**
 * Adds a new screen to the project that is displayed before the list. The user will be prompted to
 * sign-up or sign-in prior to seeing the list of notes. This ensures that all connections to the
 * backend will be authenticated.
 *
 * What does this do?
 * The AWS SDK for Android contains an in-built activity for handling the authentication UI.
 * This Activity sets up the authentication UI to work for just email and password, then sets up
 * an activity listener to handle the response. In this case, we transition to the NoteListActivity
 * when a successful sign-in occurs, and stay on this activity when it fails. Finally, we transition
 * to the Sign-In activity from the AWS SDK for Android library.
 */
public class AuthenticatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        final IdentityManager identityManager = AWSProvider.getInstance().getIdentityManager();
        // Set up the callbacks to handle the authentication response
        identityManager.setUpToAuthenticate(this, new DefaultSignInResultHandler() {
            @Override
            public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                Toast.makeText(AuthenticatorActivity.this,
                        String.format("Logged in as %s", identityManager.getCachedUserID()),
                        Toast.LENGTH_LONG).show();
                // Go to the main activity
                final Intent intent = new Intent(activity, NoteListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public boolean onCancel(Activity activity) {
                return false;
            }
        });

        // Start the authentication UI
        AuthUIConfiguration config = new AuthUIConfiguration.Builder()
                .userPools(true)
                .build();
        SignInActivity.startSignInActivity(this, config);
        AuthenticatorActivity.this.finish();

    }
}

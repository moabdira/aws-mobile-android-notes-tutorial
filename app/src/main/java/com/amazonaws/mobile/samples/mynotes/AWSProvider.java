package com.amazonaws.mobile.samples.mynotes;

import android.content.Context;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

/**
 * All access to AWS is consolidated into a singleton class
 *
 * Created by mabdira on 1/26/18.
 */

public class AWSProvider {
    private static AWSProvider instance = null;
    private Context context;
    private AWSConfiguration awsConfiguration;
    private PinpointManager pinpointManager;

    private AWSProvider(Context context) {
        this.context = context;
        this.awsConfiguration = new AWSConfiguration(context);

        IdentityManager identityManager = new IdentityManager(context, awsConfiguration);
        IdentityManager.setDefaultIdentityManager(identityManager);
        identityManager.addSignInProvider(CognitoUserPoolsSignInProvider.class);
    }

    public static AWSProvider getInstance() {
        return instance;
    }

    public static void initialize(Context context) {
        if (instance == null)
            instance = new AWSProvider(context);
    }

    public Context getContext() {
        return context;
    }

    public AWSConfiguration getAWSConfiguration() {
        return awsConfiguration;
    }

    public IdentityManager getIdentityManager() {
        return IdentityManager.getDefaultIdentityManager();
    }

    public PinpointManager getPinpointManager() {
        if (pinpointManager == null) {
            final AWSCredentialsProvider cp = getIdentityManager().getCredentialsProvider();
            PinpointConfiguration config = new PinpointConfiguration(getContext(), cp,
                    getAWSConfiguration());
            pinpointManager = new PinpointManager(config);
        }

        return pinpointManager;
    }
}

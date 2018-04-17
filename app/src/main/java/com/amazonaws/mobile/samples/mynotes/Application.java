/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
 * except in compliance with the License. A copy of the License is located at
 *
 *    http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package com.amazonaws.mobile.samples.mynotes;

import android.app.Activity;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Application class responsible for initializing singletons and other
 * common components
 */
public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the AWS Provider
        AWSProvider.initialize(getApplicationContext());

        // Facebook Log App Events via Facebook's Android SDK
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);

        registerActivityLifecycleCallbacks(new ActivityLifeCycle());

    }
}

class ActivityLifeCycle implements android.app.Application.ActivityLifecycleCallbacks {
    private int depth = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (depth == 0) {
            Log.d("ActivityLifeCycle", "Application entered foreground");
            // We need to determine when the user starts the application so that we can send a
            // startSession event to Amazon Pinpoint.
            AWSProvider.getInstance().getPinpointManager().getSessionClient().startSession();
            AWSProvider.getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
        }
        depth++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        depth--;
        if (depth == 0) {
            Log.d("ActivityLifeCycle", "Application entered background");
            // We need to determine when the user stops the application so that we can send
            // a stopSession event to Amazon Pinpoint.
            AWSProvider.getInstance().getPinpointManager().getSessionClient().stopSession();
            AWSProvider.getInstance().getPinpointManager().getAnalyticsClient().submitEvents();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
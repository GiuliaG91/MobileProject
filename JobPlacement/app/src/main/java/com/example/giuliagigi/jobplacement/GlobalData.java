package com.example.giuliagigi.jobplacement;

/**
 * Created by pietro on 20/04/2015.
 */

import android.app.Application;

import com.parse.Parse;

/**
 * Created by pietro on 20/04/2015.
 */
public class GlobalData extends Application {
//this is a class that we should use to store and retrive global data
// it must be used only to set and get from data structures

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");



    }
}

package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

/**
 * Created by MarcoEsposito90 on 13/11/2015.
 */
public class Installation extends ParseInstallation {

    public static final String USER_FIELD = "User";
    private static ParseInstallation currentInstallation = null;


    private Installation(){
        super();
    }


    public static void initialize(){

        if(currentInstallation == null)
            currentInstallation = ParseInstallation.getCurrentInstallation();
    }

    public static void setUser(ParseUserWrapper user){

        currentInstallation.put(USER_FIELD, user);
    }

    public static void commit(){

        currentInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null)
                    Log.println(Log.ASSERT, "INSTALLATION", "save complete");
                else
                    Log.println(Log.ASSERT, "INSTALLATION", "eror while saving: " + e.getMessage());
            }
        });
    }
}

package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 13/11/2015.
 */
public class Installation extends ParseInstallation {

    public static final String USER_FIELD = "User";
    public static final String CHANNELS_FIELD = "channels";
    private static ParseInstallation currentInstallation = null;
    private static ArrayList<String> channels;

    private Installation(){
        super();
    }


    public static void initialize(){

        if(currentInstallation == null){

            currentInstallation = ParseInstallation.getCurrentInstallation();
            channels = new ArrayList<>();

            List<Object> list = currentInstallation.getList(CHANNELS_FIELD);
            if(list != null)
                for (Object o : list){

                    String c = (String)o;
                    Log.println(Log.ASSERT,"INSTALLATION", "retrieved course: " + c);
                    channels.add(c);
                }
            else
                Log.println(Log.ASSERT,"INSTALLATION", "no channels");
        }
    }

    public static void setUser(ParseUserWrapper user){

        currentInstallation.put(USER_FIELD, user);
    }

    public static void addChannel(String channel){

        String c = channel.replaceAll(" ", "");
        if(!channels.contains(c)){

            ParsePush.subscribeInBackground(c);
            channels.add(c);
        }

        Log.println(Log.ASSERT ,"INSTALLATION", "added channel: " + channel);
    }

    public static void removeChannel(String channel){

        String c = channel.replaceAll(" ", "");
        if(channels.contains(c)){

            ParsePush.unsubscribeInBackground(c);
            channels.remove(c);
        }

        Log.println(Log.ASSERT ,"INSTALLATION", "removed channel: " + channel);
    }

    public static void resetChannels(){

        for(String c: channels){

            ParsePush.unsubscribeInBackground(c);
            Log.println(Log.ASSERT, "INSTALLATION", "removing " + c);
        }
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

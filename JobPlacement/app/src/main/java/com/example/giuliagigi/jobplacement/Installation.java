package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.Arrays;
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

    /* ------------------------------------------------------------------------------------------ */
    /* ----------------------- PARSEINSTALLATION WRAPPER ---------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */
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

    public static void removeUser(){
        currentInstallation.remove(USER_FIELD);
    }

    public static void addChannel(String channel){

        String c = channel.replaceAll(" ", "");
        if(!channels.contains(c)){

            ParsePush.subscribeInBackground(c);
            channels.add(c);
            Log.println(Log.ASSERT ,"INSTALLATION", "added channel: " + c);
        }

    }

    public static void removeChannel(String channel){

        String c = channel.replaceAll(" ", "");
        if(channels.contains(c)){

            ParsePush.unsubscribeInBackground(c);
            channels.remove(c);
            Log.println(Log.ASSERT ,"INSTALLATION", "removed channel: " + c);
        }

    }

    public static void resetChannels(){

        for(String c: channels){

            ParsePush.unsubscribeInBackground(c);
            Log.println(Log.ASSERT, "INSTALLATION", "removing " + c);
        }

        channels.clear();
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


    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------- PARSEPUSH WRAPPER ---------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    public static void sendPush(final String channel, String message){

        String c = channel.replaceAll(" ", "");

        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereContainsAll(CHANNELS_FIELD, Arrays.asList(c));

        ParsePush channelPush = new ParsePush();
        channelPush.setQuery(query);
        channelPush.setMessage(message);
        channelPush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                    Log.println(Log.ASSERT, "INSTALLATION", "push ok (" + channel + ")");
                else
                    Log.println(Log.ASSERT, "INSTALLATION", "push error: " + e.getMessage());
            }
        });

        channelPush = null;
    }

    public static void sendPush(ParseUserWrapper user, String message){

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo(USER_FIELD, user);

        ParsePush userPush = new ParsePush();
        userPush.setQuery(pushQuery);
        userPush.setMessage(message);
        userPush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                    Log.println(Log.ASSERT, "INSTALLATION", "notification ok");
                else
                    Log.println(Log.ASSERT, "INSTALLATION", "notification fail");

            }
        });

        userPush = null;
    }

    public static void sendPush(ArrayList<ParseUserWrapper> users, String message){


    }
}

package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 20/04/2015.
 */

@ParseClassName("User")
public class User extends ParseObject{

    protected static final String MAIL_FIELD = "mail";
    private static final String TYPE_FIELD = "type";
    protected static final String PHONE_FIELD = "phones";
    protected static final String PROFILE_PHOTO_FIELD = "profilePhoto";

    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_COMPANY = "Company";


    public static final String[] TYPES = new String[]{TYPE_STUDENT,TYPE_COMPANY};


    protected String mail;
    protected String password;
    protected String type;
    protected ArrayList<Telephone> phones;
    protected Bitmap profilePhoto;
    protected HashMap<String,Boolean> isCached;
    protected HashMap<String,Boolean> isDownloading;


    /* default zero-argument constructor:
     * no parse object field can be modified in it
     */
    public User(){

        super();

        mail = null;
        password = null;
        type = null;
        profilePhoto = null;
        isCached = new HashMap<String,Boolean>();
        isDownloading = new HashMap<String,Boolean>();

        isCached.put(MAIL_FIELD, false);
        isCached.put(TYPE_FIELD, false);
        isCached.put(PHONE_FIELD,false);
        isCached.put(PROFILE_PHOTO_FIELD,false);

        isDownloading.put(PROFILE_PHOTO_FIELD,false);
    }


    /* ------------- GETTERS AND SETTERS ------------------------- */


    public String getMail() {

        if(isCached.get(MAIL_FIELD))
            return mail;

        mail = this.getString(MAIL_FIELD);
        isCached.put(MAIL_FIELD,true);
        return mail;
    }
    public void setMail(String mail){

        this.mail = mail;
        isCached.put(MAIL_FIELD,true);
        this.put(MAIL_FIELD, mail);
    }

    public void addPhone(Telephone telephone){

    }
    public void removePhone(Telephone telephone){

    }
    public ArrayList<Telephone> getPhones(){

        return null;
    }

    public void setPassword(String password) {

        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }

    public String getType() {

        if(isCached.get(TYPE_FIELD))
            return type;

        type = getString(TYPE_FIELD);
        isCached.put(TYPE_FIELD,true);
        return type;
    }
    public void setType(String type) {

        this.type = type;
        isCached.put(TYPE_FIELD,true);
        this.put(TYPE_FIELD, type);
    }

    public void setProfilePhoto(Bitmap photoBitmap){

        profilePhoto = photoBitmap;
        isCached.put(PROFILE_PHOTO_FIELD,true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
        byte[] photoByteArray = os.toByteArray();
        final ParseFile photoFile = new ParseFile("profilePicture.jpg", photoByteArray);

        photoFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                User.this.put(PROFILE_PHOTO_FIELD, photoFile);
            }
        });
    }
    public Bitmap getProfilePhoto(){

        if(!isCached.get(PROFILE_PHOTO_FIELD) && !isDownloading.get(PROFILE_PHOTO_FIELD)){

            ParseFile file = (ParseFile)get(PROFILE_PHOTO_FIELD);

            if(file == null)
                return null;

            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {

                    profilePhoto = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    isCached.put(PROFILE_PHOTO_FIELD,true);
                    isDownloading.put(PROFILE_PHOTO_FIELD,true);
                }
            },
                new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {

                        if(!isDownloading.get(PROFILE_PHOTO_FIELD))
                            isDownloading.put(PROFILE_PHOTO_FIELD,true);
                    }
                });
        }

        return profilePhoto;
    }


    public void cacheData(){

        Log.println(Log.ASSERT,"USER", "caching!");
        getMail();
        getType();
        getProfilePhoto();
    }
    public boolean isCachingNeeded(){

        return isCached.containsValue(false);
    }
    public void printCacheStatus(){

        for(String key:isCached.keySet()){

            Log.println(Log.ASSERT, "USER", key + ": " + isCached.get(key));
        }
    }

}



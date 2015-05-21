package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 20/04/2015.
 */

@ParseClassName("User")
public class User extends ParseObject{

    protected static final String MAIL_FIELD = "mail";
    public static final String TYPE_FIELD = "type";
    protected static final String PHONE_FIELD = "phones";
    protected static final String TAG_FIELD = "tags";
    protected static final String PROFILE_PHOTO_FIELD = "profilePhoto";
    protected static final String NAME_FIELD = "name";

    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_COMPANY = "Company";
    public static final String TYPE_SELECT = "Select a type";

    public static final String TYPE_STUDENT_TRANSLATED = GlobalData.getContext().getString(R.string.string_type_student);
    public static final String TYPE_COMPANY_TRANSLATED = GlobalData.getContext().getString(R.string.string_type_company);
    public static final String TYPE_SELECT_TRANSLATED = GlobalData.getContext().getString(R.string.string_select_type);

    public static final HashMap<String, String> USER_TYPES = new HashMap<>();
    public static final String[] TYPES = new String[]{TYPE_SELECT_TRANSLATED,TYPE_STUDENT_TRANSLATED,TYPE_COMPANY_TRANSLATED};


    protected String mail;
    protected String password;
    protected String type;
    protected ArrayList<Telephone> phones;
    protected HashMap<String,Tag> tags;
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
        tags = new HashMap<String,Tag>();
        isCached = new HashMap<String,Boolean>();
        isDownloading = new HashMap<String,Boolean>();

        isCached.put(MAIL_FIELD, false);
        isCached.put(TYPE_FIELD, false);
        isCached.put(PHONE_FIELD,false);
        isCached.put(PROFILE_PHOTO_FIELD,false);
        isCached.put(TAG_FIELD,false);

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

    public void addPhone(Telephone phone){

        phones.add(phone);
        getRelation(PHONE_FIELD).add(phone);
    }
    public void removePhone(Telephone phone) {

        phones.remove(phone);
        getRelation(PHONE_FIELD).remove(phone);
    }
    public ArrayList<Telephone> getPhones(){

        if(isCached.get(PHONE_FIELD))
            return phones;


        ParseRelation<Telephone> tmp = getRelation(PHONE_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Telephone>() {
            @Override
            public void done(List<Telephone> results, ParseException e) {

                if(results!= null)
                    for(Telephone t:results)
                        phones.add(t);
            }
        });

        isCached.put(PHONE_FIELD,true);
        return phones;
    }

    public void addTag(Tag tag){

        tags.put(tag.getTag(),tag);
        getRelation(TAG_FIELD).add(tag);
    }
    public void removeTag(Tag tag){

        tags.remove(tag.getTag());
        getRelation(TAG_FIELD).remove(tag);
    }
    public HashMap<String,Tag> getTags(){

        if(isCached.get(TAG_FIELD))
            return tags;


        ParseRelation<Tag> tmp = getRelation(TAG_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Tag>() {
            @Override
            public void done(List<Tag> results, ParseException e) {

                if(results!= null)
                    for(Tag t:results)
                        tags.put(t.getTag(),t);
            }
        });

        isCached.put(TAG_FIELD,true);
        return tags;
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

        //String typeTranslated = (String)getKeyByValue(USER_TYPES, type);
        this.type = type;
        isCached.put(TYPE_FIELD,true);
        this.put(TYPE_FIELD, type);
    }

    public void setProfilePhoto(Bitmap photoBitmap , final GlobalData globalData){

        profilePhoto = photoBitmap;
        isCached.put(PROFILE_PHOTO_FIELD,true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        photoBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
        byte[] photoByteArray = os.toByteArray();
        final ParseFile photoFile = new ParseFile("profilePicture.jpg", photoByteArray);

        photoFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                Log.println(Log.ASSERT, "USER", "profile photo upload completed");
                User.this.put(PROFILE_PHOTO_FIELD, photoFile);
                Log.println(Log.ASSERT, "USER", "user updated");
                globalData.getmAdapter().notifyDataSetChanged();
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
        getTags();
        getPhones();
    }
    public boolean isCachingNeeded(){

        return isCached.containsValue(false);
    }
    public void printCacheStatus(){

        for(String key:isCached.keySet()){

            Log.println(Log.ASSERT, "USER", key + ": " + isCached.get(key));
        }
    }

    public void printCacheContent(){

        Log.println(Log.ASSERT,"USER", "mail: " + mail);

        for(Telephone t:phones)
            Log.println(Log.ASSERT,"USER", "telephone: " + t.getNumber());

    }

    public String getName(){
        return this.getString(User.NAME_FIELD);
    }

    public void setName(String name){
        this.put(User.NAME_FIELD, name);
    }

    public static void initializeLangauges(){

        USER_TYPES.put(TYPE_SELECT, TYPE_SELECT_TRANSLATED);
        USER_TYPES.put(TYPE_STUDENT, TYPE_STUDENT_TRANSLATED);
        USER_TYPES.put(TYPE_COMPANY, TYPE_COMPANY_TRANSLATED);
    }

    public static Object getKeyByValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


}



package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;


import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pietro on 20/04/2015.
 */
public class GlobalData extends Application {

    /*This is a a class that should be used to store global data
    it is also very useful to store data that should go from one activity to another
    NOTA: in this class shuld only put data structures and their setter and getter
     */


    private ParseUserWrapper currentUser;
    private User currentUserObject;
    private User latestDisplayedUser;
    Toolbar toolbar;
    CompanyOffer currentViewOffer;
    private HashMap<String,Tag> tags;
    private HashMap<String,Boolean> isCached;
    private SharedPreferences sharedPreferences;
    private InboxMessage currentViewMessage;
    private static Context applicationContext;
    private String toolbarTitle;


    private menuAdapter mAdapter=null;



    /* managing profile management display rotation*/
    private HashMap<String,MyBundle> bundles;
    private HashMap<String,Fragment> fragments;


    /************FILTERS***************/
    private OfferFilterStatus offerFilterStatus=new OfferFilterStatus();
    private StudentFilterStatus studentFilterStatus=new StudentFilterStatus();
    private CompanyFilterStatus companyFilterStatus=new CompanyFilterStatus();

    /***********************************/
    
    /*******TIMETABLE**********/
    private LecturesFileReader lecturesFileReader;
    private RoomsFileReader roomsFileReader;
    volatile private boolean isLectureReadComplete;
    private static AssetManager assetManager;
    
    

    @Override
    public void onCreate() {
        super.onCreate();


        applicationContext = getApplicationContext();
        assetManager = applicationContext.getAssets();



        // reading rooms json file in a secondary thread ------------------------------------------
        roomsFileReader = new RoomsFileReader();
        AsyncTask<Void, Boolean, Void> roomsFileReadingTask = new AsyncTask<Void, Boolean, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                roomsFileReader.readRooms();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };
        roomsFileReadingTask.execute();
        // ----------------------------------------------------------------------------------------

//        currentUser = null;
        currentUserObject = null;
        currentViewMessage = null;
        latestDisplayedUser = null;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(ParseUserWrapper.class);
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Office.class);
        ParseObject.registerSubclass(Degree.class);
        ParseObject.registerSubclass(Language.class);
        ParseObject.registerSubclass(Telephone.class);
        ParseObject.registerSubclass(CompanyOffer.class);
        ParseObject.registerSubclass(Tag.class);
        ParseObject.registerSubclass(Withdrawal.class);
        ParseObject.registerSubclass(Certificate.class);
        ParseObject.registerSubclass(InboxMessage.class);
        ParseObject.registerSubclass(InboxMessageReceived.class);
        ParseObject.registerSubclass(InboxMessageSent.class);
        ParseObject.registerSubclass(News.class);
        ParseObject.registerSubclass(StudentApplication.class);
        ParseObject.registerSubclass(Professor.class);
        ParseObject.registerSubclass(Course.class);
        ParseObject.registerSubclass(Lecture.class);


        Degree.initializeLangauges();
        Office.initializeLanguage();
        Language.initializeLangauges();
        Telephone.initializeLangauges();
        User.initializeLangauges();
        StudentApplication.initializeLangauges();


        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");

        // reading lectures DB in a secondary thread ---------------------------------------
        lecturesFileReader = new LecturesFileReader();
        isLectureReadComplete = false;
        AsyncTask<Void, Boolean, Void> lecturesDBeReadingTask = new AsyncTask<Void, Boolean, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                lecturesFileReader.readFromDB();
                isLectureReadComplete = true;
//                lecturesFileReader.getCourseNames();
//                lecturesFileReader.getProfessorNames();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };
        lecturesDBeReadingTask.execute();
        // ----------------------------------------------------------------------------------------


        isCached = new HashMap<String,Boolean>();
        tags = new HashMap<String,Tag>();
        isCached.put("tag",false);
        getTags();

        bundles = new HashMap<String,MyBundle>();
        fragments = new HashMap<>();
    }



    /* ------------------ USER METHODS -----------------------------------------------------------*/

    public ParseUserWrapper getCurrentUser() {

        currentUser = (ParseUserWrapper)ParseUser.getCurrentUser();

        if(currentUser != null){

            try {
                currentUser.fetchIfNeeded();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return  currentUser;
    }

    public User getUserObject(){

        getCurrentUser();

        if(currentUser != null){

            currentUserObject = currentUser.getUser();

            try {
                currentUserObject.fetchIfNeeded();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            currentUserObject.cacheData();
            return currentUserObject;
        }

        return null;
    }


    /* ------------------ TAGS CACHING -----------------------------------------------------------*/

    public HashMap<String,Tag> getTags(){

        if(isCached.get("tag"))
            return tags;

        ParseQuery<Tag> query=ParseQuery.getQuery(Tag.class);
        query.findInBackground(new FindCallback<Tag>() {
            @Override
            public void done(List<Tag> tagList, ParseException e) {

                for (Tag t : tagList)
                    tags.put(t.getTag(), t);
                isCached.put("tag", true);
            }
        });

        return tags;
    }

    /* ----------------------- MANAGING PREFERENCES ---------------------------------------------*/

    public SharedPreferences getSharedPreferences(){

        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences){

        this.sharedPreferences = sharedPreferences;
    }

    public void setToolbar(Toolbar t)
    {
        toolbar=t;
    }
    public Toolbar getToolbar()
    {
        return toolbar;
    }

    /***********************************************************/


    public OfferFilterStatus getOfferFilterStatus() {
        return offerFilterStatus;
    }
    public StudentFilterStatus getStudentFilterStatus(){return  studentFilterStatus;}
    public CompanyFilterStatus getCompanyFilterStatus() {
        return companyFilterStatus;
    }


    /* ----------------- BUNDLE --------------------------------------------------------- */

    public MyBundle addBundle(String key){

        if(bundles.containsKey(key))
            bundles.remove(key);

        MyBundle b = new MyBundle();
        bundles.put(key, b);
        return b;
    }

    public MyBundle getBundle(String key){

        return bundles.get(key);
    }

    public void removeBundle(String key){

        if(!bundles.containsKey(key))
            return;

        MyBundle toBeRemoved = bundles.get(key);
        bundles.remove(toBeRemoved);
    }

    public void registerFragment(String name, Fragment fragment) {

        if(fragments.containsKey(name))
            fragments.remove(name);

        fragments.put(name, fragment);
    }

    public Fragment getFragment(String name) {
        return fragments.get(name);
    }

    public void setLatestDisplayedUser(User latestDisplayedUser) {
        this.latestDisplayedUser = latestDisplayedUser;
    }

    public User getLatestDisplayedUser() {
        return latestDisplayedUser;
    }

    public static Context getContext() {
        return applicationContext;

    }


    public menuAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(menuAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }



    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }
    
    public LecturesFileReader getLecturesFileReader(){
        
        return lecturesFileReader;
    }
    
    public static AssetManager getAssetManager() {
        return assetManager;
    }


    public RoomsFileReader getRoomsFileReader() {
        return roomsFileReader;
    }

    public ArrayList<String> getRoomNames(){
        return roomsFileReader.getRoomNames();
    }
}


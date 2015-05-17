package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


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
    Toolbar toolbar;
    CompanyOffer currentViewOffer;
    private HashMap<String,Tag> tags;
    private HashMap<String,Boolean> isCached;
    private SharedPreferences loginPreferences;
    private InboxMessage currentViewMessage;
    private static Context applicationContext;

    /* managing profile management display rotation*/
    private HashMap<String,MyBundle> bundles;
    private ProfileManagementViewAdapter profileManagementViewAdapter;

    /**************NEW OFFER BUNDLE**********/
    private Bundle offerBundle;
    private ArrayList<Tag> tagBoundle=new ArrayList<>();
    /******************************************/
    /************FILTERS***************/
    private OfferFilterStatus offerFilterStatus=new OfferFilterStatus();
    /***********************************/

    @Override
    public void onCreate() {
        super.onCreate();


        applicationContext = getApplicationContext();
//        currentUser = null;
        currentUserObject = null;
        currentViewMessage = null;

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

        Degree.initializeLangauges();
        Office.initializeLanguage();
        Language.initializeLangauges();
        Telephone.initializeLangauges();

        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");

        isCached = new HashMap<String,Boolean>();
        tags = new HashMap<String,Tag>();
        isCached.put("tag",false);
        getTags();

        bundles = new HashMap<String,MyBundle>();
    }



    /* ------------------ USER METHODS -----------------------------------------------------------*/

    public ParseUserWrapper getCurrentUser() {

        currentUser = (ParseUserWrapper)ParseUser.getCurrentUser();

        if(currentUser!=null)
            try {
                currentUserObject = currentUser.getUser().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        else
            Log.println(Log.ASSERT,"GLOBAL DATA", "currentUser is null");

        return  currentUser;
    }
    public Student getStudentFromUser(){

        getCurrentUser();

        if(currentUser==null)
            return null;

        if(currentUserObject == null){

            try {
                currentUserObject = getCurrentUser().getUser().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.println(Log.ASSERT,"GLOBAL DATA", "error fetching user info");
                return null;
            }
        }

        return (Student)currentUserObject;
    }

    public Company getCompanyFromUser(){

        getCurrentUser();
        if (currentUser == null)
            return null;

        if(currentUserObject == null){

            try {
                currentUserObject = getCurrentUser().getUser().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
                Log.println(Log.ASSERT,"GLOBAL DATA", "error fetching user info");
                return null;
            }
        }

        return (Company)currentUserObject;
    }

    public User getUserObject(){

        if(getCurrentUser().getType().equals(User.TYPE_STUDENT))
            return getStudentFromUser();
        else
            return getCompanyFromUser();
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

    public SharedPreferences getLoginPreferences(){

        return loginPreferences;
    }

    public void setLoginPreferences(SharedPreferences loginPreferences){

        this.loginPreferences = loginPreferences;
    }




    public void setToolbar(Toolbar t)
    {
        toolbar=t;
    }
    public Toolbar getToolbar()
    {
        return toolbar;
    }


    public void setCurrentOffer(CompanyOffer o)
    {
        currentViewOffer=o;
    }
    public CompanyOffer getCurrentViewOffer()
    {
        return currentViewOffer;
    }

    /***********************************************************/

    public Bundle getOfferBundle() {
        return offerBundle;
    }

    public void setOfferBundle(Bundle offerBundle) {
        this.offerBundle = offerBundle;
    }

    public ArrayList<Tag> getTagBoundle() {
        return tagBoundle;
    }

    public void setTagBoundle(ArrayList<Tag> tagBoundle) {
        this.tagBoundle = tagBoundle;
    }

    public OfferFilterStatus getOfferFilterStatus() {
        return offerFilterStatus;
    }


    /* ---------------- INBOX MESSAGES -----------------------------------------------------------*/

    public InboxMessage getCurrentViewMessage(){
        return currentViewMessage;
    }

    public void setCurrentViewMessage(InboxMessage m){
        this.currentViewMessage = m;
    }


    /* ----------------- PROFILE BUNDLE --------------------------------------------------------- */

    public void setProfileManagementViewAdapter(ProfileManagementViewAdapter profileManagementViewAdapter){
        this.profileManagementViewAdapter = profileManagementViewAdapter;
    }

    public MyBundle addBundle(String key){

        if(bundles.containsKey(key))
            return bundles.get(key);

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

    public void clearProfileBundles(){

        if(profileManagementViewAdapter!= null){

            Log.println(Log.ASSERT,"GLOBAL DATA", "Proceed deleting bundles");

            for(ProfileManagementFragment f:profileManagementViewAdapter.getFragments())
                removeBundle(f.getBundleID());
        }

    }

    public static Context getContext() {
        return applicationContext;
    }

}


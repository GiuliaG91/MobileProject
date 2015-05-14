package com.example.giuliagigi.jobplacement;

import android.app.Application;
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

import bolts.Task;

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

        currentUser = null;
        currentUserObject = null;

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

        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");

        isCached = new HashMap<String,Boolean>();
        tags = new HashMap<String,Tag>();
        isCached.put("tag",false);
        getTags();
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



//        if(currentUser.getType().equals(User.TYPE_COMPANY))
//            return null;
//
//        ParseQuery<Student> studentQuery = ParseQuery.getQuery(Student.class);
//        studentQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
//        Student result = null;
//        try {
//            result = (Student)studentQuery.find().get(0);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return result;
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
//        if(currentUser.getType().equals(User.TYPE_STUDENT))
//            return null;
//
//        ParseQuery<Company> companyQuery = ParseQuery.getQuery(Company.class);
//        companyQuery.whereEqualTo(User.MAIL_FIELD, currentUser.getEmail());
//        Company result = null;
//        try {
//            result = (Company)companyQuery.find().get(0);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return result;
    }
    public User getUserObject(){

        if(getCurrentUser().getType().equals(User.TYPE_STUDENT))
            return getStudentFromUser();
        else
            return getCompanyFromUser();

//        if(currentUser == null)
//            return null;

//        if(currentUserObject == null){
//
//            if(currentUser.getType().equals((User.TYPE_STUDENT))){
//
//                ParseQuery<Student> studentQuery = ParseQuery.getQuery(Student.class);
//                studentQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
//                    studentQuery.findInBackground(new FindCallback<Student>() {
//                        @Override
//                        public void done(List<Student> students, ParseException e) {
//
//                            if(e == null)
//                                if(students!= null)
//                                    currentUserObject = students.get(0);
//                        }
//                    });
//            }
//            else {
//
//                ParseQuery<Company> companyQuery = ParseQuery.getQuery(Company.class);
//                companyQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
//                try {
//                    currentUserObject = companyQuery.find().get(0);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if(currentUserObject.isCachingNeeded())
//            currentUserObject.cacheData();
//
//        return currentUserObject;
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

}


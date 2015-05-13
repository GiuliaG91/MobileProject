package com.example.giuliagigi.jobplacement;

import android.app.Application;
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

    /*********STATE OF PAGES***************/
    private int home_student_position=0; //default
    private int home_company_position=0; //default
    private int fav_position=-1;      //default
    private int applies_position=-1;   //default
    /***************************************/

    /**************NEW OFFER BUNDLE**********/
    private Bundle offerBundle;

    private ArrayList<Tag> tagBoundle=new ArrayList<>();
    /******************************************/


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

        if(currentUser == null)
            currentUser = (ParseUserWrapper)ParseUser.getCurrentUser();
        return  currentUser;
    }
    public Student getStudentFromUser(){

        if(currentUser.getType().equals(User.TYPE_COMPANY))
            return null;

        ParseQuery<Student> studentQuery = ParseQuery.getQuery(Student.class);
        studentQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
        Student result = null;
        try {
            result = (Student)studentQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Company getCompanyFromUser(){

        if(currentUser.getType().equals(User.TYPE_STUDENT))
            return null;

        ParseQuery<Company> companyQuery = ParseQuery.getQuery(Company.class);
        companyQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
        Company result = null;
        try {
            result = (Company)companyQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public User getUserObject(){

        if(currentUserObject == null){

            if(currentUser.getType().equals((User.TYPE_STUDENT))){

                ParseQuery<Student> studentQuery = ParseQuery.getQuery(Student.class);
                studentQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
                try {
                    currentUserObject = studentQuery.find().get(0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else {

                ParseQuery<Company> companyQuery = ParseQuery.getQuery(Company.class);
                companyQuery.whereEqualTo(User.MAIL_FIELD,currentUser.getEmail());
                try {
                    currentUserObject = companyQuery.find().get(0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if(currentUserObject.isCachingNeeded())
            currentUserObject.cacheData();

        return currentUserObject;
    }


    /* ------------------ TAGS CACHING -----------------------------------------------------------*/

    public HashMap<String,Tag> getTags(){

        if(isCached.get("tag"))
            return tags;

        ParseQuery<Tag> query=ParseQuery.getQuery(Tag.class);
        query.findInBackground(new FindCallback<Tag>() {
            @Override
            public void done(List<Tag> tagList, ParseException e) {

                for(Tag t:tagList)
                    tags.put(t.getTag(),t);
                isCached.put("tag",true);
            }
        });

        return tags;
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





    /****************GETTER AND SETTER FOR STATES***********/
    public int getHome_student_position() {
        return home_student_position;
    }

    public int getHome_company_position() {
        return home_company_position;
    }

    public void setHome_student_position(int home_student_position) {
        this.home_student_position = home_student_position;
    }

    public void setHome_company_position(int home_company_position) {
        this.home_company_position = home_company_position;
    }

    public int getFav_position() {
        return fav_position;
    }

    public void setFav_position(int fav_position) {
        this.fav_position = fav_position;
    }

    public int getApplies_position() {
        return applies_position;
    }

    public void setApplies_position(int applies_position) {
        this.applies_position = applies_position;
    }
    //item_view state
    public void resetState(){
        fav_position=-1;
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


}


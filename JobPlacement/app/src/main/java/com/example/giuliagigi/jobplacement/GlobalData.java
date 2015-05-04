package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

    @Override
    public void onCreate() {
        super.onCreate();

        currentUser = null;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(ParseUserWrapper.class);
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Degree.class);
        ParseObject.registerSubclass(Language.class);
        ParseObject.registerSubclass(Telephone.class);
        ParseObject.registerSubclass(CompanyOffer.class);
        ParseObject.registerSubclass(Tag.class);

        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");
    }

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
}

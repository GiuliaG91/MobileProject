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



    /* methods to perform login:
        1: returns true if login is successful
        2: updates the value of the current user attribute
     */
    public void performLogin(String mail, String password) throws Exception{

        ParseUser.logInInBackground(mail,password,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if(e == null){

                    Log.println(Log.ASSERT,"GLOBAL DATA", "login ok");
                }
                else {

                    Log.println(Log.ASSERT,"GLOBAL DATA", "login failed");
                    try {
                        throw e;
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


//        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
//        userQuery.whereEqualTo(User.MAIL_FIELD,mail);
//
//        try{
//
//            List<User> result = userQuery.find();
//
//            if(result.size() == 1){
//
//                User user = result.get(0);
//                Log.println(Log.ASSERT,"LOGIN","user: " + result.get(0).getObjectId());
//
//                if(user.getPassword().equals(password)){
//
//                    Log.println(Log.ASSERT,"LOGIN","password matches");
//                    currentUser = user;
//                    return true;
//                }
//                else {
//
//                    Log.println(Log.ASSERT,"LOGIN","password doesn't match");
//                    return false;
//                }
//            }
//            else {
//
//                Log.println(Log.ASSERT,"LOGIN","not found");
//                return false;
//            }
//        }
//        catch (ParseException e) {
//            e.printStackTrace();
//            Log.println(Log.ASSERT,"LOGIN","Parse Exception");
//        }
//
    }
    

    /*method to register a new account:
            - returns false if the operation wasn't possibile (for example,
              the mail might be used in another existing account)
            - returns true if the operation was successful
     */
    public void registerNewAccount(User newUser) throws Exception{

        Student newStudent = null;
        Company newCompany = null;

        if(newUser.getType().equals(User.TYPE_STUDENT)){
            Log.println(Log.ASSERT,"GLOBAL DATA", "registering a student");
            newStudent = (Student)newUser;
            newStudent.saveInBackground();
        }
        else if(newUser.getType().equals(User.TYPE_COMPANY)){
            Log.println(Log.ASSERT,"GLOBAL DATA", "registering a company");
            newCompany = (Company)newUser;
            newCompany.saveInBackground();
        }
        else{
            Log.println(Log.ASSERT,"GLOBAL DATA","Error: unknown type");
            throw new Exception("invalid type of user");
        }

        ParseUserWrapper newParseUser = new ParseUserWrapper();
        newParseUser.setEmail(newUser.getMail());
        newParseUser.setPassword(newUser.getPassword());
        newParseUser.setUsername(newUser.getUsername());
        newParseUser.setType(newUser.getType());
        try{
            newParseUser.signUp();
        }
        catch (ParseException e){

            Log.println(Log.ASSERT, "GLOBAL DATA", "error: " + e.getMessage());
            if(newStudent!= null)
                newStudent.delete();
            if(newCompany != null)
                newCompany.delete();

            throw e;
        }


        /* 1 - verify we don't have other users with same mail */
//        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
//        userQuery.whereEqualTo(User.MAIL_FIELD,newUser.getMail());
//
//        try {
//
//            List<User> result = userQuery.find();
//
//            if(result.size()>0){
//
//                Log.println(Log.ASSERT,"GLOBAL DATA", "this account already exists");
//                return false;
//            }
//            else {
//
//                /* 2 - can proceed with registration */
//
//                if(newUser.getType().equals(User.TYPE_STUDENT)){
//
//                    Log.println(Log.ASSERT,"GLOBAL DATA", "registering a student");
//                    Student newStudent = (Student)newUser;
//                    newStudent.saveInBackground();
//                    registerAsUser(newStudent);
//                    return true;
//                }
//                else if(newUser.getType().equals(User.TYPE_COMPANY)){
//
//                    Log.println(Log.ASSERT,"GLOBAL DATA", "registering a company");
//                    Company newCompany = (Company)newUser;
//                    newCompany.saveInBackground();
//                    registerAsUser(newCompany);
//                    return true;
//                }
//                else{
//
//                    Log.println(Log.ASSERT,"GLOBAL DATA","Error: unknown type");
//                    return false;
//                }
//
//            }
//
//        } catch (ParseException e) {
//
//            e.printStackTrace();
//        }
//
    }

//    private void registerAsUser(User user){
//
//        User newUser = new User();
//        newUser.setMail(user.getMail());
//        newUser.setPassword(user.getPassword());
//        newUser.setType(user.getType());
//        newUser.saveInBackground();
//    }

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

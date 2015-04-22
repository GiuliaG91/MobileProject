package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by pietro on 20/04/2015.
 */
public class GlobalData extends Application {

    /*This is a a class that should be used to store global data
    it is also very useful to store data that should go from one activity to another
    NOTA: in this class shuld only put data structures and their setter and getter
     */

    private User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        currentUser = null;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Degree.class);
        Parse.initialize(this, "EICiUy2eT7CZPXw8N6I1p6lE4844svLI73JTc2QY", "8I9HZ7AgMHgeIxQKk8k653jNBvBCz57nRuSH73pA");


        /** PROVA:
         *  trying to check if works

        User fakeUser = new User();
        fakeUser.setMail("fake.user@null.com");
        fakeUser.setPassword("mypassword");
        fakeUser.setType(User.TYPE_STUDENT);
        fakeUser.saveInBackground();
        */
    }



    /* methods to perform login:
        1: returns true if login is successful
        2: updates the value of the current user attribute
     */
    public boolean performLogin(String mail, String password){

        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
        userQuery.whereEqualTo(User.MAIL_FIELD,mail);

        try{

            List<User> result = userQuery.find();

            if(result.size() == 1){

                User user = result.get(0);
                Log.println(Log.ASSERT,"LOGIN","user: " + result.get(0).getObjectId());

                if(user.getPassword().equals(password)){

                    Log.println(Log.ASSERT,"LOGIN","password matches");
                    currentUser = user;
                    return true;
                }
                else {

                    Log.println(Log.ASSERT,"LOGIN","password doesn't match");
                    return false;
                }
            }
            else {

                Log.println(Log.ASSERT,"LOGIN","not found");
                return false;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.println(Log.ASSERT,"LOGIN","Parse Exception");
        }

        return false;
    }
    

    /*method to register a new account:
            - returns false if the operation wasn't possibile (for example,
              the mail might be used in another existing account)
            - returns true if the operation was successful
     */
    public boolean registerNewAccount(User newUser){

        /* 1 - verify we don't have other users with same mail */

        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
        userQuery.whereEqualTo(User.MAIL_FIELD,newUser.getMail());

        try {

            List<User> result = userQuery.find();

            if(result.size()>0){

                Log.println(Log.ASSERT,"GLOBAL DATA", "this account already exists");
                return false;
            }
            else {

                /* 2 - can proceed with registration */

                if(newUser.getType().equals(User.TYPE_STUDENT)){

                    Log.println(Log.ASSERT,"GLOBAL DATA", "registering a student");
                    Student newStudent = (Student)newUser;
                    newStudent.saveInBackground();
                    registerAsUser(newStudent);
                    return true;
                }
                else if(newUser.getType().equals(User.TYPE_COMPANY)){

                    Log.println(Log.ASSERT,"GLOBAL DATA", "registering a company");
                    Company newCompany = (Company)newUser;
                    newCompany.saveInBackground();
                    registerAsUser(newCompany);
                    return true;
                }
                else{

                    Log.println(Log.ASSERT,"GLOBAL DATA","Error: unknown type");
                    return false;
                }

            }

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return false;
    }

    private void registerAsUser(User user){

        User newUser = new User();
        newUser.setMail(user.getMail());
        newUser.setPassword(user.getPassword());
        newUser.setType(user.getType());
        newUser.saveInBackground();
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

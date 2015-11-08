package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import bolts.Task;

/**
 * Created by MarcoEsposito90 on 04/05/2015.
 */
@ParseClassName("_User")
public class ParseUserWrapper extends ParseUser {

    protected static final String TYPE_FIELD = "type";
    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_COMPANY = "Company";
    public static final String USER_FIELD = "user";
    private static final String STUDENT_FIELD = "student";
    private static final String COMPANY_FIELD = "company";
    private static final String PROFESSOR_FIELD = "professor";
    protected static final String EMAIL_VERIFIED_FIELD = "emailVerified";

    public static final String[] TYPES = new String[]{TYPE_STUDENT,TYPE_COMPANY};

    public ParseUserWrapper() {
        super();
    }

    public void setEmail(String email){
        super.setEmail(email);
    }
    public String getEmail(){
        return super.getEmail();
    }

    public void setUsername(String email){
        super.setUsername(email);
    }
    public String getUsername(){
        return super.getUsername();
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

    public String getType() {
        return getString(TYPE_FIELD);
    }
    public void setType(String type) {
        this.put(TYPE_FIELD, type);
    }

    public User getUser(){

        User u = null;
        if(getType().equals(User.TYPE_STUDENT)){

             u = (User)get(STUDENT_FIELD);
        }
        else if (getType().equals(User.TYPE_COMPANY)){

            u = (User)get(COMPANY_FIELD);
        }
        else if (getType().equals(User.TYPE_PROFESSOR)){

            u = (User)get(PROFESSOR_FIELD);
        }

        try {
            u.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.println(Log.ASSERT, "PARSEUSWRAP", "error fetching user object");
        }
        return u;
    }

    public void setUser(User user){

        if(getType().equals(User.TYPE_STUDENT)){

            Student studentUser;
            try {
                studentUser = (Student)user;
            }
            catch (ClassCastException e){

                e.printStackTrace();
                return;
            }
            put(STUDENT_FIELD,studentUser);

        }
        else if(getType().equals(User.TYPE_COMPANY)){

            Company companyUser;

            try{

                companyUser = (Company)user;
            }
            catch (ClassCastException e){

                e.printStackTrace();
                return;
            }

            put(COMPANY_FIELD,companyUser);

        }
        else if(getType().equals(User.TYPE_PROFESSOR)){

            Professor professorUser;

            try{

                professorUser = (Professor)user;
            }
            catch (ClassCastException e){

                e.printStackTrace();
                return;
            }

            put(PROFESSOR_FIELD,professorUser);
        }
    }

    public boolean isEmailVerified(){
        return this.getBoolean(EMAIL_VERIFIED_FIELD);
    }

}

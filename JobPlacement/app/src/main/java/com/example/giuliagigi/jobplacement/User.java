package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 20/04/2015.
 */

@ParseClassName("User")

public class User extends ParseObject{

    protected static final String MAIL_FIELD = "mail";
    protected static final String PASSWORD_FIELD = "password";
    protected static final String TYPE_FIELD = "type";

    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_COMPANY = "Company";

    public static final String[] TYPES = new String[]{TYPE_STUDENT,TYPE_COMPANY};
    /** default zero-argument constructor:
     * no parse object field can be modified in it
     */
    public User(){ super(); }


    /* ------------- GETTERS AND SETTERS ------------------------- */


    public String getMail() {
        return getString(MAIL_FIELD);
    }

    public void setMail(String mail){
        this.put(MAIL_FIELD, mail);
    }


    public void setPassword(String password) {
        this.put(PASSWORD_FIELD, password);
    }

    public String getPassword() {
        return getString(PASSWORD_FIELD);
    }


    public String getType() {
        return getString(TYPE_FIELD);
    }

    public void setType(String type) {
        this.put(TYPE_FIELD, type);
    }
}



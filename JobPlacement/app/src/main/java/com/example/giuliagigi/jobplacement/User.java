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
    private static final String TYPE_FIELD = "type";

    /* it is an external key for a second relation:
            - accesses the relation "Students" in the DB if the type is student
            - otherwise accesses "Companies"
     */
    private static final String ACCOUNT_ID_FIELD = "account_id";

    public static final String TYPE_STUDENT = "Student";
    public static final String TYPE_COMPANY = "Company";

    public static final String[] TYPES = new String[]{TYPE_STUDENT,TYPE_COMPANY};
    /* default zero-argument constructor:
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

    public void setAccountId(String id){
        this.put(ACCOUNT_ID_FIELD,id);
    }

    public String getAccountID(){
        return this.getString(ACCOUNT_ID_FIELD);
    }
}



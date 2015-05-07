package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by MarcoEsposito90 on 01/05/2015.
 */

@ParseClassName("Telephone")
public class Telephone extends ParseObject{

    public static String NUMBER_FIELD = "number";
    public static String TYPE_FIELD = "type";
    public static String USER_FIELD = "user";

    public static String TYPE_CELLULAR = "Cellular";
    public static String TYPE_HOME = "Home";
    public static String TYPE_OFFICE = "Office";
    public static String TYPE_FAX= "Fax";
    public static String TYPE_OTHER = "Other";
    public static String[] TYPES = new String[]{TYPE_CELLULAR,TYPE_HOME,TYPE_OFFICE,TYPE_FAX,TYPE_OTHER};

    public Telephone(){
        super();
    }

    public void setNumber(String number){
        this.put(NUMBER_FIELD,number);
    }
    public String getNumber(){
        return this.getString(NUMBER_FIELD);
    }

    public void setType(String type){
        this.put(TYPE_FIELD,type);
    }
    public String getType(){
        return this.getString(TYPE_FIELD);
    }

    public void setUser(User owner){
        this.put(USER_FIELD,owner);
    }
    public User getUser(){
        return (User)this.get(USER_FIELD);
    }

    public static int getTypeID(String type){

        for(int i=0;i<TYPES.length;i++)
            if(type.equals(TYPES[i]))
                return i;

        return -1;
    }


}

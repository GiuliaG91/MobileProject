package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

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
    public static String TYPE_CELLULAR_TRANSLATED = GlobalData.getContext().getString(R.string.string_telephone_type_cellular);
    public static String TYPE_HOME_TRANSLATED = GlobalData.getContext().getString(R.string.string_telephone_type_home);
    public static String TYPE_OFFICE_TRANSLATED = GlobalData.getContext().getString(R.string.string_telephone_type_office);
    public static String TYPE_FAX_TRANSLATED = GlobalData.getContext().getString(R.string.string_telephone_type_fax);
    public static String TYPE_OTHER_TRANSLATED = GlobalData.getContext().getString(R.string.string_telephone_type_other);
    public static HashMap<String, String> TELEPHONE_TYPES = new HashMap<>();
    public static String[] TYPES = new String[]{TYPE_CELLULAR_TRANSLATED,TYPE_HOME_TRANSLATED,TYPE_OFFICE_TRANSLATED,TYPE_FAX_TRANSLATED,TYPE_OTHER_TRANSLATED};

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
        String typeTranslated = (String)getKeyByValue(TELEPHONE_TYPES, type);
        this.put(TYPE_FIELD,typeTranslated);
    }
    public String getType(){
        return TELEPHONE_TYPES.get(this.getString(TYPE_FIELD));
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

    public static void initializeLangauges(){

        TELEPHONE_TYPES.put(TYPE_CELLULAR, TYPE_CELLULAR_TRANSLATED);
        TELEPHONE_TYPES.put(TYPE_HOME, TYPE_HOME_TRANSLATED);
        TELEPHONE_TYPES.put(TYPE_OFFICE, TYPE_OFFICE_TRANSLATED);
        TELEPHONE_TYPES.put(TYPE_FAX, TYPE_FAX_TRANSLATED);
        TELEPHONE_TYPES.put(TYPE_OTHER, TYPE_OTHER_TRANSLATED);
    }

    public static Object getKeyByValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


}

package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Arrays;

/**
 * Created by GiuliaGiGi on 06/05/15.
 */
@ParseClassName("Office")
public class Office extends ParseObject {

    public static final String TYPE_HEADQUARTER = "Headquarter";
    public static final String TYPE_BRANCH = "Branch";
    public static final String[] TYPES = new String[]{TYPE_HEADQUARTER , TYPE_BRANCH};

    protected static final String OFFICE_ADDRESS_FIELD = "address";
    protected static final String OFFICE_CITY_FIELD = "city";
    protected static final String OFFICE_POSTAL_CODE_FIELD = "postalCode";
    protected static final String OFFICE_NATION_FIELD = "nation";
    protected static final String OFFICE_TYPE_FIELD = "officeType";

    public Office(){
        super();
    }


    public void setOfficeType(String type){
        this.put(OFFICE_TYPE_FIELD,type);
    }
    public String getOfficeType(){
        return this.getString(OFFICE_TYPE_FIELD);
    }

    public void setOfficeCity(String city){
        this.put(OFFICE_CITY_FIELD,city);
    }
    public String getOfficeCity(){
        return this.getString(OFFICE_CITY_FIELD);
    }

    public void setOfficeAddress(String address){
        this.put(OFFICE_ADDRESS_FIELD,address);
    }
    public String getOfficeAddress(){
        return this.getString(OFFICE_ADDRESS_FIELD);
    }

    public void setOfficeNation(String nation){
        this.put(OFFICE_NATION_FIELD,nation);
    }
    public String getOfficeNation(){
        return this.getString(OFFICE_NATION_FIELD);
    }

    public void setOfficeCAP(String cap){
        this.put(OFFICE_POSTAL_CODE_FIELD,cap);
    }
    public String getOfficeCAP(){
        return this.getString(OFFICE_POSTAL_CODE_FIELD);
    }

    public static int getTypeID(String type){

        for(int i=0; i<TYPES.length;i++){

            if(type.equals(TYPES[i]))
                return i;
        }
        return -1;
    }



}

package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */

@ParseClassName("Student")
public class Student extends User {

    //TODO: a thousand attributes
    protected static final String NAME_FIELD = "name";
    protected static final String SURNAME_FIELD = "surname";
    protected static final String DEGREE_FIELD = "degree";
    protected static final String SEX_FIELD = "sex";
    protected static final String STUDIES_FIELD = "studies";
    protected static final String BIRTH_FIELD = "birthDate";
    protected static final String ADDRESS_FIELD = "address";
    protected static final String CITY_FIELD = "city";
    protected static final String POSTAL_CODE_FIELD = "postalCode";
    protected static final String NATION_FIELD = "nation";
    protected static final String PHONE_FIELD = "phone";

    public static final String DEGREE_BACHELOR = "Bachelor";
    public static final String DEGREE_MASTER = "Master";
    public static final String DEGREE_DOCTORATE = "Doctorate";
    public static final String[] DEGREE_TYPES = new String[]{DEGREE_BACHELOR,DEGREE_MASTER,DEGREE_DOCTORATE};

    public static final String STUDIES_MECHANICS    =   "Mechanics";
    public static final String STUDIES_INFORMATICS  =   "Informatics";
    public static final String STUDIES_CHEMISTRY    =   "Chemistry";
    public static final String STUDIES_ENERGY       =   "Energy";
    public static final String STUDIES_MATERIALS    =   "Materials";
    public static final String[] STUDIES_TYPES = new String[]{
                                                        STUDIES_MECHANICS,
                                                        STUDIES_CHEMISTRY,
                                                        STUDIES_INFORMATICS,
                                                        STUDIES_ENERGY,
                                                        STUDIES_MATERIALS};

    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";

    public Student(){
        super();
    }

    /*GETTER METHODS*/
    public String getName() {
        return this.getString(NAME_FIELD);
    }
    public String getSurname() {
        return this.getString(SURNAME_FIELD);
    }
    public String getDegree() {
        return this.getString(DEGREE_FIELD);
    }
    public String getSex() {
        return getString(SEX_FIELD);
    }
    public String getStudies(){
        return this.getString(STUDIES_FIELD);
    }
    public Date getBirth(){
        return this.getDate(BIRTH_FIELD);
    }
    public String getAddress(){
        return this.getString(ADDRESS_FIELD);
    }
    public String getCity(){
        return this.getString(CITY_FIELD);
    }
    public String getPostalCode(){
        return this.getString(POSTAL_CODE_FIELD);
    }
    public String getNation(){
        return this.getString(NATION_FIELD);
    }
    public String getPhone(){
        return this.getString(PHONE_FIELD);
    }
    /* END GETTER METHODS*/

    /*SETTER METHODS*/
    public void setName(String name){

        this.put(NAME_FIELD,name);
    }
    public void setSurname(String surname){

        this.put(SURNAME_FIELD,surname);
    }
    public void setDegree(String degree){

        this.put(DEGREE_FIELD,degree);
    }
    public void setSex(String sex){

        this.put(SEX_FIELD,sex);
    }
    public void setStudies(String studies){

        this.put(STUDIES_FIELD,studies);
    }
    public void setBirth(String birth){

        this.put(BIRTH_FIELD,birth);
    }
    public void setAddress(String address){

        this.put(ADDRESS_FIELD,address);
    }
    public void setCity(String city){

        this.put(CITY_FIELD,city);
    }
    public void setPostalCode(String CAP){

        this.put(POSTAL_CODE_FIELD,CAP);
    }
    public void setNation(String nation){

        this.put(NATION_FIELD,nation);
    }
    public void setPhone(String phone){

        this.put(PHONE_FIELD,phone);
    }
    /*END SETTER METHODS*/

}

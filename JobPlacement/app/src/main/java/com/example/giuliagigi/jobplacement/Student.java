package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */

@ParseClassName("Student")
public class Student extends User {

    //TODO: a thousand attributes
    protected static final String NAME_FIELD = "name_field";
    protected static final String SURNAME_FIELD = "surname_field";
    protected static final String DEGREE_FIELD = "degree_field";
    protected static final String SEX_FIELD = "sex_field";

    public static final String DEGREE_BACHELOR = "Bachelor";
    public static final String DEGREE_MASTER = "Master";
    public static final String DEGREE_DOCTORATE = "Doctorate";
    public static final String[] DEGREE_TYPES = new String[]{DEGREE_BACHELOR,DEGREE_MASTER,DEGREE_DOCTORATE};

    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";

    public Student(){
        super();
    }

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
}

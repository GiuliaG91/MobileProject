package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by MarcoEsposito90 on 22/04/2015.
 */

@ParseClassName("Degree")
public class Degree extends ParseObject{

    public static final String TYPE_BACHELOR = "Bachelor";
    public static final String TYPE_MASTER = "Master";
    public static final String TYPE_DOCTORATE = "Doctorate";
    public static final String TYPE_GRADUATING = "Graduating";
    public static final String TYPE_NONE = "None";
    public static final String[] TYPES = new String[]{TYPE_NONE , TYPE_GRADUATING, TYPE_BACHELOR, TYPE_MASTER, TYPE_DOCTORATE};

    public static final String STUDIES_MECHANICS    =   "Mechanics";
    public static final String STUDIES_INFORMATICS  =   "Informatics";
    public static final String STUDIES_CHEMISTRY    =   "Chemistry";
    public static final String STUDIES_ENERGY       =   "Energy";
    public static final String STUDIES_MATERIALS    =   "Materials";
    public static final String[] STUDIES = new String[]{
            STUDIES_MECHANICS,
            STUDIES_CHEMISTRY,
            STUDIES_INFORMATICS,
            STUDIES_ENERGY,
            STUDIES_MATERIALS};

    public static final String STUDIES_FIELD = "studies";
    public static final String TYPE_FIELD = "type";
    public static final String MARK_FIELD = "mark";
    public static final String DATE_FIELD = "degreeDate";
    public static final String LOUD_FIELD = "loud";
    public static final String STUDENT_FIELD = "student";


    public Degree(){
        super();
    }

    public void setType(String type){
        this.put(TYPE_FIELD,type);
    }
    public String getType(){
        return this.getString(TYPE_FIELD);
    }

    public String getStudies(){
        return this.getString(STUDIES_FIELD);
    }
    public void setStudies(String studies){
        this.put(STUDIES_FIELD,studies);
    }

    public Integer getMark(){
        return (Integer)this.getNumber(MARK_FIELD);

    }
    public void setMark(int mark) throws Exception{

        if(mark >=60 &&  mark<=110) {
            this.put(MARK_FIELD,mark);
            return;
        }
        throw new Exception();
    }

    public void setDegreeDate(Date degreeDate){

        this.put(DATE_FIELD,degreeDate);
    }
    public Date getDegreeDate(){
        return this.getDate(DATE_FIELD);
    }

    public void setLoud(Boolean loud){
        this.put(LOUD_FIELD, loud);
    }
    public Boolean getLoud(){
        return  this.getBoolean(LOUD_FIELD);
    }

    public void setStudent(Student owner){

        this.put(STUDENT_FIELD,owner);
    }
    public Student getStudent(){

        return (Student)this.get(STUDENT_FIELD);
    }





    public static int getTypeID(String type){

        for(int i=0; i<TYPES.length;i++){

            if(type.equals(TYPES[i]))
                return i;
        }
        return -1;
    }
    public static int getStudyID(String study){

        for(int i=0; i<STUDIES.length;i++){

            if(study.equals(STUDIES[i]))
                return i;
        }
        return -1;
    }


}

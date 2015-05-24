package com.example.giuliagigi.jobplacement;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by MarcoEsposito90 on 22/04/2015.
 */

@ParseClassName("Degree")
public class Degree extends ParseObject implements Comparable<Degree>{

    public static final String TYPE_BACHELOR = "Bachelor";
    public static final String TYPE_MASTER = "Master";
    public static final String TYPE_DOCTORATE = "Doctorate";
    public static final String TYPE_GRADUATING = "Graduating";
    public static final String TYPE_NONE = "None";
    public static final String TYPE_BACHELOR_TRANSLATED = GlobalData.getContext().getString(R.string.string_degree_type_bachelor);
    public static final String TYPE_MASTER_TRANSLATED = GlobalData.getContext().getString(R.string.string_degree_type_master);
    public static final String TYPE_DOCTORATE_TRANSLATED = GlobalData.getContext().getString(R.string.string_degree_type_doctorate);
    public static final String TYPE_GRADUATING_TRANSLATED = GlobalData.getContext().getString(R.string.string_degree_type_graduating);
    public static final String TYPE_NONE_TRANSLATED = GlobalData.getContext().getString(R.string.string_degree_type_none);
    public static final HashMap<String,String> DEGREE_TYPES = new HashMap<>();
    public static final String[] TYPES = new String[]{TYPE_NONE_TRANSLATED , TYPE_GRADUATING_TRANSLATED, TYPE_BACHELOR_TRANSLATED, TYPE_MASTER_TRANSLATED, TYPE_DOCTORATE_TRANSLATED};

    public static final String STUDIES_MECHANICS    =   "Mechanics";
    public static final String STUDIES_INFORMATICS  =   "Informatics";
    public static final String STUDIES_CHEMISTRY    =   "Chemistry";
    public static final String STUDIES_ENERGY       =   "Energy";
    public static final String STUDIES_MATERIALS    =   "Materials";
    public static final String STUDIES_MECHANICS_TRANSLATED = GlobalData.getContext().getString(R.string.string_field_mechanics);
    public static final String STUDIES_INFORMATICS_TRANSLATED = GlobalData.getContext().getString(R.string.string_field_informatics);
    public static final String STUDIES_CHEMISTRY_TRANSLATED = GlobalData.getContext().getString(R.string.string_field_chemistry);
    public static final String STUDIES_ENERGY_TRANSLATED = GlobalData.getContext().getString(R.string.string_field_energy);
    public static final String STUDIES_MATERIALS_TRANSLATED = GlobalData.getContext().getString(R.string.string_field_materials);
    public static final HashMap<String, String> DEGREE_STUDIES = new HashMap<>();
    public static final String[] STUDIES = new String[]{
            STUDIES_MECHANICS_TRANSLATED,
            STUDIES_CHEMISTRY_TRANSLATED,
            STUDIES_INFORMATICS_TRANSLATED,
            STUDIES_ENERGY_TRANSLATED,
            STUDIES_MATERIALS_TRANSLATED};

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
        String typeTranslated = (String)getKeyByValue(DEGREE_TYPES, type);
        this.put(TYPE_FIELD,typeTranslated);
    }
    public String getType(){
        return DEGREE_TYPES.get(this.getString(TYPE_FIELD));
    }

    public String getStudies(){
        return DEGREE_STUDIES.get(this.getString(STUDIES_FIELD));
    }
    public void setStudies(String studies){
        String studiesTranslated = (String)getKeyByValue(DEGREE_STUDIES, studies);
        this.put(STUDIES_FIELD,studiesTranslated);
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

    @Override
    public int compareTo(Degree other) {

        if(other == null)
            return 1;

        int thisId = getTypeID(this.getType());
        int otherId = getTypeID(other.getType());

        if(thisId<otherId)
            return -1;
        else if (thisId == otherId)
            return 0;

        return 1;
    }

    public static void initializeLangauges(){

        DEGREE_TYPES.put(TYPE_BACHELOR, TYPE_BACHELOR_TRANSLATED);
        DEGREE_TYPES.put(TYPE_MASTER, TYPE_MASTER_TRANSLATED);
        DEGREE_TYPES.put(TYPE_DOCTORATE, TYPE_DOCTORATE_TRANSLATED);
        DEGREE_TYPES.put(TYPE_GRADUATING, TYPE_GRADUATING_TRANSLATED);
        DEGREE_TYPES.put(TYPE_NONE, TYPE_NONE_TRANSLATED);

        DEGREE_STUDIES.put(STUDIES_MECHANICS, STUDIES_MECHANICS_TRANSLATED);
        DEGREE_STUDIES.put(STUDIES_INFORMATICS,  STUDIES_INFORMATICS_TRANSLATED);
        DEGREE_STUDIES.put(STUDIES_CHEMISTRY, STUDIES_CHEMISTRY_TRANSLATED);
        DEGREE_STUDIES.put(STUDIES_ENERGY, STUDIES_ENERGY_TRANSLATED);
        DEGREE_STUDIES.put(STUDIES_MATERIALS, STUDIES_MATERIALS_TRANSLATED);
    }

    public static Object getKeyByValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static String getTranslatedString(String s)
    {
        return (String)getKeyByValue(DEGREE_STUDIES,s);

    }

}

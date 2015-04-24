package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by MarcoEsposito90 on 22/04/2015.
 */

@ParseClassName("Degree")
public class Degree extends ParseObject{

    public static final String TYPE_BACHELOR = "Bachelor";
    public static final String TYPE_MASTER = "Master";
    public static final String TYPE_DOCTORATE = "Doctorate";
    public static final String[] TYPES = new String[]{TYPE_BACHELOR, TYPE_MASTER, TYPE_DOCTORATE};

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

    protected static final String STUDIES_FIELD = "studies";
    protected static final String TYPE_FIELD = "type";
    protected static final String MARK_FIELD = "mark";

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

    public int getMark(){
        return (int) this.getNumber(MARK_FIELD);
    }
    public void setMark(int mark){
        this.put(MARK_FIELD,mark);
    }

}

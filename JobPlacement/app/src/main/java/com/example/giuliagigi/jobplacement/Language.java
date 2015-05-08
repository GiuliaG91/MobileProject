package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by GiuliaGiGi on 30/04/15.
 */
@ParseClassName("Language")
public class Language extends ParseObject {

    public static final String LEVEL_HIGH = "High";
    public static final String LEVEL_MEDIUM = "Medium";
    public static final String LEVEL_LOW = "Low";
    public static final String LEVEL_NONE = "None";
    public static final String[] LEVELS = new String[]{LEVEL_NONE , LEVEL_LOW, LEVEL_MEDIUM, LEVEL_HIGH};

    public static final String LEVEL_FIELD = "level";
    public static final String LANGUAGE_DESCRIPTION = "description";
    public static final String STUDENT_FIELD = "student";

    public Language(){
        super();
    }

    public void setDescription(String description){
        this.put(LANGUAGE_DESCRIPTION,description);
    }
    public String getDescription(){
        return this.getString(LANGUAGE_DESCRIPTION);
    }

    public String getLevel(){
        return this.getString(LEVEL_FIELD);
    }
    public void setLevel(String level){
        this.put(LEVEL_FIELD,level);
    }

    public void setStudent(Student owner){
        this.put(STUDENT_FIELD,owner);
    }
    public Student getStudent(){

        return (Student)this.get(STUDENT_FIELD);
    }

    public static int getLevelID(String level){

        for(int i=0; i<LEVELS.length;i++){

            if(level.equals(LEVELS[i]))
                return i;
        }
        return -1;
    }

}

package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

/**
 * Created by GiuliaGiGi on 30/04/15.
 */
@ParseClassName("Language")
public class Language extends ParseObject {

    public static final String LEVEL_HIGH = "High";
    public static final String LEVEL_MEDIUM = "Medium";
    public static final String LEVEL_LOW = "Low";
    public static final String LEVEL_NONE = "None";
    public static final String LEVEL_HIGH_TRANSLATED = GlobalData.getContext().getString(R.string.string_language_level_high);
    public static final String LEVEL_MEDIUM_TRANSLATED = GlobalData.getContext().getString(R.string.string_language_level_medium);
    public static final String LEVEL_LOW_TRANSLATED = GlobalData.getContext().getString(R.string.string_language_level_low);
    public static final String LEVEL_NONE_TRANSLATED = GlobalData.getContext().getString(R.string.string_language_level_none);
    public static final HashMap<String,String> LANGUAGE_LEVELS = new HashMap<>();
    public static final String[] LEVELS = new String[]{LEVEL_NONE_TRANSLATED , LEVEL_LOW_TRANSLATED, LEVEL_MEDIUM_TRANSLATED, LEVEL_HIGH_TRANSLATED};

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
        return LANGUAGE_LEVELS.get(this.getString(LEVEL_FIELD));
    }
    public void setLevel(String level){
        String levelTranslated = (String)getKeyByValue(LANGUAGE_LEVELS, level);
        this.put(LEVEL_FIELD,levelTranslated);
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

    public static void initializeLangauges(){

        LANGUAGE_LEVELS.put(LEVEL_NONE, LEVEL_NONE_TRANSLATED);
        LANGUAGE_LEVELS.put(LEVEL_LOW, LEVEL_LOW_TRANSLATED);
        LANGUAGE_LEVELS.put(LEVEL_MEDIUM, LEVEL_MEDIUM_TRANSLATED);
        LANGUAGE_LEVELS.put(LEVEL_HIGH, LEVEL_HIGH_TRANSLATED);
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

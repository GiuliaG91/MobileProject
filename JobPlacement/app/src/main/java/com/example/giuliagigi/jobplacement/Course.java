package com.example.giuliagigi.jobplacement;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ParseClassName("Course")
public class Course extends ParseObject {

    public static String CODE_FIELD = "Code";
    public static String PROFESSOR_FIELD = "Professor";
    public static String NAME_FIELD = "Name";
    public static String LECTURES_FIELD = "Lectures";

    private String code;
    private Professor professor;
    private String name;
    private ArrayList<Lecture> lectures;
    protected HashMap<String,Boolean> isCached;

    public Course(){
        super();

        code = null;
        professor = null;
        name = null;
        lectures = new ArrayList<Lecture>();

        isCached = new HashMap<String,Boolean>();

        isCached.put(CODE_FIELD, false);
        isCached.put(PROFESSOR_FIELD, false);
        isCached.put(NAME_FIELD, false);
        isCached.put(LECTURES_FIELD, false);
    }



    // --------------------------------------------------------------------
    // ---------------- GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    public String getCode() {

        if(isCached.get(CODE_FIELD))
            return code;

        code = getString(CODE_FIELD);
        isCached.put(CODE_FIELD, true);
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        put(CODE_FIELD,code);
    }

    public Professor getProfessor() {

        if(isCached.get(PROFESSOR_FIELD))
            return professor;

        professor = (Professor)get(PROFESSOR_FIELD);
        isCached.put(PROFESSOR_FIELD,true);
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        isCached.put(PROFESSOR_FIELD,true);
        put(PROFESSOR_FIELD, professor);
    }

    public String getName() {

        if(isCached.get(NAME_FIELD))
            return name;

        name = getString(NAME_FIELD);
        isCached.put(NAME_FIELD,true);
        return name;
    }

    public void setName(String name) {
        this.name = name;
        put(NAME_FIELD,name);
    }

    // --------------------------------------------------------------------
    // ------------ END GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    public void addLecture(Lecture l){

        lectures.add(l);
        getRelation(LECTURES_FIELD).add(l);
    }

    public void removeLecture(Lecture l){

        lectures.remove(l);
        getRelation(LECTURES_FIELD).remove(l);
    }

    public int getLectureNumber(){

        if(!isCached.get(LECTURES_FIELD)) cacheData();
        return lectures.size();
    }

    public Lecture getLecture(int i){

        if(!isCached.get(LECTURES_FIELD)) cacheData();
        if(i<0|| i>=getLectureNumber()) return null;
        return lectures.get(i);
    }

    synchronized public ArrayList<Lecture> getLectures(){

        if(isCached.get(LECTURES_FIELD)) return lectures;

        ParseRelation<Lecture> tmp = getRelation(LECTURES_FIELD);

        try {
            lectures.addAll(tmp.getQuery().find());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        isCached.put(LECTURES_FIELD,true);
        return lectures;
    }


    public void cacheData(){

        if(isCached.containsValue(false)){

            Log.println(Log.ASSERT, "COURSE", "caching");

            AsyncTask<Void,Void,Void> cacheTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    getName();
                    getCode();
                    getLectures();
                    getProfessor();

                    return null;
                }
            };
            cacheTask.execute();
        }
    }
}
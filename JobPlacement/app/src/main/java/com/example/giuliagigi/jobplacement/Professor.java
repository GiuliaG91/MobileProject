package com.example.giuliagigi.jobplacement;


import android.os.AsyncTask;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GiuliaGiGi on 07/11/15.
 */

@ParseClassName("Professor")
public class Professor extends User {

    protected static final String NAME_FIELD = "name";
    protected static final String SURNAME_FIELD = "surname";
    protected static final String COURSES_FIELD = "courses";
    protected static final String SEX_FIELD = "sex";
    protected static final String BIRTH_DATE_FIELD = "birthDate";
    protected static final String BIRTH_CITY_FIELD = "birthCity";
//    protected static final String CITY_FIELD = "city";
    protected static final String NATION_FIELD = "nation";

    protected static final String CONSULTING_SH_FIELD = "consultation_starthour";
    protected static final String CONSULTING_SM_FIELD = "consultation_startminute";
    protected static final String CONSULTING_EH_FIELD = "consultation_endhour";
    protected static final String CONSULTING_EM_FIELD = "consultation_endminute";

    protected String name;
    protected String surname;
    protected String sex;
    protected ArrayList<Course> courses;
    protected Date birthDate;
    protected String birthCity;
    protected String city;
    protected String nation;


    public Professor(){
        super();

        this.name = null;
        this.surname = null;
        this.sex = null;
        this.courses = new ArrayList<Course>();
        this.birthDate = null;
        this.birthCity = null;
        this.city = null;
        this.nation = null;

        isCached.put(NAME_FIELD,false);
        isCached.put(SURNAME_FIELD,false);
        isCached.put(COURSES_FIELD,false);
        isCached.put(SEX_FIELD,false);
        isCached.put(BIRTH_CITY_FIELD,false);
        isCached.put(BIRTH_DATE_FIELD,false);
        isCached.put(NATION_FIELD,false);
    }

    // --------- GETTERS -----------
    synchronized public String getName() {
        if(isCached.get(NAME_FIELD))
            return name;

        name = this.getString(NAME_FIELD);
        isCached.put(NAME_FIELD,true);
        return name;
    }

    synchronized public String getSurname() {
        if(isCached.get(SURNAME_FIELD))
            return surname;

        surname = this.getString(SURNAME_FIELD);
        isCached.put(SURNAME_FIELD,true);
        return surname;
    }

    synchronized public ArrayList<Course> getCourses() {

        if(isCached.get(COURSES_FIELD))
            return this.courses;

        ParseRelation<Course> tmp= getRelation(COURSES_FIELD);

        try {
            courses.addAll(tmp.getQuery().find());
            isCached.put(COURSES_FIELD, true);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return courses;
    }

    synchronized public String getSex() {
        if(isCached.get(SEX_FIELD))
            return sex;

        sex = getString(SEX_FIELD);
        isCached.put(SEX_FIELD,true);
        return sex;
    }

    synchronized public Date getBirth(){
        if(isCached.get(BIRTH_DATE_FIELD))
            return birthDate;

        birthDate = this.getDate(BIRTH_DATE_FIELD);
        isCached.put(BIRTH_DATE_FIELD,true);
        return birthDate;
    }

    synchronized public String getBirthCity(){
        if(isCached.get(BIRTH_CITY_FIELD))
            return birthCity;

        birthCity = this.getString(BIRTH_CITY_FIELD);
        isCached.put(BIRTH_CITY_FIELD,true);
        return birthCity;
    }

//    public String getCity(){
//        if(isCached.get(CITY_FIELD))
//            return city;
//
//        city = this.getString(CITY_FIELD);
//        isCached.put(CITY_FIELD,true);
//        return city;
//    }

    synchronized public String getNation(){
        if(isCached.get(NATION_FIELD))
            return nation;

        nation = this.getString(NATION_FIELD);
        isCached.put(NATION_FIELD,true);
        return nation;
    }

    synchronized public Schedule getConsulting(){

        int startHour = getInt(CONSULTING_SH_FIELD);

        if(startHour == 0) return null;

        int startMinute = getInt(CONSULTING_SM_FIELD);
        int endHour = getInt(CONSULTING_EH_FIELD);
        int endMinute = getInt(CONSULTING_EM_FIELD);

        return new Schedule(startHour,startMinute,endHour,endMinute);
    }

    // ----- END GETTERS -------

    // ----- SETTERS --------
    public void setName(String name){
        this.name = name;
        isCached.put(NAME_FIELD,true);
        this.put(NAME_FIELD,name);
    }

    public void setSurname(String surname){
        this.surname = surname;
        isCached.put(SURNAME_FIELD,true);
        this.put(SURNAME_FIELD,surname);
    }

    public void addCourse(Course course){
        courses.add(course);
        getRelation(COURSES_FIELD).add(course);
    }

    public void removeCourse(Course course){
        courses.remove(course);
        getRelation(COURSES_FIELD).remove(course);
    }

    public void setSex(String sex){
        this.sex = sex;
        isCached.put(SEX_FIELD, true);
        this.put(SEX_FIELD, sex);
    }

    public void setBirth(Date birth){
        this.birthDate = birth;
        isCached.put(BIRTH_DATE_FIELD,true);
        this.put(BIRTH_DATE_FIELD,birth);
    }

    public void setBirthCity(String birthCity){
        this.birthCity = birthCity;
        isCached.put(BIRTH_CITY_FIELD,true);
        this.put(BIRTH_CITY_FIELD,birthCity);
    }

//    public void setCity(String city){
//        this.city = city;
//        isCached.put(CITY_FIELD,true);
//        this.put(CITY_FIELD,city);
//    }

    public void setNation(String nation){
        this.nation = nation;
        isCached.put(NATION_FIELD, true);
        this.put(NATION_FIELD,nation);
    }


    public void setConsulting(Schedule schedule){

        put(CONSULTING_SH_FIELD, schedule.getStartHour());
        put(CONSULTING_SM_FIELD, schedule.getStartMinute());
        put(CONSULTING_EH_FIELD, schedule.getEndHour());
        put(CONSULTING_EM_FIELD, schedule.getEndMinute());
    }

    // ---- END SETTERS -------

    @Override
    public void cacheData() {

        super.cacheData();

        AsyncTask<Void,Void,Void> cacheTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                getName();
                getSurname();
                getSex();
                getCourses();
                getBirth();
                getBirthCity();
                getNation();
                getConsulting();
                return null;
            }
        };
        cacheTask.execute();
    }

}

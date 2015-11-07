package com.example.giuliagigi.jobplacement;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GiuliaGiGi on 07/11/15.
 */
public class Professor extends User {

    protected static final String NAME_FIELD = "name";
    protected static final String SURNAME_FIELD = "surname";
    protected static final String COURSES_FIELD = "courses";
    protected static final String SEX_FIELD = "sex";
    protected static final String BIRTH_DATE_FIELD = "birthDate";
    protected static final String BIRTH_CITY_FIELD = "birthCity";
    protected static final String CITY_FIELD = "city";
    protected static final String NATION_FIELD = "nation";
    protected static final String DESCRIPTION_FIELD = "description";

    protected String name;
    protected String surname;
    protected String sex;
    protected ArrayList<Course> courses;
    protected Date birthDate;
    protected String birthCity;
    protected String city;
    protected String nation;
    protected String description;


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
        this.description = null;

        isCached.put(NAME_FIELD,false);
        isCached.put(SURNAME_FIELD,false);
        isCached.put(COURSES_FIELD,false);
        isCached.put(SEX_FIELD,false);
        isCached.put(BIRTH_CITY_FIELD,false);
        isCached.put(BIRTH_DATE_FIELD,false);
        isCached.put(CITY_FIELD,false);
        isCached.put(NATION_FIELD,false);
        isCached.put(DESCRIPTION_FIELD,false);
    }

    // --------- GETTERS -----------
    public String getName() {
        if(isCached.get(NAME_FIELD))
            return name;

        name = this.getString(NAME_FIELD);
        isCached.put(NAME_FIELD,true);
        return name;
    }

    public String getSurname() {
        if(isCached.get(SURNAME_FIELD))
            return surname;

        surname = this.getString(SURNAME_FIELD);
        isCached.put(SURNAME_FIELD,true);
        return surname;
    }

    public ArrayList<Course> getCourses() {
        if(isCached.get(COURSES_FIELD))
            return this.courses;

        //da capire come gestire la relazione corsi-insegnante (ovvero se via parse o no)

        isCached.put(COURSES_FIELD, true);
        return courses;
    }

    public String getSex() {
        if(isCached.get(SEX_FIELD))
            return sex;

        sex = getString(SEX_FIELD);
        isCached.put(SEX_FIELD,true);
        return sex;
    }

    public Date getBirth(){
        if(isCached.get(BIRTH_DATE_FIELD))
            return birthDate;

        birthDate = this.getDate(BIRTH_DATE_FIELD);
        isCached.put(BIRTH_DATE_FIELD,true);
        return birthDate;
    }

    public String getBirthCity(){
        if(isCached.get(BIRTH_CITY_FIELD))
            return birthCity;

        birthCity = this.getString(BIRTH_CITY_FIELD);
        isCached.put(BIRTH_CITY_FIELD,true);
        return birthCity;
    }

    public String getCity(){
        if(isCached.get(CITY_FIELD))
            return city;

        city = this.getString(CITY_FIELD);
        isCached.put(CITY_FIELD,true);
        return city;
    }

    public String getNation(){
        if(isCached.get(NATION_FIELD))
            return nation;

        nation = this.getString(NATION_FIELD);
        isCached.put(NATION_FIELD,true);
        return nation;
    }

    public String getDescription() {
        if(isCached.get(DESCRIPTION_FIELD))
            return description;

        description = this.getString(DESCRIPTION_FIELD);
        isCached.put(DESCRIPTION_FIELD,true);
        return description;
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
        //capire come gestire
    }

    public void removeCourse(Course course){
        courses.remove(course);
        //capire come gestire
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

    public void setCity(String city){
        this.city = city;
        isCached.put(CITY_FIELD,true);
        this.put(CITY_FIELD,city);
    }

    public void setNation(String nation){
        this.nation = nation;
        isCached.put(NATION_FIELD, true);
        this.put(NATION_FIELD,nation);
    }

    public void setDescription(String description){
        this.description = description;
        isCached.put(DESCRIPTION_FIELD,true);
        this.put(DESCRIPTION_FIELD,description);
    }

    // ---- END SETTERS -------

    @Override
    public void cacheData() {
        super.cacheData();

        getName();
        getSurname();
        getSex();
        getCourses();
        getBirth();
        getBirthCity();
        getCity();
        getNation();
        getDescription();
    }

}

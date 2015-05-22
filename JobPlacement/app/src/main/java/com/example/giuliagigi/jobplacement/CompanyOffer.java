package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pietro on 03/05/2015.
 */
@ParseClassName("CompanyOffer")
public class CompanyOffer extends ParseObject {


    protected static final String OBJECT_FIELD = "object";
    protected static final String WORK_FIELD = "field";
    private static final String  N_POSITIONS_FIELD = "n_positions";
    private static final String  VALIDITY_FIELD = "validity";
    private static final String  CONTRACT_FIELD = "contract";
    private static final String  TERM_FIELD = "term";
    private static final String  LOCATION_FIELD = "location";
    private static final String  NATION_FIELD = "nation";
    private static final String  CITY_FIELD = "city";
    private static final String  SAlARY_FIELD = "salary";
    private static final String  DESCRIPTION_FIELD= "description";
    private static final String COMPANY_FIELD=  "company";

    private static final String TAGS_FIELD=  "tags";
    private static final String APPLIES_FIELD="applies";
    private static final String PUBLISH_FIELD="publish";

    public CompanyOffer(){
        super();
    }


    /*GETTER*/
    public Boolean getPublished(){return this.getBoolean(PUBLISH_FIELD);}

    public String getOfferObject() {
        return this.getString(OBJECT_FIELD);
    }

    public String getWorkField() {
        return this.getString(WORK_FIELD);
    }

    public Integer getnPositions() {
        return this.getInt(N_POSITIONS_FIELD);
    }

    public Date getValidity(){ return this.getDate(VALIDITY_FIELD);}

    public String getContract() {
        return this.getString(CONTRACT_FIELD);
    }

    public String getTerm() {
        return this.getString(TERM_FIELD);
    }


    public Integer getSAlARY() {
        return this.getInt(SAlARY_FIELD);
    }

    public String getDescription() {
        return this.getString(DESCRIPTION_FIELD);
    }

    public Company getCompany() {return (Company)this.get(COMPANY_FIELD);}

    public String getNation(){return this.getString(NATION_FIELD);}

    public String getCity(){return this.getString(CITY_FIELD);}


    public List<Tag> getTags( ){

        ParseRelation<Tag> tmp= getRelation(TAGS_FIELD);
        List<Tag> result= null;
        try {
            result = tmp.getQuery().find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

       public List<Student> getStudents( ){

        ParseRelation<Student> students = getRelation(APPLIES_FIELD);
          List<Student> result= null;
           try {
               result = students.getQuery().find();
           } catch (com.parse.ParseException e) {
               e.printStackTrace();
           }

        return result;
    }

    /***************END GETTER****************/



    public void setObject(String object){

        this.put(OBJECT_FIELD,object);
    }
    public void setWorkField(String workField){

        this.put(WORK_FIELD,workField);
    }
    public void setPositions(Integer positions){

        this.put(N_POSITIONS_FIELD,positions);
    }

    public void setValidity(Date validity){
        Calendar c=Calendar.getInstance();
        c.setTime(validity);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        this.put(VALIDITY_FIELD, c.getTime());
    }

    public void setContract(String contract){

        this.put(CONTRACT_FIELD,contract);
    }
    public void setTerm(String term){

        this.put(TERM_FIELD,term);
    }
    public void setLocation(String location){

        this.put(LOCATION_FIELD,location);
    }
    public void setSalary(int salay){

        this.put(SAlARY_FIELD,salay);
    }
    public void setDescription(String description){

        this.put(DESCRIPTION_FIELD,description);
    }
    public void setCompany(Company company)
    {
        this.put(COMPANY_FIELD,company);
    }
    public void setNation(String nation)
    {
        this.put(NATION_FIELD,nation);
    }
    public void setCity(String city)
    {
        this.put(CITY_FIELD,city);
    }


    public void addTag(Tag t){ //this.addUnique(TAGS_FIELD, Arrays.asList(t));
        getRelation(TAGS_FIELD).add(t);
     }

    public void removeAllTag(List<Tag> list)
    {
        for (Tag t : list)
        {
            this.getRelation(TAGS_FIELD).remove(t);
        }
    }

    public void addStudent(Student student)
    {
      getRelation(APPLIES_FIELD).add(student);
    }

    public void setPublishField(Boolean bool)
    {
        this.put(PUBLISH_FIELD,bool);
    }

    /*END SETTER METHODS*/

    public void setLocation(ParseGeoPoint location){

            this.put(LOCATION_FIELD,location);
    }
    public ParseGeoPoint getLocation(){

        return (ParseGeoPoint)get(LOCATION_FIELD);
    }



}

package com.example.giuliagigi.jobplacement;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */
@ParseClassName("Company")
public class Company extends User {

    protected static final String NAME_FIELD = "Company_Name";
    protected static final String FISCAL_CODE_FIELD = "Fiscal_Code";
    protected static final String  FIELD_FIELD = "Field";
    protected static final String  MISSION_FIELD = "Mission";
    protected static final String  DESCRIPTION_FIELD = "Description";
    protected static final String FOUNDATION_DATE_FIELD = "Foundation_Date";
    protected static final String OFFICES_FIELD = "offices";
    protected static final String STUDENTS_FIELD = "students";


    protected String name;
    protected String fiscalCode;
    protected String field;
    protected Date foundationDate;
    protected String description;
    protected ArrayList<Office> offices;
    protected ArrayList<Student> students;

    public Company(){

        super();

        name = null;
        fiscalCode = null;
        field = null;
        foundationDate = null;
        description = null;
        offices = new ArrayList<Office>();
        students=new ArrayList<>();

        isCached.put(NAME_FIELD,false);
        isCached.put(FISCAL_CODE_FIELD,false);
        isCached.put(FIELD_FIELD,false);
        isCached.put(FOUNDATION_DATE_FIELD,false);
        isCached.put(OFFICES_FIELD,false);
        isCached.put(DESCRIPTION_FIELD, false);
        isCached.put(STUDENTS_FIELD, false);
    }

    public void setName(String name){

        this.name = name;
        isCached.put(NAME_FIELD,true);
        this.put(NAME_FIELD,name);
    }
    public String getName(){

        if(isCached.get(NAME_FIELD))
            return name;

        name = this.getString(NAME_FIELD);
        isCached.put(NAME_FIELD, true);
        return name;
    }
    public void setFiscalCode(String fc){

        this.fiscalCode = fc;
        isCached.put(FISCAL_CODE_FIELD,true);
        this.put(FISCAL_CODE_FIELD, fc);
    }
    public String getFiscalCode(){

        if(isCached.get(FISCAL_CODE_FIELD))
            return fiscalCode;

        fiscalCode = this.getString(FISCAL_CODE_FIELD);
        isCached.put(FISCAL_CODE_FIELD, true);
        return fiscalCode;
    }

    public void setField(String field) {

        this.field = field;
        isCached.put(FIELD_FIELD,true);
        this.put(FIELD_FIELD, field);
    }
    public String getField(){

        if(isCached.get(FIELD_FIELD))
            return field;

        field = this.getString(FIELD_FIELD);
        isCached.put(FIELD_FIELD, true);
        return field;
    }

    public Date getFoundation(){

        if(isCached.get(FOUNDATION_DATE_FIELD))
            return foundationDate;

        foundationDate = this.getDate(FOUNDATION_DATE_FIELD);
        isCached.put(FOUNDATION_DATE_FIELD,true);
        return foundationDate;
    }

    public String getDescription() {
        if (isCached.get(DESCRIPTION_FIELD))
            return description;

        description = this.getString(DESCRIPTION_FIELD);
        isCached.put(DESCRIPTION_FIELD, true);
        return description;
    }

    public void setFoundation(Date foundation){

        this.foundationDate = foundation;
        isCached.put(FOUNDATION_DATE_FIELD,true);
        this.put(FOUNDATION_DATE_FIELD, foundation);
    }

    public ArrayList<Office> getOffices() {

        if(isCached.get(OFFICES_FIELD))
            return this.offices;


        ParseRelation<Office> tmp = getRelation(OFFICES_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Office>() {
            @Override
            public void done(List<Office> results, ParseException e) {

                if(results!= null)
                    for(Office o:results)
                        offices.add(o);
            }
        });
//        List<Object> list = this.getList(OFFICES_FIELD);
//
//        if(list!=null)
//            for (Object o : list) {
//
//                if(o instanceof Office){
//                    Office d =(Office)o;
//
//                    try {
//                        d.fetchIfNeeded();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    offices.add(d);
//                }
//            }

        isCached.put(OFFICES_FIELD, true);
        return offices;
    }

    public void addOffice(Office office){

        offices.add(office);
        getRelation(OFFICES_FIELD).add(office);
//        this.addUnique(OFFICES_FIELD, office);
    }
    public void removeOffice(Office office){

        offices.remove(office);
        getRelation(OFFICES_FIELD).remove(office);
//        removeAll(OFFICES_FIELD,Arrays.asList(office));
    }

    public void setDescription(String description){

        this.description = description;
        isCached.put(DESCRIPTION_FIELD, true);
        this.put(DESCRIPTION_FIELD, description);
    }


    public List<Student> getStudents( ){

        if(isCached.get(STUDENTS_FIELD))
            return students;

        ParseRelation<Student> tmp= getRelation(STUDENTS_FIELD);
        List<Student> result= null;
        try {
            result = tmp.getQuery().find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        isCached.put(STUDENTS_FIELD, true);
        return result;
    }

    public void addStudent(Student student)
    {
        students.add(student);
        getRelation(STUDENTS_FIELD).add(student);

    }
    public void removeStudent(Student student)
    {
        students.remove(student);
        getRelation(STUDENTS_FIELD).remove(student);
    }



    @Override
    public void cacheData() {
        super.cacheData();

        getName();
        getFiscalCode();
        getField();
        getOffices();
        getDescription();
        getFoundation();
        getStudents();
    }


}

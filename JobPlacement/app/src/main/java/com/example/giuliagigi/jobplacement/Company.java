package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ProgressCallback;

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
    protected static final String OFFERS_FIELD="offers";


    protected String name;
    protected String fiscalCode;
    protected String field;
    protected Date foundationDate;
    protected String description;
    protected ArrayList<Office> offices;
    protected ArrayList<Student> students;
    protected ArrayList<CompanyOffer> offers;

    public Company(){

        super();

        name = null;
        fiscalCode = null;
        field = null;
        foundationDate = null;
        description = null;
        offices = new ArrayList<Office>();
        students=new ArrayList<>();
        offers=new ArrayList<>();

        isCached.put(NAME_FIELD,false);
        isCached.put(FISCAL_CODE_FIELD,false);
        isCached.put(FIELD_FIELD,false);
        isCached.put(FOUNDATION_DATE_FIELD,false);
        isCached.put(OFFICES_FIELD,false);
        isCached.put(DESCRIPTION_FIELD, false);
        isCached.put(STUDENTS_FIELD, false);
        isCached.put(OFFERS_FIELD,false);
    }

    public void setName(String name){

        this.name = name;
        isCached.put(NAME_FIELD,true);
        this.put(NAME_FIELD,name);
    }
    synchronized public String getName(){

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
    synchronized public String getFiscalCode(){

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
    synchronized public String getField(){

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

    synchronized public String getDescription() {
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

    synchronized public ArrayList<Office> getOffices() {

        if(isCached.get(OFFICES_FIELD))
            return this.offices;

        ParseRelation<Office> tmp = getRelation(OFFICES_FIELD);
        try {

            offices.addAll(tmp.getQuery().find());
            isCached.put(OFFICES_FIELD, true);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
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


    synchronized public List<Student> getStudents( ){

        if(isCached.get(STUDENTS_FIELD))
            return students;

        ParseRelation<Student> tmp= getRelation(STUDENTS_FIELD);
        try {

            students.addAll(tmp.getQuery().find());
            isCached.put(STUDENTS_FIELD, true);
        }
        catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return students;
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

    synchronized public List<CompanyOffer> getOffers()
    {
        if(isCached.get(OFFERS_FIELD))
            return offers;

        ParseRelation<CompanyOffer> tmp= getRelation(OFFERS_FIELD);
        try {

            offers.addAll(tmp.getQuery().find());
            isCached.put(OFFERS_FIELD, true);
        }
        catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return offers;
    }

    public void addOffer(CompanyOffer companyOffer)
    {
        offers.add(companyOffer);
        getRelation(OFFERS_FIELD).add(companyOffer);
    }

    public void removeOffer(CompanyOffer companyOffer)
    {
        offers.remove(companyOffer);
        getRelation(OFFERS_FIELD).remove(companyOffer);
    }



    @Override
    public void cacheData() {
        super.cacheData();

        AsyncTask<Void,Void,Void> cacheTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                getName();
                getFiscalCode();
                getField();
                getOffices();
                getDescription();
                getFoundation();
                getStudents();
                getOffers();
                return null;
            }
        };

    }


}

package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */

@ParseClassName("Student")
public class Student extends User {

    //TODO: a thousand attributes
    protected static final String NAME_FIELD = "name";
    protected static final String SURNAME_FIELD = "surname";
    protected static final String DEGREES_FIELD = "degrees"; //dovrebbe tornare un array
    protected static final String SEX_FIELD = "sex";
    protected static final String BIRTH_FIELD = "birthDate";
    protected static final String BIRTH_CITY = "birthCity";
    protected static final String ADDRESS_FIELD = "address";
    protected static final String CITY_FIELD = "city";
    protected static final String POSTAL_CODE_FIELD = "postalCode";
    protected static final String NATION_FIELD = "nation";
    protected static final String PHONE_FIELD = "phones";
    protected static final String LANGUAGE_FIELD = "languages";
    protected static final String FAVOURITES_FIELD = "favourites";
    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";




    public Student(){
        super();
    }


    /*GETTER METHODS*/
    public String getName() {
        return this.getString(NAME_FIELD);
    }
    public String getSurname() {
        return this.getString(SURNAME_FIELD);
    }
    public ArrayList<Degree> getDegrees() {

        ArrayList<Degree> degrees = new ArrayList<Degree>();
        List<Object> list = this.getList(DEGREES_FIELD);

        if(list!=null)
            for (Object o : list) {

                if(o instanceof Degree){
                    Degree d =(Degree)o;

                    try {
                        d.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    degrees.add(d);
                }
            }

        return degrees;
    }
    public String getSex() {
        return getString(SEX_FIELD);
    }
    public Date getBirth(){
        return this.getDate(BIRTH_FIELD);
    }
    public String getBirthCity(){
        return this.getString(BIRTH_CITY);
    }
    public String getAddress(){
        return this.getString(ADDRESS_FIELD);
    }
    public String getCity(){
        return this.getString(CITY_FIELD);
    }
    public String getPostalCode(){
        return this.getString(POSTAL_CODE_FIELD);
    }
    public String getNation(){
        return this.getString(NATION_FIELD);
    }
    public ArrayList<Telephone> getPhones(){

        ArrayList<Telephone> phones = new ArrayList<Telephone>();
        List<Object> list = this.getList(PHONE_FIELD);

        if(list != null)
            for (Object o : list)
                if (o instanceof Telephone){

                    Telephone t = (Telephone)o;

                    try {
                        t.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    phones.add(t);
                }

        return phones;
    }
    public ArrayList<Language> getLanguages(){

        ArrayList<Language> languages = new ArrayList<Language>();
        List<Object> list = this.getList(LANGUAGE_FIELD);

        if(list!=null)
            for (Object o : list) {

                if(o instanceof Language){
                    Language l =(Language)o;

                    try {
                        l.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    languages.add(l);
                }
            }

        return languages;
    }
    public ArrayList<Company> getFavourites( ){

        ArrayList<Company> favourites = new ArrayList<Company>();
        List<Object> list = this.getList(FAVOURITES_FIELD);

        if(list!= null)
            for(Object o:list){
                if(o instanceof Company){

                    Company c = (Company)o;
                    try {
                        c.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    favourites.add(c);
                }
            }

        return favourites;
    }

    /* END GETTER METHODS*/

    public void setName(String name){

        this.put(NAME_FIELD,name);
    }
    public void setSurname(String surname){

        this.put(SURNAME_FIELD,surname);
    }
    public void addDegree(Degree degree){

        this.addUnique(DEGREES_FIELD, degree);
    }
    public void removeDegree(Degree degree){

        this.removeAll(DEGREES_FIELD,Arrays.asList(degree));
    }
    public void setSex(String sex){

        this.put(SEX_FIELD,sex);
    }

    public void setBirth(Date birth){

        this.put(BIRTH_FIELD,birth);
    }
    public void setBirthCity(String birthCity){

        this.put(BIRTH_CITY,birthCity);
    }
    public void setAddress(String address){

        this.put(ADDRESS_FIELD,address);
    }
    public void setCity(String city){

        this.put(CITY_FIELD,city);
    }
    public void setPostalCode(String CAP){

        this.put(POSTAL_CODE_FIELD,CAP);
    }
    public void setNation(String nation){

        this.put(NATION_FIELD,nation);
    }
    public void addPhone(Telephone phone){

        this.addUnique(PHONE_FIELD, phone);
    }
    public void removePhone(Telephone phone) {
        
        this.removeAll(PHONE_FIELD,Arrays.asList(phone));
    }
    public void addLanguage(Language language){

        this.addUnique(LANGUAGE_FIELD, language);
    }
    public void removeLanguage(Language language) {

        this.removeAll(LANGUAGE_FIELD,Arrays.asList(language));
    }

    public void addFavourites(Company company)
    {
        this.addUnique(FAVOURITES_FIELD,company);

    }

    /*END SETTER METHODS*/

}

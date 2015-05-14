package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */

@ParseClassName("Student")
public class Student extends User {

    protected static final String NAME_FIELD = "name";
    protected static final String SURNAME_FIELD = "surname";
    protected static final String DEGREES_FIELD = "degrees";
    protected static final String SEX_FIELD = "sex";
    protected static final String BIRTH_DATE_FIELD = "birthDate";
    protected static final String BIRTH_CITY_FIELD = "birthCity";
    protected static final String ADDRESS_FIELD = "address";
    protected static final String CITY_FIELD = "city";
    protected static final String POSTAL_CODE_FIELD = "postalCode";
    protected static final String NATION_FIELD = "nation";
    protected static final String LANGUAGE_FIELD = "languages";
    protected static final String CERTIFICATE_FIELD = "certificates";
    protected static final String FAVOURITES_FIELD = "favourites";
    protected static final String DESCRIPTION_FIELD = "description";
    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";

    protected String name;
    protected String surname;
    protected String sex;
    protected ArrayList<Degree> degrees;
    protected Date birthDate;
    protected String birthCity;
    protected String address;
    protected String city;
    protected String postalCode;
    protected String nation;
    protected String description;
    protected ArrayList<Language> languages;
    protected ArrayList<Certificate> certificates;
    protected ArrayList<CompanyOffer> favourites;

    public Student(){

        super();

        name = null;
        surname = null;
        sex = null;
        degrees = new ArrayList<Degree>();
        birthCity = null;
        birthDate = null;
        address = null;
        city = null;
        postalCode = null;
        nation = null;
        description = null;
        phones = new ArrayList<Telephone>();
        languages = new ArrayList<Language>();
        favourites = new ArrayList<CompanyOffer>();
        certificates = new ArrayList<Certificate>();

        isCached.put(NAME_FIELD,false);
        isCached.put(SURNAME_FIELD,false);
        isCached.put(SEX_FIELD,false);
        isCached.put(DEGREES_FIELD,false);
        isCached.put(BIRTH_CITY_FIELD,false);
        isCached.put(BIRTH_DATE_FIELD,false);
        isCached.put(ADDRESS_FIELD,false);
        isCached.put(CITY_FIELD,false);
        isCached.put(POSTAL_CODE_FIELD,false);
        isCached.put(NATION_FIELD,false);
        isCached.put(LANGUAGE_FIELD,false);
        isCached.put(FAVOURITES_FIELD, false);
        isCached.put(CERTIFICATE_FIELD,false);
        isCached.put(DESCRIPTION_FIELD, false);
    }


    /*GETTER METHODS*/
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
    public ArrayList<Degree> getDegrees() {

        if(isCached.get(DEGREES_FIELD))
            return this.degrees;

        ParseRelation<Degree> tmp= getRelation(DEGREES_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Degree>() {
            @Override
            public void done(List<Degree> degreesList, ParseException e) {

                if(degreesList!= null)
                    for(Degree d:degreesList)
                        degrees.add(d);
            }
        });

        isCached.put(DEGREES_FIELD, true);
        return degrees;
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
    public String getAddress(){

        if(isCached.get(ADDRESS_FIELD))
            return address;

        address = this.getString(ADDRESS_FIELD);
        isCached.put(ADDRESS_FIELD,true);
        return address;
    }
    public String getCity(){

        if(isCached.get(CITY_FIELD))
            return city;

        city = this.getString(CITY_FIELD);
        isCached.put(CITY_FIELD,true);
        return city;
    }
    public String getPostalCode(){

        if(isCached.get(POSTAL_CODE_FIELD))
            return postalCode;

        postalCode = this.getString(POSTAL_CODE_FIELD);
        isCached.put(POSTAL_CODE_FIELD,true);
        return postalCode;
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
    public ArrayList<Language> getLanguages(){

        if(isCached.get(LANGUAGE_FIELD))
            return languages;

        ParseRelation<Language> tmp= getRelation(LANGUAGE_FIELD);

        tmp.getQuery().findInBackground(new FindCallback<Language>() {
            @Override
            public void done(List<Language> languagesList, ParseException e) {

                if(languagesList!= null)
                    for(Language l:languagesList)
                        languages.add(l);
            }
        });

        isCached.put(LANGUAGE_FIELD,true);
        return languages;
    }
    public ArrayList<Certificate> getCertificates(){

        if(isCached.get(CERTIFICATE_FIELD))
            return certificates;

        ParseRelation<Certificate> tmp = getRelation(CERTIFICATE_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Certificate>() {
            @Override
            public void done(List<Certificate> results, ParseException e) {

                if(results!=null)
                    for(Certificate c:results)
                        certificates.add(c);
            }
        });

        isCached.put(CERTIFICATE_FIELD,true);
        return certificates;
    }
    public ArrayList<CompanyOffer> getFavourites( ){

        if(isCached.get(FAVOURITES_FIELD))
            return favourites;

        ParseRelation<CompanyOffer> tmp = getRelation(FAVOURITES_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<CompanyOffer>() {
            @Override
            public void done(List<CompanyOffer> results, ParseException e) {

                if(results!= null)
                    for(CompanyOffer co: results)
                        favourites.add(co);
            }
        });

        isCached.put(FAVOURITES_FIELD,true);
        return favourites;
    }

    /* END GETTER METHODS*/

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
    public void addDegree(Degree degree){

        degrees.add(degree);
        getRelation(DEGREES_FIELD).add(degree);
    }
    public void removeDegree(Degree degree){

        degrees.remove(degree);
        getRelation(DEGREES_FIELD).remove(degree);
    }
    public void setSex(String sex){

        this.sex = sex;
        isCached.put(SEX_FIELD,true);
        this.put(SEX_FIELD,sex);
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
    public void setAddress(String address){

        this.address = address;
        isCached.put(ADDRESS_FIELD,true);
        this.put(ADDRESS_FIELD,address);
    }
    public void setCity(String city){

        this.city = city;
        isCached.put(CITY_FIELD,true);
        this.put(CITY_FIELD,city);
    }
    public void setPostalCode(String CAP){

        this.postalCode = CAP;
        isCached.put(POSTAL_CODE_FIELD,true);
        this.put(POSTAL_CODE_FIELD,CAP);
    }
    public void setNation(String nation){

        this.nation = nation;
        isCached.put(NATION_FIELD,true);
        this.put(NATION_FIELD,nation);
    }
    public void setDescription(String description){

        this.description = description;
        isCached.put(DESCRIPTION_FIELD,true);
        this.put(DESCRIPTION_FIELD,description);
    }
    public void addLanguage(Language language){

        languages.add(language);
        getRelation(LANGUAGE_FIELD).add(language);
    }
    public void removeLanguage(Language language) {

        languages.remove(language);
        getRelation(LANGUAGE_FIELD).remove(language);
    }
    public void addCertificate(Certificate certificate){

        certificates.add(certificate);
        getRelation(CERTIFICATE_FIELD).add(certificate);
    }
    public void removeCertificate(Certificate certificate){

        this.certificates.remove(certificate);
        getRelation(CERTIFICATE_FIELD).remove(certificate);
    }
    public void addFavourites(CompanyOffer companyOffer){

        favourites.add(companyOffer);
        getRelation(FAVOURITES_FIELD).add(companyOffer);

    }
    public void removeFavourites(CompanyOffer companyOffer) {
        {
            favourites.remove(companyOffer);
            getRelation(FAVOURITES_FIELD).remove(companyOffer);
        }
    }
    /*END SETTER METHODS*/


    @Override
    public void cacheData() {
        super.cacheData();

        getName();
        getSurname();
        getSex();
        getDegrees();
        getBirth();
        getBirthCity();
        getAddress();
        getCity();
        getPostalCode();
        getNation();
        getDescription();
        getLanguages();
        getCertificates();
        getFavourites();
    }

    public void printCacheContent(){

        super.printCacheContent();
        Log.println(Log.ASSERT,"STUDENT","name: " + name);
        Log.println(Log.ASSERT,"STUDENT","surname: " + surname);
        Log.println(Log.ASSERT,"STUDENT","sex: " + sex);
        Log.println(Log.ASSERT,"STUDENT","address: " + address);
        Log.println(Log.ASSERT,"STUDENT","city: " + city);
        Log.println(Log.ASSERT,"STUDENT","birthCity: " + birthCity);
        Log.println(Log.ASSERT,"STUDENT","birthDate: " + birthDate.toString());
    }
}

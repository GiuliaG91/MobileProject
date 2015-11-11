package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.io.File;
import java.net.URL;
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
    protected static final String COURSES_FIELD = "courses";
    protected static final String FAVOURITES_FIELD = "favourites";
    protected static final String DESCRIPTION_FIELD = "description";
    protected static final String ADDRESS_LOCATION_FIELD = "address_location";
    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";
    public static final String COMPANIES_FIELD = "companies";
    public static final String CURRICULUM_FIELD = "curriculum";

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
    protected ParseGeoPoint addressLocation;
    protected ArrayList<Language> languages;
    protected ArrayList<Certificate> certificates;
    protected ArrayList<CompanyOffer> favourites;
    protected ArrayList<Company> companies;
    protected ArrayList<Course> courses;
    protected byte[] tempBytes;

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
        addressLocation = null;
        phones = new ArrayList<Telephone>();
        languages = new ArrayList<Language>();
        favourites = new ArrayList<CompanyOffer>();
        certificates = new ArrayList<Certificate>();
        courses = new ArrayList<Course>();
        companies=new ArrayList<>();

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
        isCached.put(COURSES_FIELD,false);
        isCached.put(DESCRIPTION_FIELD, false);
        isCached.put(ADDRESS_LOCATION_FIELD,false);
        isCached.put(COMPANIES_FIELD, false);
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

    public ParseGeoPoint getAddressLocation(){

        if(isCached.get(ADDRESS_LOCATION_FIELD))
            return addressLocation;

        addressLocation = (ParseGeoPoint)get(ADDRESS_LOCATION_FIELD);
        isCached.put(ADDRESS_LOCATION_FIELD,true);
        return addressLocation;
    }

    public List<Company> getCompanies( ){

        if(isCached.get(COMPANIES_FIELD))
            return companies;

        ParseRelation<Company> tmp= getRelation(COMPANIES_FIELD);
        List<Company> result= null;
        try {
            result = tmp.getQuery().find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        isCached.put(COMPANIES_FIELD,true);
        return result;
    }

    public byte[] getCurriculum() throws ParseException{

        ParseFile curriculumFile = (ParseFile)get(CURRICULUM_FIELD);


        if(curriculumFile!= null){

            return curriculumFile.getData();
        }

        return null;
    }

    public ArrayList<Course> getCourses() {

        if(isCached.get(COURSES_FIELD))
            return this.courses;

        ParseRelation<Course> tmp= getRelation(COURSES_FIELD);
        tmp.getQuery().findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> coursesList, ParseException e) {

                if(coursesList!= null)
                    for(Course c:coursesList)
                        courses.add(c);
            }
        });

        isCached.put(COURSES_FIELD, true);
        return courses;
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
    public void addCourse(Course course){

        courses.add(course);
        ParseRelation<Course> r = getRelation(COURSES_FIELD);
        r.add(course);
    }
    public void removeCourse(Course course){

        courses.remove(course);
        ParseRelation<Course> r = getRelation(COURSES_FIELD);
        r.remove(course);
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
    public void setAddressLocation(ParseGeoPoint addressLocation){

        this.addressLocation = addressLocation;
        isCached.put(ADDRESS_LOCATION_FIELD,true);

        if(addressLocation == null)
            remove(ADDRESS_LOCATION_FIELD);
        else
            put(ADDRESS_LOCATION_FIELD,addressLocation);
    }
    public void addCompany(Company c)
    {
        companies.add(c);
        getRelation(COMPANIES_FIELD).add(c);
    }
    public void removeCompany(Company c)
    {
        companies.remove(c);
        getRelation(COMPANIES_FIELD).remove(c);
    }

    public void setCurriculum(byte[] file){

        final ParseFile curriculum = new ParseFile("curriculum" + getName() + getSurname() + ".pdf",file);
        curriculum.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    Log.println(Log.ASSERT,"STUDENT_FIELD","curriculum updated successfully");
                    Student.this.put(CURRICULUM_FIELD,curriculum);
                    Student.this.saveEventually();
                }
            }
        });
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
        getAddressLocation();
        getCompanies();
    }

    public void printCacheContent(){

        super.printCacheContent();
        Log.println(Log.ASSERT,"STUDENT_FIELD","name: " + name);
        Log.println(Log.ASSERT,"STUDENT_FIELD","surname: " + surname);
        Log.println(Log.ASSERT,"STUDENT_FIELD","sex: " + sex);
        Log.println(Log.ASSERT,"STUDENT_FIELD","address: " + address);
        Log.println(Log.ASSERT,"STUDENT_FIELD","city: " + city);
        Log.println(Log.ASSERT,"STUDENT_FIELD","birthCity: " + birthCity);
        Log.println(Log.ASSERT,"STUDENT_FIELD","birthDate: " + birthDate.toString());
    }
}

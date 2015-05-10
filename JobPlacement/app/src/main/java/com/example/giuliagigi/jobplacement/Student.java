package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;

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
    protected static final String PHONE_FIELD = "phones";
    protected static final String LANGUAGE_FIELD = "languages";
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

        this.degrees = degrees;
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
    public ArrayList<Telephone> getPhones(){

        if(isCached.get(PHONE_FIELD))
            return phones;

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

        this.phones = phones;
        isCached.put(PHONE_FIELD,true);
        return phones;
    }
    public ArrayList<Language> getLanguages(){

        if(isCached.get(LANGUAGE_FIELD))
            return languages;

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

        this.languages = languages;
        isCached.put(LANGUAGE_FIELD,true);
        return languages;
    }
    public ArrayList<CompanyOffer> getFavourites( ){

        if(isCached.get(FAVOURITES_FIELD))
            return favourites;

        ArrayList<CompanyOffer> favourites = new ArrayList<>();
        List<Object> list = this.getList(FAVOURITES_FIELD);

        if(list!= null)
            for(Object o:list){
                if(o instanceof CompanyOffer){

                    CompanyOffer c = (CompanyOffer)o;
                    try {
                        c.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    favourites.add(c);
                }
            }

        this.favourites = favourites;
        isCached.put(FAVOURITES_FIELD,true);
        return favourites;
    }

    public String getDescription() {
        if(isCached.get(DESCRIPTION_FIELD))
            return description;

        description = this.getString(DESCRIPTION_FIELD);
        isCached.put(DESCRIPTION_FIELD,true);
        return description;
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
        this.addUnique(DEGREES_FIELD, degree);
    }
    public void removeDegree(Degree degree){

        degrees.remove(degree);
        removeAll(DEGREES_FIELD,Arrays.asList(degree));
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
    public void addPhone(Telephone phone){

        phones.add(phone);
        this.addUnique(PHONE_FIELD, phone);
    }
    public void removePhone(Telephone phone) {

        phones.remove(phone);
        removeAll(PHONE_FIELD,Arrays.asList(phone));
    }
    public void addLanguage(Language language){

        languages.add(language);
        this.addUnique(LANGUAGE_FIELD, language);
    }
    public void removeLanguage(Language language) {

        languages.remove(language);
        this.removeAll(LANGUAGE_FIELD,Arrays.asList(language));
    }
    public void addFavourites(CompanyOffer companyOffer){

        favourites.add(companyOffer);
        this.addUnique(FAVOURITES_FIELD,companyOffer);

    }

    public void removeFavourites(CompanyOffer companyOffer) {
        {
            favourites.remove(companyOffer);
            this.removeAll(FAVOURITES_FIELD, Arrays.asList(companyOffer));
        }
    }

    public void setDescription(String description){

        this.description = description;
        isCached.put(DESCRIPTION_FIELD,true);
        this.put(DESCRIPTION_FIELD,description);
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
        getPhones();
        getLanguages();
        getFavourites();
        getDescription();
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

package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private static final String  SAlARY_FIELD = "salary";
    private static final String  DESCRIPTION_FIELD= "description";
    private static final String COMPANY_FIELD=  "company";

    private static final String TAGS_FIELD=  "tags";


    public CompanyOffer(){
        super();
    }


    /*GETTER*/

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

    public String getLocation() {
        return this.getString(LOCATION_FIELD);
    }

    public String getSAlARY() {
        return this.getString(SAlARY_FIELD);
    }

    public String getDescription() {
        return this.getString(DESCRIPTION_FIELD);
    }

    public Company getCompany() {return (Company)this.get(COMPANY_FIELD);}

    public ArrayList<Tag> getTags( ){

        ArrayList<Tag> tags = new ArrayList<Tag>();
        List<Object> list = this.getList(TAGS_FIELD);

        if(list!= null)
            for(Object o:list){
                if(o instanceof Tag){

                    Tag t = (Tag)o;
                    try {
                        t.fetchIfNeeded();
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }
                    tags.add(t);
                }
            }

        return tags;
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

        this.put(VALIDITY_FIELD,validity);
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
    public void setSalary(String salay){

        this.put(SAlARY_FIELD,salay);
    }
    public void setDescription(String description){

        this.put(DESCRIPTION_FIELD,description);
    }
    public void setCompany(Company company)
    {
        this.put(COMPANY_FIELD,company);
    }

    public void addTag(Tag t){ this.addUnique(TAGS_FIELD, Arrays.asList(t));}


    /*END SETTER METHODS*/




}

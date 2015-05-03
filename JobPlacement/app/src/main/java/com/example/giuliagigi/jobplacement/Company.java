package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseException;

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
    protected static final String OFFERS_FIELD = "offers";

    public Company(){
        super();
    }

    public void setName(String name){
        this.put(NAME_FIELD,name);
    }
    public String getName(){
        return this.getString(NAME_FIELD);
    }

    public void setFiscalCode(String fc){
        this.put(FISCAL_CODE_FIELD,fc);
    }
    public String getFiscalCode(){
        return this.getString(FISCAL_CODE_FIELD);
    }

    public void setField(String field) {
        this.put(FIELD_FIELD,field);
    }
    public String getField(){
        return this.getString(FIELD_FIELD);
    }


     public void addOffer(CompanyOffer offer){
          this.addUnique(OFFERS_FIELD,offer);
     }

    public ArrayList<CompanyOffer> getOffers( ){

        ArrayList<CompanyOffer> offers = new ArrayList<CompanyOffer>();
        List<Object> list = this.getList(OFFERS_FIELD);

        if(list!= null)
            for(Object o:list){
                if(o instanceof CompanyOffer){

                    CompanyOffer of = (CompanyOffer)o;
                    try {
                        of.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    offers.add(of);
                }
            }

        return offers;
    }

    public void removeOffer(CompanyOffer offer)
    {
        this.removeAll(OFFERS_FIELD, Arrays.asList(offer));
    }
}

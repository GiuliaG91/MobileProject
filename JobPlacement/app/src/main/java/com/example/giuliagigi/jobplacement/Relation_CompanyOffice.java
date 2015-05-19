package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by MarcoEsposito90 on 19/05/2015.
 */

@ParseClassName("Relation_CompanyOffice")
public class Relation_CompanyOffice extends ParseObject{

    public static final String COMPANY_FIELD = "company";
    public static final String OFFICE_FIELD = "office";

    public Relation_CompanyOffice(){
        super();
    }

    public void setCompany(Company company){

        put(COMPANY_FIELD,company);
    }
    public Company getCompany(){

        return (Company)get(COMPANY_FIELD);
    }


    public void setOffice(Office office){

        put(OFFICE_FIELD,office);
    }
    public Office getOffice(){

        return (Office)get(OFFICE_FIELD);
    }
}

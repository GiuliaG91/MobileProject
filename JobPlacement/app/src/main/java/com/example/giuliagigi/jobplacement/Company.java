package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;

/**
 * Created by MarcoEsposito90 on 21/04/2015.
 */
@ParseClassName("Company")
public class Company extends User {

    protected static final String NAME_FIELD = "Company_Name";
    protected static final String FISCAL_CODE_FIELD = "Fiscal_Code";

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
}

package com.example.giuliagigi.jobplacement;

import java.util.List;

/**
 * Created by pietro on 14/05/2015.
 */
public class OfferFilterStatus {

    //Use this class only for set and retrive data filters


    private  List<Tag> tag_list=null;
    private  List<String> contract_list=null;
    private  List<String> term_list=null;
    private  List<String> field_list=null;
    private  List<String> location_list=null;
    private  List<String> salary_list=null;
    private  boolean Valid=false;


    public void setFilters(List<Tag> tag_list,
                          List<String> contract_list,
                             List<String> term_list,
                              List<String> field_list,
                              List<String> location_list,
                              List<String> salary_list)
    {

        this.tag_list=tag_list;
        this.contract_list=contract_list;
        this.term_list=term_list;
        this.field_list=field_list;
        this.location_list=location_list;
        this.salary_list=salary_list;
    }


    public boolean isValid() {
        return Valid;
    }

    public void setValid(boolean valid) {
        Valid = valid;
    }

    public List<Tag> getTag_list() {
        return tag_list;
    }

    public List<String> getContract_list() {
        return contract_list;
    }

    public List<String> getTerm_list() {
        return term_list;
    }

    public List<String> getField_list() {
        return field_list;
    }

    public List<String> getLocation_list() {
        return location_list;
    }

    public List<String> getSalary_list() {
        return salary_list;
    }
}

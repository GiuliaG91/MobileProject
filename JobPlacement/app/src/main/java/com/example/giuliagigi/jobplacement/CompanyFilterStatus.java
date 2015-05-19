package com.example.giuliagigi.jobplacement;

import java.util.List;

/**
 * Created by pietro on 19/05/2015.
 */
public class CompanyFilterStatus {

    private List<Tag> tag_list=null;
    private  List<String> field_list=null;
    private  List<String> location_list=null;
    private  boolean Valid=false;



    public void setFilters(List<Tag> tag_list,
                           List<String> field_list,
                           List<String> location_list)
    {

        this.tag_list=tag_list;
        this.field_list=field_list;
        this.location_list=location_list;

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


    public List<String> getField_list() {
        return field_list;
    }

    public List<String> getLocation_list() {
        return location_list;
    }

}

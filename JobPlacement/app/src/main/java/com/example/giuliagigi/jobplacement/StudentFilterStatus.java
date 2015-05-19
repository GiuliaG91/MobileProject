package com.example.giuliagigi.jobplacement;

import java.util.List;

/**
 * Created by pietro on 18/05/2015.
 */
public class StudentFilterStatus {


    private List<Tag> tag_list=null;
    private  List<String> degree_list=null;
    private  List<String> field_list=null;
    private  boolean Valid=false;



    public void setFilters(List<Tag> tag_list,
                           List<String> degree_list,
                           List<String> field_list)
    {

        this.tag_list=tag_list;
        this.degree_list=degree_list;
        this.field_list=field_list;

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

    public List<String> getDegree_list() {
        return degree_list;
    }


    public List<String> getField_list() {
        return field_list;
    }

}

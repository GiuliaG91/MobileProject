package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;


/**
 * Created by MarcoEsposito90 on 10/05/2015.
 */

@ParseClassName("Certificate")
public class Certificate extends ParseObject {


    public static final String TITLE_FIELD = "Title";
    public static final String DESCRIPTION_FIELD = "Description";
    public static final String MARK_FIELD = "Mark";
    public static final String DATE_FIELD = "Date";
    public static final String STUDENT_FIELD = "Student";

    public Certificate(){
        super();
    }

    public String getTitle() {
        return this.getString(TITLE_FIELD);
    }
    public String getDescription() {
        return this.getString(DESCRIPTION_FIELD);
    }
    public String getMark() {
        return this.getString(MARK_FIELD);
    }
    public Date getDate() {
        return this.getDate(DATE_FIELD);
    }
    public Student getStudent(){
        return (Student)this.get(STUDENT_FIELD);
    }

    public void setTitle(String title){

        this.put(TITLE_FIELD,title);
    }
    public void setDescription(String description){

        this.put(DESCRIPTION_FIELD,description);
    }
    public void setMark(String mark){

        this.put(MARK_FIELD,mark);
    }
    public void setDate(Date date){

        this.put(DATE_FIELD,date);
    }
    public void setStudent(Student student){

        this.put(STUDENT_FIELD,student);
    }

}

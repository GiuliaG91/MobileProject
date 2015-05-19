package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by MarcoEsposito90 on 19/05/2015.
 */

@ParseClassName("Relation_StudentDegree")

public class Relation_StudentDegree extends ParseObject{

    public static final String STUDENT_FIELD = "student";
    public static final String DEGREE_FIELD = "degree";

    public Relation_StudentDegree(){
        super();
    }

    public void setStudent(Student student){

        put(STUDENT_FIELD,student);
    }
    public Student getStudent(){

        return (Student)get(STUDENT_FIELD);
    }


    public void setDegree(Degree degree){

        put(DEGREE_FIELD,degree);
    }
    public Degree getDegree(){

        return (Degree)get(DEGREE_FIELD);
    }
}

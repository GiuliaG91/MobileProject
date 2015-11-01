package com.example.giuliagigi.jobplacement;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
import java.util.ArrayList;

public class Course {

    private String code;
    private String professor;
    private String name;
    private ArrayList<Lecture> lectures;

    public Course(){

        this(null,null,null);
    }

    public Course(String code, String professor, String name){

        this.code = code;
        this.professor = professor;
        this.name = name;
        lectures = new ArrayList<Lecture>();
    }



    // --------------------------------------------------------------------
    // ---------------- GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    public String getCode() { return code; }

    public void setCode(String code) {  this.code = code;   }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --------------------------------------------------------------------
    // ------------ END GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    public void addLecture(Lecture l){

        lectures.add(l);
    }

    public int getLectureNumber(){

        return lectures.size();
    }

    public Lecture getLecture(int i){

        if(i<0|| i>=getLectureNumber()) return null;

        return lectures.get(i);
    }

    public ArrayList<Lecture> getLecturesList(){

        return lectures;
    }
}
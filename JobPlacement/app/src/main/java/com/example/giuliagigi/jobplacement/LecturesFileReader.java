package com.example.giuliagigi.jobplacement;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */

import android.util.JsonReader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LecturesFileReader {

    /* STATIC FIELDS        */

    public static final String COURSES_JSON_FILE_NAME = "courses.json";
    public static final String COURSES_CODE_FIELD = "code";
    public static final String COURSE_NAME_FIELD = "name";
    public static final String COURSE_PROFESSOR_FIELD = "professor";
    public static final String COURSE_LECTURES_FIELD = "lectures";
    public static final String LECTURE_DAYINWEEK_FIELD = "dayInWeek";
    public static final String LECTURE_STARTHOUR_FIELD = "startHour";
    public static final String LECTURE_STARTMINUTE_FIELD = "startMinute";
    public static final String LECTURE_ROOM_FIELD = "room";
    public static final String LECTURE_ENDHOUR_FIELD = "endHour";
    public static final String LECTURE_ENDMINUTE_FIELD = "endMinute";
    private final static String TAG = "LecturesReader - LOG: ";

    /* DATA FIELDS        */

    private HashMap<String,Course> courses;
    private ArrayList<String> courseNames, professorNames;

    /*  CONSTRUCTOR     */

    public LecturesFileReader(){

        courses = new HashMap<String,Course>();
    }



    /*--------------------------------------------------------------------------------*/




    //////////////////////////////////////////////////////////////////////////////////
    /////// COURSE SEARCH METHODS ////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

    private boolean containsCourseByCode(String code){

        return courses.containsKey(code);
    }

    private Course getCourseByCode(String code){

        return courses.get(code);
    }

    private boolean containsCourseByName(String name){

        Log.println(Log.ASSERT, TAG, "containsCourseByName started");

        ArrayList<Course> retrievedCourses = new ArrayList<Course>(courses.values());

        for(int i = 0; i<retrievedCourses.size();i++)
            if(retrievedCourses.get(i).getName().equalsIgnoreCase(name)) return true;


        Log.println(Log.ASSERT, TAG, "no course found");

        return false;
    }

    private ArrayList<Course> getCoursesByName(String name){

        Log.println(Log.ASSERT, TAG, "getCoursesByName started: " + name);

        ArrayList<Course> allCourses = new ArrayList<Course>(courses.values());
        ArrayList<Course> retrievedCourses = new ArrayList<Course>();

        for(int i=0;i<allCourses.size();i++)
            if(allCourses.get(i).getName().equalsIgnoreCase(name)) retrievedCourses.add(allCourses.get(i));

        Log.println(Log.ASSERT, TAG, "getCoursesByName finished");
        return retrievedCourses;

    }

    private boolean containsCourseByProfessorName(String professorName){

        Log.println(Log.ASSERT, TAG, "containsCourseByProfessorName started");

        ArrayList<Course> retrievedCourses = new ArrayList<Course>(courses.values());

        for(int i = 0; i<retrievedCourses.size();i++)
            if(retrievedCourses.get(i).getProfessor().equalsIgnoreCase(professorName)) return true;


        Log.println(Log.ASSERT, TAG, "no course found");
        return false;
    }

    private ArrayList<Course> getCoursesByProfessorName(String professorName){

        Log.println(Log.ASSERT, TAG, "getCoursesByProfessorName started");

        ArrayList<Course> allCourses = new ArrayList<Course>(courses.values());
        ArrayList<Course> retrievedCourses = new ArrayList<Course>();

        for(int i=0;i<allCourses.size();i++){


            if(allCourses.get(i).getProfessor().equalsIgnoreCase(professorName)){

                retrievedCourses.add(allCourses.get(i));
            }

        }

        Log.println(Log.ASSERT, TAG, "getCoursesByName finished");
        return retrievedCourses;

    }

    private ArrayList<Course> getCoursesByNameAndProfessor(String course, String professor){

        ArrayList<Course> retrievedCourses = new ArrayList<Course>();

        if(!containsCourseByName(course)){

            if(!containsCourseByProfessorName(professor)) return retrievedCourses;
            else return getCoursesByProfessorName(professor);
        }
        else {

            if(!containsCourseByProfessorName(professor)) return getCoursesByName(course);
            else {

                ArrayList<Course> coursesByName = getCoursesByName(course);

                for(int i=0; i<coursesByName.size();i++)
                    if(coursesByName.get(i).getProfessor().equalsIgnoreCase(professor))
                        retrievedCourses.add(coursesByName.get(i));
            }
        }

        return retrievedCourses;
    }

    public ArrayList<String> getCourseNames(){

        if(this.courseNames != null) return  this.courseNames;

        ArrayList<Course> allCourses = new ArrayList<Course>(courses.values());
        ArrayList<String> coursesNames = new ArrayList<>();

        for(int i = 0; i < allCourses.size(); i++){
            String name = allCourses.get(i).getName();
            if(!coursesNames.contains(name)){
                coursesNames.add(name);
            }
        }

        this.courseNames = coursesNames;
        return coursesNames;
    }

    public ArrayList<String> getProfessorNames(){

        if(this.professorNames != null) return professorNames;

        ArrayList<Course> allCourses = new ArrayList<Course>(courses.values());
        ArrayList<String> professorNames = new ArrayList<>();

        for(int i = 0; i < allCourses.size(); i++){
            String name = allCourses.get(i).getProfessor();
            if(!professorNames.contains(name)){
                professorNames.add(name);
            }
        }

        this.professorNames = professorNames;
        return professorNames;
    }

    public String getProfessorByCourse(String course){
        ArrayList<Course> allCourses = getCoursesByName(course);
        if(allCourses.size()==1) {
            String professor = allCourses.get(0).getProfessor();
            return professor;
        }
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////
    /////// COURSE SEARCH METHODS - END //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

    /*--------------------------------------------------------------------------------*/


    //////////////////////////////////////////////////////////////////////////////////
    /////// LECTURES DELIVERY METHODS ////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

    private ArrayList<Lecture> getDayLecturesByCourse(String course,int day){

        Log.println(Log.ASSERT,TAG,"getDayLecturesByCourse started");

        ArrayList<Lecture> lectures = new ArrayList<Lecture>();

        if(!containsCourseByName(course)) return lectures;

        ArrayList<Course> retrievedCourses = getCoursesByName(course);

        Log.println(Log.ASSERT,TAG,"getDayLecturesByCourse searching.....");
        for(int i=0; i<retrievedCourses.size();i++)
            for(int j=0; j<retrievedCourses.get(i).getLecturesList().size();j++)
                if(retrievedCourses.get(i).getLecture(j).getDayInWeek() == day)
                    lectures.add(retrievedCourses.get(i).getLecture(j));


        Log.println(Log.ASSERT,TAG,"getDayLecturesByCourse complete");

        return lectures;
    }

    private ArrayList<Lecture> getDayLecturesByProfessor(String professor,int day){

        ArrayList<Lecture> lectures = new ArrayList<Lecture>();

        if(!containsCourseByProfessorName(professor)) return lectures;

        ArrayList<Course> retrievedCourses = getCoursesByProfessorName(professor);

        for(int i=0; i<retrievedCourses.size();i++)
            for(int j=0; j<retrievedCourses.get(i).getLecturesList().size();j++)
                if(retrievedCourses.get(i).getLecture(j).getDayInWeek() == day)
                    lectures.add(retrievedCourses.get(i).getLecture(j));

        return lectures;
    }

    public ArrayList<Lecture> getDayLectures(String course, String professor, int day){

        Log.println(Log.ASSERT,TAG,"Get day lectures started");


        ArrayList<Lecture> lectures = new ArrayList<Lecture>();

        if(course == null){

            if(professor == null) return lectures;
            else return getDayLecturesByProfessor(professor,day);
        }

        if(professor == null) return getDayLecturesByCourse(course,day);

        ArrayList<Course> retrievedCourses = getCoursesByNameAndProfessor(course,professor);

        Log.println(Log.ASSERT,TAG,"Searching day lectures........");

        for(int i=0;i<retrievedCourses.size();i++)
            for(int j=0;j<retrievedCourses.get(i).getLecturesList().size();j++)
                if(retrievedCourses.get(i).getLecture(j).getDayInWeek()==day)
                    lectures.add(retrievedCourses.get(i).getLecture(j));

        Log.println(Log.ASSERT,TAG,"Get day lectures complete");

        return lectures;
    }

    //////////////////////////////////////////////////////////////////////////////////
    /////// LECTURES DELIVERY METHODS - END //////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////


    /*--------------------------------------------------------------------------------*/


    /////////////////////////////////////////////////////////////////////////////////////////
    ///////// JSON FILE READING METHODS /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    private void readLecture(JsonReader reader,Course c) throws IOException {


        Lecture l = new Lecture(c.getName(),c.getProfessor());
        reader.beginObject();

        while (reader.hasNext()){

            String lectureField = reader.nextName();

            if(lectureField.equals(LECTURE_DAYINWEEK_FIELD))        l.setDayInWeek(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_STARTHOUR_FIELD))   l.setStartHour(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_STARTMINUTE_FIELD)) l.setStartMinute(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_ENDHOUR_FIELD))     l.setEndHour(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_ENDMINUTE_FIELD))   l.setEndMinute(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_ROOM_FIELD))        l.setRoom(reader.nextString());
            else {

                reader.skipValue();
            }
        }

        reader.endObject();
        c.addLecture(l);
    }

    private void readLecturesList(JsonReader reader, Course c) throws IOException{

        reader.beginArray();

        while (reader.hasNext()){

            readLecture(reader,c);
        }
        reader.endArray();

    }

    private Course readCourse(JsonReader reader) throws IOException{

        Course c = new Course();
        Log.println(Log.ASSERT,TAG,"Start reading object course");
        reader.beginObject();

        while (reader.hasNext()){

            String courseField = reader.nextName();

            if(courseField.equals(COURSES_CODE_FIELD))    c.setCode(reader.nextString());
            else if(courseField.equals(COURSE_NAME_FIELD)) c.setName(reader.nextString());
            else if(courseField.equals(COURSE_PROFESSOR_FIELD)) c.setProfessor(reader.nextString());
            else if(courseField.equals(COURSE_LECTURES_FIELD)){

                readLecturesList(reader,c);

            }
            else {

                reader.skipValue();
            }

        }
        reader.endObject();

        return c;
    }

    public void readCoursesFromFile(){

        try{

            InputStream in = GlobalData.getAssetManager().open(COURSES_JSON_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginArray();

            while (reader.hasNext()){

                Course c = readCourse(reader);
                courses.put(c.getCode(),c);
            }

            reader.endArray();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ///////// JSON FILE READING METHODS - END ///////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////


}
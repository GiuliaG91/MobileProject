package com.example.giuliagigi.jobplacement;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */

import android.util.JsonReader;
import android.util.Log;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LecturesFileReader {

    /* STATIC FIELDS        */

    public static final String COURSES_JSON_FILE_NAME = "professors.json";
    public static final String COURSES_CODE_FIELD = "code";
    public static final String COURSE_NAME_FIELD = "name";
//    public static final String COURSE_PROFESSOR_FIELD = "professor";
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

        for(int i = 0; i<retrievedCourses.size();i++){

            String name = retrievedCourses.get(i).getProfessor().getName() + " " + retrievedCourses.get(i).getProfessor().getSurname();

            if(name.equalsIgnoreCase(professorName)) return true;
        }


        Log.println(Log.ASSERT, TAG, "no course found");
        return false;
    }

    private ArrayList<Course> getCoursesByProfessorName(String professorName){

        Log.println(Log.ASSERT, TAG, "getCoursesByProfessorName started");

        ArrayList<Course> allCourses = new ArrayList<Course>(courses.values());
        ArrayList<Course> retrievedCourses = new ArrayList<Course>();

        for(int i=0;i<allCourses.size();i++){

            String name = allCourses.get(i).getProfessor().getName() + " " + allCourses.get(i).getProfessor().getSurname();
            if(name.equalsIgnoreCase(professorName)){

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

                for(int i=0; i<coursesByName.size();i++){

                    String professorName = coursesByName.get(i).getProfessor().getName() + " " + coursesByName.get(i).getProfessor().getSurname();

                    if(professorName.equalsIgnoreCase(professor))
                        retrievedCourses.add(coursesByName.get(i));
                }

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
            String name = allCourses.get(i).getProfessor().getName() + " " + allCourses.get(i).getProfessor().getSurname();
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
            String professor = allCourses.get(0).getProfessor().getName() + " " + allCourses.get(0).getProfessor().getSurname();
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
            for(int j=0; j<retrievedCourses.get(i).getLectures().size();j++)
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
            for(int j=0; j<retrievedCourses.get(i).getLectures().size();j++)
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
            for(int j=0;j<retrievedCourses.get(i).getLectures().size();j++)
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

    private Lecture readLecture(JsonReader reader) throws IOException, ParseException {

        Log.println(Log.ASSERT,TAG,"Start reading object lecture");
        Lecture l = new Lecture();
        reader.beginObject();

        int startHour = 0, startMinute = 0, endHour = 0, endMinute = 0;

        while (reader.hasNext()){

            String lectureField = reader.nextName();

            if(lectureField.equals(LECTURE_DAYINWEEK_FIELD))        l.setDayInWeek(Integer.parseInt(reader.nextString()));
            else if(lectureField.equals(LECTURE_STARTHOUR_FIELD))   startHour = Integer.parseInt(reader.nextString());
            else if(lectureField.equals(LECTURE_STARTMINUTE_FIELD)) startMinute = Integer.parseInt(reader.nextString());
            else if(lectureField.equals(LECTURE_ENDHOUR_FIELD))     endHour = Integer.parseInt(reader.nextString());
            else if(lectureField.equals(LECTURE_ENDMINUTE_FIELD))   endMinute = Integer.parseInt(reader.nextString());
            else if(lectureField.equals(LECTURE_ROOM_FIELD))        l.setRoom(reader.nextString());
            else {

                reader.skipValue();
            }
        }

        l.setSchedule(startHour, startMinute, endHour, endMinute);
        reader.endObject();
        l.save();
        return l;
    }

    private void readLecturesList(JsonReader reader, Course c) throws IOException, ParseException {

        reader.beginArray();

        while (reader.hasNext()){

            Lecture l = readLecture(reader);
            c.addLecture(l);
            c.save();
            l.setCourse(c);
            l.save();
        }
        reader.endArray();

    }

    private Course readCourse(JsonReader reader) throws IOException, ParseException {

        Course c = new Course();
        Log.println(Log.ASSERT,TAG,"Start reading object course");
        reader.beginObject();

        while (reader.hasNext()){

            String courseField = reader.nextName();

            if(courseField.equals(COURSES_CODE_FIELD))    c.setCode(reader.nextString());
            else if(courseField.equals(COURSE_NAME_FIELD)) c.setName(reader.nextString());
            else if(courseField.equals(COURSE_LECTURES_FIELD)){

                readLecturesList(reader,c);

            }
            else {

                reader.skipValue();
            }

        }
        reader.endObject();
        c.save();

        return c;
    }

    private void readCoursesList(JsonReader reader, Professor p) throws IOException, ParseException {

        reader.beginArray();

        while (reader.hasNext()){

            Course c = readCourse(reader);
            c.setProfessor(p);
            p.addCourse(c);
        }

        reader.endArray();
    }

    private Professor readProfessor(JsonReader reader) throws IOException, ParseException {

        Log.println(Log.ASSERT,TAG,"Start reading object professor");
        Professor p = new Professor();

        reader.beginObject();

        while (reader.hasNext()){


            String fieldName = reader.nextName();

            if(fieldName.equals(Professor.NAME_FIELD)) p.setName(reader.nextString());
            else if(fieldName.equals(Professor.SURNAME_FIELD)) p.setSurname(reader.nextString());
            else if(fieldName.equals(Professor.SEX_FIELD)) p.setSex(reader.nextString());
            else if(fieldName.equals(Professor.NATION_FIELD)) p.setNation(reader.nextString());
            else if(fieldName.equals(Professor.BIRTH_CITY_FIELD)) p.setBirthCity(reader.nextString());
            else if(fieldName.equals(Professor.COURSES_FIELD)){

                readCoursesList(reader,p);
            }
            else
                reader.skipValue();
        }
        reader.endObject();

        return p;
    }

    public void readFromFile(){

        try{

            InputStream in = GlobalData.getAssetManager().open(COURSES_JSON_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginArray();

            int n = 0;
            while (reader.hasNext()){

                final ParseUserWrapper newParseUser = new ParseUserWrapper();
                newParseUser.setEmail("p" + ++n + "@professor.com");
                newParseUser.setUsername("p" + n + "@professor.com");
                newParseUser.setPassword("p" + n);
                newParseUser.setType(User.TYPE_PROFESSOR);
                Professor p = readProfessor(reader);

                newParseUser.signUp();
                p.setParseUser(newParseUser);
                p.save();

                newParseUser.setUser(p);
                newParseUser.save();

                for(Course c: p.getCourses())
                    courses.put(c.getCode(),c);

            }

            reader.endArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////
    ///////// JSON FILE READING METHODS - END ///////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////


}
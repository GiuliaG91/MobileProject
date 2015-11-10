package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */

@ParseClassName("Lecture")
public class Lecture extends ParseObject implements Comparable{

    public static final String COURSE_FIELD = "Course";
    public static final String DAY_FIELD = "Day";
    public static final String ROOM_FIELD = "Room";
    public static final String SCHEDULE_FIELD = "Schedule";

    private static final String START_HOUR_FIELD = "startHour";
    private static final String START_MINUTE_FIELD = "startMinute";
    private static final String END_HOUR_FIELD = "endHour";
    private static final String END_MINUTE_FIELD = "endMinute";

    public static final int DAY_MONDAY = 0;
    public static final int DAY_TUESDAY = 1;
    public static final int DAY_WEDNESDAY = 2;
    public static final int DAY_THURSDAY = 3;
    public static final int DAY_FRIDAY = 4;

    private Course course;
    private Schedule schedule;
    private String room;
    private int dayInWeek;
//    private int numOverlapse;
//    private ArrayList<Lecture> lectureOverlapse;
//    private boolean isShown;

    HashMap<String,Boolean> isCached;

    public Lecture(){

        super();

        course = null;
        schedule = null;
        room = null;
        dayInWeek = -1;

        isCached = new HashMap<String,Boolean>();

        isCached.put(COURSE_FIELD, false);
        isCached.put(SCHEDULE_FIELD, false);
        isCached.put(ROOM_FIELD, false);
        isCached.put(DAY_FIELD, false);
    }


    // --------------------------------------------------------------------
    // ---------------- GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------

//    public String getCourseName(){
//
//        return course;
//    }

    public Course getCourse(){

        if(isCached.get(COURSE_FIELD)) return course;

        course = (Course)get(COURSE_FIELD);
        isCached.put(COURSE_FIELD,true);
        return course;
    }

    public String getTimeTable(){

        if(schedule == null) getSchedule();
        return schedule.toString();
    }

    public String getRoomName(){

        if(isCached.get(ROOM_FIELD)) return room;

        room = getString(ROOM_FIELD);
        isCached.put(ROOM_FIELD,true);
        return room;
    }

    public int getDayInWeek() {

        if(isCached.get(DAY_FIELD)) return dayInWeek;

        dayInWeek = getInt(DAY_FIELD);
        isCached.put(DAY_FIELD,true);
        return dayInWeek;
    }

    public Schedule getSchedule(){

        if(isCached.get(SCHEDULE_FIELD)) return schedule;

        int startHour = getInt(START_HOUR_FIELD);
        int startMinute = getInt(START_MINUTE_FIELD);
        int endHour = getInt(END_HOUR_FIELD);
        int endMinute = getInt(END_MINUTE_FIELD);

        schedule = new Schedule(startHour,startMinute,endHour,endMinute);
        isCached.put(SCHEDULE_FIELD,true);
        return schedule;
    }

    /* ------------------------------------------------------------------------ */

    public void setCourse(Course c){

        course = c;
        put(COURSE_FIELD,c);
    }

    public void setRoom(String room) {
        this.room = room;
        put(ROOM_FIELD,room);
    }

    public void setDayInWeek(int dayInWeek) {

        if(dayInWeek>DAY_FRIDAY) dayInWeek = DAY_FRIDAY;
        else if(dayInWeek<DAY_MONDAY) dayInWeek = DAY_MONDAY;

        this.dayInWeek = dayInWeek;
        put(DAY_FIELD,dayInWeek);
    }

    public void setSchedule(int startHour, int startMinute, int endHour, int endMinute){

        this.schedule = new Schedule(startHour,startMinute,endHour,endMinute);

        put(START_HOUR_FIELD, startHour);
        put(START_MINUTE_FIELD, startMinute);
        put(END_HOUR_FIELD, endHour);
        put(END_MINUTE_FIELD, endMinute);
    }



//    public void setStartHour(int startHour){
//
//        this.schedule.setStartHour(startHour);
//    }
//
//    public void setEndHour(int endHour){
//
//        this.schedule.setEndHour(endHour);
//    }
//
//    public void setStartMinute(int startMinute){
//
//        this.schedule.setStartMinute(startMinute);
//    }
//
//    public void setEndMinute(int endMinute){
//
//        this.schedule.setEndMinute(endMinute);
//    }

//    public void setNumOverlapse(int num){
//        this.numOverlapse = num;
//    }
//
//    public int getNumOverlapse(){
//        return this.numOverlapse;
//    }
//
//    public void addLectureOverlapse(Lecture l){
//        this.lectureOverlapse.add(l);
//    }
//
//    public ArrayList<Lecture> getLectureOverlapse(){
//        return this.lectureOverlapse;
//    }
//
//    public boolean containsOverlapse(Lecture other){
//        if(lectureOverlapse.contains(other)) return true;
//        else return false;
//    }
//
//    public boolean isShown(){
//        return this.isShown;
//    }
//
//    public void setShown(boolean b){
//        this.isShown = b;
//    }



    // --------------------------------------------------------------------
    // ------------ END GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    @Override
    public int compareTo(Object another) {

        if(!(another instanceof Lecture)){

            Log.println(Log.ASSERT, "LECTURE", "trying to compare a lecture to another class object");
            return 0;
        }

        Lecture l = (Lecture)another;

        if(l.dayInWeek > this.dayInWeek)
            return -1;

        else if(l.dayInWeek < this.dayInWeek)
            return 1;

        else {

            if(l.getSchedule().getStartHour() > this.getSchedule().getStartHour())
                return -1;

            else if (l.getSchedule().getStartHour() < this.getSchedule().getStartHour())
                return 1;

            else {

                if(l.getSchedule().getStartMinute() > this.getSchedule().getStartMinute())
                    return -1;

                else if(l.getSchedule().getStartMinute() < this.getSchedule().getStartMinute())
                    return 1;

                else {

                    if(l.getSchedule().getMinuteDuration() > this.getSchedule().getMinuteDuration())
                        return -1;

                    else if (l.getSchedule().getMinuteDuration() < this.getSchedule().getMinuteDuration())
                        return 1;

                    else return 0;
                }
            }
        }

    }


    public boolean isOverlap(Lecture l){

        int compare = this.compareTo(l);
        Lecture first, second;

        if(compare < 0){

            first = this;
            second = l;
        }
        else {

            second = this;
            first = l;
        }

        int firstEndMinute = first.getSchedule().getEndMinute();
        int firstEndHour = first.getSchedule().getEndHour();

        int secondStartHour = second.getSchedule().getStartHour();
        int secondStartMinute = second.getSchedule().getStartMinute();

        if(secondStartHour > firstEndHour) return false;
        if(secondStartHour == firstEndHour && secondStartMinute >= firstEndMinute) return false;
        return true;
    }

}

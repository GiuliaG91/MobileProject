package com.example.giuliagigi.jobplacement;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class Lecture implements Comparable{

    private String course;
    private String professor;
    private Schedule schedule;
    private String room;
    private int dayInWeek;
    private int numOverlapse;
    private ArrayList<Lecture> lectureOverlapse;
    private boolean isShown;

    public Lecture(String course, String professor){

        this(course,professor,null,0,0,0,0,0);
    }

    public Lecture (String course,String professor, String room, int startHour, int startMinute, int endHour, int endMinute,int dayInWeek){

        if(dayInWeek>6) dayInWeek = 6;
        else if(dayInWeek<1) dayInWeek = 1;

        this.course = course;
        this.professor = professor;
        this.room = room;
        this.schedule = new Schedule(startHour,startMinute,endHour,endMinute);
        this.dayInWeek = dayInWeek;
        this.numOverlapse = 0;
        this.lectureOverlapse = new ArrayList<Lecture>();
        this.isShown = false;
    }


    // --------------------------------------------------------------------
    // ---------------- GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------

    public String getCourseName(){

        return course;
    }

    public String getProfessor() {
        return professor;
    }

    public String getTimeTable(){

        return schedule.toString();
    }

    public String getRoomName(){

        return room;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDayInWeek(int dayInWeek) {

        if(dayInWeek>6) dayInWeek = 6;
        else if(dayInWeek<1) dayInWeek = 1;

        this.dayInWeek = dayInWeek;
    }

    public void setStartHour(int startHour){

        this.schedule.setStartHour(startHour);
    }

    public void setEndHour(int endHour){

        this.schedule.setEndHour(endHour);
    }

    public void setStartMinute(int startMinute){

        this.schedule.setStartMinute(startMinute);
    }

    public void setEndMinute(int endMinute){

        this.schedule.setEndMinute(endMinute);
    }

    public void setNumOverlapse(int num){
        this.numOverlapse = num;
    }

    public int getNumOverlapse(){
        return this.numOverlapse;
    }

    public void addLectureOverlapse(Lecture l){
        this.lectureOverlapse.add(l);
    }

    public ArrayList<Lecture> getLectureOverlapse(){
        return this.lectureOverlapse;
    }

    public boolean containsOverlapse(Lecture other){
        if(lectureOverlapse.contains(other)) return true;
        else return false;
    }

    public boolean isShown(){
        return this.isShown;
    }

    public void setShown(boolean b){
        this.isShown = b;
    }



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

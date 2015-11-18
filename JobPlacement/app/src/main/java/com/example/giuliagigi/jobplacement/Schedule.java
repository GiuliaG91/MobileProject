package com.example.giuliagigi.jobplacement;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class Schedule {

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int minuteDuration;
    private int overlapsType;


    // --------------------------------------------------------------------
    //--------------------- CONSTRUCTOR -----------------------------------
    // --------------------------------------------------------------------

    public Schedule(int startHour, int startMinute, int endHour, int endMinute) {

        if(startHour>endHour){ //must swap start with end because startHour was after endHour

            int temp = startHour;
            startHour = endHour;
            endHour = temp;

            temp = startMinute;
            startMinute = endMinute;
            endMinute = temp;

        }
        else if(startHour == endHour && startMinute > endMinute){

            //must swap start with end because startMinute was after endMinute (sameHour)

            int temp = startMinute;
            startMinute = endMinute;
            endMinute = temp;
        }


        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.overlapsType = 0;

        setMinuteDuration();
    }

    // --------------------------------------------------------------------
    //------------------ END CONSTRUCTOR ----------------------------------
    // --------------------------------------------------------------------



    ///////////////////////////////////////////////////////////////////////


    // --------------------------------------------------------------------
    // --------------- GETTERS AND SETTERS --------------------------------
    // --------------------------------------------------------------------


    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
        setMinuteDuration();
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
        setMinuteDuration();
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
        setMinuteDuration();
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
        setMinuteDuration();
    }

    public int getMinuteDuration() {
        return minuteDuration;
    }

    private void setMinuteDuration(){

        this.minuteDuration = (60-startMinute) + endMinute + 60*(endHour-startHour-1);
    }

    public void setOverlapsType(int type){
        this.overlapsType = type;
    }

    public int getOverlapsType(){
        return this.overlapsType;
    }



    // --------------------------------------------------------------------
    // ------------ END GETTERS AND SETTERS -------------------------------
    // --------------------------------------------------------------------


    public String toString(){
        if(getStartMinute()<10){
            if(getEndMinute()<10){
                return getStartHour() + ":0" + getStartMinute() + " - " + getEndHour() + ":0" + getEndMinute();
            }else{
                return getStartHour() + ":0" + getStartMinute() + " - " + getEndHour() + ":" + getEndMinute();
            }
        }else if(getEndMinute()<10){
            return getStartHour() + ":" + getStartMinute() + " - " + getEndHour() + ":0" + getEndMinute();
        } else {
            return getStartHour() + ":" + getStartMinute() + " - " + getEndHour() + ":" + getEndMinute();
        }
    }

    public int getStartPxl(){
        return ((startHour-8)*60+startMinute);
    }



}

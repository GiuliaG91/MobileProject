package com.example.giuliagigi.jobplacement;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class LectureParcelable implements Parcelable {

    private String title;
    private String schedule;
    private String room;
    private String professor;



    public LectureParcelable(Lecture l){

        this.title = l.getCourseName();
        this.schedule = l.getTimeTable();
        this.room = l.getRoomName();
        this.professor = l.getProfessor();
    }

    public LectureParcelable(String title, String schedule, String room, String professor) {
        this.title = title;
        this.schedule = schedule;
        this.room = room;
        this.professor = professor;
    }

    public String getTitle() {
        return title;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getRoom() {
        return room;
    }

    public String getProfessor() {
        return professor;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(schedule);
        dest.writeString(room);
        dest.writeString(professor);
    }


    public static final Creator CREATOR =
            new Creator() {
                @Override
                public Object createFromParcel(Parcel source) {

                    return new LectureParcelable(source.readString(),source.readString(),source.readString(),source.readString());
                }

                @Override
                public Object[] newArray(int size) {
                    return new LectureParcelable[size];
                }
            };
}
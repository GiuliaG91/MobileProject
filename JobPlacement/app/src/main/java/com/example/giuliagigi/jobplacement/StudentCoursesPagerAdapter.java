package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by MarcoEsposito90 on 10/11/2015.
 */
public class StudentCoursesPagerAdapter extends FragmentPagerAdapter{

    private String[] titles;
    private Student student;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public StudentCoursesPagerAdapter(FragmentManager fm, Activity activity, Student student) {
        super(fm);

        titles = activity.getApplicationContext().getResources().getStringArray(R.array.Courses_Students_Tab);

        for(String s:titles)
            Log.println(Log.ASSERT,"COURSEPAGERADAPTER", "titles: " + s);

        this.student = student;
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- IMPLEMENT METHODS ----------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            return LectureSearch.newInstance();
        }
        else if(position == 1){

            return StudentCoursesManagementFragment.newInstance(student);
        }

        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

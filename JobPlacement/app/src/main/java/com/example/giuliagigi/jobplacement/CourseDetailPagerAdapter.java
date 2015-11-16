package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Unieuro on 16/11/2015.
 */
public class CourseDetailPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private boolean isEdit;
    private Course course;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public CourseDetailPagerAdapter(FragmentManager fm, Activity activity, Course course, boolean isEdit) {

        super(fm);

        titles = activity.getApplicationContext().getResources().getStringArray(R.array.Courses_Details_Tab);
        this.course = course;
        this.isEdit = isEdit;
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- IMPLEMENTATION -------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            return CourseOverviewFragment.newInstance(course, isEdit);
        }
        else if(position == 1){

            return CourseLecturesFragment.newInstance(course, isEdit);
        }
        else if(position == 2){

            return CourseNoticesFragment.newInstance(course,isEdit);
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

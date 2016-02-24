package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Unieuro on 22/02/2016.
 */
public class StudentOffersPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Student student;

    public StudentOffersPagerAdapter(FragmentManager fm, Activity activity, Student student) {

        super(fm);

        titles = activity.getApplicationContext().getResources().getStringArray(R.array.Offers_Students_Tab);
        this.student = student;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
            return ApplicationsListFragment.newInstance(student);
        else if (position == 1)
            return StudentMyFavouriteOffersFragment.newInstance(student);

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

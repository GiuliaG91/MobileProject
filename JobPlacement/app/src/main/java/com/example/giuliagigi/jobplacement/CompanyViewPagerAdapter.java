package com.example.giuliagigi.jobplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pietro on 05/05/2015.
 */
public class CompanyViewPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when StudentViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the StudentViewPagerAdapter is created

      CompanyViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb){
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;


      }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return new TabHome();
        }
        else return FavStudentsFragment.newInstance();

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}

package com.example.giuliagigi.jobplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by pietro on 28/04/2015.
 */
public class ProfileManagementViewAdapter extends FragmentPagerAdapter

    {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ProfileManagementViewAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            return StudentProfileManagementBasicsFragment.newInstance();
        }
        else  if(position == 1)           // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            return StudentProfileManagementSkillsFragment.newInstance();
        }
        else  if(position == 2)           // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            return StudentProfileManagementRegistryFragment.newInstance();
        }
        else  if(position == 3)           // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Fav_tab tab1 = new Fav_tab();
            return tab1;
        }
            else return null;

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


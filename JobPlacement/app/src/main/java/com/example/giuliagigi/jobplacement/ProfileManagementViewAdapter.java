package com.example.giuliagigi.jobplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pietro on 28/04/2015.
 */
public class ProfileManagementViewAdapter extends FragmentPagerAdapter{

        ArrayList<ProfileManagementFragment> fragments;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ProfileManagementViewAdapter(FragmentManager fm, String userType) {
            super(fm);
            fragments = new ArrayList<ProfileManagementFragment>();

            if(userType.equals(User.TYPE_STUDENT)){

                fragments.add(StudentProfileManagementBasicsFragment.newInstance());
                fragments.add(StudentProfileManagementSkillsFragment.newInstance());
                fragments.add(StudentProfileManagementRegistryFragment.newInstance());
            }
            else {

                fragments.add(CompanyProfileManagementBasicsFragment.newInstance());
                fragments.add(CompanyProfileManagementRegistryFragment.newInstance());
            }

            fragments.add(ProfileManagementAccountFragment.newInstance());

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

        if(position<fragments.size())
            return fragments.get(position);

        else return null;

    }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
    public CharSequence getPageTitle(int position) {

        return fragments.get(position).getTitle();
    }

        // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {

        return fragments.size();
    }
  }


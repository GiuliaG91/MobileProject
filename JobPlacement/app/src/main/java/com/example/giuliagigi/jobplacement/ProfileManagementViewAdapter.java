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
        public ProfileManagementViewAdapter(FragmentManager fm, User user, boolean editable) {
            super(fm);

            fragments = new ArrayList<ProfileManagementFragment>();
            GlobalData application = (GlobalData)GlobalData.getContext();

            if(user.getType().equals(User.TYPE_STUDENT)){

                Student student = (Student)user;
                fragments.add(StudentProfileManagementBasicsFragment.newInstance(student));
                fragments.add(StudentProfileManagementSkillsFragment.newInstance(student,editable));
                fragments.add(StudentProfileManagementRegistryFragment.newInstance(student));
            }
            else if(user.getType().equals(User.TYPE_COMPANY)) {

                Company company = (Company)user;
                fragments.add(CompanyProfileManagementBasicsFragment.newInstance(company));
                fragments.add(CompanyProfileManagementRegistryFragment.newInstance(company));
            }
            else if(user.getType().equals(User.TYPE_PROFESSOR)){

                //TODO
            }

            if(editable){

                fragments.add(ProfileManagementTagsFragment.newInstance(user));
                fragments.add(ProfileManagementAccountFragment.newInstance(user));
            }

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


    public ArrayList<ProfileManagementFragment> getFragments(){

        return fragments;
    }
  }


package com.example.giuliagigi.jobplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MarcoEsposito90 on 09/11/2015.
 */
public class ProfessorViewPagerAdapter extends FragmentPagerAdapter {

    CharSequence titles[];          // Titles of the Tabs for ViewPager
    int tabsNumber;                 // number of tabs
    TabHomeProfessorFragment parent;

    public ProfessorViewPagerAdapter(FragmentManager fm, CharSequence titles[], int tabsNumber, TabHomeProfessorFragment parent) {

        super(fm);

        this.tabsNumber = tabsNumber;
        this.titles = titles;
        this.parent = parent;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0){

            TabHome tab1 = TabHome.newInstance();
            return tab1;
        }
        else
            return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}

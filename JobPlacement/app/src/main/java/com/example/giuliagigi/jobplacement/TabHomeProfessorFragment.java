package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


public class TabHomeProfessorFragment extends Fragment {

    private GlobalData globalData;
    private CharSequence[] titles;

    private ProfessorViewPagerAdapter pagerAdapter;
    private SlidingTabLayout tabs;
    private ViewPager pager;
    private View root;

    private int currentPosition = 0;

    private OnFragmentInteractionListener mListener;


    /* -------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS, SETTERS ---------------------------------------------- */
    /* -------------------------------------------------------------------------------------------- */

    public static TabHomeProfessorFragment newInstance() {
        TabHomeProfessorFragment fragment = new TabHomeProfessorFragment();
        return fragment;
    }

    public TabHomeProfessorFragment() {}


    /* -------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS ------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        globalData = (GlobalData)activity.getApplicationContext();
        titles = globalData.getResources().getStringArray(R.array.Home_Professor_Tab);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        if(savedInstanceState != null)
            currentPosition = savedInstanceState.getInt("position");

        root = inflater.inflate(R.layout.fragment_tab_home, container, false);

        globalData.setToolbarTitle(getString(R.string.ToolbarTilteHome));

        pager = (ViewPager)root.findViewById(R.id.pager);
        pagerAdapter = new ProfessorViewPagerAdapter(getChildFragmentManager(),titles, titles.length, this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentPosition);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) root.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class TabHomeStudentFragment extends Fragment {

    /**
     * *************For page viewer***************************
     */
    View root;
    ViewPager pager;
    StudentViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = null;
    int Numboftabs = 4;
    GlobalData globalData;
    private Integer currentPosition=0;
    /***************************************************************/


    private OnFragmentInteractionListener mListener;

    public TabHomeStudentFragment() {    }

    public static TabHomeStudentFragment newInstance() {

        TabHomeStudentFragment fragment = new TabHomeStudentFragment();
        return fragment;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
        }

        globalData = (GlobalData) getActivity().getApplication();
        Titles = getActivity().getResources().getStringArray(R.array.Home_Students_Tab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        globalData.setToolbarTitle(getString(R.string.ToolbarTilteHome));

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_standard_pager, container, false);

        /*************ViewPager***************************/

        // Creating The StudentViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new StudentViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs, this);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentPosition);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) root.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


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

        /****************************************************/
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("position",currentPosition);
    }
}

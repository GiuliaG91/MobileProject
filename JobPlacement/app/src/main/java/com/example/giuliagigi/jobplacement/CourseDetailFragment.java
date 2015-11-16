package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CourseDetailFragment extends Fragment {

    private Course course;
    private boolean isEdit;

    private View root;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private CourseDetailPagerAdapter adapter;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseDetailFragment newInstance(Course course, boolean isEdit) {

        CourseDetailFragment fragment = new CourseDetailFragment();

        fragment.course = course;
        fragment.isEdit = isEdit;

        return fragment;
    }

    public CourseDetailFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_standard_pager, container, false);

        pager = (ViewPager)root.findViewById(R.id.pager);

        if(pager != null){

            adapter = new CourseDetailPagerAdapter(getChildFragmentManager(),getActivity(),course,isEdit);
            pager.setAdapter(adapter);
            pager.setCurrentItem(0);
        }


        tabs = (SlidingTabLayout) root.findViewById(R.id.tabs);

        if(tabs != null){

            tabs.setDistributeEvenly(true);
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });

            tabs.setViewPager(pager);
            tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentOffersManagementFragment extends Fragment {


    private OnFragmentInteractionListener host;
    private Student student;
    private GlobalData globalData;

    private View root;
    private ViewPager pager;
    private StudentOffersPagerAdapter adapter;
    private SlidingTabLayout tabs;



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentOffersManagementFragment newInstance(Student student) {

        StudentOffersManagementFragment fragment = new StudentOffersManagementFragment();
        fragment.student = student;
        return fragment;
    }

    public StudentOffersManagementFragment() {}



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        globalData = (GlobalData)activity.getApplicationContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_standard_pager, container, false);

        pager = (ViewPager)root.findViewById(R.id.pager);

        if(pager != null){

            adapter = new StudentOffersPagerAdapter(getChildFragmentManager(),getActivity(),student);
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

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentDidacticsManagementFragment extends Fragment implements OnActivityChangedListener {

    private static final String BUNDLE_IDENTIFIER = "STUDENTDIDACTICS";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_STUDENT = "bundle_student";


    private OnFragmentInteractionListener host;
    private Student student;
    private GlobalData globalData;

    private View root;
    private ViewPager pager;
    private StudentCoursesPagerAdapter adapter;
    private SlidingTabLayout tabs;

    int tab;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentDidacticsManagementFragment newInstance(Student student) {

        StudentDidacticsManagementFragment fragment = new StudentDidacticsManagementFragment();
        fragment.student = student;

        return fragment;
    }

    public StudentDidacticsManagementFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        host = (OnFragmentInteractionListener)activity;
        globalData = (GlobalData)activity.getApplicationContext();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.addOnActivityChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null){

                student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_standard_pager, container, false);

        pager = (ViewPager)root.findViewById(R.id.pager);

        if(pager != null){

            adapter = new StudentCoursesPagerAdapter(getChildFragmentManager(),getActivity(),student);
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
    public void onSaveInstanceState(Bundle outState) {

        String tail = student.toString();
        outState.putString(BUNDLE_KEY_TAIL,tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_STUDENT, student);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {

        host.removeOnActivityChangedListener(this);
        super.onDetach();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- ACTIVITY LISTENER ----------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onActivityStateChanged(State newState, State pastState) {

        // DO NOTHING
    }

    @Override
    public void onDataSetChange() {

        adapter = new StudentCoursesPagerAdapter(getChildFragmentManager(),getActivity(),student);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1,true);
    }
}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabHomeCompanyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabHomeCompanyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabHomeCompanyFragment extends Fragment {

    View root;
    GlobalData globalData;
    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
   CompanyViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] =null;
    int Numboftabs = 2;
    private Integer currentPosition=0;

    /***************************************************************/


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TabHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabHomeCompanyFragment newInstance() {
        TabHomeCompanyFragment fragment = new TabHomeCompanyFragment();
        return fragment;
    }

    public TabHomeCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
        {
            currentPosition=savedInstanceState.getInt("position");
        }

        globalData=(GlobalData)getActivity().getApplication();
        Titles=getResources().getStringArray(R.array.Home_Company_Tab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_tab_home, container, false);


        /*************ViewPager***************************/

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new CompanyViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs);
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
                    currentPosition=position;
              }

              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });
        /****************************************************/
        // pager.setCurrentItem(globalData.getHome_company_position());
        return root;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("position",currentPosition);
    }

}

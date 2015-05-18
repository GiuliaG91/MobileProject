package com.example.giuliagigi.jobplacement;



import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileManagement extends Fragment{

    private static final String BUNDLE_KEY_IS_EDIT = "bundle_key_is_edit";
    private static final String BUNDLE_KEY_EDITABLE = "bundle_key_editable";
    private static final String BUNDLE_KEY_USER = "bundle_key_user";
    public final String BUNDLE_IDENTIFIER = "PROFILE_MANAGEMENT;"+getTag();

    private GlobalData application;
    private OnInteractionListener host;
    private ArrayList<ProfileManagementFragment> fragments;
    private boolean isEditMode,editable,orientationFlag;
    private User user;


    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
    ProfileManagementViewAdapter adapter;
    SlidingTabLayout tabs;

    /***************************************************************/


    /*------------- CONSTRUCTORS GETTERS SETTERS ------------------------------------------------*/

    public  ProfileManagement(){}
    public static ProfileManagement newInstance(boolean editable, User user) {
        ProfileManagement fragment = new ProfileManagement();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setEditable(editable);
        fragment.setUser(user);
        return fragment;
    }

    public void setEditable(boolean editable){

        this.editable = editable;
    }

    public void setUser(User user){

        this.user = user;
    }


    /*------------- STANDARD CALLBACKS ------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT,"PROFILE MANAG","on Attach");


        isEditMode = false;
        orientationFlag = false;

        try {
            host = (OnInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
        application = (GlobalData)getActivity().getApplication();

        if(user!=null)
            application.setLatestDisplayedUser(user);

        if(application.getBundle(BUNDLE_IDENTIFIER)!= null){

            Log.println(Log.ASSERT,"PROFILE MANAG","found a bundle!");
            MyBundle b = application.getBundle(BUNDLE_IDENTIFIER);
            isEditMode = b.getBoolean(BUNDLE_KEY_IS_EDIT);
            editable = b.getBoolean(BUNDLE_KEY_EDITABLE);
            user = (User)b.get(BUNDLE_KEY_USER);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.setEditMode(isEditMode);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /*************ViewPager***************************/

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ProfileManagementViewAdapter(getChildFragmentManager(),user,editable);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        fragments = adapter.getFragments();

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
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


        if(editable){

            final Button edit = new Button(getActivity().getApplicationContext());
            edit.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            edit.setText("edit profile");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switchMode();
                }
            });
            ViewGroup root = (ViewGroup)view.findViewById(R.id.fragment_tab_home);
            root.addView(edit);
        }

        /****************************************************/


    }

    @Override
    public void onDetach() {
        super.onDetach();

            Log.println(Log.ASSERT,"PROFILE MANAG", "saving in bundle");
            MyBundle b = application.addBundle(BUNDLE_IDENTIFIER);
            b.putBoolean(BUNDLE_KEY_IS_EDIT,isEditMode);
            b.putBoolean(BUNDLE_KEY_EDITABLE,editable);
            b.put(BUNDLE_KEY_USER,user);
            orientationFlag = false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (ProfileManagementFragment f: fragments)
            f.onActivityResult(requestCode,resultCode,data);
    }


    /* ----------------------------------- AUXILIARY METHODS ------------------------------------ */

    private void switchMode(){

        if(editable){

            isEditMode = !isEditMode;
            host.setEditMode(isEditMode);
        }
        else
            Toast.makeText(getActivity(), "ERROR: you are not supposed to modify this account",Toast.LENGTH_SHORT).show();

    }




    /* ----------- interface with upper activity --------------*/
    public interface OnInteractionListener {

        public void setEditMode(boolean editable);
    }

}

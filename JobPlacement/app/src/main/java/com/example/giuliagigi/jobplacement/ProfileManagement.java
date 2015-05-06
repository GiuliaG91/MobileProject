package com.example.giuliagigi.jobplacement;



import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class ProfileManagement extends Fragment{

    private GlobalData application;
    private OnInteractionListener host;
    private boolean editable;



    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
    ProfileManagementViewAdapter adapter;
    SlidingTabLayout tabs;

    /***************************************************************/



    public  ProfileManagement(){ }
    public static ProfileManagement newInstance() {
        ProfileManagement fragment = new ProfileManagement();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /*------------- STANDARD CALLBACKS ------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT,"PROFILE MANAG", "onAttach");

        editable = false;
        try {
            host = (OnInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
        host.setEditMode(editable);
        application = (GlobalData)getActivity().getApplication();
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
        adapter = new ProfileManagementViewAdapter(getChildFragmentManager(), application.getCurrentUser().getType());
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

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

        final Button edit = new Button(getActivity().getApplicationContext());
        edit.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        edit.setText("edit profile");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editable = !editable;
                host.setEditMode(editable);
            }
        });
        ViewGroup root = (ViewGroup)view.findViewById(R.id.fragment_tab_home);
        root.addView(edit);
        /****************************************************/


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.println(Log.ASSERT,"PROFILE MANAG", "onDetach");
    }

    public interface OnInteractionListener {

        public void setEditMode(boolean editable);
    }

}

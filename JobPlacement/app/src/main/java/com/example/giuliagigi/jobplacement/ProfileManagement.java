package com.example.giuliagigi.jobplacement;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileManagement extends Fragment{

    private static final String BUNDLE_KEY_IS_EDIT = "bundle_key_is_edit";
    private static final String BUNDLE_KEY_EDITABLE = "bundle_key_editable";
    private static final String BUNDLE_KEY_USER = "bundle_key_user";
    public final static String BUNDLE_IDENTIFIER = "PROFILE_MANAGEMENT;";
    public final static String BUNDLE_KEY_TAIL = "bundle_tail;";


    private GlobalData application;
    private OnInteractionListener host;
    private ArrayList<ProfileManagementFragment> fragments;
    private MenuItem editIcon;
    private boolean isEditMode,editable;
    private User user;


    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
    ProfileManagementViewAdapter adapter;
    SlidingTabLayout tabs;

    /***************************************************************/


    /*------------- CONSTRUCTORS GETTERS SETTERS ------------------------------------------------*/


    public static ProfileManagement newInstance(boolean editable, User user) {
        ProfileManagement fragment = new ProfileManagement();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setEditable(editable);
        fragment.setUser(user);
        return fragment;
    }

    public  ProfileManagement(){}

    public void setEditable(boolean editable){

        this.editable = editable;
    }

    public void setUser(User user){

        this.user = user;
    }

    public boolean isEditable(){
        return editable;
    }

    /*--------------------------------------------------------------------------------------------*/
    /*------------- STANDARD CALLBACKS -----------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        isEditMode = false;

        try {
            host = (OnInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }

        application = (GlobalData)getActivity().getApplication();
        application.registerFragment(BUNDLE_IDENTIFIER, this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.setEditMode(isEditMode);

        Toolbar toolbar=application.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteProfile);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Toolbar toolbar=application.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteProfile);

        if(user!=null)
            application.setLatestDisplayedUser(user);

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = application.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null){

                isEditMode = bundle.getBoolean(BUNDLE_KEY_IS_EDIT);
                editable = bundle.getBoolean(BUNDLE_KEY_EDITABLE);
                user = (User)bundle.get(BUNDLE_KEY_USER);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar=application.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteProfile);
        return inflater.inflate(R.layout.fragment_standard_pager, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        application.setToolbarTitle(getString(R.string.ToolbarTilteHome));

        /*************ViewPager***************************/

        // Creating The StudentViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ProfileManagementViewAdapter(getChildFragmentManager(),this,user,editable);
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

        tabs.setViewPager(pager);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        String tail = user.toString();
        outState.putString(BUNDLE_KEY_TAIL, tail);

        MyBundle b = application.addBundle(BUNDLE_IDENTIFIER + tail);
        b.putBoolean(BUNDLE_KEY_IS_EDIT,isEditMode);
        b.putBoolean(BUNDLE_KEY_EDITABLE, editable);
        b.put(BUNDLE_KEY_USER, user);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(Log.ASSERT,"PROFILE MANAG", "onActivityResult");

        for (ProfileManagementFragment f: fragments){

            Log.println(Log.ASSERT,"PROFILE MANAG", "called on: " + f.getTitle());
            f.onActivityResult(requestCode,resultCode,data);
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    /*------------------- MENU -------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if(editable){
            inflater.inflate(R.menu.menu_profile_management, menu);
            editIcon = menu.findItem(R.id.action_edit);
        }
        else if(user.getType().equals(User.TYPE_STUDENT))
            inflater.inflate(R.menu.menu_visit_profile_student, menu);
        else
            inflater.inflate(R.menu.menu_visit_profile_company, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_edit && editable) {

            switchMode();
        }

        else if(item.getItemId() == R.id.action_send && !editable){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(GlobalData.getContext().getString(R.string.string_send_email) + " " + user.getMail() + "?");
            builder.setPositiveButton(GlobalData.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    host.openMailBox(user);
                }
            });

            builder.setNegativeButton(GlobalData.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            builder.create().show();
        }

        else if(item.getItemId() == R.id.action_see_candidatures && !editable){

            SetOfferStatusDialogFragment statusDialogFragment=SetOfferStatusDialogFragment.newInstance((Student)user);

            statusDialogFragment.show(getChildFragmentManager(), "dialog");
    }

        return true;
    }


    /*--------------------------------------------------------------------------------------------*/
    /* ----------------------------------- AUXILIARY METHODS ------------------------------------ */
    /*--------------------------------------------------------------------------------------------*/

    public void switchMode(){

        if(editable){

            isEditMode = !isEditMode;
            host.setEditMode(isEditMode);
        }
        else
            Toast.makeText(getActivity(), GlobalData.getContext().getString(R.string.string_cannot_edit),Toast.LENGTH_SHORT).show();

        if(isEditMode && editable)
            editIcon.setIcon(R.drawable.ic_confirm_white);
        else if(!isEditMode && editable)
            editIcon.setIcon(R.drawable.ic_edit_white);
        else if(!editable)
            editIcon.setIcon(R.drawable.ic_mail);

    }




    /*--------------------------------------------------------------------------------------------*/
    /* ----------------------------------- ACTIVITY INTERFACE ----------------------------------- */
    /*--------------------------------------------------------------------------------------------*/

    public interface OnInteractionListener {

        public void setEditMode(boolean editable);
        public void openMailBox(User user);
    }

}

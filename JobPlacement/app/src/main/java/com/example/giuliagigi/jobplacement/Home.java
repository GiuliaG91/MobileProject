package com.example.giuliagigi.jobplacement;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;


public class Home extends ActionBarActivity
        implements ProfileManagementFragment.OnInteractionListener,
        ProfileManagement.OnInteractionListener ,
        MailBoxDisplayFragment.OnFragmentInteractionListener,
        CourseAdapter.CourseAdapterListener,
        OnFragmentInteractionListener{


    private Toolbar toolbar;   // Declaring the Toolbar Object


    /* ----------- DRAWER -------------------------- */
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private menuAdapter mAdapter;
    private ProfileManagement profileManagement;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private TypedArray ICONS;
    private String[] TITLES;
    private Boolean init = false;

    /* --------- PROFILE MANAGEMENT ---------------------*/
    private boolean isEditMode;
    private GlobalData application;
    private ArrayList<OnActivityChangedListener> listeners;


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        Log.println(Log.ASSERT,"HOME","onCreate");

        if(savedInstanceState!=null)
            init = true;

        application = (GlobalData)getApplication();

        /* initialize the listeners list:
                each listener is a fragment: when the activity
                performs a significant change, listeners are informed via
                the "onActivityStateChanged" method
         */
        listeners = new ArrayList<OnActivityChangedListener>();
        isEditMode = false;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){

            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }


      application.setToolbar(toolbar);

      // ----------------- Setup Drawer and pager -------------------------------------------------

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        //create adapter
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mDrawerList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mDrawerList.setLayoutManager(mLayoutManager);

        /*********************Different menu for Student and companies******/
        ParseUserWrapper user = application.getCurrentUser();

        if (user.getType().equalsIgnoreCase(User.TYPE_STUDENT)) {

            TITLES = getResources().getStringArray(R.array.Menu_items_student);
            ICONS = getResources().obtainTypedArray(R.array.StudentMenuicons);

            if (!init){

                FragmentManager fragmentManager = getSupportFragmentManager();
                TabHomeStudentFragment homeFragment = TabHomeStudentFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, homeFragment)
                        .addToBackStack("Home")
                        .commit();
                mDrawerLayout.closeDrawers();
            }
        }
        else if (user.getType().equalsIgnoreCase(User.TYPE_COMPANY)) {

            TITLES = getResources().getStringArray(R.array.Menu_items_Company);
            ICONS = getResources().obtainTypedArray(R.array.CompanyMenuicons);

            if (!init) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                TabHomeCompanyFragment homeFragment = TabHomeCompanyFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, homeFragment)
                        .commit();
                mDrawerLayout.closeDrawers();
            }
        }
        else if (user.getType().equalsIgnoreCase(User.TYPE_PROFESSOR)) {

            TITLES = getResources().getStringArray(R.array.Menu_items_Professor);
            ICONS = getResources().obtainTypedArray(R.array.ProfessorMenuicons);

            if (!init) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                TabHomeProfessorFragment homeFragment = TabHomeProfessorFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, homeFragment)
                        .commit();
                mDrawerLayout.closeDrawers();
            }
        }

        // specify an adapter (see also next example)
        mAdapter = new menuAdapter(TITLES, ICONS, user, this, mDrawerLayout,application);
        mDrawerList.setAdapter(mAdapter);

        application.setmAdapter(mAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if(application.getToolbarTitle()!=null) {
                    setTitle(application.getToolbarTitle());
                }
            }
        }; // Drawer Toggle Object Made

        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        /* email verification alert */
        if (!application.getCurrentUser().isEmailVerified() && !init)
            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_email_not_verified),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("init",true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.println(Log.ASSERT,"HOME", "activity result. code = " + requestCode);
        profileManagement.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /* ------------------ PROFILE MANAGEMENT FRAGMENT INTERFACE ----------------------------------*/

    @Override
    public boolean isEditMode() {
        return isEditMode;
    }

    @Override
    public void addOnActivityChangedListener(OnActivityChangedListener listener) {

        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeOnActivityChangedListener(OnActivityChangedListener listener) {

        listeners.remove(listener);
    }



    @Override
    public void startDeleteAccountActivity() {

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        ProfileDeleteFragment pdf = ProfileDeleteFragment.newInstance();

        fm.replace(R.id.tab_Home_container,pdf);
        fm.commit();

    }

    @Override
    public void setEditMode(boolean editable) {

        isEditMode = editable;

        if(editable)
            for (OnActivityChangedListener l:listeners)
                l.onActivityStateChanged(OnActivityChangedListener.State.EDIT_MODE_STATE, OnActivityChangedListener.State.DISPLAY_MODE_STATE);
        else
            for (OnActivityChangedListener l:listeners)
                l.onActivityStateChanged(OnActivityChangedListener.State.DISPLAY_MODE_STATE, OnActivityChangedListener.State.EDIT_MODE_STATE);
    }



    /* ---------------- DIDACTICS INTERFACE -----------------------------------------*/
    @Override
    public void onDataSetChanged() {

        for(OnActivityChangedListener l: listeners)
            l.onDataSetChange();
    }


    /* ---------------- MAILBOX INTERFACE -----------------------------------------*/
    @Override
    public void openMailBox(User user) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ArrayList<ParseUserWrapper> recipients = new ArrayList<ParseUserWrapper>();
        recipients.add(user.getParseUser());

        MailBoxNewFragment mailbox = MailBoxNewFragment.newInstance(recipients,null,null);
        ft.replace(R.id.tab_Home_container,mailbox);
        ft.commit();

    }

    public void setProfileManagement(ProfileManagement profileManagement) {
        this.profileManagement = profileManagement;
    }



    @Override
    public void onBackPressed() {


        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
           getSupportFragmentManager().popBackStackImmediate();
        }

        else  {
             //make an instance of logout dialog
            // Create and show the dialog.
            LogoutDialogFragment newFragment = LogoutDialogFragment.newInstance();
            newFragment.show(getFragmentManager(),"Logout");

        }
    }


}

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


public class Home extends ActionBarActivity
        implements TabHomeStudentFragment.OnFragmentInteractionListener ,
        TabHomeCompanyFragment.OnFragmentInteractionListener,
        NewOffer.OnFragmentInteractionListener,
        ProfileManagementFragment.OnInteractionListener,
        ProfileManagement.OnInteractionListener ,
        OfferSearchFragment.OnFragmentInteractionListener ,
        StudentCompanySearchFragment.OnFragmentInteractionListener,
        CompanyStudentSearchFragment.OnFragmentInteractionListener,
        MailBoxFragment.OnFragmentInteractionListener,
        Home_tab.OnFragmentInteractionListener

{
    private Toolbar toolbar;   // Declaring the Toolbar Object

    /**
     * ********For drawer*****************
     */
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private menuAdapter mAdapter;
    private ProfileManagement profileManagement;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private TypedArray ICONS;
    private String[] TITLES;
    private Boolean init=false;
/********************************************************/
/**
 *
 * *************PROFILE MAGEMENT
 */

    private boolean isEditMode;
    private GlobalData application;
    private ArrayList<OnActivityChangedListener> listeners;


    /**
     * ***********************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Log.println(Log.ASSERT,"HOME","onCreate");

        if(savedInstanceState!=null)
        {
            init=true;
        }

        application = (GlobalData)getApplication();

        /* initialize the listeners list:
                each listener is a fragment: when the activity
                performs a significant change, listeners are informed via
                the "onActivityStateChanged" method
         */
        listeners = new ArrayList<OnActivityChangedListener>();
        isEditMode = false;


        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }// Setting toolbar as the ActionBar with setSupportActionBar() call
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

      application.setToolbar(toolbar);
      //Setup Drawer

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
        if(user.getType().toLowerCase().equals("student"))
        {
            TITLES=getResources().getStringArray(R.array.Menu_items_student);
            ICONS=getResources().obtainTypedArray(R.array.StudentMenuicons);

                   if(init==false)//setUpMainFragment(1);
                   {

                       FragmentManager fragmentManager = getSupportFragmentManager();
                       //New Fragment
                           TabHomeStudentFragment homeFragment = TabHomeStudentFragment.newInstance();
                           // Insert the fragment by replacing any existing fragment
                           // Insert the fragment by replacing any existing fragment

                           fragmentManager.beginTransaction()
                                   .replace(R.id.tab_Home_container, homeFragment)
                                   .addToBackStack("Home")
                                   .commit();

                   }


        }
      else
        {
            TITLES=getResources().getStringArray(R.array.Menu_items_Company);
            ICONS=getResources().obtainTypedArray(R.array.CompanytMenuicons);

              // setUpMainFragment(2);
            if(init==false)
            {
                FragmentManager fragmentManager =getSupportFragmentManager();

                //New Fragment
                TabHomeCompanyFragment homeFragment = TabHomeCompanyFragment.newInstance();
                // Insert the fragment by replacing any existing fragment
                // Insert the fragment by replacing any existing fragment

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, homeFragment)
                        .commit();
                mDrawerLayout.closeDrawers();

            }

        }

        // specify an adapter (see also next example)
        mAdapter = new menuAdapter(TITLES, ICONS,user,this,mDrawerLayout,application);
        mDrawerList.setAdapter(mAdapter);

        application.setmAdapter(mAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed


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
        if (!application.getCurrentUser().isEmailVerified())
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

    /* -------------------- PROFILE MANAGEMENT INTERFACE -----------------------------------------*/
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

    @Override
    public void openMailBox(User user) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

//        Bundle data = new Bundle();
//        ArrayList<String> recipients = new ArrayList<String>();
//        recipients.add(user.getMail());
//        data.putStringArrayList(MailBoxDetailFragment.RECIPIENTS_KEY, recipients);

        MailBoxNewFragment mailbox = MailBoxNewFragment.newInstance();
        ft.replace(R.id.tab_Home_container,mailbox);
        ft.commit();

    }

    public void setProfileManagement(ProfileManagement profileManagement) {
        this.profileManagement = profileManagement;
    }

/* ---------------- END PROFILE MANAGEMENT INTERFACE -----------------------------------------*/


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

package com.example.giuliagigi.jobplacement;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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

import java.util.ArrayList;


public class Home extends ActionBarActivity  implements TabHomeFragment.OnFragmentInteractionListener , ProfileManagementFragment.OnInteractionListener, ProfileManagement.OnInteractionListener
{


    private Toolbar toolbar;   // Declaring the Toolbar Object

    /**
     * ********For drawer*****************
     */
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private String[] mListTitles;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
/********************************************************/
/**
 *
 * *************PROFILE MAGEMENT
 */

    private boolean isEditMode;
    private GlobalData application;
    private ProfileManagementFragment currentFragment;
    private ArrayList<OnActivityChangedListener> listeners;

/****************************************************************/
    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Home", "Favourites"};
    int Numboftabs = 2;

    /***************************************************************/

    /**
     * **********************FOR EXAMPLE**********************
     */
    String TITLES[] = {"Home", "Profile", "Search", "Companies", "MailBox"};
    int ICONS[] = {R.drawable.ic_home,
            R.drawable.ic_profile,
            R.drawable.ic_search,
            R.drawable.ic_search,
            R.drawable.ic_action};


    String NAME = "Akash Bangad";
    String EMAIL = "akash.bangad@android4devs.com";
    int PROFILE = R.drawable.ic_profile;

    /**
     * ***********************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* initialize the listeners list:
                each listener is a fragment: when the activity
                performs a significant change, listeners are informed via
                the "onActivityStateChanged" method
         */
        listeners = new ArrayList<OnActivityChangedListener>();
        isEditMode = false;

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        //Setup Drawer
        mListTitles = getResources().getStringArray(R.array.Menu_items_student);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);

        //create adapter
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mDrawerList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mDrawerList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new menuAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE,this,mDrawerLayout,mDrawerList,toolbar);
        mDrawerList.setAdapter(mAdapter);


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


        }; // Drawer Toggle Object Made
        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

           FragmentManager fragmentManager =getSupportFragmentManager();

            //New Fragment
            TabHomeFragment homeFragment = TabHomeFragment.newInstance();
            // Insert the fragment by replacing any existing fragment
            // Insert the fragment by replacing any existing fragment

            fragmentManager.beginTransaction()
                    .replace(R.id.tab_Home_container, homeFragment)
                    .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean isEditMode() {
        return isEditMode;
    }

    @Override
    public void addOnActivityChangedListener(OnActivityChangedListener listener) {

        listeners.add(listener);
        Log.println(Log.ASSERT, "HOME ACTIVITY", "number of listeners: " + listeners.size());
    }

    @Override
    public void removeOnActivityChangedListener(OnActivityChangedListener listener) {

        listeners.remove(listener);
        Log.println(Log.ASSERT,"HOME ACTIVITY", "number of listeners: " + listeners.size());
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
}

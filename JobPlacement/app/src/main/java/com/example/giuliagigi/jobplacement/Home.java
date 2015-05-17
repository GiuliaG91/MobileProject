package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;



import java.util.ArrayList;


public class Home extends ActionBarActivity  implements TabHomeStudentFragment.OnFragmentInteractionListener ,TabHomeCompanyFragment.OnFragmentInteractionListener, NewOffer.OnFragmentInteractionListener,
                                                ProfileManagementFragment.OnInteractionListener, ProfileManagement.OnInteractionListener , menuAdapter.SetSelectedItem ,
                                                  OfferSearchFragment.OnFragmentInteractionListener , StudentCompanySearchFragment.OnFragmentInteractionListener,
                                                    CompanyStudentSearchFragment.OnFragmentInteractionListener, MailBoxFragment.OnFragmentInteractionListener

{
    private Toolbar toolbar;   // Declaring the Toolbar Object

    /**
     * ********For drawer*****************
     */
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private TypedArray ICONS;
    private String[] TITLES;
    private static int mDrawerSelectedItem=1;

/********************************************************/
/**
 *
 * *************PROFILE MAGEMENT
 */

    private boolean isEditMode;
    private GlobalData application;
    private ArrayList<OnActivityChangedListener> listeners;


    /****************BACK BUTTON*************/

    protected OnBackPressedListener onBackPressedListener;

    /**
     * ***********************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        application = (GlobalData)getApplication();

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

                   setUpMainFragment(1);
        }
      else
        {
            TITLES=getResources().getStringArray(R.array.Menu_items_Company);
            ICONS=getResources().obtainTypedArray(R.array.CompanytMenuicons);

               setUpMainFragment(2);

        }

        // specify an adapter (see also next example)
        mAdapter = new menuAdapter(TITLES, ICONS,user,this,mDrawerLayout,mDrawerList,toolbar,application);
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




        /* -------------------
                    email verification alert
             */

        Log.println(Log.ASSERT,"HOME ACTIVITY", "account verified: " + application.getCurrentUser().isEmailVerified());
        if (!application.getCurrentUser().isEmailVerified())
            Toast.makeText(getApplicationContext(),"Your email wasn't verified yet. Please click on the link we sent you",Toast.LENGTH_SHORT).show();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT,"HOME ACTIVITY", "onActivityResult");
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

    @Override
    public void setSelectedItem(int position) {
        mDrawerSelectedItem=position;
    }


    public void setUpMainFragment(int type) {


        switch (type) {

            case 1 :

            //Student menu --> student fragments
            switch (mDrawerSelectedItem) {
                case 1: // Home
                    FragmentManager fragmentManager = getSupportFragmentManager();
                        //New Fragment
                        TabHomeStudentFragment homeFragment = TabHomeStudentFragment.newInstance();
                        // Insert the fragment by replacing any existing fragment
                        // Insert the fragment by replacing any existing fragment

                        fragmentManager.beginTransaction()
                                .replace(R.id.tab_Home_container, homeFragment)
                                .commit();

                        // Highlight the selected item, update the title, and close the drawer
                        // Highlight the selected item, update the title, and close the drawer
                    toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                    mDrawerLayout.closeDrawers();



                    break;

                case 2:

                    GlobalData gd = (GlobalData)getApplicationContext();
                    fragmentManager = getSupportFragmentManager();
                    Fragment fragment = ProfileManagement.newInstance(true,gd.getUserObject());

                    fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .commit();


                    toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                    mDrawerLayout.closeDrawers();



                    break;



                case 5:   //MessageBox
                    fragmentManager = getSupportFragmentManager();

                    MailBoxFragment mailBoxFragment = MailBoxFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, mailBoxFragment)
                            .commit();


                    toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                    mDrawerLayout.closeDrawers();

                    break;


                default:
                    break;


            }

       break;
  /**************************************************END STUDENT SWITCH***********************************/
            case 2:
                switch (mDrawerSelectedItem) {

                    case 1 :


                        FragmentManager fragmentManager =getSupportFragmentManager();
                        Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);


                                    //New Fragment
                        TabHomeCompanyFragment homeFragment = TabHomeCompanyFragment.newInstance();
                        // Insert the fragment by replacing any existing fragment
                                    // Insert the fragment by replacing any existing fragment

                        fragmentManager.beginTransaction()
                          .replace(R.id.tab_Home_container, homeFragment)
                          .commit();



                        toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                        mDrawerLayout.closeDrawers();


                        break;

                    case 2:

                        GlobalData gd = (GlobalData)getApplicationContext();

                        Log.println(Log.ASSERT,"HOME", "open company profile");
                        fragmentManager = getSupportFragmentManager();
                        Fragment profileFragment = ProfileManagement.newInstance(true,gd.getUserObject());

                        fragmentManager.beginTransaction()
                                .replace(R.id.tab_Home_container, profileFragment)
                                .commit();


                        toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                        mDrawerLayout.closeDrawers();

                        break;

                    case 4 :

                                 fragmentManager = getSupportFragmentManager();
                                 current = fragmentManager.findFragmentById(R.id.fragment_new_offer);


                                    //New Fragment
                                    NewOffer fragment = NewOffer.newInstance(true,true);
                                    // Insert the fragment by replacing any existing fragment
                                    // Insert the fragment by replacing any existing fragment

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .addToBackStack("Home")
                                            .commit();

                        toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                        mDrawerLayout.closeDrawers();

                        break;


                    case 5:      // MessageBox
                        fragmentManager = getSupportFragmentManager();

                        MailBoxFragment mailBoxFragment = MailBoxFragment.newInstance();

                        fragmentManager.beginTransaction()
                                .replace(R.id.tab_Home_container, mailBoxFragment)
                                .commit();

                        toolbar.setTitle(TITLES[mDrawerSelectedItem]);
                        mDrawerLayout.closeDrawers();

                        break;



                }
                break;
/**************************************************END COMPANY SWITCH***********************************/

            default:
      break;
        }
   /*****************************************END TYPE SWITCH******************************/
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();

        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            toolbar.setTitle("Home");

            getSupportFragmentManager().popBackStackImmediate();
        }

        else  {
             //make an instance of logout dialog
            // Create and show the dialog.
            LogoutDialogFragment newFragment = LogoutDialogFragment.newInstance();
            newFragment.show(getFragmentManager(),"Logout");



        }
    }

    public static void resetMyDrawerSelectedItem(){
        Home.mDrawerSelectedItem = 1;
    }

}

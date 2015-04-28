package com.example.giuliagigi.jobplacement;



import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfileManagement extends Fragment{

    private GlobalData application;

    private boolean editable;



    /**
     * *************For page viewer***************************
     */
    ViewPager pager;
    ProfileManagementViewAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Overview", "Skills","Registry","Account"};
    int Numboftabs = 4;

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
        adapter = new ProfileManagementViewAdapter(getFragmentManager(), Titles, Numboftabs);
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


        /****************************************************/


    }




    /*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listeners = new ArrayList<OnActivityChangedListener>();

        setContentView(R.layout.activity_profile_management);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        editable = false;

        application = (GlobalData)getApplicationContext();
        LinearLayout buttonsModule = new LinearLayout(getApplicationContext());

        if(application.getCurrentUser().getType().equals(User.TYPE_STUDENT)){

            LinearLayout buttonContainers = (LinearLayout)findViewById(R.id.buttons_container);
            buttonsModule = (LinearLayout)getLayoutInflater().inflate(R.layout.students_buttons_module,buttonContainers);
            currentFragment = StudentProfileManagementBasicsFragment.newInstance();

            ft.replace(R.id.container_profile_management_fragment, currentFragment);

            final Button editProfile = (Button)findViewById(R.id.editProfileButton);
            editProfile.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    if(editable) editable = false;
                    else editable = true;

                    if(editable)
                        for(OnActivityChangedListener l:listeners)
                            l.onActivityStateChanged(OnActivityChangedListener.State.EDIT_MODE_STATE,OnActivityChangedListener.State.DISPLAY_MODE_STATE);
                    else
                        for(OnActivityChangedListener l:listeners)
                            l.onActivityStateChanged(OnActivityChangedListener.State.DISPLAY_MODE_STATE,OnActivityChangedListener.State.EDIT_MODE_STATE);
                }
            });

            Button overviewButton = (Button)findViewById(R.id.student_button_overview);
            Button skillsButton = (Button)findViewById(R.id.student_button_skills);
            Button registryButton = (Button)findViewById(R.id.student_button_registry);

            overviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementBasicsFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

            skillsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementSkillsFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

            registryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementRegistryFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

        }
        else if(application.getCurrentUser().getType().equals(User.TYPE_COMPANY)){

            //TODO
        }

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/
}

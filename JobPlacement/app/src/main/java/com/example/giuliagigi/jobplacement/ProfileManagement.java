package com.example.giuliagigi.jobplacement;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.app.Fragment;
import android.widget.Spinner;


public class ProfileManagement extends ActionBarActivity implements ProfileManagementFragment.OnInteractionListener, ActionBar.TabListener{

    private GlobalData application;
    private ProfileManagementFragment currentFragment;
    private boolean editable;

    /*------------- STANDARD CALLBACKS ------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    currentFragment.setEnable(editable);

                    if(editable){
                        editProfile.setText("Save changes");
                    }else {
                        editProfile.setText("Edit Profile");
                    }
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

        // TODO: orientation change not working
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            buttonsModule.setOrientation(LinearLayout.VERTICAL);
        else
            buttonsModule.setOrientation(LinearLayout.HORIZONTAL);

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

    /*------------- FRAGMENT INTERACTION INTERFACE -----------------------------------------------*/

    @Override
    public boolean isInEditMode() {
        return editable;
    }

    /*------------- TAB LISTENER INTERFACE -------------------------------------------------------*/

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

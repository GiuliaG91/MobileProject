package com.example.giuliagigi.jobplacement;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< Updated upstream
import android.widget.TabHost;
=======
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.app.Fragment;
import android.widget.Spinner;
>>>>>>> Stashed changes


public class ProfileManagement extends ActionBarActivity {

    private GlobalData application;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        application = (GlobalData)getApplicationContext();
        if(application.getCurrentUser().getType().equals(User.TYPE_STUDENT)){

            LinearLayout buttonContainers = (LinearLayout)findViewById(R.id.buttons_container);
            getLayoutInflater().inflate(R.layout.students_buttons_module,buttonContainers);

            FrameLayout fragmentContainer = (FrameLayout)findViewById(R.id.container_profile_management_fragment);
            currentFragment = StudentProfileManagementBasicsFragment.newInstance();
            ft.replace(R.id.container_register_fragment,currentFragment);

<<<<<<< Updated upstream
=======

        }else if(application.getCurrentUser().getType().equals(User.TYPE_COMPANY)){


        }
        ft.commit();

>>>>>>> Stashed changes
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



}

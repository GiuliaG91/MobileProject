package com.example.giuliagigi.jobplacement;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import java.util.Arrays;


public class Registration extends ActionBarActivity implements StudentRegistrationFragment.onInteractionListener, CompanyRegistrationFragment.OnInteractionListener {


    private Fragment currentFragment;
    private GlobalData application;

    /* ---------------------- Standard callbacks ------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        application = (GlobalData)getApplicationContext();
        Spinner typeList = (Spinner)findViewById(R.id.type_list);
        typeList.setAdapter(new StringAdapter(User.TYPES));

        typeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                switch (User.TYPES[position]){

                    case User.TYPE_STUDENT:
                        if(findViewById(R.id.fragment_student_register_layout) == null)
                            currentFragment = StudentRegistrationFragment.newInstance();
                        ft.replace(R.id.container_register_fragment,currentFragment);
                        break;
                    case User.TYPE_COMPANY:
                        if(findViewById(R.id.fragment_company_register_layout) == null)
                            currentFragment = CompanyRegistrationFragment.newInstance();
                        ft.replace(R.id.container_register_fragment, currentFragment);
                        break;
                    default:
                        Log.println(Log.ASSERT,"REGISTRATION", "Error: type unknown");
                        break;
                }

                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = null;

                if(findViewById(R.id.fragment_student_register_layout) != null){
                    Log.println(Log.ASSERT,"REGISTRATION","Trying to register a student");
                    StudentRegistrationFragment srf = (StudentRegistrationFragment)currentFragment;
                    user = srf.retrieveRegistrationInfo();
                }
                else if(findViewById(R.id.fragment_company_register_layout)!= null){
                    Log.println(Log.ASSERT,"REGISTRATION","Trying to register a company");
                    CompanyRegistrationFragment crf = (CompanyRegistrationFragment)currentFragment;
                    user = crf.retrieveRegistrationInfo();
                }

                if(user == null)
                    Toast.makeText(getApplicationContext(),"missing informations",Toast.LENGTH_SHORT).show();
                else {

                    Log.println(Log.ASSERT,"REGISTRATION","user: " + user.getMail() + ", " + user.getType());
                    registerNewAccount(user);
                }
            }
        });
    }

    /*these methords are useless*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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



    /* ---------------------------- Adapter for string list --------------------------------- */

    private class StringAdapter extends BaseAdapter{

        public String[] stringArray;

        public StringAdapter(String[] stringArray){
            super();
            this.stringArray = stringArray;
        }

        @Override
        public int getCount() {
            return stringArray.length;
        }

        @Override
        public String getItem(int position) {
            return stringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_text_element,parent,false);

            TextView type = (TextView)convertView.findViewById(R.id.text_view);
            type.setText(stringArray[position]);
            return convertView;
        }
    }

    private void registerNewAccount(final User newUser){

        if(newUser.getType().equals(User.TYPE_STUDENT)){
            Log.println(Log.ASSERT,"GLOBAL DATA", "registering a student");
            Student newStudent = (Student)newUser;
            newStudent.saveInBackground();
        }
        else if(newUser.getType().equals(User.TYPE_COMPANY)){
            Log.println(Log.ASSERT,"GLOBAL DATA", "registering a company");
            Company newCompany = (Company)newUser;
            newCompany.saveInBackground();
        }
        else{
            Log.println(Log.ASSERT,"GLOBAL DATA","Error: unknown type");
        }


        ParseUserWrapper newParseUser = new ParseUserWrapper();
        newParseUser.setEmail(newUser.getMail());
        newParseUser.setPassword(newUser.getPassword());
        newParseUser.setType(newUser.getType());
        newParseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    Log.println(Log.ASSERT,"REGISTRATION", "signup ok");
                    Log.println(Log.ASSERT,"REGISTRATION","registration successful. Redirect to login activity");
                    startActivity(new Intent(getApplicationContext(),Login.class));


                }
                else {

                    Log.println(Log.ASSERT,"REGISTRATION", "signup fail");
                    e.printStackTrace();
                    //TODO manage different kind of errors
                    Log.println(Log.ASSERT,"REGISTRATION","registration unsuccessful. displaying error message - " + e.getMessage());
                    Toast.makeText(getApplicationContext(),"some error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

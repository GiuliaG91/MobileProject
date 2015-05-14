package com.example.giuliagigi.jobplacement;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;


public class Registration extends ActionBarActivity implements StudentRegistrationFragment.onInteractionListener, CompanyRegistrationFragment.OnInteractionListener {

    private static final String USER_TYPE_SELECT = "Select a type";

    private Fragment currentFragment;
    private GlobalData application;
    private Button register;

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
                        if(currentFragment!= null)
                            ft.remove(currentFragment);
                        currentFragment = null;
                        break;
                }

                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeList.setSelection(0);

        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = null;
                RegistrationException re = null;

                if(findViewById(R.id.fragment_student_register_layout) != null){
                    Log.println(Log.ASSERT,"REGISTRATION","Trying to register a student");
                    StudentRegistrationFragment srf = (StudentRegistrationFragment)currentFragment;
                    try {
                        user = srf.retrieveRegistrationInfo();
                    } catch (RegistrationException e) {
                       re = e;
                    }
                }
                else if(findViewById(R.id.fragment_company_register_layout)!= null){
                    Log.println(Log.ASSERT,"REGISTRATION","Trying to register a company");
                    CompanyRegistrationFragment crf = (CompanyRegistrationFragment)currentFragment;
                    try {
                        user = crf.retrieveRegistrationInfo();
                    } catch (RegistrationException e) {
                        re = e;
                    }
                }

                if(re == null){

                    if(user == null)
                        Toast.makeText(getApplicationContext(),"You must select a type of user to register",Toast.LENGTH_SHORT).show();

                    else {

                        Log.println(Log.ASSERT,"REGISTRATION","user: " + user.getMail() + ", " + user.getType());
                        registerNewAccount(user);
                    }

                }
                else {

                    switch (re.getCode()){

                        case RegistrationException.MISSING_INFORMATIONS:

                            Toast.makeText(getApplicationContext(),"missing informations",Toast.LENGTH_SHORT).show();
                            break;
                        case RegistrationException.MISMATCHING_PASSWORDS:

                            Toast.makeText(getApplicationContext(),"mismatching passwords",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
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



    /* ------------------- REGISTRATION PROCEDURE ------------------------------------------------*/

    private void registerNewAccount(final User newUser){


        final ParseUserWrapper newParseUser = new ParseUserWrapper();
        newParseUser.setEmail(newUser.getMail());
        newParseUser.setUsername(newUser.getMail());
        newParseUser.setPassword(newUser.getPassword());
        newParseUser.setType(newUser.getType());
        newParseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    Log.println(Log.ASSERT,"REGISTRATION", "signup ok");
                    Log.println(Log.ASSERT,"REGISTRATION","registration successful. Redirect to login activity");

                    if(newUser.getType().equals(User.TYPE_STUDENT)){

                        /*necessary saving degree before saving student*/
                        Student newStudent = (Student)newUser;
                        final Degree newStudentDegree = newStudent.getDegrees().get(0);
                        newStudentDegree.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    Log.println(Log.ASSERT, "SIGNUP", "Degree saved. proceeding to save user");
                                    newUser.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if (e == null) {

                                                newStudentDegree.setStudent((Student)newUser);
                                                newStudentDegree.saveEventually();
                                                newParseUser.setUser(newUser);
                                                newParseUser.saveEventually();

                                                /* saving credentials for next automatic login */
                                                SharedPreferences sp = application.getLoginPreferences();
                                                if (sp != null) {

                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putBoolean(Login.SHAREDPREF_LATEST_LOGIN_PREFERENCE, true);
                                                    Set<String> knownMails = sp.getStringSet(Login.SHAREDPREF_MAIL_LIST, new HashSet<String>());
                                                    knownMails.add(newUser.getMail());
                                                    editor.putStringSet(Login.SHAREDPREF_MAIL_LIST, knownMails);
                                                    editor.putString(newUser.getMail(), newUser.getPassword());
                                                    editor.putString(Login.SHAREDPREF_LATEST_MAIL, newUser.getMail());
                                                    editor.putString(Login.SHAREDPREF_LATEST_PASSWORD, newUser.getPassword());
                                                    editor.commit();

                                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                                }
                                            } else
                                                Log.println(Log.ASSERT, "SIGNUP", "error while saving user object: " + e.getMessage());
                                        }
                                    });

                                } else
                                    Log.println(Log.ASSERT, "SIGNUP", "error saving degree");
                            }
                        });
                    }

                }
                else {

                    Log.println(Log.ASSERT,"REGISTRATION", "signup fail");
                    e.printStackTrace();
                    Log.println(Log.ASSERT, "REGISTRATION", "registration unsuccessful. displaying error message - " + e.getMessage());

                    switch (e.getCode()){

                        case ParseException.EMAIL_TAKEN:

                            Toast.makeText(getApplicationContext(),"The mail you are trying to use is already taken by another account",Toast.LENGTH_SHORT).show();
                            break;

                        case ParseException.INVALID_EMAIL_ADDRESS:

                            Toast.makeText(getApplicationContext(),"The mail you are trying to use is not valid",Toast.LENGTH_SHORT).show();
                            break;

                        case ParseException.CONNECTION_FAILED:

                            Toast.makeText(getApplicationContext(),"Connection to sever is unavailable, try later",Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }


                }
            }
        });
    }



}

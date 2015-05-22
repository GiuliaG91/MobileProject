package com.example.giuliagigi.jobplacement;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.PersistableBundle;
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
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;


public class Registration extends ActionBarActivity implements StudentRegistrationFragment.onInteractionListener, CompanyRegistrationFragment.OnInteractionListener {

    private static final String BUNDLE_IDENTIFIER = "REGISTRATION";
    private static final String BUNDLE_KEY = "REGISTRATION";

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

                switch (position){

                    case 1:
                        if(findViewById(R.id.fragment_student_register_layout) == null)
                            currentFragment = StudentRegistrationFragment.newInstance();
                        ft.replace(R.id.container_register_fragment,currentFragment);
                        break;
                    case 2:
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
                        Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_must_select_type),Toast.LENGTH_SHORT).show();

                    else {

                        Log.println(Log.ASSERT,"REGISTRATION","user: " + user.getMail() + ", " + user.getType());
                        registerNewAccount(user);
                    }

                }
                else {

                    switch (re.getCode()){

                        case RegistrationException.MISSING_INFORMATIONS:

                            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_missing_info),Toast.LENGTH_SHORT).show();
                            break;
                        case RegistrationException.MISMATCHING_PASSWORDS:

                            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_mismatching_password),Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(currentFragment != null){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(currentFragment);
            ft.commit();
        }
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

                                                User u = new User();
                                                u.setMail(newUser.getMail());
                                                u.setType(User.TYPE_STUDENT);
                                                u.setPassword(newUser.getPassword());
                                                u.setName(newUser.getName() + " " + ((Student)newUser).getSurname());
                                                u.setParseUser(newParseUser);

                                                u.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if(e != null)
                                                            e.printStackTrace();
                                                    }
                                                });

                                                saveLoginPreferences(newUser);

                                                startActivity(new Intent(getApplicationContext(), Login.class));
                                            } else
                                                Log.println(Log.ASSERT, "SIGNUP", "error while saving user object: " + e.getMessage());
                                        }
                                    });

                                } else
                                    Log.println(Log.ASSERT, "SIGNUP", "error saving degree");
                            }
                        });



                    }
                    else if(newUser.getType().equals(User.TYPE_COMPANY)){


                        Log.println(Log.ASSERT,"SIGNUP","registrating company");
                        newUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if(e == null){

                                    newParseUser.setUser(newUser);
                                    newParseUser.saveEventually();

                                    User u = new User();
                                    u.setMail(newUser.getMail());
                                    u.setType(User.TYPE_COMPANY);
                                    u.setPassword(newUser.getPassword());
                                    u.setName(newUser.getName());
                                    u.saveInBackground();

                                    saveLoginPreferences(newUser);

                                    News news = new News();
                                    news.createNews(3, null, null, (GlobalData)getApplication());

                                    ParsePush push = new ParsePush();
                                    push.setChannel(User.TYPE_STUDENT);
                                    push.setMessage(application.getResources().getString(R.string.the_company) + " " + application.getUserObject().getName() + " " + application.getResources().getString(R.string.new_company_signed_up_message) + " \"" + application.getResources().getString(R.string.app_name) + "\"");
                                    push.sendInBackground();

                                    /*
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo(User.TYPE_FIELD, User.TYPE_STUDENT);

                                    try {

                                        JSONObject data = new JSONObject("{\"alert\": \"The company " + ((Company) newUser).getName() + " is signed up to the application\", \"uri\": " + "www.google.com" + "\"}");
                                        ParsePush push = new ParsePush();
                                        push.setQuery(pushQuery);
                                        push.setData(data);
                                        push.sendInBackground();

                                    }catch(JSONException jsonEx) {
                                        jsonEx.printStackTrace();
                                        Log.println(Log.ASSERT, "REGISTRATION", jsonEx.toString());
                                    }
                                    */

                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                }
                                else
                                    Log.println(Log.ASSERT, "SIGNUP", "error while saving user object: " + e.getMessage());

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

                            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_mail_already_used),Toast.LENGTH_SHORT).show();
                            break;

                        case ParseException.INVALID_EMAIL_ADDRESS:

                            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_mail_not_valid),Toast.LENGTH_SHORT).show();
                            break;

                        case ParseException.CONNECTION_FAILED:

                            Toast.makeText(getApplicationContext(),GlobalData.getContext().getString(R.string.string_connection_not_available),Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }


                }
            }
        });
    }


    /* --------------- SAVING LOGIN PREFERENCES -------------------------------------------------- */

    private void saveLoginPreferences(User newUser){

        /* saving credentials for next automatic login */
    /*    SharedPreferences sp = application.getLoginPreferences();
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


        }*/
    }


}

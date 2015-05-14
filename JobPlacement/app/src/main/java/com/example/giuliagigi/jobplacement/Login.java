package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Login extends ActionBarActivity {


    private CheckBox rememberAccount;
    private EditText passwordText;
    private MultiAutoCompleteTextView mailText;
    private TextView registerLink,forgotPassword;
    private Button loginButton;
    private GlobalData application;

    public static final String SHAREDPREF_LATEST_MAIL = "shared_preferences_latest_mail";
    public static final String SHAREDPREF_LATEST_PASSWORD = "shared_preferences_latest_password";
    public static final String SHAREDPREF_LATEST_LOGIN_PREFERENCE = "shared_preferences_latest_login_preference";
    public static final String SHAREDPREF_MAIL_LIST = "shared_preferences_mail_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        application = (GlobalData)getApplicationContext();

        final SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        application.setLoginPreferences(sp);
//        sp.edit().clear().apply(); // pulisce le Shared Preferences

        mailText = (MultiAutoCompleteTextView)findViewById(R.id.email_editText);
        passwordText = (EditText)findViewById(R.id.password_editText);
        loginButton = (Button)findViewById(R.id.login_button);
        registerLink = (TextView)findViewById(R.id.register_link);
        forgotPassword = (TextView)findViewById(R.id.login_passwordReset_text);
        rememberAccount = (CheckBox)findViewById(R.id.login_stayConnected_checkBox);


        mailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(!mailText.getText().toString().trim().isEmpty() && !passwordText.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String[] knownMailsList = new String[sp.getStringSet(SHAREDPREF_MAIL_LIST, new HashSet<String>()).size()];

        int i = 0;
        for(String mail: sp.getStringSet(SHAREDPREF_MAIL_LIST, new HashSet<String>()))
            knownMailsList[i++] = mail;

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, knownMailsList);

        mailText.setAdapter(adapter);
        mailText.setTokenizer(new SpaceTokenizer());
        mailText.setThreshold(1);

        mailText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedMail = adapter.getItem(position);
                String relatedPassword = sp.getString(selectedMail,"");
                passwordText.setText(relatedPassword);
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(!mailText.getText().toString().trim().isEmpty() && !passwordText.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogin(mailText.getText().toString().trim(), passwordText.getText().toString());
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                builder.setTitle("Password reset?");
                final EditText mail = new EditText(getApplicationContext());
                mail.setHint("Insert your mail");
                mail.setTextColor(Color.BLACK);
                mail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(mail);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.println(Log.ASSERT,"LOGIN", "asked a password reset for: " + mail.getText().toString());
                        ParseUser.requestPasswordResetInBackground(mail.getText().toString(), new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {

                                Toast.makeText(getApplicationContext(),"We will send you an eMail for password reset", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.create().show();
            }
        });


        /* --- automatic login --- */
        rememberAccount.setChecked(sp.getBoolean(SHAREDPREF_LATEST_LOGIN_PREFERENCE,false));

        if(rememberAccount.isChecked()){

            String latestMail = sp.getString(SHAREDPREF_LATEST_MAIL,"");
            String latestPassword = sp.getString(SHAREDPREF_LATEST_PASSWORD,"");
            Log.println(Log.ASSERT,"LOGIN","login with credentials: " + latestMail + " - " + latestPassword);

            mailText.setText(latestMail);
            passwordText.setText(latestPassword);

            Toast.makeText(getApplicationContext(),"Logging in ...",Toast.LENGTH_SHORT).show();
            performLogin(latestMail,latestPassword); //COMMENTA PER DISABILITARE LOGIN AUTOMATICO
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
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



    /* ------------------ AUXILIARY METHODS ------------------------------------------------------*/

    private void setEnable(boolean enable){

        mailText.setEnabled(enable);
        passwordText.setEnabled(enable);
        loginButton.setEnabled(enable);
        rememberAccount.setEnabled(enable);
        registerLink.setEnabled(enable);
        forgotPassword.setEnabled(enable);
    }


    private void performLogin(final String mail, final String password) {

        Log.println(Log.ASSERT,"LOGIN", "logging in");
        setEnable(false);

        ParseUser.logInInBackground(mail, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                String result;
                if (e == null) {

                    Log.println(Log.ASSERT, "LOGIN", "login ok");
                    result = "login successful";
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                    SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    if(rememberAccount.isChecked()){

                        editor.putString(SHAREDPREF_LATEST_MAIL,mail);
                        editor.putString(SHAREDPREF_LATEST_PASSWORD,password);
                    }

                    editor.putString(mail,password);
                    Set<String> mailList = sp.getStringSet(SHAREDPREF_MAIL_LIST,new HashSet<String>());
                    mailList.add(mail);
                    editor.putStringSet(SHAREDPREF_MAIL_LIST,mailList);
                    editor.putBoolean(SHAREDPREF_LATEST_LOGIN_PREFERENCE,rememberAccount.isChecked());
                    editor.apply();

//                    /* caching profile infos */
//                    GlobalData gd = (GlobalData)getApplicationContext();
//                    gd.getCurrentUser();
//                    gd.getUserObject();

                    /* launch home activity */
                    Intent i = new Intent(getApplicationContext(),Home.class);
                    startActivity(i);

                } else {

                    Log.println(Log.ASSERT, "LOGIN", "login failed");
                    e.printStackTrace();
                    result = "invalid username or password";
                    setEnable(true);
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

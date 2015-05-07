package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        final EditText mail = (EditText)findViewById(R.id.email_editText);
        final EditText password = (EditText)findViewById(R.id.password_editText);
        final Button login = (Button)findViewById(R.id.login_button);
        final TextView registerLink = (TextView)findViewById(R.id.register_link);
        final TextView forgotPassword = (TextView)findViewById(R.id.login_passwordReset_text);

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login.setEnabled(!mail.getText().toString().trim().isEmpty() && !password.getText().toString().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login.setEnabled(!mail.getText().toString().trim().isEmpty() && !password.getText().toString().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalData gd = (GlobalData)getApplicationContext();
                performLogin(mail.getText().toString(),password.getText().toString());
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

    private void performLogin(String mail, String password) {

        Log.println(Log.ASSERT,"LOGIN", "logging in");

        ParseUser.logInInBackground(mail, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                String result;
                if (e == null) {

                    Log.println(Log.ASSERT, "LOGIN", "login ok");
                    result = "login successful";
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),Home.class);
                    startActivity(i);

                } else {

                    Log.println(Log.ASSERT, "LOGIN", "login failed");
                    e.printStackTrace();
                    result = "invalid username or password";
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

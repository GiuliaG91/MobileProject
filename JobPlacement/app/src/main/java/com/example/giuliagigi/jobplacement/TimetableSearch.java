package com.example.giuliagigi.jobplacement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class TimetableSearch extends ActionBarActivity {

    private static final String TAG = "Main activity - LOG: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_search);

        final Button b = (Button)findViewById(R.id.displayButton);
        final EditText etCourse = (EditText)findViewById(R.id.editTextCourse);
        final EditText etProfessor = (EditText)findViewById(R.id.editTextProfessor);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requestedCourse = etCourse.getText().toString();
                String requestedProfessor = etProfessor.getText().toString();

                Log.println(Log.ASSERT, TAG, "Recovering information from the model");
                Log.println(Log.ASSERT, TAG, "Requested course: " + requestedCourse);
                Log.println(Log.ASSERT, TAG, "Requested professor: " + requestedProfessor);

                Intent i = new Intent(getApplicationContext(),WeekDisplayActivity.class);
                boolean isCourseRequested = false, isProfessorRequested = false;

                if(!requestedCourse.trim().isEmpty())       isCourseRequested = true;
                if(!requestedProfessor.trim().isEmpty())    isProfessorRequested = true;

                i.putExtra(GlobalData.BUNDLE_KEY_CONTAINS_COURSE_NAME,isCourseRequested);
                i.putExtra(GlobalData.BUNDLE_KEY_CONTAINS_PROFESSOR_NAME,isProfessorRequested);

                i.putExtra(GlobalData.BUNDLE_KEY_COURSE_NAME, requestedCourse);
                i.putExtra(GlobalData.BUNDLE_KEY_PROFESSOR_NAME, requestedProfessor);
                startActivity(i);
            }
        });


        etCourse.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                b.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());
            }
        });

        etProfessor.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                b.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

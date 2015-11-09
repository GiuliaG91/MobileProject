package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ProfessorProfileManagementBasicsFragment extends ProfileManagementBasicsFragment {

    private static final String BUNDLE_KEY_PROFESSOR = "bundle_key_professor";
    public static final String BUNDLE_IDENTIFIER = "PROFESSORPROFILEMANAGEMENTBASICS";

    private Professor professor;

    private Date date;
    private boolean birthDateChanged;
    EditText nameText,surnameText, birthCityText;
    TextView birthPicker;
    CheckBox male,female;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static ProfessorProfileManagementBasicsFragment newInstance(Professor professor) {

        ProfessorProfileManagementBasicsFragment fragment = new ProfessorProfileManagementBasicsFragment();
        fragment.professor = professor;
        fragment.setUser(professor);
        return fragment;
    }

    public ProfessorProfileManagementBasicsFragment() {super();}

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_professor_profile_management_basics, container, false);

        if(root == null)
            Log.println(Log.ASSERT,"STUD BASICS", "layout not found");

        nameText = (EditText)root.findViewById(R.id.professor_name_area);
        if(professor.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(professor.getName());

        surnameText = (EditText)root.findViewById(R.id.professor_surname_area);
        if(professor.getSurname() == null)
            surnameText.setText(INSERT_FIELD);
        else
            surnameText.setText(professor.getSurname());

        birthCityText = (EditText)root.findViewById(R.id.professor_birthCity_et);
        if(professor.getBirthCity() == null)
            birthCityText.setText(INSERT_FIELD);
        else
            birthCityText.setText(professor.getBirthCity());


        birthPicker = (TextView)root.findViewById(R.id.professor_birth_et);
        if(professor.getBirth() == null){

            birthPicker.setText(INSERT_FIELD);
        }

        else{

            DateFormat df= SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
            birthPicker.setText(df.format(professor.getBirth()));
        }


        birthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final DatePicker picker = new DatePicker(getActivity());
                picker.setCalendarViewShown(false);
                builder.setTitle("Edit birth date");
                builder.setView(picker);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hasChanged = true;
                        birthDateChanged = true;
                        int day = picker.getDayOfMonth();
                        int month = picker.getMonth();
                        int year = picker.getYear();
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DATE,day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        DateFormat df=SimpleDateFormat.getDateInstance( SimpleDateFormat.MEDIUM, Locale.getDefault());
                        birthPicker.setText(df.format(c.getTime()));
                        date=c.getTime();
//                        birthPicker.setText(String.format("%2d/%2d/%4d", day, month, year));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });



        textFields.add(nameText);
        textFields.add(surnameText);
        textFields.add(birthCityText);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        male = (CheckBox)root.findViewById(R.id.male_checkBox);
        female = (CheckBox)root.findViewById(R.id.female_checkBox);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged)
                    hasChanged = true;
                if(male.isChecked())
                    female.setChecked(false);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged)
                    hasChanged = true;
                if(female.isChecked())
                    male.setChecked(false);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT, "PROF BASICS", "onActivityResult");
    }


    /* ------------------------------------------------------------------------------------------ */
    /* ----------------------- AUXILIARY METHODS ------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */

    @Override
    protected void restoreStateFromBundle() {
        super.restoreStateFromBundle();

        if(bundle!=null)
            professor = (Professor)bundle.get(BUNDLE_KEY_PROFESSOR);
    }

    @Override
    protected void saveStateInBundle() {
        super.saveStateInBundle();

        if(bundle!=null){

            bundle.put(BUNDLE_KEY_PROFESSOR,professor);
        }
    }

    @Override
    public void setEnable(boolean enable) {

        super.setEnable(enable);

        boolean isMale = professor.getSex().equals(Student.SEX_MALE);
        male.setChecked(isMale);
        female.setChecked(!isMale);
        male.setEnabled(enable);
        female.setEnabled(enable);
        birthPicker.setEnabled(enable);
    }


    @Override
    public void saveChanges(){

        super.saveChanges();
        String sex;
        if(male.isChecked())
            sex = Student.SEX_MALE;
        else
            sex = Student.SEX_FEMALE;

        if(!nameText.getText().toString().equals(INSERT_FIELD)) professor.setName(nameText.getText().toString());
        if(!surnameText.getText().toString().equals(INSERT_FIELD)) professor.setSurname(surnameText.getText().toString());
        if(!birthCityText.getText().toString().equals(INSERT_FIELD)) professor.setBirthCity(birthCityText.getText().toString());
        professor.setSex(sex);

        if(birthDateChanged){


            Log.println(Log.ASSERT,"Basics", "data: " + date.toString());
            professor.setBirth(date);
            birthDateChanged = false;
        }

        Log.println(Log.ASSERT, "BASICS", "now saving..." + professor.getObjectId());
        professor.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                    Log.println(Log.ASSERT, "BASICS", "saved!");
            }
        });
    }
}

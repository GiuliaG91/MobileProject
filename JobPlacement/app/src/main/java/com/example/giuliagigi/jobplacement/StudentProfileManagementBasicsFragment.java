package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StudentProfileManagementBasicsFragment extends ProfileManagementFragment {

    private static final String BUNDLE_KEY_NAME = "StudentProfileManagementBasicsFragment_name";
    private static final String BUNDLE_KEY_SURNAME = "StudentProfileManagementBasicsFragment_surname";
    private static final String BUNDLE_KEY_MALE = "StudentProfileManagementBasicsFragment_isMale";

    private Student currentUser;
    EditText nameText,surnameText;
    CheckBox male,female;
    DatePicker birthPicker;

    public StudentProfileManagementBasicsFragment() {super();}
    public static StudentProfileManagementBasicsFragment newInstance() {
        StudentProfileManagementBasicsFragment fragment = new StudentProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentUser=application.getStudentFromUser();
        root = inflater.inflate(R.layout.fragment_student_profile_management_basics, container, false);

        nameText = (EditText)root.findViewById(R.id.student_name_area);
        if(currentUser.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(currentUser.getName());

        surnameText = (EditText)root.findViewById(R.id.student_surname_area);
        if(currentUser.getSurname() == null)
            surnameText.setText(INSERT_FIELD);
        else
            surnameText.setText(currentUser.getSurname());

        textFields.add(nameText);
        textFields.add(surnameText);

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

        birthPicker = (DatePicker)root.findViewById(R.id.student_birth_datePicker);
        birthPicker.setOnClickListener(new OnFieldClickedListener());

        EditText emailText = (EditText)root.findViewById(R.id.student_email_area);
        emailText.setText(currentUser.getMail());
        setEnable(hostActivity.isInEditMode());
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(hasChanged){

            outState.putString(BUNDLE_KEY_NAME,nameText.getText().toString());
            outState.putString(BUNDLE_KEY_SURNAME, surnameText.getText().toString());
            outState.putBoolean(BUNDLE_KEY_MALE,male.isChecked());
        }
    }




    @Override
    public void setEnable(boolean enable){

        super.setEnable(enable);

        boolean isMale = currentUser.getSex().equals(Student.SEX_MALE);
        male.setChecked(isMale);
        female.setChecked(!isMale);
        male.setEnabled(enable);
        female.setEnabled(enable);
        birthPicker.setEnabled(enable);

        if(hasChanged){

            if(enable)
                restorePreaviousState();
            else
                saveChanges();
        }

    }

    @Override
    protected void restorePreaviousState() {

        //TODO not working as expected
        nameText.setText(getArguments().getString(BUNDLE_KEY_NAME));
        surnameText.setText(getArguments().getString(BUNDLE_KEY_SURNAME));
        male.setChecked(getArguments().getBoolean(BUNDLE_KEY_MALE));
        female.setChecked(!getArguments().getBoolean(BUNDLE_KEY_MALE));
    }

    @Override
    public void saveChanges(){

        String sex;
        if(male.isChecked())
            sex = Student.SEX_MALE;
        else
            sex = Student.SEX_FEMALE;

        Date birth = null;
        Calendar c = GregorianCalendar.getInstance();
        c.set(birthPicker.getYear(),birthPicker.getMonth(),birthPicker.getDayOfMonth());
        birth = c.getTime();

        currentUser.setName(nameText.getText().toString());
        currentUser.setSurname(surnameText.getText().toString());
        currentUser.setSex(sex);
        currentUser.setBirth(birth);
        currentUser.saveInBackground();
        hasChanged = false;
    }


}

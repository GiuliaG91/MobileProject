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
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class StudentProfileManagementBasicsFragment extends ProfileManagementBasicsFragment {

    public static final String TITLE = GlobalData.getContext().getString(R.string.string_basics_tab);
    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILEMANAGEMENTBASICS";
    private static final String BUNDLE_KEY_STUDENT = "bundle_key_student";
    private static final String BUNDLE_KEY_NAME = "bundle_key_student_name";
    private static final String BUNDLE_KEY_SURNAME = "bundle_key_student_surname";

    private Student student;
    private Date date;
    private boolean birthDateChanged;
    EditText nameText,surnameText, birthCityText, emailText, descriptionText;
    TextView birthPicker;
//    LinearLayout profilePhoto;
    CheckBox male,female;


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/

    public StudentProfileManagementBasicsFragment() {super();}
    public static StudentProfileManagementBasicsFragment newInstance(Student student) {
        StudentProfileManagementBasicsFragment fragment = new StudentProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setStudent(student);
        fragment.setUser(student);
        return fragment;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        birthDateChanged = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_student_profile_management_basics, container, false);

        if(root == null)
            Log.println(Log.ASSERT,"STUD BASICS", "layout not found");

        nameText = (EditText)root.findViewById(R.id.student_name_area);
        if(student.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(student.getName());

        surnameText = (EditText)root.findViewById(R.id.student_surname_area);
        if(student.getSurname() == null)
            surnameText.setText(INSERT_FIELD);
        else
            surnameText.setText(student.getSurname());

        birthCityText = (EditText)root.findViewById(R.id.student_birthCity_et);
        if(student.getBirthCity() == null)
            birthCityText.setText(INSERT_FIELD);
        else
            birthCityText.setText(student.getBirthCity());


        birthPicker = (TextView)root.findViewById(R.id.student_birth_et);
        if(student.getBirth() == null){

            birthPicker.setText(INSERT_FIELD);
        }

        else{

            DateFormat df=SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,Locale.getDefault());
            birthPicker.setText(df.format(student.getBirth()));
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


        descriptionText = (EditText)root.findViewById(R.id.student_description_et);
        if(student.getDescription() == null)
            descriptionText.setText(INSERT_FIELD);
        else
            descriptionText.setText(student.getDescription());

        textFields.add(nameText);
        textFields.add(surnameText);
        textFields.add(birthCityText);
        textFields.add(descriptionText);

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
        host = null;

        MyBundle b = application.addBundle(BUNDLE_IDENTIFIER);
        b.put(BUNDLE_KEY_STUDENT,student);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* ----------------------- AUXILIARY METHODS ----------------------------------------------- */

    @Override
    protected void restoreStateFromBundle() {
        super.restoreStateFromBundle();

        if(bundle!=null){

            student = (Student)bundle.get(BUNDLE_KEY_STUDENT);

        }
    }

    @Override
    protected void saveStateInBundle() {
        super.saveStateInBundle();

        if(bundle!=null){

            bundle.put(BUNDLE_KEY_STUDENT,student);
        }
    }

    @Override
    public void setEnable(boolean enable){

        super.setEnable(enable);

        boolean isMale = student.getSex().equals(Student.SEX_MALE);
        male.setChecked(isMale);
        female.setChecked(!isMale);
        male.setEnabled(enable);
        female.setEnabled(enable);
        birthPicker.setEnabled(enable);
//        profilePhoto.setEnabled(enable);
    }


    @Override
    public void saveChanges(){

        super.saveChanges();
        String sex;
        if(male.isChecked())
            sex = Student.SEX_MALE;
        else
            sex = Student.SEX_FEMALE;

        if(!nameText.getText().toString().equals(INSERT_FIELD)) student.setName(nameText.getText().toString());
        if(!surnameText.getText().toString().equals(INSERT_FIELD)) student.setSurname(surnameText.getText().toString());
        if(!birthCityText.getText().toString().equals(INSERT_FIELD)) student.setBirthCity(birthCityText.getText().toString());
        if(!descriptionText.getText().toString().equals(INSERT_FIELD)) student.setDescription(descriptionText.getText().toString());
        student.setSex(sex);

        if(birthDateChanged){


            Log.println(Log.ASSERT,"Basics", "data: " + date.toString());
            student.setBirth(date);
            birthDateChanged = false;
        }

        Log.println(Log.ASSERT, "BASICS", "now saving..." + student.getObjectId());
        student.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                    Log.println(Log.ASSERT, "BASICS", "saved!");
            }
        });
    }


}

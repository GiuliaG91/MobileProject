package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.giuliagigi.jobplacement.StudentRegistrationFragment.onInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentRegistrationFragment extends Fragment {

    private static final String BUNDLE_KEY_MAIL = "BUNDLE_KEY_MAIL";
    private static final String BUNDLE_KEY_PASSWORD = "BUNDLE_KEY_PASSWORD";
    private static final String BUNDLE_KEY_CONFIRM_PASSWORD = "BUNDLE_KEY_CONFIRM_PASSWORD";
    private static final String BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME";
    private static final String BUNDLE_KEY_SURNAME = "BUNDLE_KEY_SURNAME";
    private static final String BUNDLE_KEY_MALE = "BUNDLE_KEY_MALE";
    private static final String BUNDLE_KEY_DAY = "BUNDLE_KEY_DAY";
    private static final String BUNDLE_KEY_MONTH = "BUNDLE_KEY_MONTH";
    private static final String BUNDLE_KEY_YEAR = "BUNDLE_KEY_YEAR";
    private static final String BUNDLE_KEY_FEMALE = "BUNDLE_KEY_FEMALE";
    private static final String BUNDLE_KEY_DEGREE_TYPE = "BUNDLE_KEY_DEGREE_TYPE";
    private static final String BUNDLE_KEY_DEGREE_STUDY = "BUNDLE_KEY_DEGREE_STUDY";
    private static final String BUNDLE_KEY_HAS_SAVED_STATE = "BUNDLE_KEY_SAVED_STATE";
    private static final String BUNDLE_IDENTIFIER = "STUDENTREGISTRATION";
    private static final String BUNDLE_KEY_BUNDLE = "BUNDLE_KEY_BUNDLE";

    EditText mail,password,confirmPassword,name,surname;
    CheckBox male,female;
    Spinner degreeTypeList,degreeStudiesList;
    TextView birthPicker;
    private View root;
    GlobalData application;

    int day, month, year;
    Date date;

    private onInteractionListener hostActivity;

    /*------------- Constructors ----------------------------------------------*/
    public StudentRegistrationFragment() {
        super();
    }
    public static StudentRegistrationFragment newInstance() {
        StudentRegistrationFragment fragment = new StudentRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /* --------------- Standard Callbacks --------------------------------------- */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            hostActivity = (onInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        application = (GlobalData)activity.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_student_registration, container, false);

        mail = (EditText)root.findViewById(R.id.student_mail);
        password = (EditText)root.findViewById(R.id.student_password);
        confirmPassword = (EditText)root.findViewById(R.id.student_confirmPassword);
        name = (EditText)root.findViewById(R.id.student_name);
        surname = (EditText)root.findViewById(R.id.student_surname);

        male = (CheckBox)root.findViewById(R.id.male_checkBox);
        female = (CheckBox)root.findViewById(R.id.female_checkBox);

        degreeTypeList = (Spinner)root.findViewById(R.id.degree_list);
        degreeStudiesList = (Spinner)root.findViewById(R.id.studies_list);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(male.isChecked())
                    female.setChecked(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(female.isChecked())
                    male.setChecked(false);
            }
        });

        birthPicker = (TextView)root.findViewById(R.id.birth_datePicker);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        day = today.monthDay;
        month = today.month;
        year = today.year;
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
                        day = picker.getDayOfMonth();
                        month = picker.getMonth();
                        year = picker.getYear();
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DATE,day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        DateFormat df= SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
                        birthPicker.setText(df.format(c.getTime()));
                        date=c.getTime();
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

        degreeTypeList.setAdapter(new StringAdapterDegree(Degree.TYPES));
        degreeStudiesList.setAdapter(new StringAdapterDegree(Degree.STUDIES));

        if(application.getBundle(BUNDLE_IDENTIFIER)!= null)
            restorePreviousState();

        return root;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        hostActivity = null;
        saveState();
    }





    /* -------------------- AUXILIARY METHODS --------------------------------------------------- */

    private void saveState(){

        MyBundle b = application.addBundle(BUNDLE_IDENTIFIER);

        b.putString(BUNDLE_KEY_MAIL, mail.getText().toString());
        b.putString(BUNDLE_KEY_PASSWORD, password.getText().toString());
        b.putString(BUNDLE_KEY_CONFIRM_PASSWORD, confirmPassword.getText().toString());
        b.putString(BUNDLE_KEY_NAME, name.getText().toString());
        b.putString(BUNDLE_KEY_SURNAME, surname.getText().toString());
        b.putBoolean(BUNDLE_KEY_MALE,male.isChecked());
        b.putBoolean(BUNDLE_KEY_FEMALE,female.isChecked());
        b.putInt(BUNDLE_KEY_DEGREE_TYPE,Degree.getTypeID((String)degreeTypeList.getSelectedItem()));
        b.putInt(BUNDLE_KEY_DEGREE_STUDY,Degree.getStudyID((String)degreeStudiesList.getSelectedItem()));
        b.putInt(BUNDLE_KEY_DAY,day);
        b.putInt(BUNDLE_KEY_MONTH,month);
        b.putInt(BUNDLE_KEY_YEAR,year);

    }


    private void restorePreviousState(){

        MyBundle b = application.getBundle(BUNDLE_IDENTIFIER);

        mail.setText(b.getString(BUNDLE_KEY_MAIL));
        password.setText(b.getString(BUNDLE_KEY_PASSWORD));
        confirmPassword.setText(b.getString(BUNDLE_KEY_CONFIRM_PASSWORD));
        name.setText(b.getString(BUNDLE_KEY_NAME));
        surname.setText(b.getString(BUNDLE_KEY_SURNAME));

        male.setChecked(b.getBoolean(BUNDLE_KEY_MALE));
        female.setChecked(b.getBoolean(BUNDLE_KEY_FEMALE));

        day = b.getInt(BUNDLE_KEY_DAY);
        month = b.getInt(BUNDLE_KEY_MONTH);
        year = b.getInt(BUNDLE_KEY_YEAR);

        Calendar c=Calendar.getInstance();
        c.set(Calendar.DATE,day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        DateFormat df= SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
        birthPicker.setText(df.format(c.getTime()));
        date=c.getTime();

        degreeTypeList.setSelection(b.getInt(BUNDLE_KEY_DEGREE_TYPE));
        degreeStudiesList.setSelection(b.getInt(BUNDLE_KEY_DEGREE_STUDY));
    }


    public Student retrieveRegistrationInfo() throws RegistrationException{

        String degreeType = (String)degreeTypeList.getSelectedItem();
        String degreeStudies = (String)degreeStudiesList.getSelectedItem();

        String sex;
        if(male.isChecked())
            sex = Student.SEX_MALE;
        else if(female.isChecked())
            sex = Student.SEX_FEMALE;
        else
            sex = null;

        Student newStudent = new Student();
        Degree degree = new Degree();

        RegistrationException re = new RegistrationException(RegistrationException.MISSING_INFORMATIONS);
        if(mail.getText().toString().trim().isEmpty())
            throw re;
        if(password.getText().toString().isEmpty())
            throw re;
        if(sex == null)
            throw re;
        if(name.getText().toString().isEmpty())
            throw re;
        if(surname.getText().toString().isEmpty())
            throw re;
        if(degreeType == null)
            throw re;
        if(degreeStudies == null)
            throw re;
        if(date == null)
            throw re;

        re = new RegistrationException(RegistrationException.MISMATCHING_PASSWORDS);

        if(!password.getText().toString().equals(confirmPassword.getText().toString()))
            throw re;

        degree.setType(degreeType);
        degree.setStudies(degreeStudies);

        newStudent.setMail(mail.getText().toString());
        newStudent.setPassword(password.getText().toString());
        newStudent.setType(User.TYPE_STUDENT);
        newStudent.setSex(sex);
        newStudent.setName(name.getText().toString());
        newStudent.setSurname(surname.getText().toString());
        newStudent.setBirth(date);
        newStudent.addDegree(degree);
        return newStudent;
    }

    public interface onInteractionListener {}

    protected class StringAdapterDegree extends BaseAdapter {

        public String[] stringArray;

        public StringAdapterDegree(String[] stringArray){
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.spinner_item, parent, false);

            TextView type = (TextView)convertView.findViewById(R.id.text_view);
            type.setText(stringArray[position]);
            return convertView;
        }
    }




}

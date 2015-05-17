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

    private onInteractionListener hostActivity;
    private View root;
    int day, month, year;
    TextView birthPicker;
    Date date;


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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_student_registration, container, false);
        final CheckBox male = (CheckBox)root.findViewById(R.id.male_checkBox);
        final CheckBox female = (CheckBox)root.findViewById(R.id.female_checkBox);

        Spinner degreeList = (Spinner)root.findViewById(R.id.degree_list);
        Spinner studiesList = (Spinner)root.findViewById(R.id.studies_list);

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
                        int day = picker.getDayOfMonth();
                        int month = picker.getMonth();
                        int year = picker.getYear();
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

        degreeList.setAdapter(new StringAdapterDegree(Degree.TYPES));
        studiesList.setAdapter(new StringAdapterDegree(Degree.STUDIES));

        return root;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        hostActivity = null;
    }





    /* ----------------------------------- */
    public Student retrieveRegistrationInfo() throws RegistrationException{

        EditText mail = (EditText)root.findViewById(R.id.student_mail);
        EditText password = (EditText)root.findViewById(R.id.student_password);
        EditText confirmPassword = (EditText)root.findViewById(R.id.student_confirmPassword);
        EditText name = (EditText)root.findViewById(R.id.student_name);
        EditText surname = (EditText)root.findViewById(R.id.student_surname);
        CheckBox male = (CheckBox)root.findViewById(R.id.male_checkBox);
        CheckBox female = (CheckBox)root.findViewById(R.id.female_checkBox);
        Spinner degreeTypeList = (Spinner)root.findViewById(R.id.degree_list);
        Spinner degreeStudiesList = (Spinner)root.findViewById(R.id.studies_list);


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

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


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

        degreeList.setAdapter(new StringAdapter(Degree.TYPES));
        studiesList.setAdapter(new StringAdapter(Degree.STUDIES));

        return root;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        hostActivity = null;
    }





    /* ----------------------------------- */
    public Student retrieveRegistrationInfo() {

        EditText mail = (EditText)root.findViewById(R.id.student_mail);
        EditText password = (EditText)root.findViewById(R.id.student_password);
        EditText name = (EditText)root.findViewById(R.id.student_name);
        EditText surname = (EditText)root.findViewById(R.id.student_surname);
        CheckBox male = (CheckBox)root.findViewById(R.id.male_checkBox);
        CheckBox female = (CheckBox)root.findViewById(R.id.female_checkBox);
        Spinner degreeTypeList = (Spinner)root.findViewById(R.id.degree_list);
        Spinner degreeStudiesList = (Spinner)root.findViewById(R.id.studies_list);
        DatePicker birthPicker = (DatePicker)root.findViewById(R.id.birth_datePicker);

        Date birth = null;
        Calendar c = GregorianCalendar.getInstance();
        c.set(birthPicker.getYear(),birthPicker.getMonth(),birthPicker.getDayOfMonth());
        birth = c.getTime();

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

        if(mail.getText().toString().trim().isEmpty())
            return null;
        if(password.getText().toString().isEmpty())
            return null;
        if(sex == null)
            return null;
        if(name.getText().toString().isEmpty())
            return null;
        if(surname.getText().toString().isEmpty())
            return null;
        if(degreeType == null)
            return null;
        if(degreeStudies == null)
            return null;

        degree.setType(degreeType);
        degree.setStudies(degreeStudies);

        newStudent.setMail(mail.getText().toString());
        newStudent.setUsername(mail.getText().toString());
        newStudent.setPassword(password.getText().toString());
        newStudent.setType(User.TYPE_STUDENT);
        newStudent.setSex(sex);
        newStudent.setName(name.getText().toString());
        newStudent.setSurname(surname.getText().toString());
        newStudent.setBirth(birth);
        newStudent.addDegree(degree);
        return newStudent;
    }

    public interface onInteractionListener {}

    public class StringAdapter extends BaseAdapter {

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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_text_element,parent,false);

            TextView text = (TextView)convertView.findViewById(R.id.text_view);
            text.setText(stringArray[position]);
            return convertView;
        }
    }

}

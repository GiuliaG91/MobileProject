package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;


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

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!male.isSelected())
                    female.setSelected(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!female.isSelected())
                    male.setSelected(false);
            }
        });

        degreeList.setAdapter(new StringAdapter(Student.DEGREE_TYPES));

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
        Spinner degreeList = (Spinner)root.findViewById(R.id.degree_list);
        String degree = (String)degreeList.getSelectedItem();


        //TODO: bug - for some reason it always sees both checkbox as not selected
        // everyone registered as male at the moment
        String sex = Student.SEX_MALE;

        if(male.isSelected()){
            Log.println(Log.ASSERT,"STUD REG FRAG", "a male");
            sex = Student.SEX_MALE;
        }
        else if(female.isSelected()){
            Log.println(Log.ASSERT,"STUD REG FRAG", "a female");
            sex = Student.SEX_FEMALE;
        }
//        else{
//            Log.println(Log.ASSERT,"STUD REG FRAG", "a null");
//            sex = null;
//        }


        Student newStudent = new Student();

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
        if(degree == null)
            return null;

        newStudent.setMail(mail.getText().toString());
        newStudent.setPassword(password.getText().toString());
        newStudent.setType(User.TYPE_STUDENT);
        newStudent.setSex(sex);
        newStudent.setName(name.getText().toString());
        newStudent.setSurname(surname.getText().toString());
        newStudent.setDegree(degree);

        return newStudent;
    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface onInteractionListener {
        // TODO: Update argument type and name
    }




    private class StringAdapter extends BaseAdapter {

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

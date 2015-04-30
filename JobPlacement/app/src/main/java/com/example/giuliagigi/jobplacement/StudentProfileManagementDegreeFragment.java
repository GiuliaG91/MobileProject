package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class StudentProfileManagementDegreeFragment extends ProfileManagementFragment {

    private Student currentUser;
    private Spinner degreeType, degreeStudies;
    private EditText degreeMark;
    private DatePicker degreeDate;
    private Switch hasLoud;
    Button confirm, delete;
    private Degree degree;

    /* ---------- CONSTRUCTORS, GETTERS, SETTERS ----------------------------------------------*/

    public StudentProfileManagementDegreeFragment() {
        super();
    }
    public static StudentProfileManagementDegreeFragment newInstance(Degree degree) {
        StudentProfileManagementDegreeFragment fragment = new StudentProfileManagementDegreeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setDegree(degree);
        return fragment;

    }
    public void setDegree(Degree degree){
        this.degree = degree;
    }


    /* ---------- STANDARD CALLBACKS -------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.println(Log.ASSERT,"DEGREE FRAG", "on attach started");
        currentUser = application.getStudentFromUser();
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

        Log.println(Log.ASSERT,"DEGREE FRAG", "onCreateView start");

        root = inflater.inflate(R.layout.fragment_degree_management, container, false);
        degreeType = (Spinner)root.findViewById(R.id.degree_management_spinnerType);
        degreeStudies = (Spinner)root.findViewById(R.id.degree_management_spinnerField);

        degreeType.setAdapter(new StringAdapter(Degree.TYPES));
        degreeStudies.setAdapter(new StringAdapter(Degree.STUDIES));

        if(degree.getType()!= null)
            degreeType.setSelection(Degree.getTypeID(degree.getType()));
        if(degree.getStudies()!= null)
            degreeStudies.setSelection(Degree.getStudyID(degree.getStudies()));

        confirm = (Button)root.findViewById(R.id.degree_management_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
            }
        });

        delete = (Button)root.findViewById(R.id.degree_management_delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.removeDegree(degree);
                currentUser.saveInBackground();

                try {
                    degree.delete();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                root.setVisibility(View.INVISIBLE);
            }
        });

        degreeMark = (EditText)root.findViewById(R.id.degree_management_mark_area);
        if(degreeMark!= null){
            if(degree.getMark()!= null)
                degreeMark.setText(String.valueOf(degree.getMark()));
            else
                degreeMark.setText(INSERT_FIELD);
        }

        textFields.add(degreeMark);

        degreeDate = (DatePicker)root.findViewById(R.id.degree_management_date_area);
        degreeDate.setOnClickListener(new OnFieldClickedListener());

        hasLoud = (Switch)root.findViewById(R.id.degree_management_loud_switch);
        hasLoud.setChecked(degree.getLoud());


        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        setEnable(host.isInEditMode());
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    /* ------------- AUXILIARY METHODS ----------------------------------------------------*/
    @Override
    public void saveChanges(){

        super.saveChanges();

        currentUser.addDegree(degree);
        currentUser.saveInBackground();

        Date date = null;
        Calendar c = GregorianCalendar.getInstance();
        c.set(degreeDate.getYear(),degreeDate.getMonth(),degreeDate.getDayOfMonth());
        date = c.getTime();

        degree.setType((String) degreeType.getSelectedItem());
        degree.setStudies((String) degreeStudies.getSelectedItem());
        if(!degreeMark.getText().toString().equals(INSERT_FIELD)) degree.setMark(Integer.parseInt(degreeMark.getText().toString()));
        degree.setDegreeDate(date);
        degree.setLoud((Boolean)hasLoud.isChecked());
        degree.saveInBackground();
    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        degreeType.setEnabled(enable);
        degreeStudies.setEnabled(enable);
        degreeDate.setEnabled(enable);
        if(degree.getMark()==110){
            hasLoud.setEnabled(enable);
        }else hasLoud.setEnabled(false);

        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        confirm.setVisibility(visibility);
        delete.setVisibility(visibility);
    }

    /* ---------- ADAPTERS ----------------------------------------------------------------*/

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
                convertView = new TextView(getActivity().getApplicationContext());
            TextView tv = (TextView)convertView;
            tv.setText(stringArray[position]);
            tv.setTextColor(Color.BLACK);

            return convertView;
        }
    }

}

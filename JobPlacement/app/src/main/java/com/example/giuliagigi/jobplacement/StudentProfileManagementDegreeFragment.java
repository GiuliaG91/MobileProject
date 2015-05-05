package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public class StudentProfileManagementDegreeFragment extends ProfileManagementFragment {

    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_STUDY = "bundle_study";
    private static String BUNDLE_MARK = "bundle_mark";
    private static String BUNDLE_DAY = "bundle_day";
    private static String BUNDLE_MONTH = "bundle_month";
    private static String BUNDLE_YEAR = "bundle_year";
    private static String BUNDLE_LOUD = "bundle_loud";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private Student currentUser;
    private Spinner degreeType, degreeStudies;
    private EditText degreeMark;
    private DatePicker degreeDate;
    private Switch hasLoud;
    Button confirm, delete;
    private Degree degree;
    private boolean isRemoved;

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
        currentUser = (Student)application.getUserObject();
        isListenerAfterDetach = true;
        isRemoved = false;
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

        int type,study;
        Integer mark = null;
        int day, month, year;
        boolean loud;
        if(getArguments().getBoolean(BUNDLE_HASCHANGED)){

            type = getArguments().getInt(BUNDLE_TYPE);
            study = getArguments().getInt(BUNDLE_STUDY);
            mark = getArguments().getInt(BUNDLE_MARK);
            day = getArguments().getInt(BUNDLE_DAY);
            month = getArguments().getInt(BUNDLE_MONTH);
            year = getArguments().getInt(BUNDLE_YEAR);
            loud = getArguments().getBoolean(BUNDLE_LOUD);
        }
        else {

            if(degree.getType()!= null)
                type = Degree.getTypeID(degree.getType());
            else
                type = 0;

            if(degree.getStudies()!= null)
                study = Degree.getStudyID(degree.getStudies());
            else
                study = 0;

            if(degree.getMark()!= null)
                mark = degree.getMark();

            if(degree.getDegreeDate()!= null){
                day = degree.getDegreeDate().getDay();
                month = degree.getDegreeDate().getMonth();
                year = degree.getDegreeDate().getYear();
            }
            else
            {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                day = today.monthDay; month=today.month; year=today.year;
            }
            if(degree.getLoud()!=null){
                loud = degree.getLoud();
            }

        }

        root = inflater.inflate(R.layout.fragment_degree_management, container, false);
        degreeType = (Spinner)root.findViewById(R.id.degree_management_spinnerType);
        degreeStudies = (Spinner)root.findViewById(R.id.degree_management_spinnerField);

        degreeType.setAdapter(new StringAdapter(Degree.TYPES));
        degreeStudies.setAdapter(new StringAdapter(Degree.STUDIES));


        degreeType.setSelection(type);
        degreeStudies.setSelection(study);

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

                try {
                    degree.delete();
                    isRemoved = true;
                    currentUser.removeDegree(degree);
                    currentUser.saveEventually();
                    root.setVisibility(View.INVISIBLE);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"unable to delete",Toast.LENGTH_SHORT).show();
                }
            }
        });

        degreeMark = (EditText)root.findViewById(R.id.degree_management_mark_area);
        if(mark != null)
            degreeMark.setText(String.valueOf(mark));
        else
            degreeMark.setText(INSERT_FIELD);

        textFields.add(degreeMark);

        degreeDate = (DatePicker)root.findViewById(R.id.degree_management_datePicker);
        degreeDate.updateDate(year,month,day);
        degreeDate.setOnClickListener(new OnFieldClickedListener());

        hasLoud = (Switch)root.findViewById(R.id.degree_management_loud_switch);
        if (degree.getMark()!= null)
            hasLoud.setChecked(degree.getLoud());
        else hasLoud.setChecked(false);


        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        setEnable(host.isEditMode());
        return root;
    }


    @Override
    public void onDetach() {

        super.onDetach();

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){

            getArguments().putInt(BUNDLE_TYPE,degreeType.getSelectedItemPosition());
            getArguments().putInt(BUNDLE_STUDY,degreeStudies.getSelectedItemPosition());
            getArguments().putInt(BUNDLE_MARK, Integer.parseInt(degreeMark.getText().toString()));
            getArguments().putInt(BUNDLE_DAY, Integer.parseInt(String.valueOf(degreeDate.getDayOfMonth())));
            getArguments().putInt(BUNDLE_MONTH, Integer.parseInt(String.valueOf(degreeDate.getMonth())));
            getArguments().putInt(BUNDLE_YEAR, Integer.parseInt(String.valueOf(degreeDate.getYear())));
            getArguments().putBoolean(BUNDLE_LOUD, hasLoud.isChecked());
        }

    }



    /* ------------- AUXILIARY METHODS ----------------------------------------------------*/
    @Override
    public void saveChanges(){

        super.saveChanges();

        if(!isRemoved){

            currentUser.addDegree(degree);
            currentUser.saveEventually();

            Date date = null;
            Calendar c = GregorianCalendar.getInstance();
            c.set(degreeDate.getYear(),degreeDate.getMonth(),degreeDate.getDayOfMonth());
            date = c.getTime();


            degree.setType((String) degreeType.getSelectedItem());
            degree.setStudies((String) degreeStudies.getSelectedItem());
            if(!degreeMark.getText().toString().equals(INSERT_FIELD)) degree.setMark(Integer.parseInt(degreeMark.getText().toString()));
            degree.setDegreeDate(date);
            degree.setLoud(hasLoud.isChecked());
            degree.saveEventually();
        }

    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        degreeType.setEnabled(enable);
        degreeStudies.setEnabled(enable);

        degreeDate.setEnabled(enable);

        if(degree.getMark()!= null) {
            if (degree.getMark() == 110) {
                hasLoud.setEnabled(enable);
            } else hasLoud.setEnabled(false);
        }else hasLoud.setEnabled(false);

        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        confirm.setVisibility(visibility);
        delete.setVisibility(visibility);
    }


}

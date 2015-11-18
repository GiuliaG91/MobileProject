package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class StudentProfileManagementDegreeFragment extends ProfileManagementFragment {

    private static final String TITLE = "Degree";
    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILEDEGREE";

    private static final String BUNDLE_DEGREE = "bundle_degree";
    private static final String BUNDLE_STUDENT = "bundle_student";
    private static final String BUNDLE_TYPE = "bundle_type";
    private static final String BUNDLE_STUDY = "bundle_study";
    private static final String BUNDLE_MARK = "bundle_mark";
    private static final String BUNDLE_LOUD = "bundle_loud";
    private static final String BUNDLE_DATE_DAY = "bundle_date_day";
    private static final String BUNDLE_DATE_MONTH = "bundle_date_month";
    private static final String BUNDLE_DATE_YEAR = "bundle_date_year";
    private static final String BUNDLE_PARENT = "bundle_parent";


    private DegreeFragmentInterface parent;
    private Student student;
    private Degree degree;

    private Spinner degreeType, degreeStudies;
    private EditText degreeMark;
    private TextView degreeDate;
    private Switch hasLoud;
    Button delete;


    private boolean isRemoved, degreeDateChanged;
    private Date dateDegree;
    private Integer day = null, month = null,year = null;
    private int type = 0,study = 0;
    private Integer mark = null;
    private boolean loud = false;
    private String parentName;


    /* -------------------------------------------------------------------------------------------*/
    /* ------------- CONSTRUCTORS, GETTERS, SETTERS ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public StudentProfileManagementDegreeFragment() {
        super();
    }
    public static StudentProfileManagementDegreeFragment newInstance(DegreeFragmentInterface parent, Degree degree,Student student) {

        StudentProfileManagementDegreeFragment fragment = new StudentProfileManagementDegreeFragment();

        fragment.setDegree(degree);
        fragment.parent = parent;
        fragment.setStudent(student);
        fragment.setUser(student);

        ProfileManagementFragment f = (ProfileManagementFragment)parent;
        fragment.parentName = f.bundleIdentifier();

        return fragment;

    }
    public void setDegree(Degree degree){
        this.degree = degree;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    public void setParent(DegreeFragmentInterface parent){

        this.parent = parent;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------- STANDARD CALLBACKS -------------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        isRemoved = false;
        degreeDateChanged = false;

        if(degree != null){

            if(degree.getDegreeDate() != null){

                day = degree.getDegreeDate().getDay();
                month = degree.getDegreeDate().getMonth();
                year = degree.getDegreeDate().getYear() + 1900;
            }

            mark = degree.getMark();
            loud = degree.getLoud();

            if(degree.getStudies() != null)
                study = Degree.getStudyID(degree.getStudies());

            if(degree.getType() != null)
                type = Degree.getTypeID(degree.getType());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_degree_management, container, false);

        degreeType = (Spinner)root.findViewById(R.id.degree_management_spinnerType);
        degreeStudies = (Spinner)root.findViewById(R.id.degree_management_spinnerField);

        degreeType.setAdapter(new StringAdapter(Degree.TYPES));
        degreeStudies.setAdapter(new StringAdapter(Degree.STUDIES));


        degreeType.setSelection(type);
        degreeStudies.setSelection(study);

        delete = (Button)root.findViewById(R.id.degree_management_delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // the object was not created now
                if(degree.getObjectId() != null){

                    degree.deleteEventually(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null){

                                student.removeDegree(degree);
                                student.saveEventually();
                                root.setVisibility(View.INVISIBLE);
                                parent.onDegreeDelete(StudentProfileManagementDegreeFragment.this);
                                isRemoved = true;
                            }
                            else {

                                Toast.makeText(getActivity().getApplicationContext(),getActivity().getResources().getString(R.string.profile_object_delete_failure),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else { // the object was created now: no need to delete it

                    root.setVisibility(View.INVISIBLE);
                    parent.onDegreeDelete(StudentProfileManagementDegreeFragment.this);
                    isRemoved = true;
                }

            }
        });

        degreeMark = (EditText)root.findViewById(R.id.degree_management_mark_area);
        if(mark != null)
            degreeMark.setText(String.valueOf(mark));
        else
            degreeMark.setText(INSERT_FIELD);

        degreeMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    hasLoud.setChecked((Integer.parseInt(degreeMark.getText().toString())== 110));
                    hasLoud.setEnabled((Integer.parseInt(degreeMark.getText().toString())== 110));
                }
                catch (NumberFormatException e){

                    hasLoud.setChecked(false);
                    hasLoud.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        textFields.add(degreeMark);

        degreeDate = (TextView)root.findViewById(R.id.degree_management_datePicker);
        if(degree.getDegreeDate() == null)
            degreeDate.setText(INSERT_FIELD);
        else {
            DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
            degreeDate.setText(df.format(degree.getDegreeDate()));
        }


        degreeDate.setOnClickListener(new View.OnClickListener() {
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
                        int day = picker.getDayOfMonth();
                        int month = picker.getMonth();
                        int year = picker.getYear();
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DATE,day);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        DateFormat df= SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
                        degreeDate.setText(df.format(c.getTime()));
                        dateDegree = c.getTime();
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

        hasLoud = (Switch)root.findViewById(R.id.degree_management_loud_switch);
        if (degree.getMark()!= null)
            hasLoud.setChecked(degree.getLoud());
        else hasLoud.setChecked(false);


        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

//        setEnable(listener.isEditMode());
        return root;
    }


    @Override
    public void onDetach() {

        super.onDetach();

    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------- AUXILIARY METHODS ----------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    protected void saveStateInBundle(Bundle outstate) {
        super.saveStateInBundle(outstate);


        bundle.put(BUNDLE_DEGREE,degree);
        bundle.put(BUNDLE_STUDENT, student);
        bundle.putBoolean(BUNDLE_LOUD, loud);
        bundle.putInt(BUNDLE_TYPE, type);
        bundle.putInt(BUNDLE_STUDY, study);
        bundle.putInt(BUNDLE_MARK, mark);
        bundle.putInt(BUNDLE_DATE_DAY, day);
        bundle.putInt(BUNDLE_DATE_MONTH, month);
        bundle.putInt(BUNDLE_DATE_YEAR, year);
        bundle.putString(BUNDLE_PARENT,parentName);
    }

    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);

        if(bundle != null){

            degree = (Degree)bundle.get(BUNDLE_DEGREE);
            student = (Student)bundle.get(BUNDLE_STUDENT);
            loud = bundle.getBoolean(BUNDLE_LOUD);
            type = bundle.getInt(BUNDLE_TYPE);
            study = bundle.getInt(BUNDLE_STUDY);
            mark = bundle.getInt(BUNDLE_MARK);
            day = bundle.getInt(BUNDLE_DATE_DAY);
            month = bundle.getInt(BUNDLE_DATE_MONTH);
            year = bundle.getInt(BUNDLE_DATE_YEAR);

            parentName = bundle.getString(BUNDLE_PARENT);
            parent = (DegreeFragmentInterface)application.getFragment(parentName);
        }
    }

    @Override
    public void saveChanges(){

        super.saveChanges();

        if(!isRemoved){

            degree.setType((String) degreeType.getSelectedItem());
            degree.setStudies((String) degreeStudies.getSelectedItem());

            try{

                if(!degreeMark.getText().toString().equals(INSERT_FIELD)) degree.setMark(Integer.parseInt(degreeMark.getText().toString()));
            }
            catch (NumberFormatException e){

                Toast.makeText(getActivity(),"not a number inside mark field",Toast.LENGTH_SHORT).show();
                degreeMark.setText(degree.getMark());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"invalid number inside mark field",Toast.LENGTH_SHORT).show();

            }

            if(degreeDateChanged) {


                degree.setDegreeDate(dateDegree);
            }
            degree.setLoud(hasLoud.isChecked());
            degree.setStudent(student);
            degree.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){

                        Log.println(Log.ASSERT,"DEGREE FRAG","degree saved successfully. Adding...");
                        student.addDegree(degree);
                        student.saveEventually();
                    }
                }
            });
        }

    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        int i = 1;
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
        delete.setVisibility(visibility);
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------- PARENT INTERFACE -----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public interface DegreeFragmentInterface{

        public void onDegreeDelete(StudentProfileManagementDegreeFragment toRemove);
    }
}

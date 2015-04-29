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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;


public class StudentProfileManagementDegreeFragment extends ProfileManagementFragment {

    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_STUDY = "bundle_study";
    private static String BUNDLE_MARK = "bundle_mark";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private Student currentUser;
    private Spinner degreeType, degreeStudies;
    private EditText degreeMark;
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
        Log.println(Log.ASSERT,"DEGREE FRAG", "creating new degree fragment");
        return fragment;

    }
    public void setDegree(Degree degree){
        this.degree = degree;
    }


    /* ---------- STANDARD CALLBACKS -------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.println(Log.ASSERT, "DEGREE FRAG", "OnAttach");
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
        Log.println(Log.ASSERT,"DEGREE FRAG", "OnCreateView");

        int type,study,mark;
        if(getArguments().getBoolean(BUNDLE_HASCHANGED)){

            Log.println(Log.ASSERT,"DEGREE FRAG", "changes not saved: restoring state");
            type = getArguments().getInt(BUNDLE_TYPE);
            study = getArguments().getInt(BUNDLE_STUDY);
            mark = getArguments().getInt(BUNDLE_MARK);
        }
        else {

            type = Degree.getTypeID(degree.getType());
            study = Degree.getStudyID(degree.getStudies());
            mark = degree.getMark();
        }

        root = inflater.inflate(R.layout.fragment_degree_management, container, false);
        degreeType = (Spinner)root.findViewById(R.id.degree_management_spinnerType);
        degreeStudies = (Spinner)root.findViewById(R.id.degree_management_spinnerField);

        degreeType.setAdapter(new StringAdapter(Degree.TYPES));
        degreeStudies.setAdapter(new StringAdapter(Degree.STUDIES));

        if(degree.getType()!= null)
            degreeType.setSelection(type);
        if(degree.getStudies()!= null)
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
                degreeMark.setText(String.valueOf(mark));
            else
                degreeMark.setText(INSERT_FIELD);
        }

        textFields.add(degreeMark);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        setEnable(host.isEditMode());
        return root;
    }


    @Override
    public void onDetach() {

        super.onDetach();
        Log.println(Log.ASSERT,"DEGREE FRAG", "OnDetach. saving state");

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){

            Log.println(Log.ASSERT,"DEGREE FRAG", "values changed. saving in bundle");
            getArguments().putInt(BUNDLE_TYPE,degreeType.getSelectedItemPosition());
            getArguments().putInt(BUNDLE_STUDY,degreeStudies.getSelectedItemPosition());
            getArguments().putInt(BUNDLE_MARK, Integer.parseInt(degreeMark.getText().toString()));
        }

    }



    /* ------------- AUXILIARY METHODS ----------------------------------------------------*/
    @Override
    public void saveChanges(){

        super.saveChanges();

        currentUser.addDegree(degree);
        currentUser.saveInBackground();

        degree.setType((String) degreeType.getSelectedItem());
        degree.setStudies((String) degreeStudies.getSelectedItem());
        if(!degreeMark.getText().toString().equals(INSERT_FIELD)) degree.setMark(Integer.parseInt(degreeMark.getText().toString()));
        degree.saveInBackground();

        hasChanged = false;
    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        degreeType.setEnabled(enable);
        degreeStudies.setEnabled(enable);

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

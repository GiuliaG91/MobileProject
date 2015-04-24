package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private Student currentUser;
    ListView degreeList;

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance() {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);
        degreeList = (ListView)root.findViewById(R.id.skills_degree_listview);
        degreeList.setAdapter(new DegreeAdapter(currentUser.getDegrees()));

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }

    public interface OnInteractionListener {}
    
    
    protected class DegreeAdapter extends BaseAdapter {

        private final ArrayList<Degree> degrees;

        public DegreeAdapter(ArrayList<Degree> degrees){
            this.degrees = degrees;
        }

        @Override
        public int getCount() {
            return degrees.size();
        }

        @Override
        public Object getItem(int position) {
            return degrees.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.degree_element,parent,false);

            StringAdapter stringAdapterType = new StringAdapter(Degree.TYPES);
            StringAdapter stringAdapterField = new StringAdapter(Degree.STUDIES);

            Spinner types = (Spinner)convertView.findViewById(R.id.degree_spinnerType);
            Spinner fields = (Spinner)convertView.findViewById(R.id.degree_spinnerField);
            EditText mark = (EditText)convertView.findViewById(R.id.degree__mark_area);

            types.setAdapter(stringAdapterType);
            fields.setAdapter(stringAdapterField);


            Log.println(Log.ASSERT,"DEGREEADAPTER", "getting IDs");

            degrees.get(position);

            Log.println(Log.ASSERT,"DEGREEADAPTER", "type id: " + degrees.get(position).getStudyID());
            Log.println(Log.ASSERT,"DEGREEADAPTER", "type id: " + degrees.get(position).getTypeID());

            //types.setSelection();
            //fields.setSelection();

            return convertView;
        }
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

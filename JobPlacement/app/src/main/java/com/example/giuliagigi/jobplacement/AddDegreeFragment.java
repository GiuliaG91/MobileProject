package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class AddDegreeFragment extends Fragment {

    private onInteractionListener hostActivity;
    protected GlobalData application;
    private View root;
    private Student currentUser;
    private Spinner degreeType;
    private Spinner degreeField;


    public AddDegreeFragment() {
        super();
    }
    public static AddDegreeFragment newInstance() {
        AddDegreeFragment fragment = new AddDegreeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

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

        root = inflater.inflate(R.layout.fragment_add_degree, container, false);

        degreeType = (Spinner)root.findViewById(R.id.add_degree_spinnerType);
        degreeField = (Spinner)root.findViewById(R.id.add_degree_spinnerField);

        final EditText degreeMark = (EditText)root.findViewById(R.id.add_degree__mark_area);

        degreeType.setAdapter(new StringAdapter(Degree.TYPES));
        degreeField.setAdapter(new StringAdapter(Degree.STUDIES));

        Button confirm = (Button)root.findViewById(R.id.add_degree_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = (String)degreeType.getSelectedItem();
                String field = (String)degreeField.getSelectedItem();
                Integer mark = Integer.parseInt(degreeMark.getText().toString());

                Degree newDegree = new Degree();
                newDegree.setType(type);
                newDegree.setStudies(field);
                newDegree.setMark(mark);

                currentUser.addDegree(newDegree);
            }
        });

        return inflater.inflate(R.layout.fragment_add_degree, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        application = (GlobalData)activity.getApplicationContext();
        currentUser = application.getStudentFromUser();

        try {
            hostActivity = (onInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
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

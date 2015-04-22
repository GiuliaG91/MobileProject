package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class StudentProfileManagementRegistryFragment extends ProfileManagementFragment {

    private OnInteractionListener hostActivity;
    private ProfileManagement profileActivity;
    private GlobalData application;
    private Student currentUser;
    private View root;

    public StudentProfileManagementRegistryFragment() {super();}
    public static StudentProfileManagementRegistryFragment newInstance() {
        StudentProfileManagementRegistryFragment fragment = new StudentProfileManagementRegistryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        application = (GlobalData)getActivity().getApplicationContext();
        currentUser = application.getStudentFromUser();
        try {
            profileActivity = (ProfileManagement) activity;
            hostActivity = (OnInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
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
        root = inflater.inflate(R.layout.fragment_student_profile_management_registry, container, false);

        setEnable(profileActivity.getEditable());

        EditText addressText = (EditText)root.findViewById(R.id.student_address_area);
        if(currentUser.getAddress() == null){
            addressText.setText("Inser address");
        }else{
            addressText.setText(currentUser.getAddress());
        }

        EditText cityText = (EditText)root.findViewById(R.id.student_city_area);
        if(currentUser.getCity() == null){
            cityText.setText("Inser city");
        }else{
            cityText.setText(currentUser.getCity());
        }

        EditText postalText = (EditText)root.findViewById(R.id.student_CAP_area);
        if(currentUser.getPostalCode() == null){
            postalText.setText("Inser CAP");
        }else{
            postalText.setText(currentUser.getPostalCode());
        }

        EditText nationText = (EditText)root.findViewById(R.id.student_nation_area);
        if(currentUser.getNation() == null){
            nationText.setText("Inser nation");
        }else{
            nationText.setText(currentUser.getNation());
        }

        return root;
    }


     public interface OnInteractionListener {


    }

    public void setEnable(boolean enable){
        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        EditText addressText = (EditText)root.findViewById(R.id.student_address_area);
        if(addressText.getText() == null) {
            addressText.setVisibility(visibility);
        }
        addressText.setEnabled(enable);

        EditText cityText = (EditText)root.findViewById(R.id.student_city_area);
        if(cityText.getText() == null) {
            cityText.setVisibility(visibility);
        }
        cityText.setEnabled(enable);

        EditText postalText = (EditText)root.findViewById(R.id.student_CAP_area);
        if(postalText.getText() == null) {
            postalText.setVisibility(visibility);
        }
        postalText.setEnabled(enable);

        EditText nationText = (EditText)root.findViewById(R.id.student_nation_area);
        if(nationText.getText() == null) {
            nationText.setVisibility(visibility);
        }
        nationText.setEnabled(enable);

        if(enable == false){
            currentUser.setAddress(addressText.getText().toString());
            currentUser.setCity(cityText.getText().toString());
            currentUser.setPostalCode(postalText.getText().toString());
            currentUser.setNation(nationText.getText().toString());

        }
    }

}

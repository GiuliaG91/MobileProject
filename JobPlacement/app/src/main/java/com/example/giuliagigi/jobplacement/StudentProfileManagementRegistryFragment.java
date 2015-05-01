package com.example.giuliagigi.jobplacement;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class StudentProfileManagementRegistryFragment extends ProfileManagementFragment {

    private Student currentUser;
    private EditText addressText,cityText,postalText,nationText;
    private Button phonePlus;
    private LinearLayout phonesContainer;
    private ArrayList<ProfileManagementTelephoneFragment> telephoneFragments;

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

        currentUser = application.getStudentFromUser();
        telephoneFragments = new ArrayList<ProfileManagementTelephoneFragment>();
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

        addressText = (EditText)root.findViewById(R.id.student_address_area);
        if(currentUser.getAddress() == null){
            addressText.setText(INSERT_FIELD);
        }else{
            addressText.setText(currentUser.getAddress());
        }

        cityText = (EditText)root.findViewById(R.id.student_city_area);
        if(currentUser.getCity() == null){
            cityText.setText(INSERT_FIELD);
        }else{
            cityText.setText(currentUser.getCity());
        }

        postalText = (EditText)root.findViewById(R.id.student_CAP_area);
        if(currentUser.getPostalCode() == null){
            postalText.setText(INSERT_FIELD);
        }else{
            postalText.setText(currentUser.getPostalCode());
        }

        nationText = (EditText)root.findViewById(R.id.student_nation_area);
        if(currentUser.getNation() == null){
            nationText.setText(INSERT_FIELD);
        }else{
            nationText.setText(currentUser.getNation());
        }

        phonesContainer = (LinearLayout)root.findViewById(R.id.student_phones_container);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        textFields.add(addressText);
        textFields.add(cityText);
        textFields.add(postalText);
        textFields.add(nationText);

        for(EditText et: textFields)
            et.addTextChangedListener(hasChangedListener);

        phonePlus = (Button)root.findViewById(R.id.student_phones_plusButton);
        phonePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileManagementTelephoneFragment tf = ProfileManagementTelephoneFragment.newInstance(new Telephone());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_phones_container,tf);
                ft.commit();
                telephoneFragments.add(tf);
            }
        });

        int max = Math.max(telephoneFragments.size(),currentUser.getPhones().size());

        for(int i=0;i<max;i++){

            if(i>=telephoneFragments.size()){

                ProfileManagementTelephoneFragment tf  = ProfileManagementTelephoneFragment.newInstance(currentUser.getPhones().get(i));
                telephoneFragments.add(tf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_phones_container,telephoneFragments.get(i));
            ft.commit();
        }

        setEnable(host.isEditMode());
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        for (ProfileManagementTelephoneFragment tf: telephoneFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(tf);
            ft.commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementTelephoneFragment tf:telephoneFragments)
            host.removeOnActivityChangedListener(tf);
    }

    public void setEnable(boolean enable){
        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        super.setEnable(enable);
        Button phonePlus = (Button)root.findViewById(R.id.student_phones_plusButton);
        phonePlus.setVisibility(visibility);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        if(!addressText.getText().toString().equals(INSERT_FIELD))  currentUser.setAddress(addressText.getText().toString());
        if(!cityText.getText().toString().equals(INSERT_FIELD))     currentUser.setCity(cityText.getText().toString());
        if(!postalText.getText().toString().equals(INSERT_FIELD))   currentUser.setPostalCode(postalText.getText().toString());
        if(!nationText.getText().toString().equals(INSERT_FIELD))   currentUser.setNation(nationText.getText().toString());
        currentUser.saveInBackground();

    }
}

package com.example.giuliagigi.jobplacement;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class StudentProfileManagementRegistryFragment extends ProfileManagementFragment {

    private Student currentUser;
    private EditText addressText,cityText,postalText,nationText;
    private ArrayList<EditText> phones;
    private Button phonePlus;
    private LinearLayout phonesContainer;

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
        phones = new ArrayList<EditText>();
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

        ArrayList<String> userPhones = currentUser.getPhones();
        for(String p: userPhones){
            EditText newPhone = new EditText(getActivity().getApplicationContext());
            newPhone.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            newPhone.setText(p);
            phonesContainer.addView(newPhone);
            phones.add(newPhone);
        }


        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        textFields.add(addressText);
        textFields.add(cityText);
        textFields.add(postalText);
        textFields.add(nationText);

        for(EditText et: textFields)
            et.addTextChangedListener(hasChangedListener);


        phonesContainer = (LinearLayout)root.findViewById(R.id.student_phones_container);

        phonePlus = (Button)root.findViewById(R.id.student_phones_plusButton);
        phonePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.println(Log.ASSERT,"REGISTRYFRAG", "adding a new phone");
                EditText newPhone = new EditText(getActivity().getApplicationContext());
                newPhone.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                newPhone.setHint("new number");
                newPhone.setTextColor(Color.parseColor("#000000"));

                phonesContainer.addView(newPhone);
                phones.add(newPhone);

            }
        });

        setEnable(hostActivity.isInEditMode());
        return root;
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
        for(EditText et: phones){
            et.setEnabled(enable);
        }
        if(!enable && hasChanged){

            Log.println(Log.ASSERT,"REGISTRY FRAG", "update required");
            currentUser.setAddress(addressText.getText().toString());
            currentUser.setCity(cityText.getText().toString());
            currentUser.setPostalCode(postalText.getText().toString());
            currentUser.setNation(nationText.getText().toString());

            currentUser.saveInBackground();

            for(EditText et: phones){
               currentUser.addPhone(et.getText().toString());
                currentUser.saveInBackground();
            }



            hasChanged = false;
        }
    }

}

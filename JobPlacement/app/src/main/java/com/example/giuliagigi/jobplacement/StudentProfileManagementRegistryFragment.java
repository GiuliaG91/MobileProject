package com.example.giuliagigi.jobplacement;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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
    private ArrayList<EditText> phoneTexts;
    private ArrayList<String> originalPhones;
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
        Log.println(Log.ASSERT, "REGISTRY FRAG", "OnAttach");

        currentUser = application.getStudentFromUser();
        phoneTexts = new ArrayList<EditText>();
        originalPhones = new ArrayList<String>();
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
        Log.println(Log.ASSERT,"REGISTRY FRAG", "OnCreateView");

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
        ArrayList<String> userPhones = currentUser.getPhones();

        for(String p: userPhones){

            newPhoneText(p);
            originalPhones.add(p);
        }


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

                Log.println(Log.ASSERT,"REGISTRYFRAG", "adding a new phone");
                newPhoneText(null);
            }
        });

        setEnable(host.isEditMode());
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.println(Log.ASSERT, "REGISTRY FRAG", "OnDetach");

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

        for(EditText et: phoneTexts){
            et.setEnabled(enable);
        }

        if(!enable && hasChanged){

            Log.println(Log.ASSERT,"REGISTRY FRAG", "update required");
            if(!addressText.getText().toString().equals(INSERT_FIELD))  currentUser.setAddress(addressText.getText().toString());
            if(!cityText.getText().toString().equals(INSERT_FIELD))     currentUser.setCity(cityText.getText().toString());
            if(!postalText.getText().toString().equals(INSERT_FIELD))   currentUser.setPostalCode(postalText.getText().toString());
            if(!nationText.getText().toString().equals(INSERT_FIELD))   currentUser.setNation(nationText.getText().toString());
            updatePhones();

            currentUser.saveInBackground();
            hasChanged = false;
        }
    }



    private void newPhoneText(String phone){

        EditText newPhone = new EditText(getActivity().getApplicationContext());
        newPhone.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        if(phone == null)
            newPhone.setHint("new number");
        else
            newPhone.setText(phone);

        newPhone.setTextColor(Color.parseColor("#000000"));
        newPhone.addTextChangedListener(new OnFieldChangedListener());
        phonesContainer.addView(newPhone);
        phoneTexts.add(newPhone);
    }

    private void updatePhones(){


        for(int i = 0;i<phoneTexts.size();i++){

            String phone = phoneTexts.get(i).getText().toString();

            Log.println(Log.ASSERT,"REG FRAG", "save: " + phoneTexts.get(i).getText().toString());
            if(i<originalPhones.size() && !originalPhones.get(i).equals(phone)){

                Log.println(Log.ASSERT,"REG FRAG", "older: " + originalPhones.get(i) + ", new: " + phone);
                currentUser.removePhone(originalPhones.get(i));
                currentUser.saveInBackground();
            }
            Log.println(Log.ASSERT,"REG FRAG", "ok");
            if(!phone.equals("")){

                Log.println(Log.ASSERT,"REG FRAG", "add/update the phone");
                currentUser.addPhone(phone);
                currentUser.saveInBackground();
            }
            else {

                Log.println(Log.ASSERT,"REG FRAG", "the phone is going to be deleted");
                phonesContainer.removeView(phoneTexts.get(i));
                phoneTexts.remove(i);
            }
        }

        originalPhones = new ArrayList<String>();

        for(EditText et:phoneTexts)
            originalPhones.add(et.getText().toString());
    }

}

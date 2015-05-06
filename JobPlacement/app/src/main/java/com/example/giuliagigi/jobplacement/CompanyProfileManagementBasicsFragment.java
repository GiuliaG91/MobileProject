package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CompanyProfileManagementBasicsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Overview";

    private Company currentUser;
    EditText nameText,VATNumber;
    DatePicker foundationDatePicker;

    public CompanyProfileManagementBasicsFragment() {super();}
    public static CompanyProfileManagementBasicsFragment newInstance() {
        CompanyProfileManagementBasicsFragment fragment = new CompanyProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
    @Override
    public String getTitle() {
        return TITLE;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT, "BASICS FRAG", "onAttach");

        currentUser = (Company)application.getUserObject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_company_profile_management_basics, container, false);

        nameText = (EditText)root.findViewById(R.id.company_basics_name_field);
        if(currentUser.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(currentUser.getName());

        VATNumber = (EditText)root.findViewById(R.id.company_basics_VAT_field);
        if(currentUser.getFiscalCode() == null)
            VATNumber.setText(INSERT_FIELD);
        else
            VATNumber.setText(currentUser.getFiscalCode());

        textFields.add(nameText);
        textFields.add(VATNumber);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        foundationDatePicker = (DatePicker)root.findViewById(R.id.company_basics_foundationDatePicker);
        foundationDatePicker.setOnClickListener(new OnFieldClickedListener());

        EditText emailText = (EditText)root.findViewById(R.id.company_basics_email_area);
        emailText.setText(currentUser.getMail());
        setEnable(host.isEditMode());
        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        host = null;
    }


    @Override
    public void setEnable(boolean enable){
        super.setEnable(enable);

        foundationDatePicker.setEnabled(enable);
    }


    @Override
    public void saveChanges(){
        super.saveChanges();

        Date foundation = null;
        Calendar c = GregorianCalendar.getInstance();
        c.set(foundationDatePicker.getYear(),foundationDatePicker.getMonth(),foundationDatePicker.getDayOfMonth());
        foundation = c.getTime();

        currentUser.setName(nameText.getText().toString());
        currentUser.setFiscalCode(VATNumber.getText().toString());
        currentUser.setFoundation(foundation);
        currentUser.saveEventually();

    }

}

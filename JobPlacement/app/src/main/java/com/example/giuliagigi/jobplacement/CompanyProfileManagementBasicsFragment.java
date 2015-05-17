package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class CompanyProfileManagementBasicsFragment extends ProfileManagementBasicsFragment {

    private static final String TITLE = "Overview";

    private EditText nameText,VATNumber, descriptionText;
    private TextView foundationDatePicker;
//    private LinearLayout profilePhoto;
    private Date date;
    private boolean foundationDateChanged;
    private Company company;


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/


    public CompanyProfileManagementBasicsFragment() {super();}
    public static CompanyProfileManagementBasicsFragment newInstance(Company company) {
        CompanyProfileManagementBasicsFragment fragment = new CompanyProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCompany(company);
        fragment.setUser(company);
        return fragment;

    }

    public void setCompany(Company company){

        this.company = company;
    }

    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT, "BASICS FRAG", "onAttach");
        foundationDateChanged = false;
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

        if(root == null)
            root = inflater.inflate(R.layout.fragment_company_profile_management_basics, container, false);

        nameText = (EditText)root.findViewById(R.id.company_name_area);
        if(company.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(company.getName());

        VATNumber = (EditText)root.findViewById(R.id.company_basics_VAT_field);
        if(company.getFiscalCode() == null)
            VATNumber.setText(INSERT_FIELD);
        else
            VATNumber.setText(company.getFiscalCode());

        foundationDatePicker = (TextView)root.findViewById(R.id.company_birth_et);
        if(company.getFoundation()!= null){
            DateFormat df=SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,Locale.getDefault());
            foundationDatePicker.setText(df.format(company.getFoundation()));
        }
        else
            foundationDatePicker.setText(INSERT_FIELD);

        foundationDatePicker.setOnClickListener(new View.OnClickListener() {
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
                        foundationDatePicker.setText(df.format(c.getTime()));
                        date=c.getTime();
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


        descriptionText = (EditText)root.findViewById(R.id.company_description_et);
        if(company.getDescription() == null)
            descriptionText.setText(INSERT_FIELD);
        else
            descriptionText.setText(company.getDescription());

        textFields.add(nameText);
        textFields.add(VATNumber);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    /* ---------------------------- AUXILIARY METHODS --------------------------------------------*/

    @Override
    public void setEnable(boolean enable){
        super.setEnable(enable);

        foundationDatePicker.setEnabled(enable);
    }


    @Override
    public void saveChanges(){
        super.saveChanges();

        if(foundationDateChanged){
            company.setFoundation(date);
            foundationDateChanged = false;
        }



        company.setName(nameText.getText().toString());
        company.setFiscalCode(VATNumber.getText().toString());

        Log.println(Log.ASSERT,"BASICS", "saving...");
        company.saveEventually();
    }

}

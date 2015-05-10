package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class StudentProfileManagementCertificateFragment extends ProfileManagementFragment {

    private static final String BUNDLE_HASCHANGED = "Bundle_hasChanged";
    private static final String BUNDLE_TITLE = "Bundle_title";
    private static final String BUNDLE_DESCRIPTION = "Bundle_description";
    private static final String BUNDLE_DATE_DAY = "Bundle_date_day";
    private static final String BUNDLE_DATE_MONTH = "Bundle_date_month";
    private static final String BUNDLE_DATE_YEAR = "Bundle_date_year";
    private static final String BUNDLE_MARK = "Bundle_mark";

    private static final int DESCRIPTION_PREVIEW_LENGTH = 10;

    private Certificate certificate;
    private EditText titleText, markText;
    private TextView descriptionText, datePicker;
    private Button deleteButton;
    private int day,month,year;
    private String completeDescription;

    /* ----------------- CONTRUCTORS GETTERS SETTERS ---------------------------------------------*/

    public StudentProfileManagementCertificateFragment() {}
    public static StudentProfileManagementCertificateFragment newInstance(Certificate certificate) {
        StudentProfileManagementCertificateFragment fragment = new StudentProfileManagementCertificateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCertificate(certificate);
        return fragment;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }


    /* ----------------- STANDARD CALLBACKS ------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isListenerAfterDetach = true;
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_certificate, container, false);
        String title = null, description = null, date = null, mark = null;

        if(getArguments().getBoolean(BUNDLE_HASCHANGED)){


        }
        else {

            title = certificate.getTitle();
            mark = certificate.getMark();
            completeDescription = certificate.getDescription();
            if(completeDescription.length()>DESCRIPTION_PREVIEW_LENGTH)
                description = completeDescription.substring(0,DESCRIPTION_PREVIEW_LENGTH) + "...";
            else
                description = completeDescription;

            if(certificate.getDate()!=null){

                day = certificate.getDate().getDay();
                month = certificate.getDate().getMonth();
                year = certificate.getDate().getYear() + 1900;
                date = day + "/" + month + "/" + year;
            }
        }

        titleText = (EditText)root.findViewById(R.id.certificate_title_text);
        markText = (EditText)root.findViewById(R.id.certificate_mark_text);
        descriptionText = (TextView)root.findViewById(R.id.certificate_description_text);
        datePicker = (TextView)root.findViewById(R.id.certificate_datePicker);
        deleteButton = (Button)root.findViewById(R.id.certificate_deleteButton);

        if(title!= null)
            titleText.setText(title);
        else
            titleText.setText(INSERT_FIELD);

        if(mark!= null)
            markText.setText(mark);
        else
            markText.setText(INSERT_FIELD);

        if(description!= null)
            descriptionText.setText(description);
        else
            descriptionText.setText(INSERT_FIELD);

        if(date!= null)
            datePicker.setText(date);
        else
            datePicker.setText(INSERT_FIELD);

        textFields.add(titleText);
        textFields.add(markText);

        descriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Insert a description");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        hasChanged = true;
                        //TODO
                    }
                });
                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.create().show();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Set a date");

                DatePicker dateSelect = new DatePicker(getActivity());
                dateSelect.setCalendarViewShown(false);
                builder.setView(dateSelect);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        hasChanged = true;
                        //TODO
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.create().show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){

            getArguments().putInt(BUNDLE_DATE_DAY,day);
            getArguments().putInt(BUNDLE_DATE_MONTH,month);
            getArguments().putInt(BUNDLE_DATE_YEAR,year);
            getArguments().putString(BUNDLE_DESCRIPTION,completeDescription);
            getArguments().putString(BUNDLE_TITLE,titleText.getText().toString());
            getArguments().putString(BUNDLE_MARK,markText.getText().toString());
        }
    }


    /* ---------------------------- AUXILIARY METHODS --------------------------------------------*/

    @Override
    protected void setEnable(boolean enable) {

        super.setEnable(enable);

        datePicker.setEnabled(enable);
        descriptionText.setEnabled(enable);
        deleteButton.setEnabled(enable);

        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        deleteButton.setVisibility(visibility);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        //TODO
    }
}

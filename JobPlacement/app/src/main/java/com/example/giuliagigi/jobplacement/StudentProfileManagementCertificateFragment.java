package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StudentProfileManagementCertificateFragment extends ProfileManagementFragment {

    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILECERTIFICATE";
    private static final String BUNDLE_STUDENT = "Bundle_student";
    private static final String BUNDLE_CERTIFICATE = "Bundle_certificate";
    private static final String BUNDLE_TITLE = "Bundle_title";
    private static final String BUNDLE_DESCRIPTION = "Bundle_description";
    private static final String BUNDLE_DATE_DAY = "Bundle_date_day";
    private static final String BUNDLE_DATE_MONTH = "Bundle_date_month";
    private static final String BUNDLE_DATE_YEAR = "Bundle_date_year";
    private static final String BUNDLE_MARK = "Bundle_mark";
    private static final String BUNDLE_KEY_PARENT = "bundle_parent";


    private static final int DESCRIPTION_PREVIEW_LENGTH = 10;

    private Certificate certificate;
    private EditText titleText, markText;
    private TextView descriptionText, datePicker;
    private Button deleteButton;

    private int day,month,year;
    private String completeDescription;
    private boolean isRemoved, dateChanged;

    String title = null, description = null, date = null, mark = null, parentName = null;

    private Student student;
    private CertificateFragmentInterface parent;

    /* -------------------------------------------------------------------------------------------*/
    /* ----------------- CONTRUCTORS GETTERS SETTERS ---------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public StudentProfileManagementCertificateFragment() {}
    public static StudentProfileManagementCertificateFragment newInstance(CertificateFragmentInterface parent, Certificate certificate,Student student) {

        StudentProfileManagementCertificateFragment fragment = new StudentProfileManagementCertificateFragment();
        fragment.setCertificate(certificate);
        fragment.setStudent(student);
        fragment.setUser(student);
        fragment.parent = parent;

        ProfileManagementFragment f = (ProfileManagementFragment)parent;
        fragment.parentName = f.bundleIdentifier();

        return fragment;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }
    /* -------------------------------------------------------------------------------------------*/
    /* ----------------- STANDARD CALLBACKS ------------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        isNestedFragment = true;
        isRemoved = false;

        if(certificate != null){

            title = certificate.getTitle();
            mark = certificate.getMark();

            completeDescription = certificate.getDescription();
            if(completeDescription != null && completeDescription.length()>DESCRIPTION_PREVIEW_LENGTH)
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_student_profile_management_certificate, container, false);

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
            View cardLayout = getActivity().getLayoutInflater().inflate(R.layout.card_description_element, null);
            builder.setView(cardLayout);
            final EditText dialogDescriptionText = (EditText)cardLayout.findViewById(R.id.description_text);
            dialogDescriptionText.setText(completeDescription);
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(!dialogDescriptionText.getText().toString().equals(completeDescription)){

                        hasChanged = true;
                        dateChanged = true;
                        completeDescription = dialogDescriptionText.getText().toString();
                        String description;
                        if(completeDescription != null && completeDescription.length()>DESCRIPTION_PREVIEW_LENGTH)
                            description = completeDescription.substring(0,DESCRIPTION_PREVIEW_LENGTH) + "...";
                        else
                            description = completeDescription;

                        descriptionText.setText(description);
                    }

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

                final DatePicker dateSelect = new DatePicker(getActivity());
                dateSelect.setCalendarViewShown(false);
                builder.setView(dateSelect);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        hasChanged = true;
                        day = dateSelect.getDayOfMonth();
                        month = dateSelect.getMonth();
                        year = dateSelect.getYear();
                        datePicker.setText(day + "/" + month + "/" + year);
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

                isRemoved = true;

                if(certificate.getObjectId() != null){

                    certificate.deleteEventually(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                student.removeCertificate(certificate);
                                student.saveEventually();
                                root.setVisibility(View.INVISIBLE);
                                parent.onCertificateDelete(StudentProfileManagementCertificateFragment.this);
                            }
                            else {

                                Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.profile_object_delete_failure), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    root.setVisibility(View.INVISIBLE);
                    parent.onCertificateDelete(StudentProfileManagementCertificateFragment.this);
                }
            }
        });

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------------- AUXILIARY METHODS --------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    protected void saveStateInBundle(Bundle outstate) {
        super.saveStateInBundle(outstate);

        bundle.put(BUNDLE_STUDENT, student);
        bundle.put(BUNDLE_CERTIFICATE, certificate);
        bundle.putString(BUNDLE_DESCRIPTION, completeDescription);
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putString(BUNDLE_MARK, mark);
        bundle.putInt(BUNDLE_DATE_DAY, day);
        bundle.putInt(BUNDLE_DATE_MONTH, month);
        bundle.putInt(BUNDLE_DATE_YEAR, year);
        bundle.putString(BUNDLE_KEY_PARENT,parentName);
    }


    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);

        if(bundle != null){

            student = (Student)bundle.get(BUNDLE_STUDENT);
            certificate = (Certificate)bundle.get(BUNDLE_CERTIFICATE);

            completeDescription = bundle.getString(BUNDLE_DESCRIPTION);
            if(completeDescription != null && completeDescription.length()>DESCRIPTION_PREVIEW_LENGTH)
                description = completeDescription.substring(0,DESCRIPTION_PREVIEW_LENGTH) + "...";
            else
                description = completeDescription;

            title = bundle.getString(BUNDLE_TITLE);
            mark = bundle.getString(BUNDLE_MARK);
            day = bundle.getInt(BUNDLE_DATE_DAY);
            month = bundle.getInt(BUNDLE_DATE_MONTH);
            year = bundle.getInt(BUNDLE_DATE_YEAR);

            parentName = bundle.getString(BUNDLE_KEY_PARENT);
            parent = (CertificateFragmentInterface)application.getFragment(parentName);
        }
    }

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
        datePicker.setEnabled(enable);
        descriptionText.setEnabled(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        if(!isRemoved){

            if(dateChanged){

                Date date = null;
                Calendar c = GregorianCalendar.getInstance();
                c.set(day,month,year);
                date = c.getTime();
                Log.println(Log.ASSERT, "CERTIFICATE FRAG", "DATE_FIELD:" + date.toString());
                certificate.setDate(date);
            }

            if(!titleText.getText().toString().equals(INSERT_FIELD)) certificate.setTitle(titleText.getText().toString());
            if(!markText.getText().toString().equals(INSERT_FIELD)) certificate.setMark(markText.getText().toString());
            if(completeDescription != null) certificate.setDescription(completeDescription);
            certificate.setStudent(student);

            certificate.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e==null){
                        student.addCertificate(certificate);
                        student.saveEventually();
                    }
                }
            });
        }
    }

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------------- PARENT INTERFACE ---------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public interface CertificateFragmentInterface{

        public void onCertificateDelete(StudentProfileManagementCertificateFragment toRemove);
    }
}


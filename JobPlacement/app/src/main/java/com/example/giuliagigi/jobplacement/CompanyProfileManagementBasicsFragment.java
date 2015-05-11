package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CompanyProfileManagementBasicsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Overview";

    private Company currentUser;
    private EditText nameText,VATNumber;
    private TextView foundationDatePicker;
    private LinearLayout profilePhoto;
    private int day,month,year;
    private boolean foundationDateChanged;


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/


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



    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT, "BASICS FRAG", "onAttach");
        foundationDateChanged = false;
        currentUser = (Company)application.getUserObject();
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

        root = inflater.inflate(R.layout.fragment_company_profile_management_basics, container, false);

        nameText = (EditText)root.findViewById(R.id.company_name_area);
        if(currentUser.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(currentUser.getName());

        VATNumber = (EditText)root.findViewById(R.id.company_basics_VAT_field);
        if(currentUser.getFiscalCode() == null)
            VATNumber.setText(INSERT_FIELD);
        else
            VATNumber.setText(currentUser.getFiscalCode());

        foundationDatePicker = (TextView)root.findViewById(R.id.company_birth_et);
        if(currentUser.getFoundation()!= null){

            day = currentUser.getFoundation().getDay();
            month = currentUser.getFoundation().getMonth();
            year = currentUser.getFoundation().getYear() + 1900;
            foundationDatePicker.setText(day + "/" + month + "/" + year);
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
                        foundationDateChanged = true;
                        day = picker.getDayOfMonth();
                        month = picker.getMonth();
                        year = picker.getYear();
                        foundationDatePicker.setText(day + "/" + month + "/" + year);
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



        textFields.add(nameText);
        textFields.add(VATNumber);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);


        profilePhoto = (LinearLayout)root.findViewById(R.id.basics_profilePhoto);

        if(currentUser.getProfilePhoto() != null) {
            Bitmap bmImg = currentUser.getProfilePhoto();
            BitmapDrawable background = new BitmapDrawable(bmImg);
            profilePhoto.setBackgroundDrawable(background);
        }

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent,REQUEST_IMAGE_GET);
                }
            }
        });


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(Log.ASSERT,"BASICS FRAG", "onActivity result");

        if(requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){

            Uri photoUri = data.getData();
            Bitmap photoBitmap = null;

            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photoUri);

                if(photoBitmap == null)
                    Log.println(Log.ASSERT,"PM FRAG", "photoBitmap null");
                else{

                    hasChanged = true;
                    application.getUserObject().setProfilePhoto(photoBitmap);
                    Bitmap bmImg = currentUser.getProfilePhoto();
                    BitmapDrawable background = new BitmapDrawable(bmImg);
                    profilePhoto.setBackgroundDrawable(background);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

            Date foundation = null;
            Calendar c = GregorianCalendar.getInstance();
            c.set(day,month,year);
            foundation = c.getTime();

            currentUser.setFoundation(foundation);
            foundationDateChanged = false;
        }



        currentUser.setName(nameText.getText().toString());
        currentUser.setFiscalCode(VATNumber.getText().toString());

        Log.println(Log.ASSERT,"BASICS", "saving user...");
        currentUser.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    Log.println(Log.ASSERT,"BASICS", "user saved");
            }
        });
    }

}

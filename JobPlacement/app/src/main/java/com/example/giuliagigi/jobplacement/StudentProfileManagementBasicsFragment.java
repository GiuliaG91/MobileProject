package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StudentProfileManagementBasicsFragment extends ProfileManagementFragment {

    public static final String TITLE = "Overview";
    private static final int REQUEST_IMAGE_GET = 1;

    private Student currentUser;
    private int day,month,year;
    private boolean birthDateChanged;
    EditText nameText,surnameText, birthCityText, emailText;
    TextView birthPicker;
    LinearLayout profilePhoto;
    CheckBox male,female;

    public StudentProfileManagementBasicsFragment() {super();}
    public static StudentProfileManagementBasicsFragment newInstance() {
        StudentProfileManagementBasicsFragment fragment = new StudentProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public String getTitle() {
        return TITLE;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT, "BASICS FRAG", "onAttach");
        currentUser = (Student)application.getUserObject();
        birthDateChanged = false;
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_basics, container, false);

        nameText = (EditText)root.findViewById(R.id.student_name_area);
        if(currentUser.getName() == null)
            nameText.setText(INSERT_FIELD);
        else
            nameText.setText(currentUser.getName());

        surnameText = (EditText)root.findViewById(R.id.student_surname_area);
        if(currentUser.getSurname() == null)
            surnameText.setText(INSERT_FIELD);
        else
            surnameText.setText(currentUser.getSurname());

        emailText = (EditText)root.findViewById(R.id.student_email_et);
        emailText.setText(currentUser.getMail());

        birthCityText = (EditText)root.findViewById(R.id.student_birthCity_et);
        if(currentUser.getBirthCity() == null)
            birthCityText.setText(INSERT_FIELD);
        else
            birthCityText.setText(currentUser.getBirthCity());


        birthPicker = (TextView)root.findViewById(R.id.student_birth_et);
        if(currentUser.getBirth() == null){

            birthPicker.setText(INSERT_FIELD);
        }

        else{

            day = currentUser.getBirth().getDay();
            month = currentUser.getBirth().getMonth();
            year = currentUser.getBirth().getYear() + 1900;
            birthPicker.setText(day + "/" + month + "/" + year);
        }


        birthPicker.setOnClickListener(new View.OnClickListener() {
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
                        birthDateChanged = true;
                        day = picker.getDayOfMonth();
                        month = picker.getMonth();
                        year = picker.getYear();
                        birthPicker.setText(day + "/" + month + "/" + year);
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
        textFields.add(surnameText);
        textFields.add(birthCityText);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        male = (CheckBox)root.findViewById(R.id.male_checkBox);
        female = (CheckBox)root.findViewById(R.id.female_checkBox);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged)
                    hasChanged = true;
                if(male.isChecked())
                    female.setChecked(false);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged)
                    hasChanged = true;
                if(female.isChecked())
                    male.setChecked(false);
            }
        });



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

        setEnable(host.isEditMode());
        return root;
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

    /* ----------------------- AUXILIARY METHODS ----------------------------------------------- */

    @Override
    public void setEnable(boolean enable){

        super.setEnable(enable);

        boolean isMale = currentUser.getSex().equals(Student.SEX_MALE);
        male.setChecked(isMale);
        female.setChecked(!isMale);
        male.setEnabled(enable);
        female.setEnabled(enable);
        birthPicker.setEnabled(enable);
        profilePhoto.setEnabled(enable);
    }


    @Override
    public void saveChanges(){

        super.saveChanges();
        String sex;
        if(male.isChecked())
            sex = Student.SEX_MALE;
        else
            sex = Student.SEX_FEMALE;

        if(!nameText.getText().toString().equals(INSERT_FIELD)) currentUser.setName(nameText.getText().toString());
        if(!surnameText.getText().toString().equals(INSERT_FIELD)) currentUser.setSurname(surnameText.getText().toString());
        if(!birthCityText.getText().toString().equals(INSERT_FIELD)) currentUser.setBirthCity(birthCityText.getText().toString());
        currentUser.setSex(sex);

        if(birthDateChanged){

            Date date = null;
            Calendar c = GregorianCalendar.getInstance();
            c.set(day,month,year);
            date = c.getTime();

            Log.println(Log.ASSERT,"Basics", "data: " + date.toString());
            currentUser.setBirth(date);
            birthDateChanged = false;
        }

        Log.println(Log.ASSERT,"BASICS", "now saving: " + currentUser.getObjectId());
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                Log.println(Log.ASSERT,"BASICS FRAG", "save: " + e);
            }
        });
    }


}

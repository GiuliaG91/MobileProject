package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;

public class ProfileManagementBasicsFragment extends ProfileManagementFragment {

    private static final String TITLE = GlobalData.getContext().getString(R.string.string_basics_tab);
    protected ImageView profilePhoto;
    protected TextView emailVerified;
    public static final String BUNDLE_IDENTIFIER = "PROFILEMANAGEMENTBASICS";
    private static final String BUNDLE_KEY_USER = "bundle_key_user";


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/

    public ProfileManagementBasicsFragment() {}
    public static ProfileManagementBasicsFragment newInstance(User user) {
        ProfileManagementBasicsFragment fragment = new ProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUser(user);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setUser(User user){

        this.user = user;
    }

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_management_basics, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        setBasicsView();
        setEnable(host.isEditMode());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT,"BASICS", "onActivityResult");

        if(requestCode!=REQUEST_IMAGE_GET) Log.println(Log.ASSERT,"BASICS","requestcode not matching");
        if(resultCode!=Activity.RESULT_OK) Log.println(Log.ASSERT,"BASICS","resultcode not matching");

        if(requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){

            Log.println(Log.ASSERT,"BASICS", "getting data");
            Uri photoUri = data.getData();
            Bitmap photoBitmap = null;

            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photoUri);

                if(photoBitmap == null)
                    Log.println(Log.ASSERT,"BASICS", "photoBitmap null");
                else{

                    Log.println(Log.ASSERT,"BASICS", "setting photo");
                    hasChanged = true;
                    user.setProfilePhoto(photoBitmap,(GlobalData)getActivity().getApplication());
                    BitmapDrawable background = new BitmapDrawable(GlobalData.getContext().getResources(), photoBitmap);
                    profilePhoto.setImageDrawable(background);
                    profilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* ---------------------------- AUXILIARY METHODS --------------------------------------------*/

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);

        profilePhoto.setEnabled(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }


    protected void setBasicsView(){

        profilePhoto = (ImageView)root.findViewById(R.id.basics_profilePhoto);

        if(user.getProfilePhoto() != null) {
            Bitmap bmImg = user.getProfilePhoto();
            BitmapDrawable background = new BitmapDrawable(bmImg);
            profilePhoto.setBackgroundDrawable(background);
        }

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    getActivity().startActivityForResult(intent,REQUEST_IMAGE_GET);
                }
            }
        });

        EditText emailText = (EditText)root.findViewById(R.id.basics_email_area);
        emailText.setText(user.getMail());

        ParseUserWrapper u = user.getParseUser();
        if(u != null){

            if(u.isEmailVerified()){

                emailVerified.setText("Verified user");
                // TODO icona
            }
        }

    }
}

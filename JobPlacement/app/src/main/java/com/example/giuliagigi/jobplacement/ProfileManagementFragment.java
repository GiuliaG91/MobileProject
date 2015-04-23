package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class ProfileManagementFragment extends Fragment {

    protected static final String INSERT_FIELD = "Insert";

    protected OnInteractionListener hostActivity;
    protected ProfileManagement profileActivity;
    protected ArrayList<EditText> textFields;
    protected GlobalData application;
    protected boolean hasChanged;
    protected View root;

    public ProfileManagementFragment() {}
    public static ProfileManagementFragment newInstance(String param1, String param2) {
        ProfileManagementFragment fragment = new ProfileManagementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        hasChanged = false;
        application = (GlobalData)activity.getApplicationContext();
        textFields = new ArrayList<EditText>();

        try {
            profileActivity = (ProfileManagement)activity;
            hostActivity = (OnInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_management, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




    public interface OnInteractionListener {}
    protected void setEnable(boolean enable){
        setTextFieldsEnable(enable);
    }
    protected void setTextFieldsEnable(boolean enable){

        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        for(EditText et: textFields){
            if(et.getText().toString().equals(INSERT_FIELD))
                et.setVisibility(visibility);
            et.setEnabled(enable);
        }
    }


    protected class OnFieldChangedListener implements TextWatcher{

        public OnFieldChangedListener(){
            super();
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(!hasChanged)
                hasChanged = true;
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    protected class OnFieldClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(!hasChanged)
                hasChanged = true;
        }
    }

}

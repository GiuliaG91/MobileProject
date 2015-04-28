package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class ProfileManagementFragment extends Fragment {

    protected static final String INSERT_FIELD = "Insert";
    protected static final String BUNDLE_KEY_CHANGED = "ProfileManagementFragment_hasChanged";


    protected OnInteractionListener hostActivity;
    protected ArrayList<EditText> textFields=new ArrayList<>();
    protected GlobalData application;
    protected boolean hasChanged = false;
    protected View root;

    public ProfileManagementFragment() {}
    public static ProfileManagementFragment newInstance() {
        ProfileManagementFragment fragment = new ProfileManagementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application=(GlobalData)getActivity().getApplication();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_management, container, false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_KEY_CHANGED,hasChanged);
    }



    public interface OnInteractionListener {
        public boolean isInEditMode();
    }

    protected void setEnable(boolean enable){

        setTextFieldsEnable(enable);
    }
    protected void restorePreaviousState() {}

    public void saveChanges(){}

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

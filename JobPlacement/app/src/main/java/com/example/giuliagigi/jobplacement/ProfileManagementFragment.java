package com.example.giuliagigi.jobplacement;

import android.app.Activity;
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

public class ProfileManagementFragment extends Fragment implements OnActivityChangedListener{

    protected static final String INSERT_FIELD = "Insert";


    protected OnInteractionListener host;
    protected ArrayList<EditText> textFields;
    protected GlobalData application;
    protected boolean hasChanged = false;
    protected View root;


    /* --------------------- CONSTRUCTORS -------------------------------------------------------- */

    public ProfileManagementFragment() {}
    public static ProfileManagementFragment newInstance() {
        ProfileManagementFragment fragment = new ProfileManagementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /* --------------------- STANDARD CALLBACKS ------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT, "PM FRAG", "on attach start");

        hasChanged = false;
        textFields = new ArrayList<EditText>();
        application = (GlobalData)activity.getApplicationContext();

        try {
            host = (OnInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }

        host.addOnActivityChangedListener(this);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        host.removeOnActivityChangedListener(this);
    }



        public boolean isInEditMode();
        public void addOnActivityChangedListener(OnActivityChangedListener listener);
        public void removeOnActivityChangedListener(OnActivityChangedListener listener);
    }


    /* --------------------- ACTIVITY LISTENER METHODS ------------------------------------------ */

    @Override
    public void onActivityStateChanged(State newState, State pastState) {

        if(newState.equals(State.EDIT_MODE_STATE))
            this.setEnable(true);
        else if(newState.equals(State.DISPLAY_MODE_STATE))
            this.setEnable(false);
    }

    /* --------------------- AUXILIARY METHODS ------------------------------------------------- */

    protected void setEnable(boolean enable){

        setTextFieldsEnable(enable);
    }

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


    /* --------------------- AUXILIARY CLASSES -------------------------------------------------- */

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

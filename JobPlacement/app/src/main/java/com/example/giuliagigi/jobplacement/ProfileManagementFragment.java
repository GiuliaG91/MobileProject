package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileManagementFragment extends Fragment implements OnActivityChangedListener{

    protected static final String INSERT_FIELD = GlobalData.getContext().getString(R.string.insert);
    protected static final String BUNDLE_IDENTIFIER = "PROFILEMANAGEMENTFRAG";
    protected static final String BUNDLE_KEY_USER = "BUNDLE_KEY_USER";
    protected static final String BUNDLE_KEY_HASCHANGED = "BUNDLE_KEY_HASCHANGED";
    protected static final String BUNDLE_KEY_TAIL = "bundle_tail";
    protected static final String TITLE = "None";
    public static final int REQUEST_IMAGE_GET = 1;
    public static final int REQUEST_CONTENT_GET = 2;

    protected ProfileManagement profileManagement;
    protected OnInteractionListener listener;
    protected OnFragmentInteractionListener host;
    protected ArrayList<EditText> textFields;
    protected GlobalData application;
    protected boolean hasChanged = false;
    protected View root;
    protected boolean isNestedFragment;
    protected String title;
    protected MyBundle bundle;
    protected User user;


    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- CONSTRUCTORS ------------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    public ProfileManagementFragment() {}
    public static ProfileManagementFragment newInstance(User user) {
        ProfileManagementFragment fragment = new ProfileManagementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUser(user);
        return fragment;
    }

    public String getTitle(){
        return TITLE;
    }

    public void setUser(User user){

        this.user = user;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }

    public void setProfileManagement(ProfileManagement profileManagement) {
        this.profileManagement = profileManagement;
    }


    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- STANDARD CALLBACKS ------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        textFields = new ArrayList<EditText>();
        application = (GlobalData)activity.getApplicationContext();

        try {
            listener = (OnInteractionListener)activity;
            host = (OnFragmentInteractionListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener/OnFragmentInteractionListener");
        }

        isNestedFragment = false;
        hasChanged = false;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.addOnActivityChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            restoreStateFromBundle(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_management, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEnable(listener.isEditMode());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        saveStateInBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(!isNestedFragment)
            host.removeOnActivityChangedListener(this);

    }


    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- ACTIVITY INTERFACE ------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    protected interface OnInteractionListener{

        public boolean isEditMode();
        public void startDeleteAccountActivity();
    }

    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- ACTIVITY LISTENER METHODS ------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onActivityStateChanged(State newState, State pastState) {

        if(newState.equals(State.EDIT_MODE_STATE))
            this.setEnable(true);

        else if(newState.equals(State.DISPLAY_MODE_STATE)){

            if(hasChanged) saveChanges();
            this.setEnable(false);
        }
    }

    @Override
    public void onDataSetChange() {

        // NOTHING TO DO
    }


    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- AUXILIARY METHODS ------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    protected void restoreStateFromBundle(Bundle savedInstanceState){

        Log.println(Log.ASSERT, "PROFILEMANAG", "restore from bundle");
        String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
        Log.println(Log.ASSERT, "PROFILEMANAG", "key = " + bundleIdentifier() + tail);
        bundle = application.getBundle(bundleIdentifier() + tail);
        if(bundle != null){

            Log.println(Log.ASSERT, "PROFILEMANAG", "found a bundle");
            user = (User)bundle.get(BUNDLE_KEY_USER);
            hasChanged = bundle.getBoolean(BUNDLE_KEY_HASCHANGED);
        }
    }

    protected void saveStateInBundle(Bundle outstate){

        Log.println(Log.ASSERT, "PROFILEMANAG", "saving to bundle");

        String tail = user.toString();
        outstate.putString(BUNDLE_KEY_TAIL, tail);

        Log.println(Log.ASSERT, "PROFILEMANAG", "key = " + bundleIdentifier() + tail);

        bundle = application.addBundle(bundleIdentifier() + tail);
        bundle.put(BUNDLE_KEY_USER,user);
        bundle.putBoolean(BUNDLE_KEY_HASCHANGED,hasChanged);
    }


    protected void setEnable(boolean enable){

        setTextFieldsEnable(enable);
    }

    public void saveChanges(){

        hasChanged = false;
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


    /* ------------------------------------------------------------------------------------------ */
    /* --------------------- AUXILIARY CLASSES -------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

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


    protected class StringAdapter extends BaseAdapter {

        public String[] stringArray;

        public StringAdapter(String[] stringArray){
            super();
            this.stringArray = stringArray;
        }

        @Override
        public int getCount() {
            return stringArray.length;
        }

        @Override
        public String getItem(int position) {
            return stringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = new TextView(getActivity().getApplicationContext());
            TextView tv = (TextView)convertView;
            tv.setText(stringArray[position]);
            tv.setTextColor(GlobalData.getContext().getResources().getColor(R.color.black_light_transparent));

            return convertView;
        }
    }



}

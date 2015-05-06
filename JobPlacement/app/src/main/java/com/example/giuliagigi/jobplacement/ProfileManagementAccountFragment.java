package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ProfileManagementAccountFragment extends ProfileManagementFragment {

    private User currentUser;

    private Button deleteAccount, sendVerification, changeUsername, changePassword;


    public ProfileManagementAccountFragment() {}
    public static ProfileManagementAccountFragment newInstance() {
        ProfileManagementAccountFragment fragment = new ProfileManagementAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /* -------------- STANDARD CALLBACKS ------------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentUser = application.getUserObject();
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

        root = inflater.inflate(R.layout.fragment_profile_management_account, container, false);

        deleteAccount = (Button)root.findViewById(R.id.account_deleteAccount_button);
        changePassword = (Button)root.findViewById(R.id.account_changePassword_button);
        changeUsername = (Button)root.findViewById(R.id.account_changeUsername_button);
        sendVerification = (Button)root.findViewById(R.id.account_sendVerification_button);

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* -------------- AUXILIARY METHODS -------------------------------------------------------- */

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }


}

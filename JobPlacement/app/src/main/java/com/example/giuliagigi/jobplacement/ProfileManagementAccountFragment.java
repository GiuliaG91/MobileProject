package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ProfileManagementAccountFragment extends ProfileManagementFragment {

    private static String TITLE = "Account";

    private User currentUser;
    private Button deleteAccount, sendVerification, changeUsername, changePassword;


    public ProfileManagementAccountFragment() {}
    public static ProfileManagementAccountFragment newInstance() {
        ProfileManagementAccountFragment fragment = new ProfileManagementAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
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

        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManagementDialogChangeEmail mailDialog = new ProfileManagementDialogChangeEmail();
                mailDialog.show(getFragmentManager(),"mailDialog");
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManagementDialogChangePassword passwordDialog = new ProfileManagementDialogChangePassword();
                passwordDialog.show(getFragmentManager(),"passwordDialog");
            }
        });

        sendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(application.getCurrentUser().isEmailVerified())
                    Toast.makeText(getActivity(),"No need for verification: your account mail was already verified",Toast.LENGTH_SHORT).show();
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Send verification request?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //TODO: send a new mail for verification
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });

                    builder.create().show();
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Delete this account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO: perform delete (maybe through a new activity for further safety)
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.create().show();
            }
        });

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

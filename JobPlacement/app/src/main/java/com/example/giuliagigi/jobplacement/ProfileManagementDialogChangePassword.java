package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ProfileManagementDialogChangePassword extends DialogFragment {

    private EditText newPassword,confirmPassword;

    public ProfileManagementDialogChangePassword() {}
    public static ProfileManagementDialogChangePassword newInstance() {
        ProfileManagementDialogChangePassword fragment = new ProfileManagementDialogChangePassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO: perform password change
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setTitle("Change Password");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10,10,10,10);
        newPassword = new EditText(getActivity());
        newPassword.setHint("Insert new password");
        confirmPassword = new EditText(getActivity());
        confirmPassword.setHint("Confirm new password");
        layout.addView(newPassword);
        layout.addView(confirmPassword);
        builder.setView(layout);

        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}

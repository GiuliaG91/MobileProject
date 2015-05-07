package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileManagementDialogChangeEmail extends DialogFragment {

    private EditText newMail;
    GlobalData application;

    public ProfileManagementDialogChangeEmail() {}
    public static ProfileManagementDialogChangeEmail newInstance() {
        ProfileManagementDialogChangeEmail fragment = new ProfileManagementDialogChangeEmail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        application = (GlobalData)getActivity().getApplicationContext();
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

        builder.setTitle("Change Email / Username");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        newMail = new EditText(getActivity());
        newMail.setHint("Instert new mail");
        newMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(newMail);
        builder.setView(layout);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                application.getUserObject().setMail(newMail.getText().toString());
                application.getCurrentUser().setEmail(newMail.getText().toString());
                application.getCurrentUser().setUsername(newMail.getText().toString());

                application.getCurrentUser().saveEventually();
                application.getUserObject().saveEventually();

                Toast.makeText(getActivity(),"You will receive a message on the new eMail for verification", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}

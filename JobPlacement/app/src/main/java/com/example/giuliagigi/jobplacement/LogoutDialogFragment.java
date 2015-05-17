package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by MarcoEsposito90 on 08/05/2015.
 */
public class LogoutDialogFragment extends DialogFragment {

    GlobalData application;

    public LogoutDialogFragment() {}
    public static LogoutDialogFragment newInstance() {
        LogoutDialogFragment fragment = new LogoutDialogFragment();
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

        builder.setTitle("Do you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "Logging out...", Toast.LENGTH_SHORT).show();
                ParseUser.logOutInBackground();
                completeLogoutProcedure();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        return builder.create();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* ---------------------------- AUXILIARY METHODS --------------------------------------------*/

    private void completeLogoutProcedure(){

        Toast.makeText(getActivity(), "Logout completed", Toast.LENGTH_SHORT).show();
        application.getCurrentUser(); //updates currentUser in GlobalData

        SharedPreferences sp = application.getLoginPreferences();

        if(sp != null){

            /* next time login won't be automatic */
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Login.SHAREDPREF_LATEST_LOGIN_PREFERENCE,false);
            editor.putString(Login.SHAREDPREF_LATEST_MAIL,"");
            editor.putString(Login.SHAREDPREF_LATEST_PASSWORD,"");
            editor.commit();
        }

        Intent i = new Intent(getActivity(),Login.class);
        startActivity(i);
    }
}

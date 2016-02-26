package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ApplicationReplyDialogFragment extends DialogFragment {

    GlobalData globalData;

    private StudentApplication studentApplication;
    private RadioGroup replyOptionsGroup;
    private RadioButton considering, accept, refuse;
    private TextView studentTV;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static ApplicationReplyDialogFragment newInstance(StudentApplication studentApplication) {

        ApplicationReplyDialogFragment fragment = new ApplicationReplyDialogFragment();
        fragment.studentApplication = studentApplication;
        return fragment;
    }

    public ApplicationReplyDialogFragment() {}



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        globalData = (GlobalData)activity.getApplicationContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- DIALOG CALLBACKS -----------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(GlobalData.getContext().getString(R.string.application_dialog_message));


        /* ------------ set up the dialog window ------------------------------------------------ */
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_application_reply_dialog, null, false);

        replyOptionsGroup = (RadioGroup)v.findViewById(R.id.radio_group_application_reply);
        considering = (RadioButton)v.findViewById(R.id.radio_button_application_considering);
        accept = (RadioButton)v.findViewById(R.id.radio_button_application_accept);
        refuse = (RadioButton)v.findViewById(R.id.radio_button_application_refuse);
        studentTV = (TextView)v.findViewById(R.id.application_dialog_student);


        if(studentApplication.getStatus().equals(StudentApplication.TYPE_CONSIDERING)){

            considering.setChecked(true);
            considering.setEnabled(false);
        }

        String name = "";
        Student s = null;

        try {

            s = studentApplication.getStudent().fetchIfNeeded();
            name = s.getName() + " " + s.getSurname();
        }
        catch (ParseException e) {

            e.printStackTrace();
        }

        studentTV.setText(name);
        builder.setView(v);


        /* ---------- confirm operation ----------------------------------------------------------*/
        builder.setPositiveButton(GlobalData.getContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 1) for further safety, a inner dialog window is opened --------------------------
                AlertDialog.Builder innerBuilder = new AlertDialog.Builder(getActivity());
                innerBuilder.setTitle(R.string.application_dialog_confirm_request);

                TextView warningMessage = new TextView(getActivity());
                warningMessage.setText(R.string.application_dialog_confirm_message);
                innerBuilder.setView(warningMessage);


                // 2) in case the operation is confirmed -------------------------------------------
                innerBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /* -------- first, we modify the application object ----------*/
                        if (replyOptionsGroup.getCheckedRadioButtonId() == considering.getId()) {

                            studentApplication.setStatus(StudentApplication.TYPE_CONSIDERING);
                        } else if (replyOptionsGroup.getCheckedRadioButtonId() == accept.getId()) {

                            studentApplication.setStatus(StudentApplication.TYPE_ACCEPTED);
                        } else if (replyOptionsGroup.getCheckedRadioButtonId() == refuse.getId()) {

                            studentApplication.setStatus(StudentApplication.TYPE_REFUSED);
                        } else {

                            Toast.makeText(globalData, R.string.operation_canceled, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        /* -------- if necessary, modify the related offer ----------*/
                        studentApplication.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null && replyOptionsGroup.getCheckedRadioButtonId() == accept.getId()) {

                                    int positions = studentApplication.getOffer().getnPositions() - 1;
                                    studentApplication.getOffer().setPositions(positions);
                                    studentApplication.getOffer().saveInBackground();
                                }
                            }
                        });


                        /* -------- create a News object -----------------------------*/
                        News news = new News();
                        news.createNews(News.TYPE_APPLICATION_STATE, studentApplication.getOffer(), studentApplication.getStudent(), studentApplication, globalData);


                        /* -------- send a push notification to the student ----------*/
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("User", studentApplication.getStudent().getParseUser());

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);

                        push.setMessage("" + globalData.getString(R.string.Message_Update) + " " + studentApplication.getOffer().getOfferObject() +
                                " " + globalData.getString(R.string.Message_UpdateEnd) + " " + globalData.getUserObject().getMail());
                        push.sendInBackground();

                        Toast.makeText(globalData,R.string.Done,Toast.LENGTH_SHORT).show();
                    }
                });


                // 3) in case the operation is not confirmed ---------------------------------------
                innerBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(globalData,R.string.operation_canceled,Toast.LENGTH_SHORT).show();
                    }
                });

                // 4) display the inner dialog window ----------------------------------------------
                innerBuilder.create().show();
            }
        });


        /* ---------- cancel operation -----------------------------------------------------------*/
        builder.setNegativeButton(GlobalData.getContext().getString(R.string.cancel_), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(globalData,R.string.operation_canceled,Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();

    }
}

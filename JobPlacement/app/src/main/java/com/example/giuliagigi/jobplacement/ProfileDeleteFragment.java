package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProfileDeleteFragment extends Fragment {

    private View root;
    private CheckBox foundJob, badApplication, fewUsers, other;
    private ArrayList<CheckBox> checkboxes;
    private EditText otherReason, hints;
    private Button confirmButton;
    private GlobalData application;

    public ProfileDeleteFragment() {}
    public static ProfileDeleteFragment newInstance() {
        ProfileDeleteFragment fragment = new ProfileDeleteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        checkboxes = new ArrayList<CheckBox>();
        application = (GlobalData)getActivity().getApplicationContext();
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

        root = inflater.inflate(R.layout.fragment_profile_delete, container, false);

        foundJob = (CheckBox)root.findViewById(R.id.profileDelete_foundJob_checkBox);
        badApplication = (CheckBox)root.findViewById(R.id.profileDelete_badApp_checkBox);
        fewUsers = (CheckBox)root.findViewById(R.id.profileDelete_fewUsers_checkBox);
        other = (CheckBox)root.findViewById(R.id.profileDelete_other_checkBox);
        checkboxes.add(foundJob);
        checkboxes.add(badApplication);
        checkboxes.add(fewUsers);
        checkboxes.add(other);


        otherReason = (EditText)root.findViewById(R.id.profileDelete_otherReason_text);
        hints = (EditText)root.findViewById(R.id.profileDelete_hints_text);
        confirmButton = (Button)root.findViewById(R.id.profileDelete_confirmButton);

        otherReason.setEnabled(false);

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otherReason.setEnabled(other.isChecked());
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = false;

                for(CheckBox cb: checkboxes)
                    if(cb.isChecked()){

                        flag = true;
                        break;
                    }

                if(!flag)
                    Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_must_specify), Toast.LENGTH_SHORT).show();
                else {

                    Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_proceding), Toast.LENGTH_SHORT).show();
                    final String objectId = application.getUserObject().getObjectId();

                    application.getUserObject().deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null){

                                if(application.getCurrentUser().getType().equals(User.TYPE_STUDENT)){

                                    ParseQuery<Student> studentQuery = ParseQuery.getQuery(Student.class);
                                    studentQuery.whereEqualTo("objectId",objectId);
                                    studentQuery.findInBackground(new FindCallback<Student>() {
                                        @Override
                                        public void done(List<Student> students, ParseException e) {

                                            if(e == null){
                                                if(students.isEmpty())
                                                    completeAccountDeletion();
                                                else
                                                    Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_try_later),Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_error),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else if(application.getCurrentUser().getType().equals(User.TYPE_COMPANY)){

                                    ParseQuery<Company> companyQuery = ParseQuery.getQuery(Company.class);
                                    companyQuery.whereEqualTo("objectId", objectId);
                                    companyQuery.findInBackground(new FindCallback<Company>() {
                                        @Override
                                        public void done(List<Company> companies, ParseException e) {

                                            if(e == null){
                                                if(companies.isEmpty())
                                                    completeAccountDeletion();
                                                else
                                                    Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_try_later),Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_error),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else
                                Log.println(Log.ASSERT,"DELETE FRAG", "error: " + e.getMessage());
                        }
                    });


                }
            }
        });

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }



    /* --------------- AUXILIARY METHODS ---------------------------------------------------------*/

    private void completeAccountDeletion(){

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> knownMails = sp.getStringSet(Login.SHAREDPREF_MAIL_LIST, new HashSet<String>());
        knownMails.remove(application.getCurrentUser().getEmail());
        editor.putStringSet(Login.SHAREDPREF_MAIL_LIST,knownMails);
        editor.remove(application.getCurrentUser().getEmail());
        editor.apply();

        application.getCurrentUser().deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    Withdrawal w = new Withdrawal();

                    ArrayList<String> reasons = new ArrayList<String>();

                    if(foundJob.isChecked()) reasons.add(Withdrawal.REASON_FOUND_JOB);
                    if(badApplication.isChecked()) reasons.add(Withdrawal.REASON_BAD_APPLICATION);
                    if(fewUsers.isChecked()) reasons.add(Withdrawal.REASON_FEW_USERS);
                    if(other.isChecked()){
                        reasons.add(Withdrawal.REASON_OTHER);
                        w.setOtherReason(otherReason.getText().toString());
                    }

                    w.setReasons(reasons);
                    if(!hints.getText().toString().equals(""))
                        w.setHints(hints.getText().toString());

                    w.saveEventually();



                    Intent i = new Intent(getActivity().getApplicationContext(),Login.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(getActivity(),GlobalData.getContext().getResources().getString(R.string.account_delete_error_2),Toast.LENGTH_SHORT).show();
            }
        });


    }
}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Intent;
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
import java.util.List;


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
                    Toast.makeText(getActivity(),"You must specify at least one reason!", Toast.LENGTH_SHORT).show();
                else {

                    Toast.makeText(getActivity(),"proceeding with account delete", Toast.LENGTH_SHORT).show();

                    deleteRelatedObjects();
                    application.getUserObject().deleteInBackground();
                    application.getCurrentUser().deleteInBackground();

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
            }
        });

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void deleteRelatedObjects(){

        ParseQuery<Telephone> telephoneQuery =ParseQuery.getQuery(Telephone.class);
        ParseQuery<Degree> degreeQuery =ParseQuery.getQuery(Degree.class);
        ParseQuery<Language> languageQuery =ParseQuery.getQuery(Language.class);
        ParseQuery<Office> officeQuery =ParseQuery.getQuery(Office.class);

        telephoneQuery.whereEqualTo(Telephone.USER_FIELD,application.getUserObject());
        telephoneQuery.findInBackground(new FindCallback<Telephone>() {
            @Override
            public void done(List<Telephone> telephones, ParseException e) {

                if(e==null && telephones.size()>0)
                    for (Telephone t:telephones)
                        t.deleteInBackground();
                else
                    Log.println(Log.ASSERT,"DELETE PROF", "error in deleting telephones");
            }
        });

        if(application.getUserObject().getType().equals(User.TYPE_STUDENT)){

            Log.println(Log.ASSERT,"DELETE PROF", "deleting a student");

            degreeQuery.whereEqualTo(Degree.STUDENT_FIELD,application.getUserObject());
            degreeQuery.findInBackground(new FindCallback<Degree>() {
                @Override
                public void done(List<Degree> degrees, ParseException e) {

                    if(e == null && degrees.size()>0)
                        for(Degree d:degrees)
                            d.deleteInBackground();
                    else
                        Log.println(Log.ASSERT,"DELETE PROF", "error in deleting degrees");
                }
            });

            languageQuery.whereEqualTo(Language.STUDENT_FIELD,application.getUserObject());
            languageQuery.findInBackground(new FindCallback<Language>() {
                @Override
                public void done(List<Language> languages, ParseException e) {

                    if(e==null && languages.size()>0)
                        for(Language l:languages)
                            l.deleteInBackground();
                    else
                        Log.println(Log.ASSERT,"DELETE PROF", "error in deleting languages");
                }
            });
        }
        else {

            Log.println(Log.ASSERT,"DELETE PROF", "deleting a company");

            officeQuery.whereEqualTo(Office.OFFICE_COMPANY_FIELD,application.getUserObject());
            officeQuery.findInBackground(new FindCallback<Office>() {
                @Override
                public void done(List<Office> offices, ParseException e) {

                    if(e==null && offices.size()>0)
                        for(Office o:offices)
                            o.deleteInBackground();
                    else
                        Log.println(Log.ASSERT,"DELETE PROF", "error in deleting offices");
                }
            });
        }
    }
}

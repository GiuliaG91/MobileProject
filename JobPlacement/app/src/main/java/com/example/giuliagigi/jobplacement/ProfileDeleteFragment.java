package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileDeleteFragment extends Fragment {

    private View root;
    private CheckBox foundJob, badApplication, fewUsers, other;
    private ArrayList<CheckBox> checkboxes;
    private EditText otherReason, hints;
    private Button confirmButton;

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
                }
            }
        });

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}

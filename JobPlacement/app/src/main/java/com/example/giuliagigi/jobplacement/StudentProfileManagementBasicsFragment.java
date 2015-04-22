package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class StudentProfileManagementBasicsFragment extends Fragment {

    private OnInteractionListener hostActivity;
    private ProfileManagement profileActivity;
    private GlobalData application;
    private Student currentUser;
    private View root;

    public StudentProfileManagementBasicsFragment() {super();}
    public static StudentProfileManagementBasicsFragment newInstance() {
        StudentProfileManagementBasicsFragment fragment = new StudentProfileManagementBasicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        application = (GlobalData)getActivity().getApplicationContext();
        currentUser = application.getStudentFromUser();
        try {
            profileActivity = (ProfileManagement) activity;
            hostActivity = (OnInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_basics, container, false);

        setEnable(profileActivity.getEditable());

        EditText nameText = (EditText)root.findViewById(R.id.student_name_area);
        if(currentUser.getName() == null){
            nameText.setText("Inser name");
        }else{
            nameText.setText(currentUser.getName());
        }

        EditText surnameText = (EditText)root.findViewById(R.id.student_surname_area);
        if(currentUser.getSurname() == null){
            surnameText.setText("Inser surname");
        }else{
            surnameText.setText(currentUser.getSurname());
        }

        EditText emailText = (EditText)root.findViewById(R.id.student_email_area);
        surnameText.setText(currentUser.getMail());



        if(currentUser.getNation() == null)
            Log.println(Log.ASSERT,"FRAG OVERVIEW ST", "no nation");
        else
            Log.println(Log.ASSERT,"FRAG OVERVIEW ST", "nation: " + currentUser.getNation());


        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnInteractionListener {


    }

    public void setEnable(boolean enable){

        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        EditText nameText = (EditText)root.findViewById(R.id.student_name_area);
        if(nameText.getText() == null) {
            nameText.setVisibility(visibility);
        }

        EditText surnameText = (EditText)root.findViewById(R.id.student_surname_area);
        if(surnameText.getText() == null) {
            surnameText.setVisibility(visibility);
        }

        EditText birthText = (EditText)root.findViewById(R.id.student_birth_area);
        if(birthText.getText() == null) {
            birthText.setVisibility(visibility);
        }

        EditText sexText = (EditText)root.findViewById(R.id.student_sex_area);
        if(sexText.getText() == null) {
            sexText.setVisibility(visibility);
        }

        if(enable == false){
            currentUser.setName(nameText.getText().toString());
            currentUser.setSurname(surnameText.getText().toString());
            currentUser.setSex(sexText.getText().toString());
            //currentUser.setBirth(birthText.getText().toString());
            
        }

        //TODO quando imposto a false è peechè devo salvare le modifiche --> fare i setter e fare saveInBackground()
    }


}

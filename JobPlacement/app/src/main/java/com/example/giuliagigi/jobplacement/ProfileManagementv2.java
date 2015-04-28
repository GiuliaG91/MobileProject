package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileManagementv2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileManagementv2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileManagementv2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;


    private GlobalData application;
    private ProfileManagementFragment currentFragment;
    private boolean editable;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProfileManagementv2.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileManagementv2 newInstance() {
        ProfileManagementv2 fragment = new ProfileManagementv2();
        return fragment;
    }

    public ProfileManagementv2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_managementv2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTransaction ft=getFragmentManager().beginTransaction();

        editable = false;

        application = (GlobalData)getActivity().getApplication();
        LinearLayout buttonsModule = new LinearLayout(getActivity().getApplicationContext());

        if(application.getCurrentUser().getType().equals(User.TYPE_STUDENT)){

            LinearLayout buttonContainers = (LinearLayout)view.findViewById(R.id.buttons_container);
            buttonsModule = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.students_buttons_module,buttonContainers);
            currentFragment = StudentProfileManagementBasicsFragment.newInstance();

            ft.replace(R.id.container_profile_management_fragmentv2, currentFragment);

            final Button editProfile = (Button)view.findViewById(R.id.editProfileButton);
            editProfile.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    if(editable) editable = false;
                    else editable = true;
                    currentFragment.setEnable(editable);

                    if(editable){
                        editProfile.setText("Save changes");
                    }else {
                        editProfile.setText("Edit Profile");
                    }
                }
            });

            Button overviewButton = (Button)view.findViewById(R.id.student_button_overview);
            Button skillsButton = (Button)view.findViewById(R.id.student_button_skills);
            Button registryButton = (Button)view.findViewById(R.id.student_button_registry);

            overviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementBasicsFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

            skillsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementSkillsFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

            registryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    currentFragment = StudentProfileManagementRegistryFragment.newInstance();
                    ft.replace(R.id.container_profile_management_fragment, currentFragment);
                    ft.commit();
                }
            });

        }
        else if(application.getCurrentUser().getType().equals(User.TYPE_COMPANY)){

            //TODO
        }

        // TODO: orientation change not working
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            buttonsModule.setOrientation(LinearLayout.VERTICAL);
        else
            buttonsModule.setOrientation(LinearLayout.HORIZONTAL);

        ft.commit();
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}

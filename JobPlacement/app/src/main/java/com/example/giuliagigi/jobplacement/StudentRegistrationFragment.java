package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.giuliagigi.jobplacement.StudentRegistrationFragment.onInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentRegistrationFragment extends Fragment {

    // TODO: attributes

    private onInteractionListener hostActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StudentRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentRegistrationFragment newInstance() {
        StudentRegistrationFragment fragment = new StudentRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StudentRegistrationFragment() {
        super();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_registration, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (hostActivity != null) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            hostActivity = (onInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onInteractionListener {
        // TODO: Update argument type and name

    }

}

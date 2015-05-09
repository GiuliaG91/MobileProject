package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pietro on 08/05/2015.
 */
public class StudentMailBoxFragment extends Fragment {


    View root;

    public StudentMailBoxFragment(){

    }


    public static StudentMailBoxFragment newInstance()
    {
        StudentMailBoxFragment fragment=new StudentMailBoxFragment();


        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_student_mailbox,container,false);


        return root;
    }
}

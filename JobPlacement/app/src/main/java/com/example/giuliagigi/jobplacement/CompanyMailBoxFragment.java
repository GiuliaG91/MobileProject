package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pietro on 08/05/2015.
 */
public class CompanyMailBoxFragment extends Fragment {

    View root;

     public static CompanyMailBoxFragment newInstance(){


         CompanyMailBoxFragment fragment=new CompanyMailBoxFragment();
         return fragment;
     }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           root=inflater.inflate(R.layout.fragment_company_mailbox,container,false);



        return root;
    }
}

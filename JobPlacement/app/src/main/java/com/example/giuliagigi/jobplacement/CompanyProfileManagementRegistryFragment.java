package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class CompanyProfileManagementRegistryFragment extends ProfileManagementFragment {

    private Company currentUser;
    Button addOffice;
    ArrayList<CompanyProfileManagementOfficeFragment> officeFragments;


    public CompanyProfileManagementRegistryFragment() {
        super();
    }

    public static CompanyProfileManagementRegistryFragment newInstance() {
        CompanyProfileManagementRegistryFragment fragment = new CompanyProfileManagementRegistryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentUser = (Company)application.getUserObject();
        officeFragments = new ArrayList<CompanyProfileManagementOfficeFragment>();
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
        root = inflater.inflate(R.layout.fragment_company_profile_management_registry, container, false);

        addOffice = (Button)root.findViewById(R.id.company_add_office);
        addOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CompanyProfileManagementOfficeFragment dmf = CompanyProfileManagementOfficeFragment.newInstance(new Office());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.company_offices_container,dmf);
                ft.commit();
                officeFragments.add(dmf);
            }
        });

        return root;
    }


    @Override
    public void onPause() {
        super.onPause();

        for(Fragment f: officeFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        int max = Math.max(officeFragments.size(),currentUser.getOffices().size());
        for(int i=0;i<max;i++){

            if(i>=officeFragments.size()){

                CompanyProfileManagementOfficeFragment dmf = CompanyProfileManagementOfficeFragment.newInstance(currentUser.getOffices().get(i));
                officeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.company_offices_container, officeFragments.get(i));
            ft.commit();

        }

        setEnable(host.isEditMode());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementFragment f: officeFragments)
            host.removeOnActivityChangedListener(f);
    }

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }

}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class CompanyProfileManagementRegistryFragment extends ProfileManagementFragment {

    private static final String TITLE = GlobalData.getContext().getString(R.string.profile_registry_tab);
    public static final String BUNDLE_IDENTIFIER = "COMPANYPROFILEREGISTRY";
    private static final String BUNDLE_KEY_COMPANY = "BUNDLE_KEY_COMPANY";

    private Company company;
    Button addOffice;
    ArrayList<CompanyProfileManagementOfficeFragment> officeFragments;


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/

    public CompanyProfileManagementRegistryFragment() {
        super();
    }
    public static CompanyProfileManagementRegistryFragment newInstance(Company company) {
        CompanyProfileManagementRegistryFragment fragment = new CompanyProfileManagementRegistryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCompany(company);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setCompany(Company company){

        this.company = company;
    }

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /*------------- STANDARD CALLBACKS ------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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

        if(root == null)
            root = inflater.inflate(R.layout.fragment_company_profile_management_registry, container, false);

        addOffice = (Button)root.findViewById(R.id.company_add_office);
        addOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CompanyProfileManagementOfficeFragment dmf = CompanyProfileManagementOfficeFragment.newInstance(new Office(), company);
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

        int max = Math.max(officeFragments.size(), company.getOffices().size());
        for(int i=0;i<max;i++){

            if(i>=officeFragments.size()){

                CompanyProfileManagementOfficeFragment dmf = CompanyProfileManagementOfficeFragment.newInstance(company.getOffices().get(i), company);
                officeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.company_offices_container, officeFragments.get(i));
            ft.commit();

        }

        setEnable(listener.isEditMode());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementFragment f: officeFragments)
            host.removeOnActivityChangedListener(f);
    }


    /* ---------------- AUXILIARY METHODS ------------------------------------------------------- */

    @Override
    protected void restoreStateFromBundle() {
        super.restoreStateFromBundle();

        if(bundle!=null)
            company = (Company)bundle.get(BUNDLE_KEY_COMPANY);
    }


    @Override
    protected void saveStateInBundle() {
        super.saveStateInBundle();

        if(bundle!= null)
            bundle.put(BUNDLE_KEY_COMPANY,company);
    }

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;
        addOffice.setVisibility(visibility);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }

}

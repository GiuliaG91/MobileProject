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


public class CompanyProfileManagementRegistryFragment extends ProfileManagementFragment implements CompanyProfileManagementOfficeFragment.OfficeFragmentInterface {

    private static final String TITLE = GlobalData.getContext().getString(R.string.profile_registry_tab);
    public static final String BUNDLE_IDENTIFIER = "COMPANYPROFILEREGISTRY";
    private static final String BUNDLE_KEY_COMPANY = "BUNDLE_KEY_COMPANY";
    private static final String BUNDLE_KEY_INIT = "bundle_init";

    private Company company;
    Button addOffice;
    private boolean init;


    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/

    public CompanyProfileManagementRegistryFragment() {
        super();
    }
    public static CompanyProfileManagementRegistryFragment newInstance(Company company) {
        CompanyProfileManagementRegistryFragment fragment = new CompanyProfileManagementRegistryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCompany(company);
        fragment.setUser(company);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setCompany(Company company){

        this.company = company;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }

    /*------------- STANDARD CALLBACKS ------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        init = true;
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

                CompanyProfileManagementOfficeFragment dmf;
                dmf = CompanyProfileManagementOfficeFragment.newInstance(CompanyProfileManagementRegistryFragment.this, new Office(), company);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.company_offices_container,dmf);
                ft.commit();
            }
        });

        return root;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(init){

            for (Office o : company.getOffices()) {

                CompanyProfileManagementOfficeFragment dmf;
                dmf = CompanyProfileManagementOfficeFragment.newInstance(this, o, company);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.company_offices_container, dmf);
                ft.commit();
            }
        }


        setEnable(listener.isEditMode());
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    /* ------------------------------------------------------------------------------------------ */
    /* ---------------- AUXILIARY METHODS ------------------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);

        if(bundle!=null){

            company = (Company)bundle.get(BUNDLE_KEY_COMPANY);
            init = bundle.getBoolean(BUNDLE_KEY_INIT);
        }
    }


    @Override
    protected void saveStateInBundle(Bundle outState) {
        super.saveStateInBundle(outState);

        init = false;
        bundle.put(BUNDLE_KEY_COMPANY,company);
        bundle.putBoolean(BUNDLE_KEY_INIT,init);
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


    /* ------------------------------------------------------------------------------------------ */
    /* ---------------- NESTED FRAGMENTS INTERFACE ---------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    @Override
    public void onOfficeDelete(CompanyProfileManagementOfficeFragment toRemove) {

        host.removeOnActivityChangedListener(toRemove);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(toRemove);
        ft.commit();
    }
}

















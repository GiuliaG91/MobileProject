package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ApplicationsListFragment extends Fragment {

    private GlobalData globalData;
    private Student student = null;
    private CompanyOffer offer = null;

    private View root;
    private RecyclerView mRecyclerView;
    private StudentApplicationAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static ApplicationsListFragment newInstance(Student student) {

        ApplicationsListFragment fragment = new ApplicationsListFragment();
        fragment.student = student;
        return fragment;
    }

    public static ApplicationsListFragment newInstance(CompanyOffer offer) {

        ApplicationsListFragment fragment = new ApplicationsListFragment();
        fragment.offer = offer;
        return fragment;
    }

    public ApplicationsListFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        globalData = (GlobalData)activity.getApplicationContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        root  = inflater.inflate(R.layout.fragment_standard_recycler_view,container,false);

        String toolbarTitle = "";

        if(student != null)
            toolbarTitle = getContext().getString(R.string.ToolbarTilteMyApplications);

        else if(offer != null)
            toolbarTitle = getContext().getString(R.string.ToolbarTilteApplicationsFor) + offer.getOfferObject();

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(toolbarTitle);
        globalData.setToolbarTitle(toolbarTitle);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);


        ArrayList<StudentApplication> applications = student == null ? offer.getApplications() : student.getApplications();
        int mode = student == null ? StudentApplicationAdapter.MODE_COMPANY_VIEW : StudentApplicationAdapter.MODE_STUDENT_VIEW;

        adapter = new StudentApplicationAdapter(getActivity(),applications,mode,position,mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        return root;
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }

}

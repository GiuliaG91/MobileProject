package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 16/05/2015.
 */
public class FavCompaniesFragment extends Fragment {

    private View root;
    private GlobalData globalData;
    private Student student;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FavCompaniesAdapter adapter;


    public static FavCompaniesFragment newInstance()
    {
        FavCompaniesFragment fragment=new FavCompaniesFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root  = inflater.inflate(R.layout.recycler_view_template,container,false);

        globalData=(GlobalData)getActivity().getApplication();
        globalData.getToolbar().setTitle(getResources().getString(R.string.ToolbarTitleMyCompanies));
        student=globalData.getStudentFromUser();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_template);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Company> companies=student.getCompanies();
        if(companies.isEmpty())
        {
            voidPrefAdapter ad=new voidPrefAdapter(this.getActivity());
            // specify an adapter
            mRecyclerView.setAdapter(ad);
        }
        else {
            adapter = new FavCompaniesAdapter(this.getActivity(), (ArrayList<Company>) companies, mRecyclerView, this);
            // specify an adapter
            mRecyclerView.setAdapter(adapter);
        }


        return root;
    }


}

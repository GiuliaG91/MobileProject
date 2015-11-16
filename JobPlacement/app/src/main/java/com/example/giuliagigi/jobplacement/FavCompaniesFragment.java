package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    private Integer position=0;

    public static FavCompaniesFragment newInstance()
    {
        FavCompaniesFragment fragment=new FavCompaniesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
        {
            position=savedInstanceState.getInt("position");
        }
        globalData=(GlobalData)getActivity().getApplication();

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar=globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteMyCompanies);
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteMyJobOffers));

        root  = inflater.inflate(R.layout.recycler_view_template,container,false);

        student = (Student)globalData.getUserObject();
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
            adapter = new FavCompaniesAdapter(this.getActivity(), (ArrayList<Company>) companies, mRecyclerView, this,position,mLayoutManager);
            // specify an adapter
            mRecyclerView.setAdapter(adapter);
        }


        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putInt("position", mLayoutManager.findFirstVisibleItemPosition());
        }catch (Exception e){
            outState.putInt("position",0);
        }
    }


}

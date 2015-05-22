package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private ToolbarTitleChange mcallback;

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
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.recycler_view_template,container,false);

        mcallback=((Home)getActivity());
        mcallback.SetNewTitle(getResources().getString(R.string.ToolbarTitleMyCompanies));
        globalData=(GlobalData)getActivity().getApplication();
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

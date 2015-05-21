package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 05/05/2015.
 */
public class CompanyShowOfferFragment extends Fragment {

    View root;
    private RecyclerView mRecyclerView;
    private  ShowOffersAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;

    public static CompanyShowOfferFragment newInstance() {
        CompanyShowOfferFragment fragment = new CompanyShowOfferFragment();

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.recycler_view_template, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_template);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);


          adapter = new ShowOffersAdapter(this.getActivity(),position,mLayoutManager);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

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



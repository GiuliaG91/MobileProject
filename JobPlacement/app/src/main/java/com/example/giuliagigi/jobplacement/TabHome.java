package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pietro on 25/04/2015.
 */

public class TabHome extends Fragment {

    GlobalData globalData;

    View root;
    FragmentActivity activity;
    private RecyclerView mRecyclerView;
    private TabHomeAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    Integer position;


   public TabHome(){}

    public static TabHome newInstance(){

        TabHome fragment = new TabHome();
        return fragment;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {
            //FilterFragment newFragment = FilterFragment.newInstance();
            //newFragment.show(getChildFragmentManager(), "dialog");

        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.globalData = (GlobalData) getActivity().getApplication();

        setHasOptionsMenu(true);
        if(savedInstanceState!=null)
        {
            position = savedInstanceState.getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        root = inflater.inflate(R.layout.home_tab, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_news);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        // use a linear layout manager

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new TabHomeAdapter(this.getActivity(), mRecyclerView, this, position, mLayoutManager);

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


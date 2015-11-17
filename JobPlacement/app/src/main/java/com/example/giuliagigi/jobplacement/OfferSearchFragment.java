package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class OfferSearchFragment extends Fragment{

    View root;
    FragmentActivity activity;
    private RecyclerView mRecyclerView;
    private OfferSearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;



    public static OfferSearchFragment newInstance() {
        OfferSearchFragment fragment = new OfferSearchFragment();

        return fragment;
    }

    public OfferSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null)
        {
            position=savedInstanceState.getInt("position");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {
            FilterFragment newFragment = FilterFragment.newInstance();
            newFragment.show(getChildFragmentManager(), "dialog");


        }
        return true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();

        root = inflater.inflate(R.layout.fragment_offer_search, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_offer_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        // use a linear layout manager

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new OfferSearchAdapter(this.getActivity(), mRecyclerView,this,position,mLayoutManager);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        ((Toolbar)activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.home));

        return root;
    }


    public void addFilters(List<Tag> tag_list, List<String> contract_list, List<String> term_list, List<String> field_list, List<String> location_list, List<String> salary_list) {
        adapter.setFactory(tag_list, contract_list, term_list, field_list, location_list, salary_list);
        adapter.setAdapter();
        adapter.notifyDataSetChanged();

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
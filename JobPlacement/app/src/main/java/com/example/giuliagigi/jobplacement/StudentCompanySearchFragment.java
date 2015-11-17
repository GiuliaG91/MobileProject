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

/**
 * Created by pietro on 14/05/2015.
 */
public class StudentCompanySearchFragment extends Fragment {

    View root;
    FragmentActivity activity;
    private RecyclerView mRecyclerView;
    private StudentCompanySearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentCompanySearchFragment newInstance(){

        StudentCompanySearchFragment fragment=new StudentCompanySearchFragment();
        return fragment;
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar=((GlobalData)getActivity().getApplication()).getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteCompanySearch);
        setHasOptionsMenu(true);

        if(savedInstanceState!=null){
            position=savedInstanceState.getInt("position");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        activity = getActivity();

        root = inflater.inflate(R.layout.fragment_offer_search, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_offer_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new StudentCompanySearchAdapter(this.getActivity(), mRecyclerView,this,position,mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        ((Toolbar)activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.home));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {
            FilterCompaniesFragment newFragment = FilterCompaniesFragment.newInstance();
            newFragment.show(getChildFragmentManager(), "dialog");
        }
        return true;
    }


    public void addFilters(List<Tag> tag_list, List<String> field_list, List<String> location_list) {
        adapter.setFactory(tag_list,field_list, location_list);
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

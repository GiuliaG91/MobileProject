package com.example.giuliagigi.jobplacement;

import android.app.Activity;
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
 * Created by pietro on 15/05/2015.
 */
public class CompanyStudentSearchFragment extends Fragment {

    View root;
    FragmentActivity activity;
    private RecyclerView mRecyclerView;
    private CompanyStudentSearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;
    private GlobalData globalData;



    public static CompanyStudentSearchFragment newInstance()
    {
        CompanyStudentSearchFragment fragment=new CompanyStudentSearchFragment();
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        globalData=(GlobalData)getActivity().getApplication();
        activity = getActivity();

        root = inflater.inflate(R.layout.fragment_standard_recycler_view, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        Toolbar toolbar=globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteCompanySearch);
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteCompanySearch));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new CompanyStudentSearchAdapter(this.getActivity(), mRecyclerView,this,position,mLayoutManager);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

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
            FilterStudentFragment newFragment = FilterStudentFragment.newInstance();
            newFragment.show(getChildFragmentManager(), "dialog");


        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void addFiters(List<Tag> tag_list, List<String> degree_list, List<String> field_list) {
        adapter.setFactory(tag_list, degree_list, field_list);
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

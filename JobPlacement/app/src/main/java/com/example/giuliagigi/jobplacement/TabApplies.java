package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by pietro on 12/05/2015.
 */
public class TabApplies extends Fragment {



    private View root;
    private GlobalData globalData;
    private Student student;
    private RecyclerView mRecyclerView;
    private AppliesAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;


    public static TabApplies newInstance(){
        TabApplies fragment=new TabApplies();
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



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root  = inflater.inflate(R.layout.recycler_view_template,container,false);

        globalData=(GlobalData)getActivity().getApplication();
        student = (Student)globalData.getUserObject();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_template);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new AppliesAdapter(this.getActivity(),mRecyclerView,position,mLayoutManager);
        // specify an adapter
        mRecyclerView.setAdapter(adapter);


        ParseQuery<CompanyOffer> query=ParseQuery.getQuery("CompanyOffer");
        query.whereEqualTo("applies",student);

        query.findInBackground(new FindCallback<CompanyOffer>() {
            @Override
            public void done(List<CompanyOffer> list, ParseException e) {
                adapter.updateDataset(list);
            }
        });

        ((Toolbar)this.getActivity().findViewById(R.id.toolbar)).setTitle(globalData.getResources().getString(R.string.home));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /*  int position=globalData.getFav_position();
        if(position!=-1)
        {
            adapter.startItem(position);
        }*/

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

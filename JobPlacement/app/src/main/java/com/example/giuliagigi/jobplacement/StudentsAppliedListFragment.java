package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pietro on 19/05/2015.
 */
public class StudentsAppliedListFragment extends Fragment {

    List<Student> students;
    View root;
    private RecyclerView mRecyclerView;
    private StudentAppliedListAdapter adapter;
    private LinearLayoutManager mLayoutManager;


    public static StudentsAppliedListFragment newInstance()
    {
        StudentsAppliedListFragment fragment=new StudentsAppliedListFragment();
          return fragment;
    }

    public void setStudents(List<Student> students)
    {
        this.students=students;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.recycler_view_template,container,false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_template);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new StudentAppliedListAdapter(this.getActivity(),students);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        return root;

    }
}

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

    private OnFragmentInteractionListener mListener;



    public static CompanyStudentSearchFragment newInstance()
    {
        CompanyStudentSearchFragment fragment=new CompanyStudentSearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        activity = getActivity();
        ((Home) activity).setOnBackPressedListener(new BaseBackPressedListener(activity));


        root = inflater.inflate(R.layout.fragment_offer_search, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_offer_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new CompanyStudentSearchAdapter(this.getActivity(), mRecyclerView,this,mLayoutManager);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void addFiters(List<Tag> tag_list, List<String> degree_list, List<String> field_list, List<String> location_list) {
        adapter.setFactory(tag_list, degree_list, field_list, location_list);
        adapter.setAdapter();
        adapter.notifyDataSetChanged();

    }


}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferSearchFragment extends Fragment implements FilterFragment.addFilter{


    View root;
    private RecyclerView mRecyclerView;
    private  OfferSearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private ParseQueryAdapter<CompanyOffer> queryAdapter;
    ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> listener;

    private boolean loading = true;

    private OnFragmentInteractionListener mListener;

    List<String> tag_list=new ArrayList<>();
    List<String> contract_list=new ArrayList<>();
    List<String> term_list=new ArrayList<>();
    List<String> field_list=new ArrayList<>();
    List<String> location_list=new ArrayList<>();
    List<String> salary_list=new ArrayList<>();



    public static OfferSearchFragment newInstance() {
        OfferSearchFragment fragment = new OfferSearchFragment();

        return fragment;
    }

    public OfferSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_offer_search, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_offer_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new OfferSearchAdapter(this.getActivity(),mRecyclerView);

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        return root;
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

        if(item.getItemId()== R.id.action_filter)
        {
            FilterFragment newFragment = FilterFragment.newInstance();
            newFragment.setTargetFragment(this,0);
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

    @Override
    public void addFilter(List<String> tag_list, List<String> contract_list, List<String> term_list, List<String> field_list, List<String> location_list, List<String> salary_list) {
                adapter.setFactory(tag_list,contract_list,term_list,field_list,location_list,salary_list);
                adapter.setAdapter();
               adapter.notifyDataSetChanged();
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



}

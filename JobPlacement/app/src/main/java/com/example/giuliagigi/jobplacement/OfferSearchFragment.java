package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferSearchFragment extends Fragment {


    View root;
    private RecyclerView mRecyclerView;
    private  OfferSearchAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private ParseQueryAdapter<CompanyOffer> queryAdapter;
    ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> listener;

    private boolean loading = true;

    private OnFragmentInteractionListener mListener;


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

        /*ParseQueryAdapeter */

        // Instantiate a QueryFactory to define the ParseQuery to be used for fetching items in this
        // Adapter.
        ParseQueryAdapter.QueryFactory<CompanyOffer> factory =
                new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
                    public ParseQuery create() {

                        ParseQuery query = new ParseQuery("CompanyOffer");
                        return query;
                    }
                };

        // Pass the factory into the ParseQueryAdapter's constructor.

        queryAdapter=new ParseQueryAdapter<>(getActivity(),factory);
        queryAdapter.setObjectsPerPage(15);
        queryAdapter.addOnQueryLoadListener(new OnQueryLoadListener());


        adapter = new OfferSearchAdapter(this.getActivity());

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int total=   mLayoutManager.getItemCount();
                if(mLayoutManager.findLastVisibleItemPosition()==total-1)
                {
                    queryAdapter.loadNextPage();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        queryAdapter.loadObjects();

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> {

        public void onLoading() {
//rotellina
        }

        @Override
        public void onLoaded(List<CompanyOffer> companyOffers, Exception e) {

            adapter.updateMyDataset(companyOffers);
            adapter.notifyDataSetChanged();

        }


    }


}

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
    private ParseQueryAdapter<CompanyOffer> queryAdapter;
    ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> listener;

    private boolean loading = true;

    public CompanyShowOfferFragment() {
    }

    public static CompanyShowOfferFragment newInstance() {
        CompanyShowOfferFragment fragment = new CompanyShowOfferFragment();
          /*
            Bundle args = new Bundle();
           fragment.setArguments(args);

           */
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        /*ParseQueryAdapeter */

        // Instantiate a QueryFactory to define the ParseQuery to be used for fetching items in this
        // Adapter.
        ParseQueryAdapter.QueryFactory<CompanyOffer> factory =
                new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
                    public ParseQuery create() {
                        Company c = ((GlobalData) getActivity().getApplication()).getCompanyFromUser();
                        ParseQuery query = new ParseQuery("CompanyOffer").whereEqualTo("company",c);
                        return query;
                    }
                };

        // Pass the factory into the ParseQueryAdapter's constructor.

        queryAdapter=new ParseQueryAdapter<>(getActivity(),factory);
        queryAdapter.setObjectsPerPage(15);
        queryAdapter.addOnQueryLoadListener(new OnQueryLoadListener());


          adapter = new ShowOffersAdapter(this.getActivity());

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                 int total=   mLayoutManager.getItemCount();
                if(mLayoutManager.findLastVisibleItemPosition()==total-1)
                {
                    Log.println(Log.ASSERT, "Ultimo", String.valueOf(total));
                    Toast.makeText(getActivity(),"Ultimo-->"+String.valueOf(total),Toast.LENGTH_SHORT).show();
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



    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> {

        public void onLoading() {
//rotellina
        }

        @Override
        public void onLoaded(List<CompanyOffer> companyOffers, Exception e) {
            if(companyOffers!=null) {
                adapter.updateMyDataset(companyOffers);
                adapter.notifyDataSetChanged();
            }//else nothing to do.... nothing has been found
        }


    }


}



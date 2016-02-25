package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pietro on 05/05/2015.
 */
public class CompanyShowOffersFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER_HEADER = "ShowOffers_Bundle_";
    private static final String BUNDLE_IDENTIFIER_TAIL_KEY = "ShowOffers_bundle_id_tail";

    private static final String BUNDLE_KEY_COMPANY = "company";
    private static final String BUNDLE_KEY_POSITION = "position";

    private GlobalData application;
    private Company company;

    private View root;
    private RecyclerView mRecyclerView;
    private OfferAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private Integer position = 0;

    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS -------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    public static CompanyShowOffersFragment newInstance(Company company) {

        CompanyShowOffersFragment fragment = new CompanyShowOffersFragment();
        fragment.company = company;
        return fragment;
    }

    public CompanyShowOffersFragment(){}


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        application = (GlobalData)context.getApplicationContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if(savedInstanceState!=null){

            String tail = savedInstanceState.getString(BUNDLE_IDENTIFIER_TAIL_KEY);
            MyBundle bundle = application.getBundle(BUNDLE_IDENTIFIER_HEADER + tail);

            if(bundle != null){

                company = (Company)bundle.get(BUNDLE_KEY_COMPANY);
                position = bundle.getInt(BUNDLE_KEY_POSITION);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application.getToolbar().setTitle(R.string.ToolbarTilteMyJobOffers);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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

        Log.println(Log.ASSERT,"COMPSHOWOFF", "number of offers:" + company.getOffers().size());
        adapter = new OfferAdapter(this.getActivity(), company.getOffers(), OfferAdapter.MODE_COMPANY_VIEW, position, mLayoutManager);

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        ((Toolbar)this.getActivity().findViewById(R.id.toolbar)).setTitle("Home");

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_IDENTIFIER_TAIL_KEY, company.toString());
        MyBundle bundle = application.addBundle(BUNDLE_IDENTIFIER_HEADER + company.toString());

        bundle.put(BUNDLE_KEY_COMPANY, company);
        bundle.putInt(BUNDLE_KEY_POSITION, position);
    }
}



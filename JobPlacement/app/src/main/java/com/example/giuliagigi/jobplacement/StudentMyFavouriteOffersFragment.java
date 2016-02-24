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

/**
 * Created by pietro on 25/04/2015.
 */
public class StudentMyFavouriteOffersFragment extends Fragment{

    private View root;
    private GlobalData globalData;
    private Student student;
    private RecyclerView mRecyclerView;
    private OfferAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private Integer position=0;


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentMyFavouriteOffersFragment newInstance(Student student){

        StudentMyFavouriteOffersFragment fragment=new StudentMyFavouriteOffersFragment();
        fragment.student = student;
        return fragment;

    }

    public StudentMyFavouriteOffersFragment(){}



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {

            position=savedInstanceState.getInt("position");
        }

        globalData=(GlobalData)getActivity().getApplication();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.fragment_standard_recycler_view,container,false);

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteFavouriteOffers);
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteFavouriteOffers));
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new OfferAdapter(getActivity(),
                student.getFavouriteOffers(),
                OfferAdapter.MODE_STUDENT_VIEW,
                position,
                mLayoutManager);

        mRecyclerView.setAdapter(adapter);

        return root;
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

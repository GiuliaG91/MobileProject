package com.example.giuliagigi.jobplacement;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pietro on 25/04/2015.
 */
public class Fav_tab extends Fragment {

    private   View root;
    private GlobalData globalData;
    private Student student;
    private RecyclerView mRecyclerView;
    private  FavouritesAdapter adapter;
    private LinearLayoutManager mLayoutManager;



   public static Fav_tab newInstance(){
       Fav_tab fragment=new Fav_tab();
       return fragment;

   }



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root  = inflater.inflate(R.layout.fragment_offer_search,container,false);

        globalData=(GlobalData)getActivity().getApplication();
        student=globalData.getStudentFromUser();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_offer_search);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);


        adapter = new FavouritesAdapter(this.getActivity(),student.getFavourites(),mRecyclerView);
        // specify an adapter
        mRecyclerView.setAdapter(adapter);




        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}

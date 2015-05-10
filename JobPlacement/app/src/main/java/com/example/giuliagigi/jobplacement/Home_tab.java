package com.example.giuliagigi.jobplacement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pietro on 25/04/2015.
 */
public class Home_tab extends Fragment {

   public Home_tab(){}

    public static Home_tab newInstance(){

        Home_tab fragment=new Home_tab();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_tab,container,false);
        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.println(Log.ASSERT, "A/HOME_tab","OnDeatach");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.println(Log.ASSERT, "A/HOME_tab","OnPause");
    }
}


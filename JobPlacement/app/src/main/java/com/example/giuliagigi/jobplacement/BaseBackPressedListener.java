package com.example.giuliagigi.jobplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

/**
 * Created by pietro on 10/05/2015.
 */
public class BaseBackPressedListener implements OnBackPressedListener {
    private final FragmentActivity activity;
    GlobalData globalData;

    public BaseBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
        globalData = (GlobalData) activity.getApplication();
    }

    @Override
    public void doBack() {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

        if (current instanceof OfferDetail) {

          //  Toast.makeText(activity," Ero in offerDetail",Toast.LENGTH_SHORT).show();

        }
        if(current instanceof OfferSearchFragment)
        {
           // Toast.makeText(activity,"Ero in ricerca",Toast.LENGTH_SHORT).show();
            globalData.getOfferFilterStatus().setValid(false);
        }



    }

}
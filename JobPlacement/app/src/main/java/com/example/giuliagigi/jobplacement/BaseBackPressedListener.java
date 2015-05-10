package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by pietro on 10/05/2015.
 */
public class BaseBackPressedListener implements OnBackPressedListener {
    private final FragmentActivity activity;
    GlobalData globalData;
    public BaseBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
        globalData=(GlobalData)activity.getApplication();
    }

    @Override
    public void doBack() {
        globalData.resetState();
  //      activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;

/**
 * Created by MarcoEsposito90 on 27/04/2015.
 */
public interface OnActivityChangedListener {


    public enum State{

        EDIT_MODE_STATE,DISPLAY_MODE_STATE
    }

    public void onActivityStateChanged(State newState, State pastState);
}

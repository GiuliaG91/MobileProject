package com.example.giuliagigi.jobplacement;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by pietro on 03/05/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    OnDataSetListener mCallback;

    // Container Activity must implement this interface
    public interface OnDataSetListener {
        public void onDataSet(GregorianCalendar gc);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        mCallback = (OnDataSetListener) getTargetFragment();


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        GregorianCalendar gc=new GregorianCalendar(year,month,day,0,0,0);

                mCallback.onDataSet(gc);

    }
}
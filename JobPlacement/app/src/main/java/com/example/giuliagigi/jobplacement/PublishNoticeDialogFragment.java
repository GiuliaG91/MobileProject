package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class PublishNoticeDialogFragment extends DialogFragment {

    public static final String TAG = "PublishNotice";

    private Course course;

    private View root;
    private EditText message;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONTRUCTOR -----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static PublishNoticeDialogFragment newInstance(Course course) {
        PublishNoticeDialogFragment fragment = new PublishNoticeDialogFragment();
        return fragment;
    }

    public PublishNoticeDialogFragment() {}

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CALLBACKS ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.professor_notice_dialog,container,false);

        message = (EditText)root.findViewById(R.id.notice_dialog_message);

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- IMPLEMENTATION -------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Notice for " + course.getName());


        builder.setPositiveButton(GlobalData.getContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String noticeMessage = message.getText().toString();

                News courseNotice = new News();
                courseNotice.createNews(7, course, noticeMessage);

                Log.println(Log.ASSERT, "COURSEADAPTER", "sending push");
                Installation.sendPush(course.getName(),
                        course.getName() + ": " + noticeMessage);

            }
        });

        builder.setNegativeButton(GlobalData.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity().getApplicationContext(), "The notice won't be published", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();

    }
}

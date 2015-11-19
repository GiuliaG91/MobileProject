package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PublishNoticeDialogFragment extends DialogFragment {

    public static final String TAG = "PublishNotice";

    private Activity activity;
    private Course course;
    private NoticeDialogInterface parent;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONTRUCTOR -----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static PublishNoticeDialogFragment newInstance(NoticeDialogInterface parent, Course course) {
        PublishNoticeDialogFragment fragment = new PublishNoticeDialogFragment();
        fragment.course = course;
        fragment.parent = parent;
        return fragment;
    }

    public PublishNoticeDialogFragment() {}

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CALLBACKS ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- IMPLEMENTATION -------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog noticeDialog = new Dialog(activity);
        noticeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noticeDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        noticeDialog.setContentView(R.layout.professor_notice_dialog);
        noticeDialog.setTitle(GlobalData.getContext().getResources().getString(R.string.notice_insert_message));

        Button confirmButton = (Button) noticeDialog.findViewById(R.id.notice_dialog_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) noticeDialog.findViewById(R.id.notice_dialog_message);
                String noticeMessage = edit.getText().toString();

                News courseNotice = new News();
                courseNotice.createNews(7, course, noticeMessage);

                Log.println(Log.ASSERT, "PUBLISHNOTICE", "sending push");
                Installation.sendPush(course.getName(),course.getName() + ": " + noticeMessage);

                if(parent != null)
                    parent.onNoticeCreated(courseNotice);

                noticeDialog.dismiss();
                Log.println(Log.ASSERT, "PUBLISHNOTICE", "Message: " + noticeMessage);
            }
        });

        Button cancelButton = (Button) noticeDialog.findViewById(R.id.notice_dialog_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(activity, GlobalData.getContext().getResources().getString(R.string.notice_wont_be_published),Toast.LENGTH_LONG).show();
                noticeDialog.dismiss();
            }
        });

        return noticeDialog;
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- INTERFACE ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public interface NoticeDialogInterface{

        public void onNoticeCreated(News notice);
    }
}

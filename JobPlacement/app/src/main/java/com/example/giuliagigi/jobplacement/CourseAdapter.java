package com.example.giuliagigi.jobplacement;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.ArrayList;

/**
 * Created by MarcoEsposito90 on 09/11/2015.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    public static final int MODE_PROFESSOR_VIEW = 0;
    public static final int MODE_STUDENT_VIEW = 1;
    public static final int MODE_STUDENT_ADD = 2;
    public static final int MODE_DIALOG_ADD = 0;
    public static final int MODE_DIALOG_REMOVE = 1;

    FragmentActivity activity;
    ArrayList<Course> courses;
    User user;
    CourseAdapterListener listener;
    int mode = MODE_PROFESSOR_VIEW;
    //String  noticeMessage = "";


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public CourseAdapter(FragmentActivity activity, ArrayList<Course> courses, User user, int mode) {

        Log.println(Log.ASSERT,"COURSEADAPTER", "size = " + courses.size());
        this.courses = courses;
        this.mode = mode;
        this.user = user;
        this.activity = activity;

        try {
            this.listener = (CourseAdapterListener)activity;
        }
        catch (ClassCastException e){

            e.printStackTrace();
        }
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    // 1) create a view and the related view holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.println(Log.ASSERT,"COURSEADAPTER", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_row, parent, false);
        return new ViewHolder(v, mode);
    }

    // 2) setting the content of the view's widgets depending on its position
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(courses.get(position).getName());
        holder.code.setText(courses.get(position).getCode());

        if (holder.professor != null) {

            try {
                courses.get(position).getProfessor().fetchIfNeeded();
            } catch (ParseException e) {

                Log.println(Log.ASSERT,"COURSEADAPTER", "failed fecth");
                e.printStackTrace();
            }

            holder.professor.setText(courses.get(position).getProfessor().getName() + " " + courses.get(position).getProfessor().getSurname());

        }

        if(holder.details != null){

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean edit = false;
                    if (holder.mode == MODE_PROFESSOR_VIEW) edit = true;

//                    Log.println(Log.ASSERT,"COURSEADAPTER", "invoking cache on course");
                    CourseDetailFragment cdf = CourseDetailFragment.newInstance(courses.get(position), edit);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.tab_Home_container, cdf).addToBackStack(null).commit();
                }
            });
        }

        if(holder.buttonAddRemovePublish != null){

            /* ----------------------- remove course ---------------------------------------------*/
            if(holder.mode == MODE_STUDENT_VIEW){

                holder.buttonAddRemovePublish.setText(GlobalData.getContext().getResources().getString(R.string.button_remove_my_courses));
                holder.buttonAddRemovePublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Student s = (Student) user;

                        if (!s.getCourses().contains(courses.get(position))) {

                            Toast.makeText(activity, "This course is not among your courses", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Dialog confirmDialog = createConfirmDialog(MODE_DIALOG_REMOVE, s, position, holder);
                            confirmDialog.show();
                        }

                    }
                });

            }

            /* ------------------------ add course -----------------------------------------------*/

            else if(holder.mode == MODE_STUDENT_ADD){

                holder.buttonAddRemovePublish.setText(GlobalData.getContext().getResources().getString(R.string.button_add_my_courses));
                holder.buttonAddRemovePublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Student s = (Student) user;
                        if (s.getCourses().contains(courses.get(position))) {

                            Toast.makeText(activity, "This course is already among your courses", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            Dialog confirmDialog = createConfirmDialog(MODE_DIALOG_ADD, s, position, holder);
                            confirmDialog.show();

                        }

                    }
                });
            }

            /* ----------------------- edit course -----------------------------------------------*/

            else if(holder.mode == MODE_PROFESSOR_VIEW){

                holder.buttonAddRemovePublish.setText(GlobalData.getContext().getResources().getString(R.string.button_publish_notice));
                holder.buttonAddRemovePublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                                courseNotice.createNews(7, courses.get(position), noticeMessage);

                                Installation.sendPush(courses.get(position).getName(),
                                        courses.get(position).getName() + ": " + noticeMessage);

                                noticeDialog.dismiss();
                                Log.println(Log.ASSERT, "COURSEADAPTER", "Message: " + noticeMessage);
                            }
                        });

                        Button cancelButton = (Button) noticeDialog.findViewById(R.id.notice_dialog_cancel_button);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                noticeDialog.dismiss();
                            }
                        });

                        noticeDialog.show();
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- VIEW HOLDER ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/
    public static class ViewHolder extends RecyclerView.ViewHolder{

        View root;
        TextView name, professor, code;
        Button details, buttonAddRemovePublish;
        int mode;

        public ViewHolder(View itemView, int mode) {

            super(itemView);

            this.root = itemView;
            this.mode = mode;
            code = (TextView)itemView.findViewById(R.id.course_code_textView);
            name = (TextView)itemView.findViewById(R.id.course_name_textView);
            professor = (TextView)itemView.findViewById(R.id.course_professor_textView);
            details = (Button)itemView.findViewById(R.id.course_details_button);
            buttonAddRemovePublish = (Button)itemView.findViewById(R.id.course_add_button);

            LinearLayout l = (LinearLayout)itemView;

            if(mode == MODE_PROFESSOR_VIEW) {


                l.removeView(professor);
                l.removeView(itemView.findViewById(R.id.course_professor_title));
                professor = null;

            }
            else if(mode == MODE_STUDENT_ADD){

                l.removeView(details);
                details = null;
            }
            else if(mode == MODE_STUDENT_VIEW){

//                l.removeView(buttonAddRemovePublish);
//                buttonAddRemovePublish = null;
            }
        }
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- INTERFACE ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/
    public interface CourseAdapterListener {

        public void onDataSetChanged();
    }

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- DIALOG CREATION ------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public Dialog createConfirmDialog(final int type, final Student s, final int position, final ViewHolder holder){

        final Dialog confirmDialog = new Dialog(activity);
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        confirmDialog.setContentView(R.layout.dialog_confirm_add_remove);

        TextView confirmText = (TextView)confirmDialog.findViewById(R.id.dialog_confirm_text);
        if(type == MODE_DIALOG_ADD)
            confirmText.setText(GlobalData.getContext().getResources().getString(R.string.add_course_confirm));
        else if(type == MODE_DIALOG_REMOVE)
            confirmText.setText(GlobalData.getContext().getResources().getString(R.string.delete_course_confirm));

        Button confirmButton = (Button) confirmDialog.findViewById(R.id.dialog_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Installation.initialize();

                if(type == MODE_DIALOG_ADD) {
                    s.addCourse(courses.get(position));
                    s.saveInBackground();
                    Installation.addChannel(courses.get(position).getName());
                    listener.onDataSetChanged();
                }
                else if(type == MODE_DIALOG_REMOVE){

                    s.removeCourse(courses.get(position));
                    s.saveInBackground();
                    Installation.removeChannel(courses.get(position).getName());
                    listener.onDataSetChanged();
                }

                Installation.commit();
                confirmDialog.dismiss();

                holder.buttonAddRemovePublish.setEnabled(false);
                holder.buttonAddRemovePublish.setVisibility(View.INVISIBLE);
            }
        });
        Button cancelButton = (Button) confirmDialog.findViewById(R.id.dialog_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                confirmDialog.dismiss();
            }
        });
        return  confirmDialog;
    }

}

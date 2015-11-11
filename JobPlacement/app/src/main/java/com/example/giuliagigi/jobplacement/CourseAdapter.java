package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by MarcoEsposito90 on 09/11/2015.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    public static final int MODE_PROFESSOR_VIEW = 0;
    public static final int MODE_STUDENT_VIEW = 1;
    public static final int MODE_STUDENT_ADD = 2;

    FragmentActivity activity;
    ArrayList<Course> courses;
    User user;
    int mode = MODE_PROFESSOR_VIEW;


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public CourseAdapter(FragmentActivity activity, ArrayList<Course> courses, User user, int mode) {

        Log.println(Log.ASSERT,"COURSEADAPTER", "size = " + courses.size());
        this.courses = courses;
        this.mode = mode;
        this.user = user;
        this.activity = activity;
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

            holder.professor.setText(courses.get(position).getProfessor().getName() + " " + courses.get(position).getProfessor().getSurname());
        }

        if(holder.details != null){

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean edit = false;
                    if (holder.mode == MODE_PROFESSOR_VIEW) edit = true;

                    Log.println(Log.ASSERT,"COURSEADAPTER", "invoking cache on course");
                    CourseDetailFragment cdf = CourseDetailFragment.newInstance(courses.get(position), edit);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.tab_Home_container, cdf).addToBackStack(null).commit();
                }
            });
        }

        if(holder.addToMyCourses != null){

            holder.addToMyCourses.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(user.getType().equals(User.TYPE_STUDENT)){

                        Student s = (Student)user;

                        if(courses != null){

                            s.addCourse(courses.get(position));
                            s.saveEventually();
                            holder.addToMyCourses.setEnabled(false);
                        }
                        else {
                            Log.println(Log.ASSERT,"COURSEADAPTER", "ERROR: trying to add a null courses");
                        }
                    }
                    else {

                        Log.println(Log.ASSERT,"COURSEADAPTER", "ERROR: trying to add a course to a non-student");
                    }
                }
            });
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

        TextView name, professor, code;
        Button details, addToMyCourses;
        int mode;

        public ViewHolder(View itemView, int mode) {

            super(itemView);

            this.mode = mode;
            code = (TextView)itemView.findViewById(R.id.course_code_textView);
            name = (TextView)itemView.findViewById(R.id.course_name_textView);
            professor = (TextView)itemView.findViewById(R.id.course_professor_textView);
            details = (Button)itemView.findViewById(R.id.course_details_button);
            addToMyCourses = (Button)itemView.findViewById(R.id.course_add_button);

            LinearLayout l = (LinearLayout)itemView;

            if(mode == MODE_PROFESSOR_VIEW) {


                l.removeView(professor);
                l.removeView(itemView.findViewById(R.id.course_professor_title));
                professor = null;

                l.removeView(addToMyCourses);
                addToMyCourses = null;
            }
            else if(mode == MODE_STUDENT_ADD){

                l.removeView(details);
                details = null;
            }
            else if(mode == MODE_STUDENT_VIEW){

                l.removeView(addToMyCourses);
                addToMyCourses = null;
            }
        }
    }
}

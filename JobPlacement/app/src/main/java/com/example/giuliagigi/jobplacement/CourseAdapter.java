package com.example.giuliagigi.jobplacement;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MarcoEsposito90 on 09/11/2015.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    public static final int MODE_PROFESSOR_VIEW = 0;
    public static final int MODE_STUDENT_VIEW = 1;

    ArrayList<Course> courses;
    int mode = MODE_PROFESSOR_VIEW;


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public CourseAdapter(ArrayList<Course> courses, int mode) {

        Log.println(Log.ASSERT,"COURSEADAPTER","course adapter constructor; size = " + courses.size());
        this.courses = courses;
        this.mode = mode;
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    // 1) create a view and the related view holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.println(Log.ASSERT,"COURSEADAPTER","onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_row, parent, false);
        Log.println(Log.ASSERT,"COURSEADAPTER","inflate ok");
        return new ViewHolder(v, MODE_PROFESSOR_VIEW);
    }

    // 2) setting the content of the view's widgets
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.println(Log.ASSERT,"COURSEADAPTER","onBindViewHolder");
        holder.name.setText(courses.get(position).getName());
        holder.code.setText(courses.get(position).getCode());

        if(holder.professor != null){

            holder.professor.setText(courses.get(position).getProfessor().getName() + " " + courses.get(position).getProfessor().getSurname());
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

        public ViewHolder(View itemView, int mode) {

            super(itemView);

            Log.println(Log.ASSERT,"COURSEADAPTER","view Holder constructor");

            code = (TextView)itemView.findViewById(R.id.course_code_textView);
            name = (TextView)itemView.findViewById(R.id.course_name_textView);
            professor = (TextView)itemView.findViewById(R.id.course_professor_textView);

            if(mode == MODE_PROFESSOR_VIEW) {

//                ((ViewManager)itemView.getParent()).removeView(professor);
                LinearLayout l = (LinearLayout)itemView;
                l.removeView(professor);
                professor = null;
            }
        }
    }
}

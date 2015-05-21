package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pietro on 17/05/2015.
 */
public class FavStudentsAdapter extends RecyclerView.Adapter<FavStudentsAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<Student> mDataset;
    private GlobalData globalData;
    private Company company;
    private FavStudentsAdapter favStudentsAdapter=this;
    private RecyclerView mRecyclerView;
    private FavStudentsFragment parent;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView profile;
        private TextView studentName;
        private TextView studentDegree;
        private TextView studentGrade;
        private CheckBox pref;




        public ViewHolder(View v) {
            super(v);
            v.setTag(this);

            profile = (ImageView) v.findViewById(R.id.profile_img);
            studentName = (TextView) v.findViewById(R.id.student_name_tv);
            studentDegree = (TextView) v.findViewById(R.id.student_degree_tv );
            studentGrade = (TextView) v.findViewById(R.id.student_grade_tv );
            pref=(CheckBox)v.findViewById(R.id.star);
        }
    }

    public  FavStudentsAdapter(FragmentActivity c, ArrayList<Student> students, RecyclerView r,FavStudentsFragment fragment,int pos,LinearLayoutManager manager)
    {
        context=c;
        mDataset=new ArrayList<>(students);
        globalData=(GlobalData)context.getApplication();
        company=globalData.getCompanyFromUser();
        mRecyclerView=r;
        parent=fragment;
        currentPosition=pos;
        mLayoutManager=manager;

        if(currentPosition!=0)
        {
            mLayoutManager.scrollToPosition(currentPosition);
        }

    }


    // Create new views (invoked by the layout manager)
    @Override
    public  FavStudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(FavStudentsAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element



        Bitmap img=null;
        try{
            img=mDataset.get(position).getProfilePhoto();

        }catch (Exception e){
            img=null;
        }

        if(img!=null) {
            holder.profile.setImageBitmap(img);
        }else
            holder.profile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
        holder.studentName.setText(mDataset.get(position).getName());

        List<Degree> degrees=mDataset.get(position).getDegrees();
        if(!degrees.isEmpty()) {
            Collections.sort(degrees);

            holder.studentDegree.setText(degrees.get(0).getType() + " " + degrees.get(0).getStudies());
            Integer mark=null;
            try{
                mark=degrees.get(0).getMark();
            }catch (Exception e){mark=null;}
            if(mark!=null) {
                holder.studentGrade.setText(context.getResources().getString(R.string.Mark) + String.valueOf(mark));
            }
            else{
                holder.studentGrade.setText(context.getResources().getString(R.string.noMark));
            }
        }
        else
        {
            holder.studentDegree.setText(context.getResources().getString(R.string.noDegree));
            holder.studentGrade.setText("");
        }

        holder.pref.setChecked(true);
        holder.pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox=(CheckBox)v;

                if (checkBox.isChecked()) {
                    //add this offer to pref
                    company.addStudent(mDataset.get(position));
                    company.saveInBackground();
                } else {
                    //delete this offer from pref
                    company.removeStudent(mDataset.get(position));
                    company.saveInBackground();

                }
            }
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }





    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();

        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        //New Fragment
        ProfileManagement fragment=ProfileManagement.newInstance(false,mDataset.get(vh.getPosition()));
        // Insert the fragment by replacing any existing fragment
        // Insert the fragment by replacing any existing fragment

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("PrefView")
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item, update the title, and close the drawer
        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle("Student");


    }




}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pietro on 17/05/2015.
 */
public class FavCompaniesAdapter extends RecyclerView.Adapter<FavCompaniesAdapter.ViewHolder> implements View.OnClickListener {




    private FragmentActivity context;
    private ArrayList<Company> mDataset;
    private GlobalData globalData;
    private Student student;
    private FavCompaniesAdapter favCompaniesAdapter=this;
    private RecyclerView mRecyclerView;
    private FavCompaniesFragment parent;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView profile;
        private TextView companyName;
        private CheckBox pref;




        public ViewHolder(View v) {
            super(v);
            v.setTag(this);

            profile = (ImageView) v.findViewById(R.id.logo_img);
           companyName = (TextView) v.findViewById(R.id.company_name_tv);
            pref=(CheckBox)v.findViewById(R.id.star);
        }
    }

    public  FavCompaniesAdapter(FragmentActivity c, ArrayList<Company> companies, RecyclerView r,FavCompaniesFragment fragment,int position,LinearLayoutManager manager)
    {
        context=c;
        mDataset=new ArrayList<>(companies);
        globalData=(GlobalData)context.getApplication();
        student = (Student)globalData.getUserObject();
        mRecyclerView=r;
        parent=fragment;
        currentPosition=position;
        mLayoutManager=manager;

        if(currentPosition!=0)
        {
            mLayoutManager.scrollToPosition(currentPosition);
        }

    }


    // Create new views (invoked by the layout manager)
    @Override
    public  FavCompaniesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(FavCompaniesAdapter.this);

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
        holder.companyName.setText(mDataset.get(position).getName());


        holder.pref.setChecked(true);
        holder.pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox checkBox=(CheckBox)v;

                if (checkBox.isChecked()) {
                    //add this offer to pref
                    student.addCompany(mDataset.get(position));
                    student.saveInBackground();
                } else {
                    //delete this offer from pref
                    student.removeCompany(mDataset.get(position));
                    student.saveInBackground();

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

package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by pietro on 24/04/2015.
 */
public class FavouritesAdapter extends  RecyclerView.Adapter<FavouritesAdapter.ViewHolder>{

    List<Company> companyList=new ArrayList<>();
    FragmentActivity myContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        protected TextView tv_companyName;
        protected ImageView iv_companyLogo;
        protected CheckBox cb_star;


        public ViewHolder(View v) {
            super(v);
            tv_companyName=(TextView)v.findViewById(R.id.tv_companyName);
            iv_companyLogo=(ImageView)v.findViewById(R.id.iv_companyLogo);
            cb_star=(CheckBox)v.findViewById(R.id.star);
        }

    }



    public FavouritesAdapter(List<Company> companies,FragmentActivity c)
    {

         for(Company comp : companies)
         {

             companyList.add(comp);
         }


        myContext=c;

        Log.d("AAAAAAAAAAAA", companyList.get(0).getClass().toString());

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Integer id=myContext.getResources().getIdentifier("nolecture" ,"string", myContext.getPackageName());

        holder.iv_companyLogo.setImageResource(R.drawable.ic_profile);


        Company c=companyList.get(position);

        holder.tv_companyName.setText(companyList.get(position).getName());
        holder.cb_star.setChecked(true);
        holder.cb_star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    //add to favourites
                }
                else
                {
                    //remove

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }


    public void removeAt(int position) {
        companyList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, companyList.size());
    }


}

package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 07/05/2015.
 */
public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.ViewHolder> implements View.OnClickListener{


    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private CheckBox pref;


        public ViewHolder(View v) {
            super(v);
           v.setTag(this);
            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.objectOffer_tv);
            descriprion=(TextView)v.findViewById(R.id.description_tv);
            pref=(CheckBox)v.findViewById(R.id.star);
        }
    }

    public OfferSearchAdapter(FragmentActivity c)
    {
        context=c;
        mDataset=new ArrayList<>();
        globalData=(GlobalData)context.getApplication();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public  OfferSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
            v.setClickable(true);
            v.setOnClickListener(OfferSearchAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.logo.setImageResource(R.drawable.ic_profile);

        holder.object.setText(mDataset.get(position).getOfferObject());
        String description=mDataset.get(position).getDescription();
        if(description==null || description.isEmpty() || description.equals(""))
        {
            holder.descriprion.setText("...");
        }
        else{

            if(description.length()<30)
            {
                holder.descriprion.setText(description+"...");
            }
            else
            {
                holder.descriprion.setText(mDataset.get(position).getDescription().substring(0,1)+"...");
            }
        }

        holder.pref.setChecked(false);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void updateMyDataset(List<CompanyOffer> offers)
    {
        mDataset.addAll(offers);

    }



    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();

        globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
       //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

            //New Fragment
          OfferDetail fragment=OfferDetail.newInstance();
            // Insert the fragment by replacing any existing fragment
            // Insert the fragment by replacing any existing fragment

            fragmentManager.beginTransaction()
                    .replace(R.id.tab_Home_container, fragment)
                    .addToBackStack("OfferSearch")
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            // Highlight the selected item, update the title, and close the drawer
           Toolbar toolbar= globalData.getToolbar();
            toolbar.setTitle("Offer");


    }


}

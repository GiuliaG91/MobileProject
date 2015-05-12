package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import java.util.List;

/**
 * Created by pietro on 12/05/2015.
 */
public class AppliesAdapter extends RecyclerView.Adapter<AppliesAdapter.ViewHolder> implements View.OnClickListener{



    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;
    private Student student;
    private AppliesAdapter appliesAdapter=this;
    private RecyclerView mRecyclerView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView status;





        public ViewHolder(View v) {
            super(v);
            v.setTag(this);
            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.objectOffer_tv);
            descriprion=(TextView)v.findViewById(R.id.description_tv);
            status=(TextView)v.findViewById(R.id.applies_tv);

        }
    }
    public AppliesAdapter (FragmentActivity c, RecyclerView r)
    {
        context=c;
        mDataset=new ArrayList<>();
        globalData=(GlobalData)context.getApplication();
        student=globalData.getStudentFromUser();
        mRecyclerView=r;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public  AppliesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_offers_company, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(AppliesAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
                holder.descriprion.setText(mDataset.get(position).getDescription().substring(0,29)+"...");
            }
        }

        holder.status.setText("Boh");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();

        globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        globalData.setFav_position(vh.getPosition());
        //New Fragment
        OfferDetail fragment=OfferDetail.newInstance();
        // Insert the fragment by replacing any existing fragment
        // Insert the fragment by replacing any existing fragment

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("PrefView")
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item, update the title, and close the drawer
        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle("Offer");


    }

    public void startItem(int position)
    {


        globalData.setCurrentOffer(mDataset.get(position));
        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        OfferDetail fragment=OfferDetail.newInstance();
        // Insert the fragment by replacing any existing fragment
        // Insert the fragment by replacing any existing fragment

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("PrefView")
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item, update the title, and close the drawer
        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle("Offer");

    }

    public void updateDataset(List<CompanyOffer> off)
    {
        mDataset.addAll(off);
        notifyDataSetChanged();
    }

}

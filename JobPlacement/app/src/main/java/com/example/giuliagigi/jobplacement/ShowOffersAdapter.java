package com.example.giuliagigi.jobplacement;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pietro on 05/05/2015.
 */
public class ShowOffersAdapter extends RecyclerView.Adapter<ShowOffersAdapter.ViewHolder>
{
    private ParseQueryAdapter<CompanyOffer> queryAdapter;
    private ShowOffersAdapter adapter=this;

    private Context context;
    private ParseQueryAdapter.QueryFactory<CompanyOffer> query;
    private ArrayList<CompanyOffer> mDataset;

        ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> listener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView appliesCount;


        public ViewHolder(View v) {
            super(v);

            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.objectOffer_tv);
            descriprion=(TextView)v.findViewById(R.id.description_tv);
            appliesCount=(TextView)v.findViewById(R.id.applies_tv);


        }
    }

   public ShowOffersAdapter(Context c)
   {
       context=c;

       mDataset=new ArrayList<>();



   }



    // Create new views (invoked by the layout manager)
    @Override
    public ShowOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_offers_company, parent, false);
        // set the view's size, margins, paddings and layout parameters

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
        int dim=mDataset.get(position).getStudents().size();
        holder.appliesCount.setText(String.valueOf(dim));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> {

        public void onLoading() {

        }

        @Override
        public void onLoaded(List<CompanyOffer> companyOffers, Exception e) {
            mDataset=(ArrayList)companyOffers;
            adapter.notifyDataSetChanged();

        }


    }

    public void updateMyDataset(List<CompanyOffer> offers)
    {
        mDataset.addAll(offers);

    }

}

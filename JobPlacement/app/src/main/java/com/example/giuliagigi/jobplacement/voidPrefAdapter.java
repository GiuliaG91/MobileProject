package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pietro on 24/04/2015.
 */
public class voidPrefAdapter extends RecyclerView.Adapter<voidPrefAdapter.ViewHolder>{


    FragmentActivity myContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        protected TextView vText;


        public ViewHolder(View v) {
            super(v);
            vText=(TextView)v.findViewById(R.id.noFav);
        }

    }

    public voidPrefAdapter(FragmentActivity c)
    {
        myContext=c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.no_favourites, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Integer id=myContext.getResources().getIdentifier("nolecture" ,"string", myContext.getPackageName());

        holder.vText.setText(myContext.getString(id));

    }

    @Override
    public int getItemCount() {
        return 1;
    }


}

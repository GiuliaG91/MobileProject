package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pietro on 23/04/2015.
 */
public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder> {

    private User mDataset;

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_PROFILE = 1;

    private static final int TYPE_SEARCH = 2;

    private static final int TYPE_ITEM = 3;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

         //String Resource for header view email
    FragmentActivity act;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView , int ViewType) {
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_ITEM || ViewType== TYPE_PROFILE || ViewType== TYPE_SEARCH ) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            } else {


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public menuAdapter(User u, String Titles[], int Icons[],FragmentActivity a) {
        mDataset = u;
        mNavTitles=Titles;
        mIcons=Icons;
        act=a;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public menuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   final int viewType) {

        if (viewType == TYPE_ITEM ||viewType== TYPE_PROFILE || viewType== TYPE_SEARCH) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        if(viewType== TYPE_PROFILE) {
                            Intent intent = new Intent(act, ProfileManagement.class);
                            act.startActivity(intent);

                        }
                       else if(viewType== TYPE_SEARCH) {
                            Intent intent = new Intent(act, ProfileManagement.class); // Search activity
                            act.startActivity(intent);

                        }
                        else {

                            Intent intent = new Intent(act, ProfileManagement.class); // Inbox activity
                            act.startActivity(intent);
                        }



                        }
                    });
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
        } else {

            holder.profile.setImageResource(R.drawable.ic_profile);           // Similarly we set the resources for header view
            holder.Name.setText(mDataset.getAccountID());
            holder.email.setText(mDataset.getMail());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 4;
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        else if(position==1) return TYPE_PROFILE;
        else if(position==2) return TYPE_SEARCH;
        else
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}


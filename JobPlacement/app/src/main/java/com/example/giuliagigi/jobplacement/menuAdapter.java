package com.example.giuliagigi.jobplacement;


import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by pietro on 25/04/2015.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_HOME =1 ;

    private static final int TYPE_PROFILE=2;
    private static final int TYPE_SEARCH=3;
    private static final int TYPE_COMPANIES=4;
    private static final int TYPE_MAILBOX=5;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private TypedArray ICONS;      // Int Array to store the passed icons resource value from MainActivity.java

    private  GlobalData gd;
    private  User user;
    private  int flag;
    private    FragmentActivity activity;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private Toolbar toolbar;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView , int ViewType) {
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_HOME || ViewType == TYPE_PROFILE || ViewType == TYPE_SEARCH || ViewType == TYPE_COMPANIES || ViewType == TYPE_MAILBOX) {
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


    public MenuAdapter(String Titles[], TypedArray Icons, User u, FragmentActivity act, DrawerLayout d, RecyclerView l, Toolbar t,GlobalData globalData)
    {
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        ICONS = Icons;
         user=u;
        activity=act;
        mDrawerLayout=d;
        mDrawerList=l;
        toolbar=t;
        gd=globalData;

    }



    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HOME || viewType == TYPE_PROFILE || viewType == TYPE_SEARCH || viewType == TYPE_COMPANIES || viewType == TYPE_MAILBOX ) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout
        switch (flag) {

            case 1:


                switch (viewType) {
                    case TYPE_HOME:
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                if (!(current instanceof TabHomeStudentFragment)) {
                                    //New Fragment
                                    TabHomeStudentFragment homeFragment = TabHomeStudentFragment.newInstance();
                                    // Insert the fragment by replacing any existing fragment
                                    // Insert the fragment by replacing any existing fragment

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, homeFragment)
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();

                                }
                            }


                        });

                        break;

                    case TYPE_PROFILE:

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                if(!(current instanceof ProfileManagement)) {

                                    Fragment fragment = ProfileManagement.newInstance();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();
                                }

                            }
                        });

                        break;


                    default:  break;

                }
             break;
/*****************************************************STUDENT******************************************************/
            case 2 :

                  switch (viewType)
                  {
                      case TYPE_HOME :

                          v.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {


                                  FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                  Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                  if (!(current instanceof TabHomeCompanyFragment)) {
                                      //New Fragment
                                      TabHomeCompanyFragment homeFragment = TabHomeCompanyFragment.newInstance();
                                      // Insert the fragment by replacing any existing fragment
                                      // Insert the fragment by replacing any existing fragment

                                      fragmentManager.beginTransaction()
                                              .replace(R.id.tab_Home_container, homeFragment)
                                              .commit();

                                      // Highlight the selected item, update the title, and close the drawer
                                      // Highlight the selected item, update the title, and close the drawer
                                      TextView tv = (TextView) v.findViewById(R.id.rowText);

                                      toolbar.setTitle(tv.getText());
                                      mDrawerLayout.closeDrawers();

                                  }
                              }


                          });


                          break;






                  }


                break;
/********************************************COMPANY******************************************************************/


            default :       break;  //caso default dello switch dei flag

        }


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

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(ICONS.getResourceId(position - 1,0));// Settimg the image with array of our icons

        } else {
            if(user.getType().toLowerCase().equals("student"))
            {
               Student s=gd.getStudentFromUser();
            holder.profile.setImageResource(R.drawable.ic_profile);           // Similarly we set the resources for header view
            holder.Name.setText(s.getName());
            holder.email.setText(s.getMail());
                flag= 1;

            }
            else
            {
                Company c=gd.getCompanyFromUser();
                holder.profile.setImageResource(R.drawable.ic_profile);           //logo azienda
                holder.Name.setText(c.getName());
                holder.email.setText(c.getMail());

                flag=2;
            }


        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        else if(position==1) return TYPE_HOME;
        else if(position==2) return TYPE_PROFILE;
        else if(position==3) return TYPE_SEARCH;
        else if(position==4) return TYPE_COMPANIES;  // for student is autocandidature for companies is new offer
        else if(position==5) return TYPE_COMPANIES;   // after it became mail box
        return TYPE_HOME;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

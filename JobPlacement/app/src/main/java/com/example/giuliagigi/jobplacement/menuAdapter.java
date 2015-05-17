package com.example.giuliagigi.jobplacement;


import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.LastOwnerException;


/**
 * Created by pietro on 25/04/2015.
 */
public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_HOME =1 ;

    private static final int TYPE_PROFILE=2;
    private static final int TYPE_MYJOBOFFERS=3;
    private static final int TYPE_MY_COMPANIES=4;
    private static final int TYPE_MAILBOX=5;
    private static final int TYPE_LOGOUT=6;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private TypedArray ICONS;      // Int Array to store the passed icons resource value from MainActivity.java

    private  GlobalData gd;
    private  ParseUserWrapper user;
    private  int flag;
    private  FragmentActivity activity;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private Toolbar toolbar;


    private SetSelectedItem mCallbacks;

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

            if (ViewType == TYPE_HEADER) {
                                                        // setting holder id as 1 as the object being populated are of type item row
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }

            else{

                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;

            }
        }
    }


    public menuAdapter(String Titles[], TypedArray Icons, ParseUserWrapper u, FragmentActivity act, DrawerLayout d, RecyclerView l, Toolbar t, GlobalData globalData)
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
        mCallbacks=(SetSelectedItem)activity;
    }



    @Override
    public menuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (!(viewType == TYPE_HEADER ) ) {
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
                                    mCallbacks.setSelectedItem(TYPE_HOME);

                                }
                                mDrawerLayout.closeDrawers();
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

                                    Fragment fragment = ProfileManagement.newInstance(true,gd.getUserObject());

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();
                                    mCallbacks.setSelectedItem(TYPE_PROFILE);

                                }
                                mDrawerLayout.closeDrawers();
                            }
                        });

                        break;


                    case TYPE_MYJOBOFFERS :
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                if(!(current instanceof Fav_tab)) {

                                   Fav_tab fragment = Fav_tab.newInstance();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();
                                    mCallbacks.setSelectedItem(TYPE_MYJOBOFFERS);

                                }
                                mDrawerLayout.closeDrawers();
                            }
                        });

                         break;

                    case TYPE_MY_COMPANIES :

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                if (!(current instanceof FavCompaniesFragment)) {
                                    //New Fragment
                                    FavCompaniesFragment fragment = FavCompaniesFragment.newInstance();
                                    // Insert the fragment by replacing any existing fragment
                                    // Insert the fragment by replacing any existing fragment

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .addToBackStack("Home")
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();
                                    mCallbacks.setSelectedItem(TYPE_MY_COMPANIES);

                                }
                                mDrawerLayout.closeDrawers();
                            }


                        });


                    break;
                    case  TYPE_MAILBOX :

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                if(!(current instanceof MailBoxFragment)) {

                                    Fragment fragment = MailBoxFragment.newInstance();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .commit();

                                    // Highlight the selected item, update the title, and close the drawer
                                    // Highlight the selected item, update the title, and close the drawer
                                    TextView tv = (TextView) v.findViewById(R.id.rowText);

                                    toolbar.setTitle(tv.getText());
                                    mDrawerLayout.closeDrawers();
                                    mCallbacks.setSelectedItem(TYPE_MAILBOX);

                                    //********
                                    //activity.invalidateOptionsMenu();
                                }
                            }
                        });
                        break;

                    case TYPE_LOGOUT :

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                LogoutDialogFragment fragment = LogoutDialogFragment.newInstance();
                                fragment.show(activity.getFragmentManager(), "Logout");

                                mDrawerLayout.closeDrawers();
                            }
                        });


                        break;

                    default:  break;

                }
             break;
/*****************************************************END STUDENT******************************************************/
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
                                      mCallbacks.setSelectedItem(TYPE_HOME);

                                  }
                                  mDrawerLayout.closeDrawers();
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

                                      Fragment fragment = ProfileManagement.newInstance(true,gd.getUserObject());

                                      fragmentManager.beginTransaction()
                                              .replace(R.id.tab_Home_container, fragment)
                                              .commit();

                                      // Highlight the selected item, update the title, and close the drawer
                                      // Highlight the selected item, update the title, and close the drawer
                                      TextView tv = (TextView) v.findViewById(R.id.rowText);

                                      toolbar.setTitle(tv.getText());
                                      mDrawerLayout.closeDrawers();
                                      mCallbacks.setSelectedItem(TYPE_PROFILE);

                                  }
                                  mDrawerLayout.closeDrawers();
                              }
                          });

                          break;

                      case TYPE_MYJOBOFFERS: //Il nome nn centra richiama la search per studente

                          v.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {


                                  FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                  Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                  if (!(current instanceof CompanyStudentSearchFragment)) {
                                      //New Fragment
                                      CompanyStudentSearchFragment fragment = CompanyStudentSearchFragment.newInstance();
                                      // Insert the fragment by replacing any existing fragment
                                      // Insert the fragment by replacing any existing fragment

                                      fragmentManager.beginTransaction()
                                              .replace(R.id.tab_Home_container, fragment)
                                              .addToBackStack("Home")
                                              .commit();

                                      // Highlight the selected item, update the title, and close the drawer
                                      // Highlight the selected item, update the title, and close the drawer
                                      TextView tv = (TextView) v.findViewById(R.id.rowText);

                                      toolbar.setTitle(tv.getText());
                                      mDrawerLayout.closeDrawers();
                                      mCallbacks.setSelectedItem(TYPE_MYJOBOFFERS);

                                  }
                                  mDrawerLayout.closeDrawers();
                              }


                          });


                          break;

                      case TYPE_MY_COMPANIES : //NUOVA OFFERTA


                          v.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {


                                  FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                  Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                  if ((current instanceof NewOffer)){
                                      NewOffer o=(NewOffer)current;
                                      if (o.isInEditMode())
                                      {
                                          Toast.makeText(activity,"Please save changes",Toast.LENGTH_SHORT).show();
                                      }
                                      else {

                                          //New Fragment
                                          NewOffer fragment = NewOffer.newInstance(true, true);
                                          // Insert the fragment by replacing any existing fragment
                                          // Insert the fragment by replacing any existing fragment

                                          fragmentManager.beginTransaction()
                                                  .replace(R.id.tab_Home_container, fragment)
                                                  .addToBackStack("Home")
                                                  .commit();

                                          // Highlight the selected item, update the title, and close the drawer
                                          // Highlight the selected item, update the title, and close the drawer
                                          TextView tv = (TextView) v.findViewById(R.id.rowText);

                                          toolbar.setTitle(tv.getText());
                                          mDrawerLayout.closeDrawers();
                                          mCallbacks.setSelectedItem(TYPE_MY_COMPANIES);
                                      }

                              }  else
                                  {
                                      //New Fragment
                                      NewOffer fragment = NewOffer.newInstance(true, true);
                                      // Insert the fragment by replacing any existing fragment
                                      // Insert the fragment by replacing any existing fragment

                                      fragmentManager.beginTransaction()
                                              .replace(R.id.tab_Home_container, fragment)
                                              .addToBackStack("Home")
                                              .commit();

                                      // Highlight the selected item, update the title, and close the drawer
                                      // Highlight the selected item, update the title, and close the drawer
                                      TextView tv = (TextView) v.findViewById(R.id.rowText);

                                      toolbar.setTitle(tv.getText());
                                      mCallbacks.setSelectedItem(TYPE_MY_COMPANIES);
                                  }
                                  mDrawerLayout.closeDrawers();

                              }


                          });


                          break;
                      case  TYPE_MAILBOX :

                          v.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                                  FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                  Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                  if(!(current instanceof MailBoxFragment)) {

                                      Fragment fragment = MailBoxFragment.newInstance();

                                      fragmentManager.beginTransaction()
                                              .replace(R.id.tab_Home_container, fragment)
                                              .commit();

                                      // Highlight the selected item, update the title, and close the drawer
                                      // Highlight the selected item, update the title, and close the drawer
                                      TextView tv = (TextView) v.findViewById(R.id.rowText);

                                      toolbar.setTitle(tv.getText());
                                      mDrawerLayout.closeDrawers();
                                      mCallbacks.setSelectedItem(TYPE_MAILBOX);

                                      //********
                                      //activity.invalidateOptionsMenu();

                                  }
                                }
                          });

                            break;

                      case TYPE_LOGOUT :

                          v.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                                  LogoutDialogFragment fragment = LogoutDialogFragment.newInstance();
                                  fragment.show(activity.getFragmentManager(), "Logout");

                                  mDrawerLayout.closeDrawers();
                              }
                          });


                          break;




                      default: break;
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
    public void onBindViewHolder(menuAdapter.ViewHolder holder, int position) {
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
        else if(position==3) return TYPE_MYJOBOFFERS;
        else if(position==4) return TYPE_MY_COMPANIES;  // for student is autocandidature for companies is new offer
        else if(position==5) return TYPE_MAILBOX;   // after it became mail box
        else if(position==6) return TYPE_LOGOUT;
        return TYPE_HOME;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


   public interface SetSelectedItem{

 void setSelectedItem(int item);

   }

}

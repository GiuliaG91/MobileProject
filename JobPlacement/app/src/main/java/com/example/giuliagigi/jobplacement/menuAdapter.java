package com.example.giuliagigi.jobplacement;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by pietro on 25/04/2015.
 */
public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item

    /* ----- for any user type ------------- */

    private static final int ACTION_PROFILE = 2;
    private static final int ACTION_MAILBOX = 3;

    /* ----- for students ------------- */
    private static final int STUDENT_ACTION_HOME = 1 ;
    private static final int STUDENT_ACTION_LECTURES = 4;
    private static final int STUDENT_ACTION_MAP = 5;
    private static final int STUDENT_ACTION_MYJOBOFFERS = 6;
    private static final int STUDENT_ACTION_MY_COMPANIES = 7;
    private static final int STUDENT_ACTION_LOGOUT = 8;

    /* ----- for companies ------------- */
    private static final int COMPANY_ACTION_HOME = 1 ;
    private static final int COMPANY_ACTION_SEARCH = 4;
    private static final int COMPANY_ACTION_MYOFFERS = 5;
    private static final int COMPANY_ACTION_NEW_OFFER = 6;
    private static final int COMPANY_ACTION_LOGOUT = 7;

    /* ----- for professors ------------- */
    private static final int PROFESSOR_ACTION_HOME = 1 ;
    private static final int PROFESSOR_ACTION_COURSES = 4;
    private static final int PROFESSOR_ACTION_LOGOUT = 5;


    private static final int USER_STUDENT = 1;
    private static final int USER_COMPANY = 2;
    private static final int USER_PROFESSOR = 3;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private TypedArray ICONS;      // Int Array to store the passed icons resource value from MainActivity.java

    private  GlobalData gd;
    private  ParseUserWrapper user;
    private  int userFlag;
    private  FragmentActivity activity;
    private DrawerLayout mDrawerLayout;


    // this class is used to describe the visual content of each item inside the RecyclerView
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


    public menuAdapter(String Titles[], TypedArray Icons, ParseUserWrapper u, FragmentActivity act, DrawerLayout d, GlobalData globalData) {

        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        ICONS = Icons;
        user = u;
        activity = act;
        mDrawerLayout = d;
        gd = globalData;
    }



    // this method creates a new view inside the recycler view. the view is empty at beginning.
    // the proper content will be set with the onBindViewHolder method
    @Override
    public menuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (!(viewType == TYPE_HEADER)) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

            // the View "v" s a row inside the recycler view. depending on "viewType", the corresponding
            // fragment will be inserted


            /* -----------------------------------------------------------------------------------*/
            /* ------------------------- ANY-USER MENU -------------------------------------------*/
            /* -----------------------------------------------------------------------------------*/
            switch (viewType) {

                case ACTION_PROFILE:

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                            if (!(current instanceof ProfileManagement) || gd.getLatestDisplayedUser() != gd.getUserObject()) {

                                Fragment fragment = ProfileManagement.newInstance(true, gd.getUserObject());

                                if (activity instanceof Home)
                                    ((Home) activity).setProfileManagement((ProfileManagement) fragment);

                                //clear backstack
                                int count = fragmentManager.getBackStackEntryCount();
                                for (int i = 0; i < count; ++i) {
                                    fragmentManager.popBackStack();
                                }

                                fragmentManager.beginTransaction()
                                        .replace(R.id.tab_Home_container, fragment)
                                        .commit();

                            }
                            mDrawerLayout.closeDrawers();

                        }
                    });

                    break;

                case ACTION_MAILBOX:

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                            if (!(current instanceof MailBoxDisplayFragment)) {

                                Fragment fragment = MailBoxDisplayFragment.newInstance();

                                //clear backstack
                                int count = fragmentManager.getBackStackEntryCount();
                                for (int i = 0; i < count; ++i) {
                                    fragmentManager.popBackStack();
                                }


                                Fragment f1 = new TabHomeStudentFragment();
                                fragmentManager.beginTransaction().replace(R.id.tab_Home_container, f1).commit();

                                fragmentManager.beginTransaction()
                                        .replace(R.id.tab_Home_container, fragment)
                                        .addToBackStack("Home")
                                        .commit();

                            }
                            mDrawerLayout.closeDrawers();

                        }
                    });
                    break;

                default:
                    break;
            }

            switch (userFlag) {

                /* -------------------------------------------------------------------------------*/
                /* ------------------------- STUDENT MENU ----------------------------------------*/
                /* -------------------------------------------------------------------------------*/
                case USER_STUDENT:

                    switch (viewType) {

                        case STUDENT_ACTION_HOME:
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof TabHomeStudentFragment)) {

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        //New Fragment
                                        TabHomeStudentFragment homeFragment = TabHomeStudentFragment.newInstance();

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, homeFragment)
                                                .commit();

                                    }
                                    mDrawerLayout.closeDrawers();

                                }
                            });

                            break;

                        case STUDENT_ACTION_LECTURES:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof StudentDidacticsManagementFragment)) {

                                        StudentDidacticsManagementFragment fragment = StudentDidacticsManagementFragment.newInstance((Student)gd.getUserObject());

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();

                                    }

                                    mDrawerLayout.closeDrawers();
                                }
                            });

                            break;


                        case STUDENT_ACTION_MAP:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof RoomSearch)) {

                                        Fragment fragment = RoomSearch.newInstance();

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();

                                    }

                                    mDrawerLayout.closeDrawers();
                                }
                            });

                            break;

                        case STUDENT_ACTION_MYJOBOFFERS:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof StudentMyFavouriteOffersFragment)) {


                                        StudentOffersManagementFragment fragment = StudentOffersManagementFragment.newInstance((Student)gd.getUserObject());

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();

                                    }
                                    mDrawerLayout.closeDrawers();
                                }
                            });

                            break;

                        case STUDENT_ACTION_MY_COMPANIES:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof FavCompaniesFragment)) {

                                        //New Fragment
                                        FavCompaniesFragment fragment = FavCompaniesFragment.newInstance();

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();
                                    }
                                    mDrawerLayout.closeDrawers();

                                }


                            });


                            break;


                        case STUDENT_ACTION_LOGOUT:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    openLogoutDialog();
                                }
                            });


                            break;

                        default:
                            break;

                    }

                    break;

                /* -------------------------------------------------------------------------------*/
                /* ------------------------- COMPANY MENU ----------------------------------------*/
                /* -------------------------------------------------------------------------------*/
                case USER_COMPANY:

                    switch (viewType) {

                        case COMPANY_ACTION_HOME:
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof TabHomeCompanyFragment)) {

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        //New Fragment
                                        TabHomeCompanyFragment homeFragment = TabHomeCompanyFragment.newInstance();

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, homeFragment)
                                                .commit();

                                    }
                                    mDrawerLayout.closeDrawers();

                                }
                            });

                            break;

                        case COMPANY_ACTION_SEARCH:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof CompanyStudentSearchFragment)) {
                                        //New Fragment
                                        CompanyStudentSearchFragment fragment = CompanyStudentSearchFragment.newInstance();

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();
                                    }
                                    mDrawerLayout.closeDrawers();

                                }


                            });


                            break;

                        case COMPANY_ACTION_MYOFFERS:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    CompanyShowOffersFragment fragment = CompanyShowOffersFragment.newInstance((Company)gd.getUserObject());

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.tab_Home_container, fragment)
                                            .commit();

                                    mDrawerLayout.closeDrawers();
                                }


                            });


                            break;

                        case COMPANY_ACTION_NEW_OFFER:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    final FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    CompanyEditOfferFragment o = null;
                                    if(current instanceof  CompanyEditOfferFragment)
                                        o = (CompanyEditOfferFragment)current;

                                    // in case we are already creating a new offer or editing an existing one
                                    if (o != null && o.isInEditMode()) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                        View view = activity.getLayoutInflater().inflate(R.layout.warning_message_generic, null);
                                        LinearLayout container = (LinearLayout) view.findViewById(R.id.warning_message_container);

                                        TextView warningMessage = new TextView(activity);
                                        warningMessage.setTextColor(activity.getResources().getColor(R.color.black_light_transparent));
                                        warningMessage.setText(R.string.offer_detail_save_warning);
                                        container.addView(warningMessage);

                                        builder.setView(view);
                                        builder.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                //clear backstack
                                                int count = fragmentManager.getBackStackEntryCount();
                                                for (int i = 0; i < count; ++i) {
                                                    fragmentManager.popBackStack();
                                                }

                                                //New Fragment
                                                CompanyEditOfferFragment fragment = CompanyEditOfferFragment.newInstance(true, new CompanyOffer());

                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.tab_Home_container, fragment)
                                                        .commit();
                                            }
                                        });

                                        builder.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                        builder.create().show();

                                    }
                                    else {

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        //New Fragment
                                        CompanyEditOfferFragment fragment = CompanyEditOfferFragment.newInstance(true, new CompanyOffer());

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, fragment)
                                                .commit();
                                    }

                                    mDrawerLayout.closeDrawers();
                                }


                            });

                            break;

                        case COMPANY_ACTION_LOGOUT:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    openLogoutDialog();
                                }
                            });


                            break;

                        default:
                            break;
                    }

                    break;
                /* -------------------------------------------------------------------------------*/
                /* --------------------- PROFESSOR MENU ------------------------------------------*/
                /* -------------------------------------------------------------------------------*/
                case USER_PROFESSOR:

                    switch (viewType) {

                        case PROFESSOR_ACTION_HOME:
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof TabHomeProfessorFragment)) {

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        //New Fragment
                                        TabHomeProfessorFragment homeFragment = TabHomeProfessorFragment.newInstance();

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, homeFragment)
                                                .commit();

                                    }
                                    mDrawerLayout.closeDrawers();

                                }
                            });

                            break;

                        case PROFESSOR_ACTION_COURSES:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    Fragment current = fragmentManager.findFragmentById(R.id.tab_Home_container);

                                    if (!(current instanceof ProfessorCoursesManagementFragment)) {

                                        //clear backstack
                                        int count = fragmentManager.getBackStackEntryCount();
                                        for (int i = 0; i < count; ++i) {
                                            fragmentManager.popBackStack();
                                        }

                                        //New Fragment
                                        ProfessorCoursesManagementFragment homeFragment = ProfessorCoursesManagementFragment.newInstance((Professor)gd.getUserObject());

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.tab_Home_container, homeFragment)
                                                .commit();

                                    }
                                    mDrawerLayout.closeDrawers();
                                }
                            });

                            break;


                        case PROFESSOR_ACTION_LOGOUT:

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openLogoutDialog();
                                }
                            });
                            break;

                        default:
                            break;
                    }

                    break;

                default:
                    break;

            }

            //Creating MailboxViewHolder and passing the object of type view
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;

        }
        else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating MailboxViewHolder and passing the object of type view
            return vhHeader;
        }
        return null;
    }

    // this method fills the recycler view items with their proper content
    @Override
    public void onBindViewHolder(menuAdapter.ViewHolder holder, int position) {

        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(ICONS.getResourceId(position - 1,0));// Settimg the image with array of our icons

        }
        else {

            /* ----------------- setting the drawer header ---------------------------------- */
            User u = gd.getUserObject();
            Bitmap img = null;

            try{
                img = u.getProfilePhoto();
            }
            catch(Exception e){
                img = null;
            }

            if(img != null){

                holder.profile.setImageBitmap(img);
            }
            else
                holder.profile.setImageResource(R.drawable.ic_profile);

            holder.Name.setText(u.getName());
            holder.email.setText(u.getMail());
            /* ------------------------------------------------------------------------------ */

            if(user.getType().equalsIgnoreCase(User.TYPE_STUDENT))
                userFlag = USER_STUDENT;
            else if(user.getType().equalsIgnoreCase(User.TYPE_COMPANY))
                userFlag = USER_COMPANY;
            else if(user.getType().equalsIgnoreCase(User.TYPE_PROFESSOR))
                userFlag = USER_PROFESSOR;
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void openLogoutDialog(){

        LogoutDialogFragment fragment = LogoutDialogFragment.newInstance();
        fragment.show(activity.getFragmentManager(), "Logout");

        mDrawerLayout.closeDrawers();
    }

}

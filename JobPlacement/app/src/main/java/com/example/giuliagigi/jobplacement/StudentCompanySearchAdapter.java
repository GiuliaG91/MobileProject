package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 14/05/2015.
 */
public class StudentCompanySearchAdapter extends RecyclerView.Adapter<StudentCompanySearchAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<Company> mDataset;
    private GlobalData globalData;
    private Student student;
    private List<Company> supportSet;
    private StudentCompanySearchFragment currentFragment;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;
    private final int pageSize=15;
    private int count=0;

    private ParseQueryAdapter<Company> parseAdapter;
    private ParseQueryAdapter.QueryFactory<Company> factory;

    private ViewGroup parseParent;

    StudentCompanySearchAdapter studentCompanySearchAdapter=this;


    public StudentCompanySearchAdapter(FragmentActivity c, ViewGroup parentIn , StudentCompanySearchFragment fragment,int pos,LinearLayoutManager manager) {
        count=0;
        parseParent = parentIn;
        context = c;
        currentFragment=fragment;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        student = (Student)globalData.getUserObject();
        supportSet = new ArrayList<>(student.getCompanies());
        currentPosition=pos;
        mLayoutManager=manager;

        if(globalData.getCompanyFilterStatus().isValid())
        {
            CompanyFilterStatus status=globalData.getCompanyFilterStatus();
            setFactory(status.getTag_list(), status.getField_list(),status.getLocation_list());
        }
        else {

            factory = new ParseQueryAdapter.QueryFactory<Company>() {
                @Override
                public ParseQuery<Company> create() {
                    ParseQuery query = new ParseQuery("Company");
                    return query;
                }
            };

            setAdapter();
        }

    }

    /************************************************   query adapter things **********************/


    public void setFactory(final List<Tag> tag_list,
                           final List<String> field_list,
                           final List<String> location_list)
    {



        factory=new ParseQueryAdapter.QueryFactory<Company>() {
            @Override
            public ParseQuery<Company> create() {
                ParseQuery query = new ParseQuery("Company");

                if(!tag_list.isEmpty())
                {
                    query.whereContainedIn("tags",tag_list);
                }
                if(!field_list.isEmpty())
                {
                    query.whereContainedIn("field",field_list);
                }
                if (!location_list.isEmpty()) {
                    Integer type = Integer.parseInt(location_list.get(0));
                    Integer distance = Integer.parseInt(location_list.get(1));
                    String nation = location_list.get(2);
                    String city = location_list.get(3);

                    List<Address> addressList = null;

                    Geocoder geocoder = new Geocoder(context);
                    String geoAddress = nation + ", " + city;
                    try {
                        addressList = geocoder.getFromLocationName(geoAddress, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList.isEmpty()) {

                    } else {
                        Address a = addressList.get(0);
                        ParseGeoPoint geoPoint = new ParseGeoPoint(a.getLatitude(), a.getLongitude());

                        if (type == 1) //city in general
                        {
                            //Prendo tutti gli uffici vicini
                            ParseQuery officeQuery=new ParseQuery("Office");
                                officeQuery.whereWithinKilometers("location",geoPoint,100);

                            List<Office> offices=null;

                            try {
                                offices=officeQuery.find();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(offices!=null) {
                                //prendo le company che contengono quegli uffici
                                query.whereContainedIn("offices",offices);
                            }


                        } else if (type == 2) //less then
                        {

                            //Prendo tutti gli uffici vicini
                            ParseQuery officeQuery=new ParseQuery("Office");
                            officeQuery.whereWithinKilometers("location", geoPoint, distance);

                            List<Office> offices=null;

                            try {
                                offices=officeQuery.find();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(offices!=null) {
                                //prendo le company che contengono quegli uffici
                                query.whereContainedIn("offices",offices);
                            }

                        }

                    }
                }

                return query;
            }
        };
    }

    public void setAdapter(){

        mDataset.clear();
        parseAdapter = new ParseQueryAdapter<Company>(context,factory) {


            @Override
            public View getItemView(final Company object, View v, final ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_row, parent, false);
                }
                super.getItemView(object, v, parent);


                mDataset.add(object);
                ImageView logo = (ImageView) v.findViewById(R.id.logo_img);
                TextView companyName = (TextView) v.findViewById(R.id.company_name_tv);
                CheckBox pref = (CheckBox) v.findViewById(R.id.checkbox_favourite);

                Bitmap img=null;
                try{
                    img=object.getProfilePhoto();

                }catch (Exception e){
                    img=null;
                }

                if(img!=null) {
                    logo.setImageBitmap(img);
                }else
                logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
                companyName.setText(object.getName());


                pref.setChecked(false);

                if (supportSet.contains(object)) {
                    pref.setChecked(true);
                }
                pref.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox=(CheckBox)v;

                        if (checkBox.isChecked()) {
                            //add this offer to pref
                            student.addCompany(object);
                            student.saveInBackground();
                            supportSet.add(object);
                        } else {
                            //delete this offer from pref
                            student.removeCompany(object);
                            student.saveInBackground();
                            supportSet.remove(object);

                        }

                    }
                });


                return v;
            }

            @Override
            public View getNextPageView(View v, ViewGroup parent) {
                if (v == null) {
                    // R.layout.adapter_next_page contains an ImageView with a custom graphic
                    // and a TextView.
                    v = View.inflate(getContext(), R.layout.favourites_row, null);
                }
                loadNextPage();
                return v;
            }

        };


        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.setObjectsPerPage(pageSize);
        parseAdapter.loadObjects();


    }



/***************************************************************************************************/
    @Override
    public StudentCompanySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(StudentCompanySearchAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        v.setTag(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        parseAdapter.getView(position, holder.myView, parseParent);
    }

    @Override
    public int getItemCount() {
        return parseAdapter.getCount();
    }

    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();

        //globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        ProfileManagement fragment=ProfileManagement.newInstance(false,mDataset.get(vh.getPosition()));
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

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<Company> {

        public void onLoading() {

        }

        public void onLoaded(List<Company> objects, Exception e) {
            studentCompanySearchAdapter.notifyDataSetChanged();
            if(currentPosition!=0) {
                count += pageSize;
                if (count < currentPosition) {
                    parseAdapter.loadNextPage();
                } else {
                    mLayoutManager.scrollToPosition(currentPosition);
                }
            }
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        View myView;

        public ViewHolder(View v) {
            super(v);
            v.setTag(this);
            myView = v;

        }


    }

}

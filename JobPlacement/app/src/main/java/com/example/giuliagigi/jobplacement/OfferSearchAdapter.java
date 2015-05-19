package com.example.giuliagigi.jobplacement;


import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.ParseGeoPoint;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pietro on 07/05/2015.
 */

public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;
    private Student student;
    private Set<CompanyOffer> supportSet;
    private OfferSearchFragment parent;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;
    private final int pageSize=15;
    private int count=0;


    private ParseQueryAdapter<CompanyOffer> parseAdapter;
    private ParseQueryAdapter.QueryFactory<CompanyOffer> factory;

    private ViewGroup parseParent;
    OfferSearchAdapter offerSearchAdapter = this;


    public OfferSearchAdapter(FragmentActivity c, ViewGroup parentIn, OfferSearchFragment fragment,Integer pos, LinearLayoutManager layoutManager) {
        parseParent = parentIn;
        context = c;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        student = globalData.getStudentFromUser();
        supportSet = new HashSet<>();
        parent=fragment;
        currentPosition=pos;
        mLayoutManager=layoutManager;
        count=0;


        if(globalData.getOfferFilterStatus().isValid())
        {
            OfferFilterStatus status=globalData.getOfferFilterStatus();
            setFactory(status.getTag_list(),status.getContract_list(),status.getTerm_list(),status.getField_list(),status.getLocation_list(),
                        status.getSalary_list());
        }
        else {

            factory = new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
                @Override
                public ParseQuery<CompanyOffer> create() {
                    ParseQuery query = new ParseQuery("CompanyOffer");
                    query.whereEqualTo("publish",true);
                    return query;
                }
            };

        }
     setAdapter();


    }

    public void setFactory(final List<Tag> tag_list,
                           final List<String> contract_list,
                           final List<String> term_list,
                           final List<String> field_list,
                           final List<String> location_list,
                           final List<String> salary_list)
    {



        factory=new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
            @Override
            public ParseQuery<CompanyOffer> create() {
                ParseQuery query = new ParseQuery("CompanyOffer");
                query.whereEqualTo("publish",true);
               if(!tag_list.isEmpty())
                {
                    query.whereContainedIn("tags",tag_list);
                }
                if(!contract_list.isEmpty())
                {
                    query.whereContainedIn("contract",contract_list);
                }
                if(!term_list.isEmpty())
                {
                    query.whereContainedIn("term",term_list);
                }
                if(!field_list.isEmpty())
                {
                    query.whereContainedIn("field",field_list);
                }

                if(!salary_list.isEmpty()) {
                    Integer type = Integer.parseInt(salary_list.get(0));
                    Integer sal = Integer.parseInt(salary_list.get(1));


                    if (type == 1) //less then
                    {
                        query.whereLessThan("salary", sal);

                    } else if (type == 2) //more then
                    {
                        query.whereGreaterThan("salary", sal);
                    } else if (type == 3) //equal to
                    {
                        query.whereEqualTo("salary", sal);

                    }

                    if (!location_list.isEmpty()) {
                        type = Integer.parseInt(location_list.get(0));
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
                            return null;
                        } else {
                            Address a = addressList.get(0);
                            ParseGeoPoint geoPoint = new ParseGeoPoint(a.getLatitude(), a.getLongitude());

                            if (type == 1) //city in general
                            {

                                query.whereNear("location", geoPoint);


                            } else if (type == 2) //less then
                            {
                            } else if (type == 3) //more then
                            {

                            } else if (type == 4) //equal to
                            {

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
        parseAdapter = new ParseQueryAdapter<CompanyOffer>(context,factory) {


            @Override
            public View getItemView(final CompanyOffer object, View v, final ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_row, parent, false);
                }
                super.getItemView(object, v, parent);


                mDataset.add(object);
                ImageView logo = (ImageView) v.findViewById(R.id.logo_img);
                TextView object_tv = (TextView) v.findViewById(R.id.objectOffer_tv);
                TextView descriprion = (TextView) v.findViewById(R.id.description_tv);
                TextView date = (TextView) v.findViewById(R.id.date_row_tv);
                CheckBox pref = (CheckBox) v.findViewById(R.id.star);

              Bitmap img=null;
                //img=object.getCompany().getProfilePhoto();
                if(img!=null) {
                    logo.setImageBitmap(img);
                }else {
                    logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
                }
                object_tv.setText(object.getOfferObject());
                descriprion.setText(object.getDescription());


                String des = object.getDescription();

                if (des == null || des.equals("")) {
                    descriprion.setText("...");
                } else {

                    if (des.length() < 30) {
                        descriprion.setText(des + "...");
                    } else {
                        descriprion.setText(des.substring(0, 29) + "...");
                    }
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String d = dateFormat.format(object.getValidity());
                date.setText(d);

                supportSet.clear();
                supportSet.addAll(student.getFavourites());

                if (supportSet.add(object) == false) {
                    pref.setChecked(true);
                } else {
                    pref.setChecked(false);
                }
                pref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            //add this offer to pref
                            student.addFavourites(object);
                            student.saveInBackground();
                        } else {
                            //delete this offer from pref
                            student.removeFavourites(object);
                            student.saveInBackground();
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

    @Override
    public OfferSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(OfferSearchAdapter.this);

        ViewHolder vh = new ViewHolder(v);
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


        ViewHolder vh = (ViewHolder)v.getTag();

        globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        OfferDetail fragment = OfferDetail.newInstance();
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

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<CompanyOffer> {

        public void onLoading() {

        }

        public void onLoaded(List<CompanyOffer> objects, Exception e) {
            offerSearchAdapter.notifyDataSetChanged();
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
package com.example.giuliagigi.jobplacement;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseGeoPoint;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pietro on 07/05/2015.
 */

public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;
    private Student student;
    private StudentOfferSearchFragment parent;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;
    private final int pageSize=15;
    private int count=0;

    private ParseQueryAdapter<CompanyOffer> parseAdapter;
    private ParseQueryAdapter.QueryFactory<CompanyOffer> factory;

    private ViewGroup parseParent;
    OfferSearchAdapter offerSearchAdapter = this;


    public OfferSearchAdapter(FragmentActivity c, ViewGroup parentIn, StudentOfferSearchFragment fragment,Integer pos, LinearLayoutManager layoutManager) {

        parseParent = parentIn;
        context = c;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        student = (Student)globalData.getUserObject();
        parent=fragment;
        currentPosition=pos;
        mLayoutManager=layoutManager;
        count=0;


        if(globalData.getOfferFilterStatus().isValid()) {

            OfferFilterStatus status = globalData.getOfferFilterStatus();
            setFactory(status.getTag_list(),status.getContract_list(),status.getTerm_list(),status.getField_list(),status.getLocation_list(),
                    status.getSalary_list());
        }
        else {

            factory = new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
                @Override
                public ParseQuery<CompanyOffer> create() {

                    Calendar today= Calendar.getInstance();

                    today.set(Calendar.MILLISECOND, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.HOUR_OF_DAY, 0);

                    ParseQuery query = new ParseQuery("CompanyOffer");
                    query.whereEqualTo(CompanyOffer.STATUS_FIELD,CompanyOffer.STATUS_PUBLISHED);
                    query.whereGreaterThanOrEqualTo("validity", today.getTime());



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



        factory = new ParseQueryAdapter.QueryFactory<CompanyOffer>() {
            @Override
            public ParseQuery<CompanyOffer> create() {

                Calendar today = Calendar.getInstance();
                today.set(Calendar.MILLISECOND, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.setTimeZone(TimeZone.getTimeZone("UTC"));

                //pubblicate e valide + filtri
                ParseQuery query = new ParseQuery("CompanyOffer");
                query.whereEqualTo(CompanyOffer.STATUS_FIELD,CompanyOffer.STATUS_PUBLISHED);
                query.whereGreaterThanOrEqualTo("validity", today.getTime());

                if(!tag_list.isEmpty()) {

                    query.whereContainedIn("tags",tag_list);
                }

                if(!contract_list.isEmpty()) {

                    List<String> newList=new ArrayList<>();
                    for(String s : contract_list) {

                        newList.add(CompanyOffer.getEnglishContractField(s));
                    }

                    query.whereContainedIn("contract",newList);
                }

                if(!term_list.isEmpty()) {

                    List<String> newList=new ArrayList<>();

                    for(String s : term_list) {

                        newList.add(CompanyOffer.getEnglishTermField(s));
                    }

                    query.whereContainedIn("term",term_list);
                }

                if(!field_list.isEmpty()) {
                    List<String> newList=new ArrayList<>();

                    for(String s : field_list) {

                        newList.add(CompanyOffer.getEnglishWorkField(s));
                    }
                    query.whereContainedIn("field",field_list);
                }

                if(!salary_list.isEmpty()) {

                    Integer type = Integer.parseInt(salary_list.get(0));
                    Integer sal = Integer.parseInt(salary_list.get(1));

                    if (type == 1) { //less then

                        query.whereLessThan("salary", sal);
                    }
                    else if (type == 2) { //more then

                        query.whereGreaterThan("salary", sal);
                    }
                    else if (type == 3) { //equal to

                        query.whereEqualTo("salary", sal);
                    }
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
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList.isEmpty()) {

                    }
                    else {

                        Address a = addressList.get(0);
                        ParseGeoPoint geoPoint = new ParseGeoPoint(a.getLatitude(), a.getLongitude());

                        if (type == 1) { //city in general

                            query.whereWithinKilometers("location",geoPoint,100);
                        }
                        else if (type == 2){ //less then

                            query.whereWithinKilometers("location", geoPoint, distance);
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
            public View getItemView(final CompanyOffer offer, View v, final ViewGroup parent) {

                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_row, parent, false);
                }
                super.getItemView(offer, v, parent);


                mDataset.add(offer);
                ImageView logo = (ImageView) v.findViewById(R.id.logo_img);
                TextView object_tv = (TextView) v.findViewById(R.id.textView_title);
                TextView descriprion = (TextView) v.findViewById(R.id.textView_description);
                TextView date = (TextView) v.findViewById(R.id.textView_date);
                CheckBox pref = (CheckBox) v.findViewById(R.id.checkbox_favourite);


                if(offer.getCompany().getProfilePhoto() != null)
                    logo.setImageBitmap(offer.getCompany().getProfilePhoto());

                else
                    logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));


                object_tv.setText(offer.getOfferObject());
                descriprion.setText(offer.getDescription());


                String des = offer.getDescription();

                if (des == null || des.equals("")) {
                    descriprion.setText("...");
                }
                else {

                    if (des.length() < 30) {
                        descriprion.setText(des + "...");
                    }
                    else {
                        descriprion.setText(des.substring(0, 29) + "...");
                    }
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String d = dateFormat.format(offer.getValidity());
                date.setText(globalData.getResources().getString(R.string.new_offer_fragment_expiring_title) + " " + d);
                pref.setChecked(false);


                if (student.getFavouriteOffers().contains(offer)) {
                    pref.setChecked(true);
                }

                pref.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final CheckBox checkBox = (CheckBox)v;

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        TextView message = new TextView(globalData);
                        message.setTextColor(context.getResources().getColor(R.color.black_light_transparent));

                        message.setText(checkBox.isChecked() ? R.string.offer_detail_add_favourite : R.string.offer_detail_remove_favourite);

                        builder.setView(message);
                        builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (checkBox.isChecked()) {

                                    student.addFavouriteOffer(offer);
                                    student.saveInBackground();
                                }
                                else {

                                    ArrayList<CompanyOffer> studentOffers = new ArrayList<CompanyOffer>();
                                    for (StudentApplication a: student.getApplications())
                                        studentOffers.add(a.getOffer());

                                    if(studentOffers.contains(offer)){

                                        Toast.makeText(getContext(), R.string.offer_detail_favourites_warning, Toast.LENGTH_LONG).show();
                                    }
                                    else {

                                        student.removeFavouriteOffer(offer);
                                        student.saveInBackground();
                                    }
                                }
                            }
                        });

                        builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                checkBox.setChecked(!checkBox.isChecked());
                            }
                        });

                        builder.create().show();

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

        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        StudentOfferDetailFragment fragment = StudentOfferDetailFragment.newInstance(mDataset.get(vh.getPosition()), student);
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
                }
                else {

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
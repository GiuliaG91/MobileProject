package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pietro on 07/05/2015.
 */
/*
public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.ViewHolder> implements View.OnClickListener{


    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;
    private Student student;
    private Set<CompanyOffer> supportSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView date;
        private CheckBox pref;



        public ViewHolder(View v) {
            super(v);
           v.setTag(this);
            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.objectOffer_tv);
            descriprion=(TextView)v.findViewById(R.id.description_tv);
            date=(TextView)v.findViewById(R.id.date_row_tv);
            pref=(CheckBox)v.findViewById(R.id.star);
        }
    }

    public OfferSearchAdapter(FragmentActivity c)
    {
        context=c;
        mDataset=new ArrayList<>();
        globalData=(GlobalData)context.getApplication();
        student=globalData.getStudentFromUser();
        supportSet=new HashSet<>();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public  OfferSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
            v.setClickable(true);
            v.setOnClickListener(OfferSearchAdapter.this);

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
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String d=dateFormat.format(mDataset.get(position).getValidity());
        holder.date.setText(d);

       supportSet.clear();
        supportSet.addAll(student.getFavourites());

        if(supportSet.add(mDataset.get(position))==false) {
            holder.pref.setChecked(true);
        }
        else
        {
            holder.pref.setChecked(false);
        }
        holder.pref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    //add this offer to pref
                   student.addFavourites(mDataset.get(position));
                    student.saveInBackground();
                }
                else
                {
                    //delete this offer from pref
                    student.removeFavourites(mDataset.get(position));
                    student.saveInBackground();

                }

            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void updateMyDataset(List<CompanyOffer> offers)
    {
        mDataset.addAll(offers);

    }



    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();

        globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
       //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

            //New Fragment
          OfferDetail fragment=OfferDetail.newInstance();
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


}
*/

public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<CompanyOffer> mDataset;
    private GlobalData globalData;
    private Student student;
    private Set<CompanyOffer> supportSet;


    private ParseQueryAdapter<CompanyOffer> parseAdapter;
    private ParseQueryAdapter.QueryFactory<CompanyOffer> factory;

    private ViewGroup parseParent;
    OfferSearchAdapter offerSearchAdapter = this;


    public OfferSearchAdapter(FragmentActivity c, ViewGroup parentIn) {
        parseParent = parentIn;
        context = c;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        student = globalData.getStudentFromUser();
        supportSet = new HashSet<>();

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

                if(!salary_list.isEmpty())
                {
                    Integer type=Integer.parseInt(salary_list.get(0));
                    Integer sal=Integer.parseInt(salary_list.get(1));


                    if(type==1) //less then
                    {
                        query.whereLessThan("salary", sal);

                    }
                    else if (type==2) //more then
                    {
                      query.whereGreaterThan("salary",sal);
                    }
                    else if(type == 3) //equal to
                    {
                        query.whereEqualTo("salary",sal);


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

             /*  Bitmap img=object.getCompany().getProfilePhoto();
                if(img!=null) {
                    logo.setImageBitmap(img);
                }else*/
                logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
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

        };


        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.setObjectsPerPage(15);
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


        ViewHolder vh=(ViewHolder)v.getTag();

        globalData.setCurrentOffer(mDataset.get(vh.getPosition()));
        //Pass Object to fragment
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        OfferDetail fragment=OfferDetail.newInstance();
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
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

      /*  private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView date;
        private CheckBox pref;

*/

        View myView;

        public ViewHolder(View v) {
            super(v);
            v.setTag(this);
            myView = v;

        /*    logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.objectOffer_tv);
            descriprion=(TextView)v.findViewById(R.id.description_tv);
            date=(TextView)v.findViewById(R.id.date_row_tv);
            pref=(CheckBox)v.findViewById(R.id.star);
        }*/
        }


    }
}
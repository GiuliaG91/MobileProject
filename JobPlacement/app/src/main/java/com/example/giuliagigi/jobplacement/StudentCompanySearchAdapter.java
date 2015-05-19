package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pietro on 14/05/2015.
 */
public class StudentCompanySearchAdapter extends RecyclerView.Adapter<StudentCompanySearchAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<Company> mDataset;
    private GlobalData globalData;
    private Student student;
    private Set<Company> supportSet;
    private StudentCompanySearchFragment currentFragment;

    private ParseQueryAdapter<Company> parseAdapter;
    private ParseQueryAdapter.QueryFactory<Company> factory;

    private ViewGroup parseParent;

    StudentCompanySearchAdapter studentCompanySearchAdapter=this;


    public StudentCompanySearchAdapter(FragmentActivity c, ViewGroup parentIn , StudentCompanySearchFragment fragment) {
        parseParent = parentIn;
        context = c;
        currentFragment=fragment;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        student=globalData.getStudentFromUser();
        supportSet = new HashSet<>();

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
                CheckBox pref = (CheckBox) v.findViewById(R.id.star);

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

                supportSet.clear();
                supportSet.addAll(student.getCompanies());

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
                           student.addCompany(object);
                            student.saveInBackground();
                        } else {
                            //delete this offer from pref
                           student.removeCompany(object);
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

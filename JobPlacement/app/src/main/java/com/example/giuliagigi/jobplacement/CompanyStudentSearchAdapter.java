package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SimpleTimeZone;

/**
 * Created by pietro on 15/05/2015.
 */
public class CompanyStudentSearchAdapter extends RecyclerView.Adapter<CompanyStudentSearchAdapter.ViewHolder> implements View.OnClickListener {



    private FragmentActivity context;
    private ArrayList<Student> mDataset;
    private GlobalData globalData;
    private Company company;
    private Set<Student> supportSet;
    private CompanyStudentSearchFragment currentFragment;
    private Integer currentPosition=0;
    private LinearLayoutManager mLayoutManager;
    private final int pageSize=15;
    private int count=0;

    private ParseQueryAdapter<Student> parseAdapter;
    private ParseQueryAdapter.QueryFactory<Student> factory;

    private ViewGroup parseParent;
    CompanyStudentSearchAdapter companyStudentSearchAdapter=this;


    public CompanyStudentSearchAdapter(FragmentActivity c, ViewGroup parentIn ,CompanyStudentSearchFragment fragment,LinearLayoutManager layoutManager) {
        parseParent = parentIn;
        context = c;
        currentFragment = fragment;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        company = globalData.getCompanyFromUser();
        supportSet = new HashSet<>();
        count = 0;
        mLayoutManager = layoutManager;

        if (globalData.getOfferFilterStatus().isValid()) {
            StudentFilterStatus status = globalData.getStudentFilterStatus();
            setFactory(status.getTag_list(), status.getDegree_list(), status.getField_list());
        } else {

            factory = new ParseQueryAdapter.QueryFactory<Student>() {
                @Override
                public ParseQuery<Student> create() {
                    ParseQuery query = new ParseQuery("Student");
                    return query;
                }
            };

            setAdapter();


        }
    }

    /************************************************   query adapter things **********************/


    public void setFactory(final List<Tag> tag_list,
                           final List<String> degree_list,
                           final List<String> field_list)
    {



        factory=new ParseQueryAdapter.QueryFactory<Student>() {
            @Override
            public ParseQuery<Student> create() {
                ParseQuery query = new ParseQuery("Student");

                if(!tag_list.isEmpty())
                {
                    query.whereContainedIn("tags",tag_list);

                }
                if(!degree_list.isEmpty())
                {
                    String myTypeDegree=degree_list.get(1);
                    int mark=Integer.parseInt(degree_list.get(2));
                        //QUERY SU TUTTE LE LAUREE
                    ParseQuery degreeQuery=new ParseQuery("Degree");
                    List<Degree> degrees=null;

                    try {
                        degrees=degreeQuery.find();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(degrees!=null)
                    {
                        List<Degree> results=new ArrayList<>();
                        for (Degree d : degrees)
                        {
                            int myType=Degree.getTypeID(myTypeDegree);
                            int thisType=Degree.getTypeID(d.getType());

                            if(thisType>myType)
                            {
                                results.add(d);
                            }

                        }

                        query.whereContainedIn("degrees",results);

                    }


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
        parseAdapter = new ParseQueryAdapter<Student>(context,factory) {


            @Override
            public View getItemView(final Student object, View v, final ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row, parent, false);
                }

                    super.getItemView(object, v, parent);


                    mDataset.add(object);
                    ImageView profile = (ImageView) v.findViewById(R.id.profile_img);
                    TextView studentName = (TextView) v.findViewById(R.id.student_name_tv);
                    TextView studentDegree = (TextView) v.findViewById(R.id.student_degree_tv);
                    TextView studentGrade = (TextView) v.findViewById(R.id.student_grade_tv);

                    CheckBox pref = (CheckBox) v.findViewById(R.id.star);

                    Bitmap img = null;
                    try {
                        img = object.getProfilePhoto();

                    } catch (Exception e) {
                        img = null;
                    }

                    if (img != null) {
                        profile.setImageBitmap(img);
                    } else
                        profile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
                    studentName.setText(object.getName());

                    List<Degree> degrees = object.getDegrees();
                    if (!degrees.isEmpty()) {
                        Collections.sort(degrees);

                        studentDegree.setText(degrees.get(0).getType() + " " + degrees.get(0).getStudies());
                        Integer mark = null;
                        try {
                            mark = degrees.get(0).getMark();
                        } catch (Exception e) {
                            mark = null;
                        }
                        if (mark != null) {
                            studentGrade.setText(context.getResources().getString(R.string.Mark) + String.valueOf(mark));
                        } else {
                            studentGrade.setText(context.getResources().getString(R.string.noMark));
                        }
                    } else {
                        studentDegree.setText(context.getResources().getString(R.string.noDegree));
                        studentGrade.setText("");
                    }
                    supportSet.clear();
                    supportSet.addAll(company.getStudents());

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
                                company.addStudent(object);
                                company.saveInBackground();
                            } else {
                                //delete this offer from pref
                                company.removeStudent(object);
                                company.saveInBackground();

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
                    v = View.inflate(getContext(), R.layout.student_row, null);
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
    public CompanyStudentSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(CompanyStudentSearchAdapter.this);

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
        FragmentManager fragmentManager =context.getSupportFragmentManager();

        //New Fragment
       ProfileManagement fragment= ProfileManagement.newInstance(false, mDataset.get(vh.getPosition()));
        // Insert the fragment by replacing any existing fragment
        // Insert the fragment by replacing any existing fragment

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("OfferSearch")
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item, update the title, and close the drawer
        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle(context.getResources().getString(R.string.ToolbarTitleMyStudents));


    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<Student> {

        public void onLoading() {

        }

        public void onLoaded(List<Student> objects, Exception e) {

            companyStudentSearchAdapter.notifyDataSetChanged();
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

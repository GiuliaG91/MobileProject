package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
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
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Silvia on 21/05/2015.
 */

public class Home_tabAdapter extends RecyclerView.Adapter<Home_tabAdapter.ViewHolder> implements View.OnClickListener {


    private FragmentActivity context;
    private ArrayList<News> mDataset;
    private GlobalData globalData;
    private Home_tab parent;
    private Integer currentPosition;
    private LinearLayoutManager mLayoutManager;
    private final int pageSize = 15;
    private int count = 0;


    private ParseQueryAdapter<News> parseAdapter;
    private ParseQueryAdapter.QueryFactory<News> factory;

    private ViewGroup parseParent;
    Home_tabAdapter newsAdapter = this;


    public Home_tabAdapter(FragmentActivity c, ViewGroup parentIn, Home_tab fragment, Integer pos, LinearLayoutManager layoutManager) {
        parseParent = parentIn;
        context = c;
        mDataset = new ArrayList<>();
        globalData = (GlobalData) context.getApplication();
        parent = fragment;
        currentPosition = pos;
        mLayoutManager = layoutManager;
        count = 0;

        setAdapter();

    }

    public void setFactory(){

        factory = new ParseQueryAdapter.QueryFactory<News>() {

            @Override
            public ParseQuery<News> create() {
                ParseQuery query;

                if(globalData.getUserObject() instanceof Student) {

                    ParseQuery<News> query1 = ParseQuery.getQuery("News");
                    query1.whereEqualTo(News.TYPE, 0);

                    ParseQuery<News> query2 = ParseQuery.getQuery("News");
                    query2.whereEqualTo(News.TYPE, 2);
                    query2.whereEqualTo(News.STUDENT, (Student)globalData.getUserObject());

                    ParseQuery<News> query3 = ParseQuery.getQuery("News");
                    query3.whereEqualTo(News.TYPE, 3);

                    List<ParseQuery<News>> queries = new ArrayList<ParseQuery<News>>();
                    queries.add(query1);
                    queries.add(query2);
                    queries.add(query3);

                    query = ParseQuery.or(queries);


                }else{

                    query = new ParseQuery("News");
                    query.whereEqualTo(News.TYPE, 1);
                    query.whereEqualTo(News.COMPANY, (Company)globalData.getUserObject());

                }

                return query;
            }
        };

    }

    public void setAdapter(){

        mDataset.clear();
        setFactory();
        parseAdapter = new ParseQueryAdapter<News>(context, factory) {

            @Override
            public View getItemView(final News object, View v, final ViewGroup parent) {

                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
                }

                super.getItemView(object, v, parent);


                mDataset.add(object);
                CircleImageView icon = (CircleImageView) v.findViewById(R.id.logo_img);
                TextView title = (TextView) v.findViewById(R.id.title_news);
                TextView message = (TextView) v.findViewById(R.id.news_message);
                TextView date = (TextView) v.findViewById(R.id.news_date);

                switch (object.getType()) {

                    case 0: icon.setImageResource(R.drawable.ic_offer);
                            title.setText(context.getResources().getString(R.string.new_job_offer));
                            break;

                    case 1:
                        try {
                            Student student=object.getStudent().fetchIfNeeded();

                        if(student.getProfilePhoto() != null)
                                icon.setImageBitmap(object.getStudent().getProfilePhoto());
                            title.setText(context.getResources().getString(R.string.new_student_applied));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                            break;

                    case 2: try {CompanyOffer offer=object.getCompanyOffer().fetchIfNeeded();
                                Company company=offer.getCompany().fetchIfNeeded();
                        if (company.getProfilePhoto() != null)
                                    icon.setImageBitmap(object.getCompanyOffer().getCompany().getProfilePhoto());
                            }catch(RuntimeException re){
                            } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        OfferStatus status= null;
                        try {
                            status = object.getOfferStatus().fetchIfNeeded();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                            switch (status.getType()) {
                                case OfferStatus.TYPE_ACCEPTED: title.setText(context.getResources().getString(R.string.student_accepted));
                                                                break;
                                case OfferStatus.TYPE_START: title.setText(context.getResources().getString(R.string.student_processing));
                                                             break;
                                case OfferStatus.TYPE_CONSIDERING: title.setText(context.getResources().getString(R.string.student_considering));
                                                                   break;
                                case OfferStatus.TYPE_REFUSED: title.setText(context.getResources().getString(R.string.student_refused));
                                                               break;
                            }
                            break;

                    case 3: try {
                                        Company company=object.getCompany().fetchIfNeeded();

                                if (company.getProfilePhoto() != null)
                                    icon.setImageBitmap(object.getCompany().getProfilePhoto());
                            }catch(RuntimeException re){
                            } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        title.setText(context.getResources().getString(R.string.new_company_signed_up));
                            break;

                    case 4:
                            break;

                    default:

                }

                message.setText(object.getMessage());

                Calendar dateNews = object.getDate();
                Calendar now = Calendar.getInstance();

                if(dateNews.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) && dateNews.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dateNews.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                    date.setText(dateNews.get(Calendar.HOUR_OF_DAY) + ":" + dateNews.get(Calendar.MINUTE));
                else if(dateNews.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                    date.setText(dateNews.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[dateNews.get(Calendar.MONTH)]);
                else
                    date.setText(dateNews.get(Calendar.DAY_OF_MONTH) + "/" + dateNews.get(Calendar.MONTH) + "/" + dateNews.get(Calendar.YEAR));


                return v;
            }

            @Override
            public View getNextPageView(View v, ViewGroup parent) {
                if (v == null) {
                    // R.layout.adapter_next_page contains an ImageView with a custom graphic
                    // and a TextView.
                    v = View.inflate(getContext(), R.layout.news_row, null);
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
    public Home_tabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(Home_tabAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        parseAdapter.getView(position, holder.myView, parseParent);

        holder.type = mDataset.get(position).getType();

    }

    @Override
    public int getItemCount() {
        return parseAdapter.getCount();
    }


    @Override
    public void onClick(View v) {

        ViewHolder vh = (ViewHolder)v.getTag();

        Fragment fragment;

        switch (vh.type){

            case 0: globalData.setCurrentOffer(mDataset.get(vh.getPosition()).getCompanyOffer());

                    FragmentManager fragmentManager = context.getSupportFragmentManager();

                    fragment = OfferDetail.newInstance();

                    fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(globalData.getResources().getString(R.string.job_offer))
                        .commit();

                    globalData.getToolbar().setTitle(globalData.getResources().getString(R.string.job_offer));

                    break;

            case 1: fragmentManager = context.getSupportFragmentManager();

                    fragment = ProfileManagement.newInstance(false, mDataset.get(vh.getPosition()).getStudent());

                    fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(mDataset.get(vh.getPosition()).getStudent().getName() + " " + mDataset.get(vh.getPosition()).getStudent().getSurname() + globalData.getResources().getString(R.string.profile))
                        .commit();

                    globalData.getToolbar().setTitle(mDataset.get(vh.getPosition()).getStudent().getName() + " " + mDataset.get(vh.getPosition()).getStudent().getSurname() + globalData.getResources().getString(R.string.profile));

                    break;

            case 2: globalData.setCurrentOffer(mDataset.get(vh.getPosition()).getCompanyOffer());

                    fragmentManager = context.getSupportFragmentManager();

                    fragment = OfferDetail.newInstance();

                    fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(globalData.getResources().getString(R.string.job_offer))
                        .commit();

                    globalData.getToolbar().setTitle(globalData.getResources().getString(R.string.job_offer));

                    break;

            case 3: fragmentManager = context.getSupportFragmentManager();

                    fragment = ProfileManagement.newInstance(false, mDataset.get(vh.getPosition()).getCompany());

                    fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(globalData.getResources().getStringArray(R.array.Menu_items_Company)[0])
                        .commit();

                    //mDataset.get(vh.getPosition()).getCompany().getName() + " " + globalData.getResources().getString(R.string.profile)

                    //globalData.getToolbar().setTitle(mDataset.get(vh.getPosition()).getCompany().getName() + " " + globalData.getResources().getString(R.string.profile));

                    break;

        }

    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<News> {

        public void onLoading() {

        }

        public void onLoaded(List<News> objects, Exception e) {

            orderMyDataset();

            newsAdapter.notifyDataSetChanged();

            /*
            if(currentPosition!=0) {
                count += pageSize;
                if (count < currentPosition) {
                    parseAdapter.loadNextPage();
                } else {
                    mLayoutManager.scrollToPosition(currentPosition);
                }
            }
            */
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        View myView;
        int type;

        public ViewHolder(View v) {
            super(v);
            v.setTag(this);
            myView = v;

        }

    }

    public void orderMyDataset(){

        for(int i = 0; i < mDataset.size()-1; i++)
            for(int j = i+1; j < mDataset.size()-1; j++)
                if(mDataset.get(i).getDate().after(mDataset.get(j).getDate())) {
                    News tmp = mDataset.get(i);
                    mDataset.remove(i);
                    mDataset.add(i, mDataset.get(j));
                    mDataset.remove(j);
                    mDataset.add(j, tmp);
                }


    }


}
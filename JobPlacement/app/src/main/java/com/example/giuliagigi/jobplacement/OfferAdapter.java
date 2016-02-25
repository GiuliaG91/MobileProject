package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 12/05/2015.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> implements View.OnClickListener{

    public static final int MODE_STUDENT_VIEW = 0;
    public static final int MODE_COMPANY_VIEW = 1;
    public static final int MODE_SIMPLE_VIEW = 2;


    private GlobalData globalData;
    private FragmentActivity context;

    private ArrayList<CompanyOffer> offers;
    private Student student = null;
    private Company company = null;
    private int mode;

    private Integer currentPosition = 0;
    private LinearLayoutManager mLayoutManager;


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- VIEW HOLDER --------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView validity;
        private CheckBox favourite;

        public ViewHolder(View v) {

            super(v);
            v.setTag(this);
            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.textView_title);
            descriprion=(TextView)v.findViewById(R.id.textView_description);
            favourite = (CheckBox)v.findViewById(R.id.checkbox_favourite);
            validity = (TextView)v.findViewById(R.id.textView_date);
        }
    }

    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTOR --------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    public OfferAdapter(FragmentActivity c,
                        ArrayList<CompanyOffer> offers,
                        int mode,
                        Integer pos,
                        LinearLayoutManager linearLayoutManager){

        context = c;
        globalData = (GlobalData)context.getApplication();
        currentPosition = pos;
        mLayoutManager = linearLayoutManager;

        this.offers = offers;
        this.mode = mode;

        switch (mode){

            case MODE_STUDENT_VIEW:

                student = (Student)globalData.getUserObject();
                break;

            case MODE_COMPANY_VIEW:

                company = (Company)globalData.getUserObject();
                break;

            default:

                break;
        }

    }


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    // Create new views (invoked by the layout manager)
    @Override
    public  OfferAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(OfferAdapter.this);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        try {
            Company publisher = offers.get(position).getCompany().fetchIfNeeded();

            if(publisher.getProfilePhoto() != null)
                holder.logo.setImageBitmap(publisher.getProfilePhoto());
            else
                holder.logo.setImageResource(R.drawable.ic_profile);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }



        holder.object.setText(offers.get(position).getOfferObject());
        String description = offers.get(position).getDescription();

        if(description == null || description.isEmpty() || description.equals("")) {

            holder.descriprion.setText("...");
        }
        else{

            if(description.length()<30) {

                holder.descriprion.setText(description+"...");
            }
            else {

                holder.descriprion.setText(offers.get(position).getDescription().substring(0,29)+"...");
            }
        }

        holder.favourite.setEnabled(mode == MODE_STUDENT_VIEW);
        holder.favourite.setVisibility(mode == MODE_STUDENT_VIEW ? View.VISIBLE : View.INVISIBLE);
        holder.favourite.setChecked(true);

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CheckBox checkBox = (CheckBox)v;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                TextView message = new TextView(globalData);
                message.setTextColor(context.getResources().getColor(R.color.black_light_transparent));

                message.setText(R.string.offer_detail_remove_favourite);

                builder.setView(message);
                builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ArrayList<CompanyOffer> studentOffers = new ArrayList<CompanyOffer>();
                        for (StudentApplication a: student.getApplications())
                            studentOffers.add(a.getOffer());

                        if(studentOffers.contains(offers.get(position))){

                            Toast.makeText(context, R.string.offer_detail_favourites_warning, Toast.LENGTH_LONG).show();
                        }
                        else {

                            student.removeFavouriteOffer(offers.get(position));
                            student.saveInBackground();
                            OfferAdapter.this.notifyDataSetChanged();
                        }
                    }
                });

                builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkBox.setChecked(true);
                    }
                });

                builder.create().show();

            }
        });


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(offers.get(position).getValidity());
        holder.validity.setText(date);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }


//    public void updateDataSet(List<CompanyOffer> off) {
//
//        offers.addAll(off);
//        notifyDataSetChanged();
//
//        if(currentPosition!=0)
//            mLayoutManager.scrollToPosition(currentPosition);
//    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- ITEM CLICK -----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {


        ViewHolder vh=(ViewHolder)v.getTag();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Fragment fragment = null;

        if(mode == MODE_STUDENT_VIEW)
            fragment = StudentOfferDetailFragment.newInstance(offers.get(vh.getPosition()), (Student)globalData.getUserObject());

        else if(mode == MODE_COMPANY_VIEW)
            fragment = CompanyEditOfferFragment.newInstance(false,offers.get(vh.getPosition()));


        if(fragment == null){

            Toast.makeText(context, "We are sorry. An application error occurred. Impossible to perform operation", Toast.LENGTH_LONG).show();
            return;
        }

        //clear backstack
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fragmentManager.popBackStack();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("PrefView")
                .commit();


        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle("Offer");
    }




}

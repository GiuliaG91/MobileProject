package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferDetail extends  Fragment {

    View root;
    CompanyOffer offer;
    GlobalData globalData;
    Company company;
    FragmentActivity activity;


    // TODO: Rename and change types and number of parameters
    public static OfferDetail newInstance() {
        OfferDetail fragment = new OfferDetail();
        return fragment;
    }

    public OfferDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData=(GlobalData)getActivity().getApplication();
        offer=globalData.getCurrentViewOffer();
        globalData.getToolbar().setTitle(getResources().getString(R.string.ToolbarTilteOffer));
        if(offer==null)
        {
            getFragmentManager().popBackStackImmediate();
        }

        try {
            company=offer.getCompany().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       root=inflater.inflate(R.layout.offer_layout,container,false);
            activity=getActivity();
        //set object
        //todo mettere hint come stringhe
        LinearLayout linearLayout=(LinearLayout)root.findViewById(R.id.object_row);
        TextView hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        TextView content=(TextView)linearLayout.findViewById(R.id.content_tv);
        ImageView icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Object");
        content.setText(offer.getOfferObject());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_objectoffer));

        //set mission
         linearLayout=(LinearLayout)root.findViewById(R.id.mission_row);
         hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
         content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Mission");
        content.setText(offer.getOfferObject());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mission));

        //set field
        linearLayout=(LinearLayout)root.findViewById(R.id.field_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Field");
        content.setText(offer.getWorkField());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_field));

        //set places
        linearLayout=(LinearLayout)root.findViewById(R.id.places_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Places");
        content.setText(String.valueOf(offer.getnPositions()));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_places));

        //set validity
        linearLayout=(LinearLayout)root.findViewById(R.id.validity_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Validity");
        SimpleDateFormat dateformat=new SimpleDateFormat("dd/MM/yyyy");
        String date=dateformat.format(offer.getValidity());
        content.setText(date);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_validity));

        //set contract
        linearLayout=(LinearLayout)root.findViewById(R.id.contract_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Contract");
        content.setText(offer.getContract());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_contract));

        //set term
        linearLayout=(LinearLayout)root.findViewById(R.id.term_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Term");
        content.setText(offer.getTerm());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_term));

        //set location
        linearLayout=(LinearLayout)root.findViewById(R.id.location_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Location");
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location));

        if(offer.getLocation()!=null && !offer.getLocation().equals(""))
        content.setText(offer.getOfferObject());
        else content.setText("To be defined");

        //set salary
        linearLayout=(LinearLayout)root.findViewById(R.id.salary_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText("Salary:");
        Integer salary=offer.getSAlARY();
        if(salary!=-1) {
            content.setText(String.valueOf(salary).trim());
        }
        else
        {
            content.setText("To be defined");
        }
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_salary));

        ImageView logo=(ImageView)root.findViewById(R.id.circleView_logo);
        TextView companyName=(TextView)root.findViewById(R.id.company_name_tv);
        TextView companyMail=(TextView)root.findViewById(R.id.Company_email);

        TextView description=(TextView)root.findViewById(R.id.DescriptionText_tv);
        description.setText(offer.getDescription());

        Bitmap img=null;
        try {
          img = company.getProfilePhoto();
        }catch (Exception e)
        {img =null;}
        if(img!=null)
        {
            logo.setImageBitmap(img);
        }
        companyName.setText(company.getName());
        companyMail.setText(company.getMail());


        /*Attach on click listener to button menu */

        final FloatingActionButton applyButton=(FloatingActionButton)root.findViewById(R.id.action_apply);
        applyButton.setIcon(R.drawable.ic_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student s = globalData.getStudentFromUser();
                // if is possible perform apply
                if (!offer.getStudents().contains(s)) {
                    if (offer.getnPositions() > 0 && offer.getValidity().after(Calendar.getInstance().getTime())) {
                        //Set apply

                        offer.addStudent(s);
                        offer.saveInBackground();
                        OfferStatus offerStatus=new OfferStatus();
                        offerStatus.setOffer(offer);
                        offerStatus.setStudent(s);
                        offerStatus.saveInBackground();
                        //todo notify company

                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();

                        News news = new News();
                        news.createNews(1, offer, (Student) globalData.getUserObject(), null, globalData);


                    } else {
                        Toast.makeText(getActivity(), "Can't perform apply", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "You are already applied", Toast.LENGTH_SHORT).show();
                }

                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions)).collapse();
            }
        });

        final FloatingActionButton contactButton=(FloatingActionButton)root.findViewById(R.id.action_contact_company);
        contactButton.setIcon(R.drawable.ic_mail);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Bundle data = new Bundle();

                ArrayList<String> recipients = new ArrayList<String>();
                recipients.add(company.getMail());
                data.putStringArrayList(MailBoxDetailFragment.RECIPIENTS_KEY, recipients);

                Fragment fragment = MailBoxNewFragment.newInstance(data);

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(activity.getResources().getString(R.string.new_message_toolbar_title))
                        .commit();

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
                toolbar.setTitle(activity.getResources().getString(R.string.new_message_toolbar_title));

                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions)).collapse();
            }
        });

        ((Toolbar)activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.job_offer));

        return root;
    }


}

package com.example.giuliagigi.jobplacement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferDetail extends Fragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData=(GlobalData)getActivity().getApplication();
        offer=globalData.currentViewOffer;
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
        ((Home)activity).setOnBackPressedListener(new BaseBackPressedListener(activity));
        //set object
        //todo mettere hint come stringhe
        LinearLayout linearLayout=(LinearLayout)root.findViewById(R.id.object_row);
        TextView hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        TextView content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Object");
        content.setText(offer.getOfferObject());

        //set mission
         linearLayout=(LinearLayout)root.findViewById(R.id.mission_row);
         hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
         content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Mission");
        content.setText(offer.getOfferObject());

        //set field
        linearLayout=(LinearLayout)root.findViewById(R.id.field_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Field");
        content.setText(offer.getWorkField());

        //set places
        linearLayout=(LinearLayout)root.findViewById(R.id.places_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Places");
        content.setText(String.valueOf(offer.getnPositions()));

        //set validity
        linearLayout=(LinearLayout)root.findViewById(R.id.validity_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Validity");
        SimpleDateFormat dateformat=new SimpleDateFormat("dd/MM/yyyy");
        String date=dateformat.format(offer.getValidity());
        content.setText(date);

        //set contract
        linearLayout=(LinearLayout)root.findViewById(R.id.contract_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Contract");
        content.setText(offer.getContract());

        //set term
        linearLayout=(LinearLayout)root.findViewById(R.id.term_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Term");
        content.setText(offer.getTerm());

        //set location
        linearLayout=(LinearLayout)root.findViewById(R.id.location_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        hint.setText("Location");

        if(offer.getLocation()!=null && !offer.getLocation().equals(""))
        content.setText(offer.getOfferObject());
        else content.setText("To be defined");

        //set salary
        linearLayout=(LinearLayout)root.findViewById(R.id.salary_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);

        content.setText(offer.getSAlARY());


        ImageView logo=(ImageView)root.findViewById(R.id.circleView_logo);
        TextView companyName=(TextView)root.findViewById(R.id.company_name_tv);
        TextView companyMail=(TextView)root.findViewById(R.id.company_mail);

        TextView description=(TextView)root.findViewById(R.id.DescriptionText_tv);
        description.setText(offer.getDescription());

        companyName.setText(company.getName());
        companyName.setText(company.getMail());


        return root;
    }


}

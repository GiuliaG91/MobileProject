package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;


public class CompanyProfileManagementOfficeFragment extends ProfileManagementFragment{

    private static final String TITLE = "Office";

    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_CITY = "bundle_city";
    private static String BUNDLE_ADDRESS = "bundle_address";
    private static String BUNDLE_CAP = "bundle_cap";
    private static String BUNDLE_NATION = "bundle_nation";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private Company currentUser;
    private Spinner officeType;
    private GeoLocalization geoloc;
    private EditText officeCity, officeAddress, officeCAP, officeNation;
    private GoogleMap map;
    SupportMapFragment mapFragment;

    Button delete;
    private Office office;
    private boolean isRemoved;

    public CompanyProfileManagementOfficeFragment() {
        super();
    }
    public static CompanyProfileManagementOfficeFragment newInstance(Office office) {
        CompanyProfileManagementOfficeFragment fragment = new CompanyProfileManagementOfficeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setOffice(office);
        return fragment;
    }

    public void setOffice(Office office){
        this.office = office;
    }


    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        currentUser = (Company)application.getUserObject();
        isListenerAfterDetach = true;
        isRemoved = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int type;
        String city, address, cap, nation;
        if(getArguments().getBoolean(BUNDLE_HASCHANGED)){

            type = getArguments().getInt(BUNDLE_TYPE);
            city = getArguments().getString(BUNDLE_CITY);
            address = getArguments().getString(BUNDLE_ADDRESS);
            cap = getArguments().getString(BUNDLE_CAP);
            nation = getArguments().getString(BUNDLE_NATION);
        }
        else {

            if(office.getOfficeType()!= null)
                type = Office.getTypeID(office.getOfficeType());
            else
                type = 0;

            if(office.getOfficeCity()!= null)
                city = office.getOfficeCity();
            else
                city = null;

            if(office.getOfficeAddress()!= null)
                address = office.getOfficeAddress();
            else
                address = null;

            if(office.getOfficeCAP()!=null)
                cap = office.getOfficeCAP();
            else
                cap = null;

            if(office.getOfficeNation()!= null)
                nation = office.getOfficeNation();
            else
                nation = null;


        }

        root = inflater.inflate(R.layout.fragment_office_management, container, false);




        officeType = (Spinner)root.findViewById(R.id.office_management_spinnerType);

        officeType.setAdapter(new StringAdapter(Office.TYPES));
        officeType.setSelection(type);


        delete = (Button)root.findViewById(R.id.office_management_delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    office.delete();
                    isRemoved = true;
                    currentUser.removeOffice(office);
                    currentUser.saveEventually();
                    root.setVisibility(View.INVISIBLE);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "unable to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        officeCity = (EditText)root.findViewById(R.id.office_city_area);
        if(city != null)
            officeCity.setText(city);
        else
            officeCity.setText(INSERT_FIELD);

        textFields.add(officeCity);

        officeAddress = (EditText)root.findViewById(R.id.office_address_area);
        if(address != null)
            officeAddress.setText(address);
        else
            officeAddress.setText(INSERT_FIELD);

        textFields.add(officeAddress);

        officeCAP = (EditText)root.findViewById(R.id.office_CAP_area);
        if(cap != null)
            officeCAP.setText(cap);
        else
            officeCAP.setText(INSERT_FIELD);

        textFields.add(officeCAP);

        officeNation = (EditText)root.findViewById(R.id.office_nation_area);
        if(nation != null)
            officeNation.setText(nation);
        else
            officeNation.setText(INSERT_FIELD);

        textFields.add(officeNation);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et:textFields)
            et.addTextChangedListener(hasChangedListener);

        geoloc = (GeoLocalization)getChildFragmentManager().findFragmentById(R.id.office_map_fragment);
        if(geoloc == null)
            Log.println(Log.ASSERT,"OFFICE FRAG", "geoloc is null");

        return root;
    }


    @Override
    public void onDetach() {

        super.onDetach();

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){

            getArguments().putInt(BUNDLE_TYPE,officeType.getSelectedItemPosition());
            getArguments().putString(BUNDLE_CITY, officeCity.getText().toString());
            getArguments().putString(BUNDLE_ADDRESS,officeAddress.getText().toString());
            getArguments().putString(BUNDLE_CAP,officeCAP.getText().toString());
            getArguments().putString(BUNDLE_NATION,officeNation.getText().toString());
        }

    }



    /* ------------------- AUXILIARY METHODS -----------------------------------------------------*/

    @Override
    public void saveChanges(){

        super.saveChanges();

        if(!isRemoved){

            office.setOfficeType((String) officeType.getSelectedItem());
            if(!officeCity.getText().toString().equals(INSERT_FIELD)) office.setOfficeCity(officeCity.getText().toString());
            if(!officeAddress.getText().toString().equals(INSERT_FIELD)) office.setOfficeAddress(officeAddress.getText().toString());
            if(!officeCAP.getText().toString().equals(INSERT_FIELD)) office.setOfficeCAP(officeCAP.getText().toString());
            if(!officeNation.getText().toString().equals(INSERT_FIELD)) office.setOfficeNation(officeNation.getText().toString());
            office.setCompany(currentUser);
            office.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e==null){

                        Log.println(Log.ASSERT,"OFFICE FRAG", "office saved. Updating company");
                        currentUser.addOffice(office);
                        currentUser.saveEventually();

                        /* convert to geoLocation */
                        if(!officeNation.getText().toString().equals(INSERT_FIELD) && !officeCity.getText().toString().equals(INSERT_FIELD)){

                            String geoAddress = officeNation.getText().toString() + ", " + officeCity.getText().toString();
                            if(!officeAddress.getText().toString().equals(INSERT_FIELD))
                                geoAddress += ", " + officeAddress.getText().toString();

                            setMapLocation(geoAddress);
                        }
                        else
                            Toast.makeText(getActivity(),"You must specify at least office country and city to get a physic location", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Log.println(Log.ASSERT,"OFFICE FRAG","error saving office");

                }
            });


        }

    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        officeType.setEnabled(enable);
        officeCity.setEnabled(enable);
        officeAddress.setEnabled(enable);
        officeCAP.setEnabled(enable);
        officeNation.setEnabled(enable);

        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;


        delete.setVisibility(visibility);
    }


    private void setMapLocation(String geoAddress){

        List<Address> addressList;
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            addressList = geocoder.getFromLocationName(geoAddress,5);
        } catch (IOException e) {
            e.printStackTrace();
            Log.println(Log.ASSERT,"OFFICE FRAG", "IOException during setMapLocation");
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Address[] addressArray = new Address[addressList.size()];
        int i=0;
        for(Address a:addressList)
            addressArray[i++] = a;

        ListView addressListView = new ListView(getActivity());
        final ArrayAdapter<Address> adapter = new ArrayAdapter<Address>(getActivity(),android.R.layout.simple_dropdown_item_1line,addressArray);
        addressListView.setAdapter(adapter);
        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Address selected = adapter.getItem(position);
                geoloc.setMarkerPosition(new LatLng(selected.getLatitude(), selected.getLongitude()));
            }
        });

        builder.setTitle("Select a location for " + officeCity.getText().toString() + " office");
        builder.setView(addressListView);
        builder.create().show();
    }
}

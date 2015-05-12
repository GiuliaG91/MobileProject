package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;


public class CompanyProfileManagementOfficeFragment extends ProfileManagementFragment
        implements OnMapReadyCallback{

    private static final String TITLE = "Office";

    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_CITY = "bundle_city";
    private static String BUNDLE_ADDRESS = "bundle_address";
    private static String BUNDLE_CAP = "bundle_cap";
    private static String BUNDLE_NATION = "bundle_nation";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private Company currentUser;
    private Spinner officeType;
    private EditText officeCity, officeAddress, officeCAP, officeNation;
    private GoogleMap mMap;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        GoogleMap googleMap = mapFragment.getMap();




        //Intent searchAddress = new  Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
        //startActivity(searchAddress);


//        setEnable(host.isEditMode());
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

    @Override
    public void saveChanges(){

        super.saveChanges();

        if(!isRemoved){

            currentUser.addOffice(office);
            currentUser.saveEventually();

            office.setOfficeType((String) officeType.getSelectedItem());
            if(!officeCity.getText().toString().equals(INSERT_FIELD)) office.setOfficeCity(officeCity.getText().toString());
            if(!officeAddress.getText().toString().equals(INSERT_FIELD)) office.setOfficeAddress(officeAddress.getText().toString());
            if(!officeCAP.getText().toString().equals(INSERT_FIELD)) office.setOfficeCAP(officeCAP.getText().toString());
            if(!officeNation.getText().toString().equals(INSERT_FIELD)) office.setOfficeNation(officeNation.getText().toString());
            office.setCompany(currentUser);
            office.saveEventually();
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


    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.IOException;


public class CompanyProfileManagementOfficeFragment extends ProfileManagementFragment implements OnMapReadyCallback{

    private static final String TITLE = "Office";

    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_CITY = "bundle_city";
    private static String BUNDLE_ADDRESS = "bundle_address";
    private static String BUNDLE_CAP = "bundle_cap";
    private static String BUNDLE_NATION = "bundle_nation";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private Spinner officeType;
    private GeoLocalization geoloc;
    private EditText officeCity, officeAddress, officeCAP, officeNation;
    Button delete;

    private Office office;
    private Company company;

    private boolean addressChanged;
    private boolean isRemoved;

    public CompanyProfileManagementOfficeFragment() {
        super();
    }
    public static CompanyProfileManagementOfficeFragment newInstance(Office office, Company company) {
        CompanyProfileManagementOfficeFragment fragment = new CompanyProfileManagementOfficeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setOffice(office);
        fragment.setCompany(company);
        return fragment;
    }

    public void setOffice(Office office){
        this.office = office;
    }

    public void setCompany(Company company){

        this.company = company;
    }


    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isNestedFragment = true;
        isRemoved = false;
        addressChanged = false;
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

            hasChanged = true;
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
                    company.removeOffice(office);
                    company.saveEventually();
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

        OnAddressChangedListener addressListener = new OnAddressChangedListener();
        officeAddress.addTextChangedListener(addressListener);
        officeCity.addTextChangedListener(addressListener);
        officeNation.addTextChangedListener(addressListener);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.println(Log.ASSERT,"OFFICE FRAG", "onViewCreated");

        geoloc = (GeoLocalization)getChildFragmentManager().findFragmentById(R.id.office_map_fragment);
        geoloc.setOnMapReadyCallback(this);
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
            office.setCompany(company);

            if(addressChanged &&
                    !officeNation.getText().toString().equals(INSERT_FIELD) &&
                    !officeCity.getText().toString().equals(INSERT_FIELD)){

                String geoAddress = officeNation.getText().toString() + ", " + officeCity.getText().toString();
                if(!officeAddress.getText().toString().equals(INSERT_FIELD))
                    geoAddress += ", " + officeAddress.getText().toString();
                else
                    Toast.makeText(getActivity(),"To get a better geographical localization, consider adding your address",Toast.LENGTH_SHORT).show();

                try {

                    Address a = geoloc.setMapLocation(geoAddress);
                    if(a!=null)
                        office.setLocation(new ParseGeoPoint(a.getLatitude(),a.getLongitude()));
                    else {
                        Toast.makeText(getActivity(),"Unfortunately, your addres doesn't match with a Google Maps address",Toast.LENGTH_SHORT).show();
                        office.setLocation(null);
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Due to an error, it was not possible to save your geographical location",Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getActivity(),"You must specify at least office country and city to get a physic location", Toast.LENGTH_SHORT).show();


            Log.println(Log.ASSERT,"OFFICE FRAG", "proceed saving office...");
            office.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Log.println(Log.ASSERT, "OFFICE FRAG", "office saved. Updating company");
                        company.addOffice(office);
                        company.saveEventually();
                    } else
                        Log.println(Log.ASSERT, "OFFICE FRAG", "error saving office");

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



    /* ------------------------------ MAPS METHODS ------------------------------------------------*/


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.println(Log.ASSERT,"OFFICE FRAG", "Map was created. Setting marker position: " + office.getOfficeAddress() + ", " + office.getOfficeCity());

        if(office.getLocation()!=null)
            geoloc.setMarkerPosition(new LatLng(office.getLocation().getLatitude(),office.getLocation().getLongitude()));
    }


    private class OnAddressChangedListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            hasChanged = true;
            addressChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}

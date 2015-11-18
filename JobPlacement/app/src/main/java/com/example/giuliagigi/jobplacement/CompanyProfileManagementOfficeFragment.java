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
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.IOException;


public class CompanyProfileManagementOfficeFragment extends ProfileManagementFragment implements OnMapReadyCallback{

    private static final String TITLE = GlobalData.getContext().getString(R.string.profile_office_tab);


    public static final String BUNDLE_IDENTIFIER = "COMPANYPROFILEOFFICE";


    private static final String BUNDLE_COMPANY = "bundle_company";
    private static final String BUNDLE_OFFICE = "bundle_office";
    private static final String BUNDLE_TYPE = "bundle_type";
    private static final String BUNDLE_CITY = "bundle_city";
    private static final String BUNDLE_ADDRESS = "bundle_address";
    private static final String BUNDLE_CAP = "bundle_cap";
    private static final String BUNDLE_NATION = "bundle_nation";
    private static final String BUNDLE_PARENT = "bundle_parent";

    private Spinner officeType;
    private GeoLocalization geoloc;
    private EditText officeCity, officeAddress, officeCAP, officeNation;
    Button delete;

    private OfficeFragmentInterface parent;
    private Office office;
    private Company company;

    private boolean addressChanged;
    private boolean isRemoved;

    int type;
    String city, address, cap, nation, parentName;
    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- CONTRUCTORS -----------------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public CompanyProfileManagementOfficeFragment() {
        super();
    }

    public static CompanyProfileManagementOfficeFragment newInstance(OfficeFragmentInterface parent, Office office, Company company) {

        CompanyProfileManagementOfficeFragment fragment = new CompanyProfileManagementOfficeFragment();

        fragment.setOffice(office);
        fragment.setCompany(company);
        fragment.setUser(company);
        fragment.parent = parent;

        ProfileManagementFragment f = (ProfileManagementFragment)parent;
        fragment.parentName = f.bundleIdentifier();

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

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }
    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- STANDARD CALLBACKS ----------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isRemoved = false;
        addressChanged = false;

        if(office != null){

            if(office.getOfficeType() != null)
                type = Office.getTypeID(office.getOfficeType());
            city = office.getOfficeCity();
            address = office.getOfficeAddress();
            cap = office.getOfficeCAP();
            nation = office.getOfficeNation();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_office_management, container, false);

        officeType = (Spinner)root.findViewById(R.id.office_management_spinnerType);
        officeType.setAdapter(new StringAdapter(Office.TYPES));
        officeType.setSelection(type);

        delete = (Button)root.findViewById(R.id.office_management_delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(office.getObjectId() != null){

                    office.deleteEventually(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e == null){

                                company.removeOffice(office);
                                company.saveEventually();
                                root.setVisibility(View.INVISIBLE);
                                parent.onOfficeDelete(CompanyProfileManagementOfficeFragment.this);
                                isRemoved = true;
                            }
                            else {

                                Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.profile_object_delete_failure), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    root.setVisibility(View.INVISIBLE);
                    parent.onOfficeDelete(CompanyProfileManagementOfficeFragment.this);
                    isRemoved = true;
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

        geoloc = (GeoLocalization)getChildFragmentManager().findFragmentById(R.id.office_map_fragment);
        geoloc.setOnMapReadyCallback(this);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }



    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- AUXILIARY METHODS -----------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    protected void saveStateInBundle(Bundle outstate) {
        super.saveStateInBundle(outstate);

        bundle.put(BUNDLE_COMPANY, company);
        bundle.put(BUNDLE_OFFICE, office);
        bundle.putString(BUNDLE_CITY,city);
        bundle.putString(BUNDLE_CAP,cap);
        bundle.putString(BUNDLE_ADDRESS,address);
        bundle.putString(BUNDLE_NATION,nation);
        bundle.putInt(BUNDLE_TYPE,type);
        bundle.putString(BUNDLE_PARENT,parentName);
    }


    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);


        if(bundle != null){

            company = (Company)bundle.get(BUNDLE_COMPANY);
            office = (Office)bundle.get(BUNDLE_OFFICE);
            city = bundle.getString(BUNDLE_CITY);
            address = bundle.getString(BUNDLE_ADDRESS);
            cap = bundle.getString(BUNDLE_CAP);
            nation = bundle.getString(BUNDLE_NATION);
            type = bundle.getInt(BUNDLE_TYPE);

            parentName = bundle.getString(BUNDLE_PARENT);
            parent = (OfficeFragmentInterface)application.getFragment(parentName);
        }
    }

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


    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- PARENT FRAGMENT INTERFACE ---------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public interface OfficeFragmentInterface{

        public void onOfficeDelete(CompanyProfileManagementOfficeFragment toRemove);
    }

    /* -------------------------------------------------------------------------------------------*/
    /* ------------------------------ MAPS METHODS ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

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

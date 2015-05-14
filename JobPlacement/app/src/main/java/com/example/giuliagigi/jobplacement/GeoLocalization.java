package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GeoLocalization extends SupportMapFragment{

    static final LatLng TutorialsPoint = new LatLng(45 , 8);
    private GoogleMap googleMap;
    private View root;
    protected GlobalData application;
    private Company currentUser;
    private double longitude;
    private double latitude;
    private LatLng position;
    private Office office;


    public GeoLocalization(){
        super();
    }
    public static GeoLocalization newInstance(Office office) {
        GeoLocalization fragment = new GeoLocalization();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setOffice(office);
        return fragment;
    }

    public void setOffice(Office office){
        this.office = office;
    }



    /* ----------------- STANDARD CALLBACKS ------------------------------------------------------*/

    public void onAttach(Activity activity) {

        super.onAttach(activity);
        application = (GlobalData)activity.getApplicationContext();
        currentUser = (Company)application.getUserObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        Log.println(Log.ASSERT,"GEOLOC","onViewCreated");
        //SupportMapFragment map = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap == null)
                    Log.println(Log.ASSERT, "GEOLOC", "map object is null");
                else
                    GeoLocalization.this.googleMap = googleMap;

                latitude = 45;
                longitude = 8;
                setMarkerPosition(new LatLng(latitude, longitude));
            }
        });



    }


    private void setMarkerPosition(LatLng pos){

        googleMap.addMarker(new MarkerOptions().position(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,18.0f));

    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_localization);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager(). findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String addressStr = office.getOfficeCity()+" " +office.getOfficeAddress();
        //String addressStr = "Sainta Augustine,FL,4405 Avenue A";
        Geocoder geoCoder = new Geocoder(this);

        try {
            List<Address> addresses =  geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                latitude = addresses.get(0).getLatitude();
                longtitude = addresses.get(0).getLongitude(); }

        }  catch (IOException e) {
            e.printStackTrace();
        }

        latitude = 45.00;
        longitude = 8.00;

        pos = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,100));


    }*/

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

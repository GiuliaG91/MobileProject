package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GeoLocalization extends FragmentActivity {

    static final LatLng TutorialsPoint = new LatLng(45 , 8);
    private GoogleMap googleMap;
    protected GlobalData application;
    private Company currentUser;
    private double longitude;
    private double latitude;
    private LatLng pos;
    private Office office;



    public static GeoLocalization newInstance(Office office) {
        GeoLocalization fragment = new GeoLocalization();
        Bundle args = new Bundle();
        fragment.setOffice(office);
        return fragment;
    }

    public void setOffice(Office office){
        this.office = office;
    }

    public void onAttach(Activity activity) {

        application = (GlobalData)activity.getApplicationContext();
        currentUser = (Company)application.getUserObject();
    }


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
        /*
        try {
            List<Address> addresses =  geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                latitude = addresses.get(0).getLatitude();
                longtitude = addresses.get(0).getLongitude(); }

        }  catch (IOException e) {
            e.printStackTrace();
        }
        */
        latitude = 45.00;
        longitude = 8.00;

        pos = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,100));


    }

}

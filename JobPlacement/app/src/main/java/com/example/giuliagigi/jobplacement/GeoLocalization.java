package com.example.giuliagigi.jobplacement;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class GeoLocalization extends SupportMapFragment{

    private GoogleMap googleMap;
    protected GlobalData application;
    private Marker currentLocation;
    private OnMapReadyCallback host;



    public GeoLocalization(){ super(); }
    public static GeoLocalization newInstance() {
        GeoLocalization fragment = new GeoLocalization();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnMapReadyCallback(OnMapReadyCallback host){
        this.host = host;
    }



    /* ----------------- STANDARD CALLBACKS ------------------------------------------------------*/

    public void onAttach(Activity activity) {

        super.onAttach(activity);
        application = (GlobalData)activity.getApplicationContext();
        currentLocation = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap == null)
                    Log.println(Log.ASSERT, "GEOLOC", "map object is null");
                else{

                    GeoLocalization.this.googleMap = googleMap;

                    if(host == null)
                        Log.println(Log.ASSERT,"GEOLOC","ERROR: map fragment doesn't have a OnMapReadyCallback object linked to it!!");
                    else
                        host.onMapReady(googleMap);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    /* ----------------------- AUXILIARY METHODS -------------------------------------------------*/

    public void setMarkerPosition(LatLng pos){

        if(currentLocation != null)
            currentLocation.remove();

        currentLocation = googleMap.addMarker(new MarkerOptions().position(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,11.0f));
    }



    public Address setMapLocation(String geoAddress) throws IOException{

        List<Address> addressList;

        Geocoder geocoder = new Geocoder(getActivity());
        addressList = geocoder.getFromLocationName(geoAddress,5);

        if(addressList.isEmpty())
            return null;
        else {

            Log.println(Log.ASSERT,"OFFICE FRAG", "only one address found on maps");
            Address a = addressList.get(0);
            setMarkerPosition(new LatLng(a.getLatitude(), a.getLongitude()));

            Log.println(Log.ASSERT,"OFFICE FRAG", "setting map location finished");

            return a;
        }

    }

}

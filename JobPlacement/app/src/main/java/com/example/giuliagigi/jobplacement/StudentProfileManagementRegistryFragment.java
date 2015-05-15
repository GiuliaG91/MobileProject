package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.ArrayList;

public class StudentProfileManagementRegistryFragment extends ProfileManagementFragment implements OnMapReadyCallback{


    private static final String TITLE = "Registry";

    private Student student;
    private boolean addressChanged;
    private EditText addressText,cityText,postalText,nationText;
    private Button phonePlus;
    private GeoLocalization geoloc;

    private ArrayList<ProfileManagementTelephoneFragment> telephoneFragments;


    /* ----------------- CONTRUCTORS GETTERS SETTERS ---------------------------------------------*/

    public StudentProfileManagementRegistryFragment() {super();}
    public static StudentProfileManagementRegistryFragment newInstance(Student student) {
        StudentProfileManagementRegistryFragment fragment = new StudentProfileManagementRegistryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setStudent(student);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setStudent(Student student){

        this.student = student;
    }


    /* ----------------- STANDARD CALLBACKS ------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT,"REGISTRY FRAG", "onAttach");
        telephoneFragments = new ArrayList<ProfileManagementTelephoneFragment>();
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_registry, container, false);

        geoloc = (GeoLocalization)getChildFragmentManager().findFragmentById(R.id.office_map_fragment);
        geoloc.setOnMapReadyCallback(this);

        addressText = (EditText)root.findViewById(R.id.student_address_area);
        if(student.getAddress() == null){
            addressText.setText(INSERT_FIELD);
        }else{
            addressText.setText(student.getAddress());
        }

        cityText = (EditText)root.findViewById(R.id.student_city_area);
        if(student.getCity() == null){
            cityText.setText(INSERT_FIELD);
        }else{
            cityText.setText(student.getCity());
        }

        postalText = (EditText)root.findViewById(R.id.student_CAP_area);
        if(student.getPostalCode() == null){
            postalText.setText(INSERT_FIELD);
        }else{
            postalText.setText(student.getPostalCode());
        }

        nationText = (EditText)root.findViewById(R.id.student_nation_area);
        if(student.getNation() == null){
            nationText.setText(INSERT_FIELD);
        }else{
            nationText.setText(student.getNation());
        }

        textFields.add(addressText);
        textFields.add(cityText);
        textFields.add(postalText);
        textFields.add(nationText);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for(EditText et: textFields)
            et.addTextChangedListener(hasChangedListener);

        OnAddressChangedListener addressListener = new OnAddressChangedListener();
        addressText.addTextChangedListener(addressListener);
        cityText.addTextChangedListener(addressListener);
        nationText.addTextChangedListener(addressListener);

        phonePlus = (Button)root.findViewById(R.id.student_phones_plusButton);
        phonePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileManagementTelephoneFragment tf = ProfileManagementTelephoneFragment.newInstance(new Telephone(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_phones_container,tf);
                ft.commit();
                telephoneFragments.add(tf);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        int max = Math.max(telephoneFragments.size(), student.getPhones().size());
        for(int i=0;i<max;i++){

            if(i>=telephoneFragments.size()){

                ProfileManagementTelephoneFragment tf  = ProfileManagementTelephoneFragment.newInstance(student.getPhones().get(i), student);
                telephoneFragments.add(tf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_phones_container,telephoneFragments.get(i));
            ft.commit();
        }

        setEnable(host.isEditMode());
    }

    @Override
    public void onPause() {
        super.onPause();

        for (ProfileManagementTelephoneFragment tf: telephoneFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(tf);
            ft.commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementTelephoneFragment tf:telephoneFragments)
            host.removeOnActivityChangedListener(tf);
    }

    public void setEnable(boolean enable){
        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        super.setEnable(enable);
        Button phonePlus = (Button)root.findViewById(R.id.student_phones_plusButton);
        phonePlus.setVisibility(visibility);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        if(!addressText.getText().toString().equals(INSERT_FIELD))  student.setAddress(addressText.getText().toString());
        if(!cityText.getText().toString().equals(INSERT_FIELD))     student.setCity(cityText.getText().toString());
        if(!postalText.getText().toString().equals(INSERT_FIELD))   student.setPostalCode(postalText.getText().toString());
        if(!nationText.getText().toString().equals(INSERT_FIELD))   student.setNation(nationText.getText().toString());

        if(addressChanged && !nationText.getText().toString().equals(INSERT_FIELD) && !cityText.getText().toString().equals(INSERT_FIELD)){

            String geoAddress = nationText.getText().toString() + ", " + cityText.getText().toString();

            if(!addressText.getText().toString().equals(INSERT_FIELD))
                geoAddress = geoAddress + ", " + addressText.getText().toString();
            else
                Toast.makeText(getActivity(), "To get a better geographical localization, consider adding your address", Toast.LENGTH_SHORT).show();

            try {
                Address a = geoloc.setMapLocation(geoAddress);
                if(a!= null)
                    student.setAddressLocation(new ParseGeoPoint(a.getLatitude(),a.getLongitude()));
                else{
                    Toast.makeText(getActivity(),"Unfortunately, your addres doesn't match with a Google Maps address",Toast.LENGTH_SHORT).show();
                    student.setAddressLocation(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"Due to an error, it was not possible to save your geographical location",Toast.LENGTH_SHORT).show();
            }

        }
        else
            Toast.makeText(getActivity(),"You must specify at least office country and city to get a physic location", Toast.LENGTH_SHORT).show();

        student.saveEventually();

    }


    /* ------------------------------ MAPS METHODS ------------------------------------------------*/


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.println(Log.ASSERT,"OFFICE FRAG", "Map was created. Setting marker position");
        if(student.getAddressLocation()!=null)
            geoloc.setMarkerPosition(new LatLng(student.getAddressLocation().getLatitude(), student.getAddressLocation().getLongitude()));
    }


    private class OnAddressChangedListener implements TextWatcher {

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

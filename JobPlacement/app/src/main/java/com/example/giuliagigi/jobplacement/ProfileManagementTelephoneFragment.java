package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;


public class ProfileManagementTelephoneFragment extends ProfileManagementFragment {

    private static final String TITLE = "Telephone";
    public static final String BUNDLE_IDENTIFIER = "PROFILETELEPHONE";
    private static final String BUNDLE_TELEPHONE = "bundle_telephone";
    private static final String BUNDLE_NUMBER = "bundle_number";
    private static final String BUNDLE_TYPE = "bundle_type";
    private static final String BUNDLE_PARENT = "bundle_parent";

    private TelephoneFragmentInterface parent;
    private Telephone telephone;

    private Spinner typeSelector;
    private Button removeButton;
    private boolean isRemoved;
    private EditText numberText;

    int type = 0;
    String number = null, parentName;

    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- CONSTRUCTOR GETTERS SETTERS -------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public ProfileManagementTelephoneFragment() { super(); }

    public static ProfileManagementTelephoneFragment newInstance(TelephoneFragmentInterface parent, Telephone telephone, User user) {

        ProfileManagementTelephoneFragment fragment = new ProfileManagementTelephoneFragment();

        fragment.setTelephone(telephone);
        fragment.setUser(user);
        fragment.parent = parent;

        ProfileManagementFragment f = (ProfileManagementFragment)parent;
        fragment.parentName = f.bundleIdentifier();

        return fragment;
    }

    public void setTelephone(Telephone telephone){
        this.telephone = telephone;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setUser(User user){

        Log.println(Log.ASSERT,"TEL FRAG","setting user" + user);
        this.user = user;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }
    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- STANDARD CALLBACKS ---------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        isNestedFragment = true;
        isRemoved = false;

        if (telephone != null) {

            if(telephone.getType() != null)
                type = Telephone.getTypeID(telephone.getType());
            number = telephone.getNumber();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile_management_telephone, container, false);

        removeButton = (Button)root.findViewById(R.id.telephone_remove_button);
        typeSelector = (Spinner)root.findViewById(R.id.telefone_type_selector);
        numberText = (EditText)root.findViewById(R.id.telephone_number_text);
        ImageView phoneIcon = (ImageView)root.findViewById(R.id.telephoneIcon);
        Drawable new_image = getResources().getDrawable(R.drawable.ic_phone);
        phoneIcon.setBackgroundDrawable(new_image);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(telephone.getObjectId() != null){

                    telephone.deleteEventually(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e == null){

                                user.removePhone(telephone);
                                user.saveEventually();
                                root.setVisibility(View.INVISIBLE);
                                parent.onTelephoneDelete(ProfileManagementTelephoneFragment.this);
                                isRemoved = true;
                            }
                            else {

                                Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.profile_object_delete_failure), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    isRemoved = true;
                    parent.onTelephoneDelete(ProfileManagementTelephoneFragment.this);
                    root.setVisibility(View.INVISIBLE);
                }
            }
        });

        typeSelector.setAdapter(new StringAdapter(Telephone.TYPES));
        typeSelector.setSelection(type);

        numberText.addTextChangedListener(new OnFieldChangedListener());
        if(number==null)    numberText.setText(INSERT_FIELD);
        else                numberText.setText(telephone.getNumber());
        textFields.add(numberText);

//        setEnable(listener.isEditMode());
        return root;
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* ------------------- AUXILIARY METHODS ---------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    protected void saveStateInBundle(Bundle outstate) {
        super.saveStateInBundle(outstate);

        bundle.put(BUNDLE_TELEPHONE, telephone);
        bundle.putInt(BUNDLE_TYPE, type);
        bundle.putString(BUNDLE_NUMBER,number);
        bundle.putString(BUNDLE_PARENT,parentName);
    }

    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);

        if(bundle != null){

            telephone = (Telephone)bundle.get(BUNDLE_TELEPHONE);
            type = bundle.getInt(BUNDLE_TYPE);
            number = bundle.getString(BUNDLE_NUMBER);

            parentName = bundle.getString(BUNDLE_PARENT);
            parent = (TelephoneFragmentInterface)application.getFragment(parentName);
        }
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        if(!isRemoved && !numberText.getText().toString().equals(INSERT_FIELD)){

            Log.println(Log.ASSERT,"TELEPHONE FRAG","saving a phone...");

            telephone.setNumber(numberText.getText().toString());
            telephone.setType((String) typeSelector.getSelectedItem());

            telephone.setUser(user);
            telephone.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){

                        Log.println(Log.ASSERT,"TELEPHONE FRAG", "phone saved. updating user");
                        user.addPhone(telephone);
                        user.saveEventually();
                    }
                }
            });
        }

    }

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);

        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        removeButton.setVisibility(visibility);

        typeSelector.setEnabled(enable);
        removeButton.setEnabled(enable);
    }


    /* -------------------------------------------------------------------------------------------*/
    /* ------------------------- PARENT FRAGMENT INTERFACE ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public interface TelephoneFragmentInterface{

        public void onTelephoneDelete(ProfileManagementTelephoneFragment toRemove);
    }
}

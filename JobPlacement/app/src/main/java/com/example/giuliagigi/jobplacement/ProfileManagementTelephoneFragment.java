package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;


public class ProfileManagementTelephoneFragment extends ProfileManagementFragment {

    private static String BUNDLE_NUMBER = "bundle_number";
    private static String BUNDLE_TYPE = "bundle_type";
    private static String BUNDLE_HASCHANGED = "bundle_hasChanged";

    private Telephone telephone;
    private User currentUser = null;
    private Spinner typeSelector;
    private Button removeButton;
    private EditText numberText;


    /* ------------------- CONSTRUCTOR GETTERS SETTERS ------------------------------------------*/
    public ProfileManagementTelephoneFragment() { super(); }
    public static ProfileManagementTelephoneFragment newInstance(Telephone telephone) {
        ProfileManagementTelephoneFragment fragment = new ProfileManagementTelephoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setTelephone(telephone);
        return fragment;
    }

    public void setTelephone(Telephone telephone){
        this.telephone = telephone;
    }

    /* ------------------- STANDARD CALLBACKS ---------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isListenerAfterDetach = true;

        if(application.getCurrentUser().getType().equals(User.TYPE_STUDENT))
            currentUser = application.getStudentFromUser();
        else
            currentUser = application.getCompanyFromUser();
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

        root = inflater.inflate(R.layout.fragment_profile_management_telephone, container, false);

        int type = 0;
        String number = null;
        if(getArguments().getBoolean(BUNDLE_HASCHANGED)){

            type = getArguments().getInt(BUNDLE_TYPE);
            number = getArguments().getString(BUNDLE_NUMBER);
        }
        else{

            if(telephone.getType()!=null)
                type = Telephone.getTypeID(telephone.getType());
            if(telephone.getNumber()!=null)
                number = telephone.getNumber();
        }

        removeButton = (Button)root.findViewById(R.id.telephone_remove_button);
        typeSelector = (Spinner)root.findViewById(R.id.telefone_type_selector);
        numberText = (EditText)root.findViewById(R.id.telephone_number_text);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUser.removePhone(telephone);
                currentUser.saveInBackground();

                try {
                    telephone.delete();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                root.setVisibility(View.INVISIBLE);
            }
        });

        typeSelector.setAdapter(new StringAdapter(Telephone.TYPES));
        typeSelector.setSelection(type);

        numberText.addTextChangedListener(new OnFieldChangedListener());
        if(number==null)    numberText.setText(INSERT_FIELD);
        else                numberText.setText(telephone.getNumber());
        textFields.add(numberText);

        setEnable(host.isEditMode());
        return root;
    }

    @Override
    public void onDetach(){
        super.onDetach();

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){
            getArguments().putInt(BUNDLE_TYPE,typeSelector.getSelectedItemPosition());
            getArguments().putString(BUNDLE_NUMBER,numberText.getText().toString());
        }
    }


    /* ------------------- AUXILIARY METHODS ---------------------------------------------------*/

    @Override
    public void saveChanges() {

        currentUser.addPhone(telephone);
        currentUser.saveInBackground();

        telephone.setNumber(numberText.getText().toString());
        telephone.setType((String) typeSelector.getSelectedItem());
        telephone.saveInBackground();
    }

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);

        typeSelector.setEnabled(enable);
    }
}

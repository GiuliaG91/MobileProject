package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;


public class CompanyRegistrationFragment extends Fragment {

    private static final String BUNDLE_KEY_HAS_SAVED_STATE = "BUNDLE_KEY_HAS_SAVED_STATE";
    private static final String BUNDLE_KEY_NAME = "BUNDLE_KEY_NAME";
    private static final String BUNDLE_KEY_MAIL = "BUNDLE_KEY_MAIL";
    private static final String BUNDLE_KEY_PASSWORD = "BUNDLE_KEY_PASSWORD";
    private static final String BUNDLE_KEY_CONFIRM_PASSWORD = "BUNDLE_KEY_CONFIRM_PASSWORD";
    private static final String BUNDLE_KEY_FISCAL_CODE = "BUNDLE_KEY_FISCAL_CODE";
    private static final String BUNDLE_KEY_FIELD = "BUNDLE_KEY_FIELD";
    public static final String BUNDLE_IDENTIFIER = "COMPANYREGISTRATION";

    private OnInteractionListener hostActivity;
    private View root;
    private EditText mail, password, confirmPassword, fiscalCode, name;
    private Spinner fieldsList;
    GlobalData application;

    public CompanyRegistrationFragment() {     super();    }
    public static CompanyRegistrationFragment newInstance() {
        CompanyRegistrationFragment fragment = new CompanyRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            hostActivity = (OnInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        application = (GlobalData)activity.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_company_registration, container, false);

        mail = (EditText)root.findViewById(R.id.company_mail);
        password = (EditText)root.findViewById(R.id.company_password);
        confirmPassword = (EditText)root.findViewById(R.id.company_confirmPassword);
        fiscalCode = (EditText)root.findViewById(R.id.company_fiscal_code);
        name = (EditText)root.findViewById(R.id.company_name);
        fieldsList = (Spinner)root.findViewById(R.id.fields_list);

        fieldsList.setAdapter(new StringAdapter(Degree.STUDIES));

        if(application.getBundle(BUNDLE_IDENTIFIER)!= null)
            restorePreviousState();

        return root;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;

        saveState();
    }




    /* --------------------- AUXILIARY METHODS -------------------------------------------------- */

    private void restorePreviousState() {

        MyBundle b = application.getBundle(BUNDLE_IDENTIFIER);

        mail.setText(b.getString(BUNDLE_KEY_MAIL));
        password.setText(b.getString(BUNDLE_KEY_PASSWORD));
        confirmPassword.setText(b.getString(BUNDLE_KEY_CONFIRM_PASSWORD));
        name.setText(b.getString(BUNDLE_KEY_NAME));
        fiscalCode.setText(b.getString(BUNDLE_KEY_FISCAL_CODE));
        fieldsList.setSelection(b.getInt(BUNDLE_KEY_FIELD));
        b.putBoolean(BUNDLE_KEY_HAS_SAVED_STATE,false);
    }


    private void saveState() {

        MyBundle b = application.addBundle(BUNDLE_IDENTIFIER);

        b.putString(BUNDLE_KEY_MAIL,mail.getText().toString());
        b.putString(BUNDLE_KEY_CONFIRM_PASSWORD,confirmPassword.getText().toString());
        b.putString(BUNDLE_KEY_PASSWORD,password.getText().toString());
        b.putString(BUNDLE_KEY_NAME,name.getText().toString());
        b.putString(BUNDLE_KEY_FISCAL_CODE,fiscalCode.getText().toString());
        b.putInt(BUNDLE_KEY_FIELD, Degree.getStudyID((String) fieldsList.getSelectedItem()));
        b.putBoolean(BUNDLE_KEY_HAS_SAVED_STATE,true);
    }


    public Company retrieveRegistrationInfo() throws RegistrationException, ParseException{


        String field = (String)fieldsList.getSelectedItem();
        String type = User.TYPE_COMPANY;

        if(mail.getText().toString().trim().isEmpty() || field == null || password.getText().toString().isEmpty() || fiscalCode.getText().toString().trim().isEmpty())
            throw new RegistrationException(RegistrationException.MISSING_INFORMATIONS);

        if(!password.getText().toString().equals(confirmPassword.getText().toString()))
            throw new RegistrationException((RegistrationException.MISMATCHING_PASSWORDS));

        Company newCompany = new Company();
        newCompany.setName(name.getText().toString());
        newCompany.setFiscalCode(fiscalCode.getText().toString());
        newCompany.setMail(mail.getText().toString().trim());
        newCompany.setPassword(password.getText().toString());
        newCompany.setType(type);
        newCompany.setField(field);
        newCompany.save();

        return newCompany;
    }

    public interface OnInteractionListener {}

    private class StringAdapter extends BaseAdapter {

        public String[] stringArray;

        public StringAdapter(String[] stringArray){
            super();
            this.stringArray = stringArray;
        }

        @Override
        public int getCount() {
            return stringArray.length;
        }

        @Override
        public String getItem(int position) {
            return stringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.spinner_item, parent, false);

            TextView type = (TextView)convertView.findViewById(R.id.text_view);
            type.setText(stringArray[position]);
            return convertView;
        }
    }

}

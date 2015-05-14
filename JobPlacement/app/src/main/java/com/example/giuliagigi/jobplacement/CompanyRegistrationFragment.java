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


public class CompanyRegistrationFragment extends Fragment {

    private OnInteractionListener hostActivity;
    private View root;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_company_registration, container, false);

        Spinner fields = (Spinner)root.findViewById(R.id.fields_list);
        fields.setAdapter(new StringAdapter(Degree.STUDIES));
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }

    public Company retrieveRegistrationInfo() throws RegistrationException{

        EditText mail = (EditText)root.findViewById(R.id.company_mail);
        EditText password = (EditText)root.findViewById(R.id.company_password);
        EditText confirmPassword = (EditText)root.findViewById(R.id.company_confirmPassword);
        EditText fiscalCode = (EditText)root.findViewById(R.id.company_fiscal_code);
        EditText name = (EditText)root.findViewById(R.id.company_name);

        mail.setEnabled(true);
        password.setEnabled(true);
        confirmPassword.setEnabled(true);

        Spinner fieldsList = (Spinner)root.findViewById(R.id.fields_list);
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

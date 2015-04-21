package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.giuliagigi.jobplacement.CompanyRegistrationFragment.OnInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyRegistrationFragment extends Fragment {

    private OnInteractionListener hostActivity;
    private View root;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CompanyRegistrationFragment.
     */
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
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }


    public Company retrieveRegistrationInfo(){

        EditText mail = (EditText)root.findViewById(R.id.company_mail);
        EditText password = (EditText)root.findViewById(R.id.company_password);
        EditText fiscalCode = (EditText)root.findViewById(R.id.company_fiscal_code);
        EditText name = (EditText)root.findViewById(R.id.company_name);
        String type = User.TYPE_COMPANY;

        if(mail.getText().toString().trim().isEmpty())
            return null;
        if(password.getText().toString().isEmpty())
            return null;

        Company newCompany = new Company();
        newCompany.setName(name.getText().toString());
        newCompany.setFiscalCode(fiscalCode.getText().toString());
        newCompany.setMail(mail.getText().toString().trim());
        newCompany.setPassword(password.getText().toString());
        newCompany.setType(type);

        return newCompany;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnInteractionListener {
        // TODO: Update argument type and name
    }

}

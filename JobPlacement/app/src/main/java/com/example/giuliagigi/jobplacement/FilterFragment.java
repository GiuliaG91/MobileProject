package com.example.giuliagigi.jobplacement;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by pietro on 13/05/2015.
 */
public class FilterFragment extends DialogFragment {


    View root;
    // private addFilter mCallback;

    GlobalData globalData = null;

    private EditText editSalary = null;
    private EditText editDistance = null;
    private EditText nation = null;
    private EditText city = null;

    private Spinner fieldSpinner = null;
    private Spinner contractSpinner = null;
    private Spinner salarySpinner = null;
    private Spinner termSpinner = null;
    private Spinner distanceSpinner=null;

    private ImageButton addtagsButton = null;
    private ImageButton addfieldButton = null;
    private ImageButton addcontractButton = null;
    private ImageButton addtermsButton = null;
    private ImageButton remove = null;

    private Button okButton;
    private Button cancelButton;


    private String[] typeOfFields;
    private String[] typeOfContracts;
    private String[] salaries;
    private String[] durations;
    private String[] distances;

    private MultiAutoCompleteTextView tagsView = null;

    private Map<String, Tag> retriveTag = null;
    private Set<String> correct;

    private Set<String> supportTag;
    private Set<String> supportField;
    private Set<String> supportTerm;
    private Set<String> supportContract;

    /**
     * ************************FILTERS******************
     */
    List<Tag> tag_list = null;
    List<String> contract_list = null;
    List<String> term_list = null;
    List<String> field_list = null;
    List<String> location_list = null;
    List<String> salary_list = null;

    /**
     * ***********************************************
     */


    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportTag = new HashSet<>();
        supportContract = new HashSet<>();
        supportField = new HashSet<>();
        supportTerm = new HashSet<>();


        tag_list = new ArrayList<>();
        contract_list = new ArrayList<>();
        term_list = new ArrayList<>();
        field_list = new ArrayList<>();
        location_list = new ArrayList<>();
        salary_list = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        globalData = (GlobalData) getActivity().getApplication();


        root = inflater.inflate(R.layout.fragment_filter, container, false);

        //addbuttons

        addtagsButton = (ImageButton) root.findViewById(R.id.filter_tag_add);
        addfieldButton = (ImageButton) root.findViewById(R.id.filter_field_add);
        addcontractButton = (ImageButton) root.findViewById(R.id.filter_contract_add);
        addtermsButton = (ImageButton) root.findViewById(R.id.filter_term_add);
        remove = (ImageButton) root.findViewById(R.id.filter_remove);

        okButton = (Button) root.findViewById(R.id.ok_button);
        cancelButton = (Button) root.findViewById(R.id.cancel_button);

        //set all possible filters
        editSalary = (EditText) root.findViewById(R.id.filter_edit_salary);

        editDistance=(EditText) root.findViewById(R.id.filter_edit_distance);
        nation = (EditText) root.findViewById(R.id.filter_edit_nation);
        city = (EditText) root.findViewById(R.id.filter_edit_city);

        fieldSpinner = (Spinner) root.findViewById(R.id.filter_field_spinner);
        contractSpinner = (Spinner) root.findViewById(R.id.filter_contract_spinner);
        salarySpinner = (Spinner) root.findViewById(R.id.filter_salary_spinner);
        termSpinner = (Spinner) root.findViewById(R.id.filter_term_spinner);
        distanceSpinner=(Spinner) root.findViewById(R.id.filter_distance_spinner);
        //MultiAutoCompletetextview
        tagsView = (MultiAutoCompleteTextView) root.findViewById(R.id.filter_tag_complete_tv);

        typeOfFields = getResources().getStringArray(R.array.new_offer_fragment_fields);
        fieldSpinner.setAdapter(new StringAdapter(typeOfFields));

        typeOfContracts = getResources().getStringArray(R.array.new_offer_fragment_typeOfContracts);
        contractSpinner.setAdapter(new StringAdapter(typeOfContracts));

        salaries = getResources().getStringArray(R.array.filter_salary);
        salarySpinner.setAdapter(new StringAdapter(salaries));

        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    editSalary.setEnabled(false);
                } else {
                    editSalary.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        distances=  getResources().getStringArray(R.array.filter_location);
       distanceSpinner.setAdapter(new StringAdapter(distances));

        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position== 1) {
                    editDistance.setEnabled(false);
                } else {
                    editDistance.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        durations = getResources().getStringArray(R.array.new_offer_fragment_termContracts);
        termSpinner.setAdapter(new StringAdapter(durations));


        //multiautocompletetextview
        ParseQuery<Tag> query = ParseQuery.getQuery("Tag");
        List<Tag> tags = null;
        try {
            tags = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] t = new String[tags.size()];

        correct = new HashSet<String>();
        retriveTag = new HashMap<>();


        for (int i = 0; i < tags.size(); i++) {
            t[i] = tags.get(i).getTag();
            retriveTag.put(tags.get(i).getTag(), tags.get(i));
            correct.add(tags.get(i).getTag().trim());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, t);

        tagsView.setAdapter(adapter);
        tagsView.setTokenizer(new SpaceTokenizer());
        tagsView.setThreshold(1);


        addtagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTag(v);
            }
        });

        addfieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickfield(v);
            }
        });

        addtermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickterm(v);
            }
        });

        addcontractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContract(v);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFilters(v);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilters(v);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        checkStatus();

        return root;
    }

    /**
     * *******Controllo dello stato dei filtri***************
     */
    private void checkStatus() {

        if (globalData.getOfferFilterStatus().isValid()) {

            tag_list = globalData.getOfferFilterStatus().getTag_list();
            contract_list = globalData.getOfferFilterStatus().getContract_list();
            term_list = globalData.getOfferFilterStatus().getTerm_list();
            field_list = globalData.getOfferFilterStatus().getField_list();
            location_list = globalData.getOfferFilterStatus().getLocation_list();
            salary_list = globalData.getOfferFilterStatus().getSalary_list();

            final GridLayout container = (GridLayout) root.findViewById(R.id.filter_tag_container);

            for (int i = 0; i < tag_list.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(tag_list.get(i).getTag());
                container.addView(mytagView);
            }


            final GridLayout fieldContainer = (GridLayout) root.findViewById(R.id.filter_field_container);
            for (int i = 0; i < field_list.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fieldContainer.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(field_list.get(i));
                fieldContainer.addView(mytagView);
            }

            final GridLayout termContainer = (GridLayout) root.findViewById(R.id.filter_term_container);
            for (int i = 0; i < term_list.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        termContainer.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(term_list.get(i));
                termContainer.addView(mytagView);
            }

            final GridLayout contractContainer = (GridLayout) root.findViewById(R.id.filter_contract_container);
            for (int i = 0; i < contract_list.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contractContainer.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(contract_list.get(i));
                contractContainer.addView(mytagView);
            }

            if (!salary_list.isEmpty()) {
                Integer pos = Integer.parseInt(salary_list.get(0));
                salarySpinner.setSelection(pos);
                editSalary.setText(salary_list.get(1));

            }

            if (!location_list.isEmpty()) {
                Integer pos = Integer.parseInt(location_list.get(0));
                distanceSpinner.setSelection(pos);
                editDistance.setText(location_list.get(1));
                nation.setText(location_list.get(2));
                city.setText(location_list.get(3));

            }


        }
    }


    /**
     * *************************Aggiunta e rimozione filtri*****************
     */
    public void onClickTag(View v) {
        final GridLayout container = (GridLayout) root.findViewById(R.id.filter_tag_container);
        String filter = tagsView.getText().toString().trim();
        if (correct.contains(filter)) {
            if (!supportTag.add(filter)) {
                Toast.makeText(getActivity(), getString(R.string.filter_AlreadyAdded), Toast.LENGTH_SHORT).show();
            } else {


                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                        globalData.getOfferFilterStatus().setFilters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(filter);
                container.addView(mytagView);

            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_WrongTag), Toast.LENGTH_SHORT).show();
        }
        tagsView.clearComposingText();
        ;
    }

    public void onClickterm(View v) {
        final GridLayout container = (GridLayout) root.findViewById(R.id.filter_term_container);
        if (termSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), getString(R.string.filter_hint_term), Toast.LENGTH_SHORT).show();
        } else {

            String filter = termSpinner.getSelectedItem().toString().trim();
            if (!supportTerm.add(filter)) {
                Toast.makeText(getActivity(), getString(R.string.filter_AlreadyAdded), Toast.LENGTH_SHORT).show();
            } else {

                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                        globalData.getOfferFilterStatus().setFilters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(filter);
                container.addView(mytagView);
            }
        }
        salarySpinner.setSelection(0);
    }

    public void onClickContract(View v) {
        final GridLayout container = (GridLayout) root.findViewById(R.id.filter_contract_container);
        if (contractSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), getString(R.string.filter_hint_contract), Toast.LENGTH_SHORT).show();
        } else {

            String filter = contractSpinner.getSelectedItem().toString().trim();
            if (!supportContract.add(filter)) {
                Toast.makeText(getActivity(), getString(R.string.filter_AlreadyAdded), Toast.LENGTH_SHORT).show();
            } else {

                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                        Toast.makeText(getActivity(),getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                        globalData.getOfferFilterStatus().setFilters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(filter);
                container.addView(mytagView);
            }
        }
        contractSpinner.setSelection(0);
    }

    public void onClickfield(View v) {
        final GridLayout container = (GridLayout) root.findViewById(R.id.filter_field_container);
        if (fieldSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), getString(R.string.filter_hint_term), Toast.LENGTH_SHORT).show();
        } else {

            String filter = fieldSpinner.getSelectedItem().toString().trim();
            if (!supportField.add(filter)) {
                Toast.makeText(getActivity(), getString(R.string.filter_AlreadyAdded), Toast.LENGTH_SHORT).show();
            } else {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mytagView = inflater.inflate(R.layout.taglayout, null);

                mytagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeView(v);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                        globalData.getOfferFilterStatus().setFilters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
                    }
                });

                TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                t.setText(filter);
                container.addView(mytagView);
            }
        }
        fieldSpinner.setSelection(0);
    }


    /**
     * *******************APPLY FILTERS*******************************
     */

    public void applyFilters(View v) {

        tag_list.clear();
        contract_list.clear();
        term_list.clear();
        field_list.clear();
        location_list.clear();
        salary_list.clear();
        Integer pos=0;

        int flag=0;
        
        if(nation.getText().length()==0 && city.getText().length()!=0 ||
                nation.getText().length()!=0 && city.getText().length()==0
                ){
            flag=1;
        }
if(flag==0) {
    GridLayout container = (GridLayout) root.findViewById(R.id.filter_tag_container);
    if (container.getChildCount() > 0) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View tv = container.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);
            tag_list.add(retriveTag.get(t.getText().toString().trim()));
        }
    }

    container = (GridLayout) root.findViewById(R.id.filter_field_container);
    if (container.getChildCount() > 0) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View tv = container.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);
            field_list.add(t.getText().toString());
        }
    }

    pos = salarySpinner.getSelectedItemPosition();
    if (pos != 0) {

        salary_list.add(String.valueOf(pos));
        if (editSalary.getText().toString().equals("")) {
            salary_list.add("0");
        } else {
            salary_list.add(editSalary.getText().toString());
        }
    }
    container = (GridLayout) root.findViewById(R.id.filter_term_container);
    if (container.getChildCount() > 0) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View tv = container.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);
            term_list.add(t.getText().toString().trim());
        }
    }

    container = (GridLayout) root.findViewById(R.id.filter_contract_container);
    if (container.getChildCount() > 0) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View tv = container.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);
            contract_list.add(t.getText().toString().trim());
        }
    }


    pos = distanceSpinner.getSelectedItemPosition();
    if (pos != 0) {

        location_list.add(String.valueOf(pos));
        if (editDistance.getText().toString().equals("")) {
            location_list.add("0");
        } else {
            location_list.add(editDistance.getText().toString());
        }
        location_list.add(nation.getText().toString());
        location_list.add(city.getText().toString());
    }

    globalData.getOfferFilterStatus().setFilters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
    globalData.getOfferFilterStatus().setValid(true);

    OfferSearchFragment fragment = (OfferSearchFragment) this.getParentFragment();
    fragment.addFiters(tag_list, contract_list, term_list, field_list, location_list, salary_list);
    getDialog().dismiss();
       }else Toast.makeText(getActivity(), getString(R.string.filter_hint_location),Toast.LENGTH_SHORT).show();
    }


    public void removeFilters(View v) {

        GridLayout container = (GridLayout) root.findViewById(R.id.filter_tag_container);
        container.removeAllViews();


        container = (GridLayout) root.findViewById(R.id.filter_field_container);
        container.removeAllViews();
        fieldSpinner.setSelection(0);

        container = (GridLayout) root.findViewById(R.id.filter_term_container);
        container.removeAllViews();
        termSpinner.setSelection(0);

        container = (GridLayout) root.findViewById(R.id.filter_contract_container);
        container.removeAllViews();
        contractSpinner.setSelection(0);



        salarySpinner.setSelection(0);
        editSalary.clearComposingText();

        distanceSpinner.setSelection(0);
        editDistance.setText("");
        nation.setText("");
        city.setText("");


        /*clear list*/

        tag_list.clear();
        contract_list.clear();
        term_list.clear();
        field_list.clear();
        location_list.clear();
        salary_list.clear();

        //no filters
        globalData.getOfferFilterStatus().setValid(false);
        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
    }


    /**
     * ***************************STRING ADAPTER*************************
     */
    public class StringAdapter extends BaseAdapter {

        public String[] stringArray;

        public StringAdapter(String[] stringArray) {
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

            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_text_element, parent, false);

            TextView text = (TextView) convertView.findViewById(R.id.text_view);
            text.setTextSize(15);
            text.setText(stringArray[position]);
            return convertView;
        }
    }

}

package com.example.giuliagigi.jobplacement;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private addFilter mCallback;

    GlobalData globalData=null;

    private EditText editSalary=null;

    private Spinner fieldSpinner = null;
    private Spinner contractSpinner = null;
    private Spinner salarySpinner = null;
    private Spinner termSpinner =null;

    private ImageButton addtagsButton=null;
    private ImageButton addfieldButton=null;
    private ImageButton addcontractButton=null;
    private ImageButton addtermsButton=null;
    private ImageButton addlocationButton=null;

    private Button okButton;
    private Button cancelButton;


    private String[] typeOfFields;
    private String[] typeOfContracts;
    private String[] salaries;
    private String[] durations;

    private MultiAutoCompleteTextView tagsView=null;
    private MultiAutoCompleteTextView location=null;

    private Map<String,Tag> retriveTag=null;
    private  Set<String> correct;

    private Set<String> supportTag;
    private Set<String> supportField;
    private Set<String> supportTerm;
    private Set<String> supportContract;
    private Set<String> supportLocation;


    public FilterFragment(){}

    public static FilterFragment newInstance()
    {
        FilterFragment fragment=new FilterFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportTag=new HashSet<>();
        supportContract=new HashSet<>();
        supportField=new HashSet<>();
        supportLocation=new HashSet<>();
        supportTerm=new HashSet<>();

        mCallback=(addFilter)getTargetFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        globalData=(GlobalData)getActivity().getApplication();



      root=inflater.inflate(R.layout.fragment_filter,container,false);

        //addbuttons

        addtagsButton=(ImageButton)root.findViewById(R.id.filter_tag_add);
        addfieldButton=(ImageButton)root.findViewById(R.id.filter_field_add);
        addcontractButton=(ImageButton)root.findViewById(R.id.filter_contract_add);
        addtermsButton=(ImageButton)root.findViewById(R.id.filter_term_add);
        addlocationButton=(ImageButton)root.findViewById(R.id.filter_location_add);

        okButton=(Button)root.findViewById(R.id.ok_button);
        cancelButton=(Button)root.findViewById(R.id.cancel_button);

        //set all possible filters
        editSalary=(EditText)root.findViewById(R.id.filter_edit_salary);
        fieldSpinner = (Spinner) root.findViewById(R.id.filter_field_spinner);
        contractSpinner = (Spinner) root.findViewById(R.id.filter_contract_spinner);
        salarySpinner = (Spinner) root.findViewById(R.id.filter_salary_spinner);
        termSpinner = (Spinner) root.findViewById(R.id.filter_term_spinner);
        //MultiAutoCompletetextview
        tagsView = (MultiAutoCompleteTextView) root.findViewById(R.id.filter_tag_complete_tv);
        location=(MultiAutoCompleteTextView)root.findViewById(R.id.filter_location_complete_tv);


        typeOfFields=getResources().getStringArray(R.array.new_offer_fragment_fields);
        fieldSpinner.setAdapter(new StringAdapter(typeOfFields));

        typeOfContracts=getResources().getStringArray(R.array.new_offer_fragment_typeOfContracts);
        contractSpinner.setAdapter(new StringAdapter(typeOfContracts));

        salaries=getResources().getStringArray(R.array.filter_salary);
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


        durations=getResources().getStringArray(R.array.new_offer_fragment_termContracts);
        termSpinner.setAdapter(new StringAdapter(durations));


        //multiautocompletetextview
        ParseQuery<Tag> query=ParseQuery.getQuery("Tag");
        List<Tag> tags=null;
        try {
            tags= query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] t=new String[tags.size()];

       correct=new HashSet<String>();
        retriveTag=new HashMap<>();


        for(int i=0;i<tags.size();i++)
        {
            t[i]=tags.get(i).getTag();
            retriveTag.put(tags.get(i).getTag().toLowerCase().trim(),tags.get(i));
            correct.add(tags.get(i).getTag().toLowerCase().trim());
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

        addlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLocation(v);
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


        return root;
    }

  /****************************Aggiunta e rimozione filtri******************/
    public void  onClickTag(View v)
    {
        LinearLayout container=(LinearLayout)root.findViewById(R.id.filter_tag_container);
        String filter=tagsView.getText().toString().toLowerCase().trim();
        if(correct.contains((String)filter)) {
            if (!supportTag.add(filter)) {
                Toast.makeText(getActivity(), "Filter already added", Toast.LENGTH_SHORT).show();
            } else {
                TextView tv = new TextView(getActivity());
                tv.setText(filter);
                container.addView(tv);
            }

        }
        else {      Toast.makeText(getActivity(), "Wrong tag", Toast.LENGTH_SHORT).show();}
    }
    public void  onClickterm(View v)
    {
        LinearLayout container=(LinearLayout)root.findViewById(R.id.filter_term_container);
        if(termSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(getActivity(), "Please choose a term if you want", Toast.LENGTH_SHORT).show();
        }
        else {

            String filter = termSpinner.getSelectedItem().toString().toLowerCase().trim();
            if (!supportTerm.add(filter)) {
                Toast.makeText(getActivity(), "Filter already added", Toast.LENGTH_SHORT).show();
            } else {
                TextView tv = new TextView(getActivity());
                tv.setText(filter);
                container.addView(tv);
            }
        }

    }
    public void  onClickContract(View v)
    {
        LinearLayout container=(LinearLayout)root.findViewById(R.id.filter_contract_container);
        if(contractSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(getActivity(), "Please choose a contract if you want", Toast.LENGTH_SHORT).show();
        }
        else {

            String filter = contractSpinner.getSelectedItem().toString().toLowerCase().trim();
            if (!supportContract.add(filter)) {
                Toast.makeText(getActivity(), "Filter already added", Toast.LENGTH_SHORT).show();
            } else {
                TextView tv = new TextView(getActivity());
                tv.setText(filter);
                container.addView(tv);
            }
        }

    }
    public void  onClickfield(View v)
    {
        LinearLayout container=(LinearLayout)root.findViewById(R.id.filter_field_container);
        if(fieldSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(getActivity(), "Please choose a term if you want", Toast.LENGTH_SHORT).show();
        }
        else {

            String filter = fieldSpinner.getSelectedItem().toString().toLowerCase().trim();
            if (!supportField.add(filter)) {
                Toast.makeText(getActivity(), "Filter already added", Toast.LENGTH_SHORT).show();
            } else {
                TextView tv = new TextView(getActivity());
                tv.setText(filter);
                container.addView(tv);
            }
        }
    }
    public void  onClickLocation(View v)
    {

      /*  LinearLayout container=(LinearLayout)root.findViewById(R.id.filter_tag_container);
        String filter=tagsView.getText().toString().toLowerCase().trim();
        if(!supportTag.add(filter))
        {
            Toast.makeText(getActivity(),"Tag already present" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            TextView tv=new TextView(getActivity());
            tv.setText(filter);
            container.addView(tv);
        }*/
    }

    public void applyFilters(View v)
    {
        List<String> tag_list=new ArrayList<>();
        List<String> contract_list=new ArrayList<>();
        List<String> term_list=new ArrayList<>();
        List<String> field_list=new ArrayList<>();
        List<String> location_list=new ArrayList<>();
        List<String> salary_list=new ArrayList<>();

        LinearLayout tagcontainer=(LinearLayout)root.findViewById(R.id.filter_tag_container);
        if(tagcontainer.getChildCount()>0)
        {
            for(int i=0;i<tagcontainer.getChildCount();i++)
            {
                TextView tv=(TextView)tagcontainer.getChildAt(i);
                tag_list.add(tv.getText().toString());
            }
        }

        tagcontainer=(LinearLayout)root.findViewById(R.id.filter_field_container);
        if(tagcontainer.getChildCount()>0)
        {
            for(int i=0;i<tagcontainer.getChildCount();i++)
            {
                TextView tv=(TextView)tagcontainer.getChildAt(i);
                field_list.add(tv.getText().toString());
            }
        }

        tagcontainer=(LinearLayout)root.findViewById(R.id.filter_term_container);
        if(tagcontainer.getChildCount()>0)
        {
            for(int i=0;i<tagcontainer.getChildCount();i++)
            {
                TextView tv=(TextView)tagcontainer.getChildAt(i);
                term_list.add(tv.getText().toString());
            }
        }

        tagcontainer=(LinearLayout)root.findViewById(R.id.filter_contract_container);
        if(tagcontainer.getChildCount()>0)
        {
            for(int i=0;i<tagcontainer.getChildCount();i++)
            {
                TextView tv=(TextView)tagcontainer.getChildAt(i);
                contract_list.add(tv.getText().toString());
            }
        }

        tagcontainer=(LinearLayout)root.findViewById(R.id.filter_location_container);
        if(tagcontainer.getChildCount()>0)
        {
            for(int i=0;i<tagcontainer.getChildCount();i++)
            {
                TextView tv=(TextView)tagcontainer.getChildAt(i);
                location_list.add(tv.getText().toString().toLowerCase().trim());
            }
        }

        Integer pos=salarySpinner.getSelectedItemPosition();
        if(pos!=0)
        {

            salary_list.add(String.valueOf(pos));
            salary_list.add(editSalary.getText().toString());
        }


            mCallback.addFilter(tag_list,contract_list,term_list,field_list,location_list,salary_list);
            getDialog().dismiss();
    }


/******************************STRING ADAPTER**************************/
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

            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_text_element,parent,false);

            TextView text = (TextView)convertView.findViewById(R.id.text_view);
            text.setTextSize(15);
            text.setText(stringArray[position]);
            return convertView;
        }
    }

    public interface addFilter
    {
        void addFilter( List<String> tag_list,
        List<String> contract_list,
        List<String> term_list,
        List<String> field_list,
        List<String> location_list,
        List<String> salary_list);

    }

}

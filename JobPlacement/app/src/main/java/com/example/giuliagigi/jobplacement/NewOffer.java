package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewOffer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOffer extends Fragment implements DatePickerFragment.OnDataSetListener{


    private View root;

    private OnFragmentInteractionListener mListener;


   private EditText editDescriptionText=null;
   private TextView textView=null;
   private EditText editObject=null;
   private  EditText editSalary=null;
   private Button dateButton=null;
   private TextView validity=null;
   private Button publish=null;
   private EditText places=null;
    private EditText location=null;

    private Spinner fieldSpinner = null;
    private Spinner contractSpinner = null;
    private Spinner salarySpinner = null;
    private Spinner termSpinner =null;
    private ImageButton addtag=null;

    private MultiAutoCompleteTextView tagsView=null;

     private Map<String,Tag> retriveTag=null;

    Company company=null;
    GlobalData globalData=null;


    public static NewOffer newInstance() {
        NewOffer fragment = new NewOffer();

        return fragment;
    }

    public NewOffer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        globalData=(GlobalData)getActivity().getApplication();
        company=(Company)globalData.getUserObject();

        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_new_offer, container, false);

        editDescriptionText=(EditText)root.findViewById(R.id.DescriptionText);
        textView=(TextView)root.findViewById(R.id.countCharacter);
        editObject=(EditText)root.findViewById(R.id.offerObject);
        editSalary=(EditText)root.findViewById(R.id.offerSalary);
        dateButton=(Button)root.findViewById(R.id.dateButton);
        validity=(TextView)root.findViewById(R.id.validity_tv);
        publish=(Button)root.findViewById(R.id.publishOfferbutton);
        places=(EditText)root.findViewById(R.id.offerAvailability);
        location=(EditText)root.findViewById(R.id.offerLocation);
        addtag=(ImageButton)root.findViewById(R.id.addTagButton);

        fieldSpinner = (Spinner) root.findViewById(R.id.fieldOffer);
        contractSpinner = (Spinner) root.findViewById(R.id.typeContract);
        salarySpinner = (Spinner) root.findViewById(R.id.spinnerSalary);
        termSpinner = (Spinner) root.findViewById(R.id.spinnerDuration);

       tagsView = (MultiAutoCompleteTextView) root.findViewById(R.id.tagAutoComplete_tv);

        String[] typeOfFields=getResources().getStringArray(R.array.new_offer_fragment_fields);

        fieldSpinner.setAdapter(new StringAdapter(typeOfFields));

        String[] typeOfContracts=getResources().getStringArray(R.array.new_offer_fragment_typeOfContracts);
        contractSpinner.setAdapter(new StringAdapter(typeOfContracts));

        String[] salaries=getResources().getStringArray(R.array.new_offer_fragment_salary_spinner_content);
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



        String[] durations=getResources().getStringArray(R.array.new_offer_fragment_termContracts);
        termSpinner.setAdapter(new StringAdapter(durations));

        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    termSpinner.setPrompt("Per favore scrivi");
            }
        });

           final Fragment tf=this;

            dateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerFragment datapicker=new DatePickerFragment();
                    datapicker.setTargetFragment(tf,0);
                    datapicker.show(getFragmentManager(),"datapicker");
                }
            });


        //Conta caratteri
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                textView.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };


        //Gestione object obbligatorio
        final TextWatcher mobjectTextWhatcer = new TextWatcher() {
            int complete=0; //Vuoto

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(complete==0)
                {
                    if(s.length()>0)
                    {
                        complete=1;
                        editObject.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_tick,0);
                    }
                }
                if(s.length()==0)
                {
                    complete=0;
                    editObject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };


        editDescriptionText.addTextChangedListener(mTextEditorWatcher);
        editObject.addTextChangedListener(mobjectTextWhatcer);


                publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        publishOffer(v);
                    }
                });

        //multiautocompletetextview
        ParseQuery<Tag> query=ParseQuery.getQuery("Tag");
        List<Tag> tags=null;
        try {
            tags= query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

      String[] t=new String[tags.size()];

        final Set<String> support=new HashSet<String>();
        retriveTag=new HashMap<>();
        final Set<String> existent=new HashSet<>();

        for(int i=0;i<tags.size();i++)
        {
            t[i]=tags.get(i).getTag();
            retriveTag.put(tags.get(i).getTag().toLowerCase().trim(),tags.get(i));
            support.add(tags.get(i).getTag().toLowerCase().trim());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, t);

       tagsView.setAdapter(adapter);
       tagsView.setTokenizer(new SpaceTokenizer());
        tagsView.setThreshold(1);







        addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(support.add(tagsView.getText().toString().toLowerCase().trim())==false)
                {
                    if(existent.add(tagsView.getText().toString().toLowerCase().trim())==true) {
                        final GridLayout tagConntainer = (GridLayout) root.findViewById(R.id.tagContainder);

                        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mytagView = inflater.inflate(R.layout.taglayout, null);

                        mytagView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tagConntainer.removeView(v);
                                Toast.makeText(getActivity(),"Removed",Toast.LENGTH_SHORT ).show();
                            }
                        });

                        TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                        t.setText(tagsView.getText().toString());
                        tagConntainer.addView(mytagView);

                        tagsView.setText("");
                        Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT ).show();

                    }else Toast.makeText(getActivity().getApplicationContext(),"Existent tag",Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(getActivity().getApplicationContext(),"Wrong tag",Toast.LENGTH_SHORT).show();

            }
        });

        return root;

    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDataSet(GregorianCalendar gc) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateFormat.format(gc.getTime());


      validity.setText(dateFormat.format(gc.getTime()).toString());

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



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

    public void publishOffer(View v) {

            //Make all check

            if(editObject.getText().length()==0 ||
                  fieldSpinner.getSelectedItemPosition()==0  ||
                    places.getText().length()==0 ||
                    validity.getText().length()==0 ||
                    contractSpinner.getSelectedItemPosition()==0 ||
                    termSpinner.getSelectedItemPosition() ==0
                    )
            {

            Toast.makeText(getActivity(),"Missing field",Toast.LENGTH_SHORT ).show();
            }

         //Check for wrong thinks


else {
                int flag = 0;
                Integer p= null;
                Integer s=null;
                Date d=null;
                try {
                    p = Integer.parseInt(places.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    d= dateFormat.parse(validity.getText().toString());
                    Date today= Calendar.getInstance().getTime();
                    if(d.compareTo(today)<=0)
                    {
                        Toast.makeText(getActivity(),"Can't set this date",Toast.LENGTH_SHORT ).show();

                        throw new Exception() ;
                    }

                    if(salarySpinner.getSelectedItemPosition()!=0)
                    {
                        s=Integer.parseInt(editSalary.getText().toString());
                    }

                } catch (Exception e) {

                    flag = 1;
                    Toast.makeText(getActivity(),"Wrong data",Toast.LENGTH_SHORT ).show();
                }


                if (flag == 0){
                    CompanyOffer offer = new CompanyOffer();

                    offer.setObject(editObject.getText().toString());
                    offer.setWorkField(fieldSpinner.getSelectedItem().toString());
                    offer.setPositions(p);
                    offer.setContract(contractSpinner.getSelectedItem().toString());
                    offer.setValidity(d);
                    offer.setTerm(termSpinner.getSelectedItem().toString());
                    GlobalData gd=(GlobalData)getActivity().getApplication();

                    Company company=(Company)gd.getUserObject();

                    offer.setCompany(company);


                    if(salarySpinner.getSelectedItemPosition()!=0)
                    {
                        offer.setSalary(editSalary.getText().toString());
                    }
                    else
                    {
                        offer.setSalary("To be defined");
                    }

                    if(location.getText().length()==0)
                    {
                        offer.setLocation("");
                    }
                    else {
                        offer.setLocation(location.getText().toString());
                    }

                    if(editDescriptionText.getText().length()==0)
                    {
                        offer.setDescription("");
                    }
                    else {
                        offer.setDescription(editDescriptionText.getText().toString());
                    }

                    final GridLayout tagContainer=(GridLayout)root.findViewById(R.id.tagContainder);

                    int n_tags=tagContainer.getChildCount();
                    for(int i=0;i<n_tags;i++)
                    {
                        View tv=tagContainer.getChildAt(i);
                        TextView t=(TextView)tv.findViewById(R.id.tag_tv);

                        Tag tag=retriveTag.get(t.getText().toString().toLowerCase().trim());
                        offer.addTag(tag);

                    }



                    company.addOffer(offer);
                    offer.saveInBackground();
                    Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT ).show();

                  ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Home");
                   getFragmentManager().popBackStackImmediate();

                }


            }

        }
 }


package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompanyEditOfferFragment extends Fragment implements DatePickerFragment.OnDataSetListener {


    private static final String BUNDLE_IDENTIFIER_HEADER = "offerNew_bundle";
    private static final String BUNDLE_IDENTIFIER_TAIL_KEY = "offerNew_bundle_id_tail";

    Company company = null;
    CompanyOffer offer = null;
    GlobalData globalData = null;

    private boolean editMode = true;
    private boolean justCreated = false;

    private View root;
    private MenuItem saveToolbarButton = null;
    private EditText editDescriptionText = null;
    private TextView textView = null;
    private EditText editObject = null;
    private EditText editSalary = null;
    private Button dateButton = null;
    private TextView validity = null;
    //Button and its children

    private FloatingActionButton publishFloatingButton = null;
    private FloatingActionButton modifyFloatingButton = null;
    private FloatingActionButton deleteFloatingButton = null;
    private FloatingActionButton viewAppliesFloatingButton = null;
    private FloatingActionButton saveFloatingButton = null;
    private FloatingActionsMenu floatingActionMenu = null;

    private EditText places = null;
    private EditText nation = null;
    private EditText city = null;

    private Spinner fieldSpinner = null;
    private Spinner contractSpinner = null;
    private Spinner salarySpinner = null;
    private Spinner termSpinner = null;
    private ImageButton addtag = null;


    private String[] typeOfFields;
    private String[] typeOfContracts;
    private String[] salaries;
    private String[] durations;

    private MultiAutoCompleteTextView tagsView = null;

    private Map<String, Tag> retriveTag = null;
    final Set<String> existent = new HashSet<>();

    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS -------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    public static CompanyEditOfferFragment newInstance(boolean editMode, CompanyOffer offer) {

        CompanyEditOfferFragment fragment = new CompanyEditOfferFragment();
        fragment.editMode = editMode;
        fragment.offer = offer;

        if(fragment.offer.getStatus() == null){

            fragment.justCreated = true;
            fragment.offer.setStatus(CompanyOffer.STATUS_NEW);
            fragment.offer.saveInBackground();
        }

        return fragment;
    }

    public CompanyEditOfferFragment() {
    }


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        globalData = (GlobalData) getActivity().getApplication();
        company = (Company) globalData.getUserObject();

        if (savedInstanceState != null) {

            Boolean tmp = savedInstanceState.getBoolean("editMode");

            if (tmp) {

                editMode = true;
            }
        }

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteProfile);
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteProfile));

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_offer, container, false);

        editDescriptionText = (EditText) root.findViewById(R.id.DescriptionText);
        textView = (TextView) root.findViewById(R.id.countCharacter);
        editObject = (EditText) root.findViewById(R.id.offerObject);
        editSalary = (EditText) root.findViewById(R.id.offerSalary);
        dateButton = (Button) root.findViewById(R.id.dateButton);
        validity = (TextView) root.findViewById(R.id.validity_tv);

        publishFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_publish);
        modifyFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_modify);
        deleteFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_delete);
        viewAppliesFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_seeApplications);
        saveFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_save);
        floatingActionMenu = (FloatingActionsMenu) root.findViewById(R.id.new_offer_floating_menu);

        places = (EditText) root.findViewById(R.id.offerAvailability);
        nation = (EditText) root.findViewById(R.id.new_offer_edit_nation);
        city = (EditText) root.findViewById(R.id.new_offer_edit_city);


        addtag = (ImageButton) root.findViewById(R.id.addTagButton);
        tagsView = (MultiAutoCompleteTextView) root.findViewById(R.id.tagAutoComplete_tv);

        fieldSpinner = (Spinner) root.findViewById(R.id.fieldOffer);
        typeOfFields = getResources().getStringArray(R.array.new_offer_fragment_fields);
        fieldSpinner.setAdapter(new StringAdapter(typeOfFields));

        contractSpinner = (Spinner) root.findViewById(R.id.typeContract);
        typeOfContracts = getResources().getStringArray(R.array.new_offer_fragment_typeOfContracts);
        contractSpinner.setAdapter(new StringAdapter(typeOfContracts));

        salarySpinner = (Spinner) root.findViewById(R.id.spinnerSalary);
        salaries = getResources().getStringArray(R.array.new_offer_fragment_salary_spinner_content);
        salarySpinner.setAdapter(new StringAdapter(salaries));

        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    editSalary.setText("");
                    editSalary.setEnabled(false);

                } else {
                    editSalary.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        termSpinner = (Spinner) root.findViewById(R.id.spinnerDuration);
        durations = getResources().getStringArray(R.array.new_offer_fragment_termContracts);
        termSpinner.setAdapter(new StringAdapter(durations));


        final Fragment tf = this;
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datapicker = new DatePickerFragment();
                datapicker.setTargetFragment(tf, 0);
                datapicker.show(getFragmentManager(), "datapicker");
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
            int complete = 0; //Vuoto

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (complete == 0) {
                    if (s.length() > 0) {
                        complete = 1;
                        editObject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }
                }
                if (s.length() == 0) {
                    complete = 0;
                    editObject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };

        editDescriptionText.addTextChangedListener(mTextEditorWatcher);
        editObject.addTextChangedListener(mobjectTextWhatcer);


        //multiautocompletetextview
        ParseQuery<Tag> query = ParseQuery.getQuery("Tag");
        List<Tag> tags = null;
        try {
            tags = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] t = new String[tags.size()];

        final Set<String> support = new HashSet<String>();
        retriveTag = new HashMap<>();


        for (int i = 0; i < tags.size(); i++) {
            t[i] = tags.get(i).getTag();
            retriveTag.put(tags.get(i).getTag().toLowerCase().trim(), tags.get(i));
            support.add(tags.get(i).getTag().toLowerCase().trim());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, t);

        tagsView.setAdapter(adapter);
        tagsView.setTokenizer(new SpaceTokenizer());
        tagsView.setThreshold(1);

        addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (support.contains((String) tagsView.getText().toString().toLowerCase().trim())) {

                    if (existent.add(tagsView.getText().toString().toLowerCase().trim())) {

                        final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);

                        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mytagView = inflater.inflate(R.layout.taglayout, null);

                        mytagView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tv = (TextView) v.findViewById(R.id.tag_tv);
                                existent.remove((tv.getText().toString().toLowerCase().trim()));
                                tagContainer.removeView(v);
                                Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();


                            }
                        });

                        TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                        t.setText(tagsView.getText().toString());
                        tagContainer.addView(mytagView);

                        tagsView.setText("");
                        addtag.setBackgroundColor(Color.DKGRAY);

                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_added), Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.new_offer_fragment_existenTtag), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.new_offer_fragment_WrongTag), Toast.LENGTH_SHORT).show();

            }
        });


        tagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                addtag.setBackgroundColor(Color.BLUE);
            }
        });


        publishFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                publish();
            }
        });


        modifyFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = true;
                switchEditMode();
            }
        });

        deleteFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOffer();
            }
        });

        viewAppliesFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewAppliedStudents();
            }
        });

        saveFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
            }
        });


        if (!editMode) {

            editDescriptionText.setText(offer.getDescription());
            textView.setEnabled(false);
            editObject.setText(offer.getOfferObject());
            Integer salary = offer.getSAlARY();
            if (salary != -1) {
                editSalary.setText(String.valueOf(salary));
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(offer.getValidity());

            validity.setText(date);
            places.setText(offer.getnPositions().toString());
            nation.setText(offer.getNation());
            city.setText(offer.getCity());
            int i;

            for (i = 0; i < typeOfFields.length; i++) {
                if (typeOfFields[i].toLowerCase().trim().equals(offer.getWorkField().toLowerCase().trim())) {
                    fieldSpinner.setSelection(i);
                    break;
                }
                fieldSpinner.setSelection(0);

            }


            for (i = 0; i < typeOfContracts.length; i++) {
                if (typeOfContracts[i].toLowerCase().trim().equals(offer.getContract().toLowerCase().trim())) {
                    contractSpinner.setSelection(i);
                    break;
                }
                contractSpinner.setSelection(0);
            }


            for (i = 0; i < salaries.length; i++) {
                if (salaries[i].toLowerCase().trim().equals(String.valueOf(offer.getSAlARY()).trim())) {
                    salarySpinner.setSelection(i);
                    break;
                }
                salarySpinner.setSelection(0);
            }


            for (i = 0; i < durations.length; i++) {
                if (durations[i].toLowerCase().trim().equals(offer.getTerm().toLowerCase().trim())) {
                    termSpinner.setSelection(i);
                    break;
                }
                termSpinner.setSelection(0);
            }


            /****TAGS*****/

            final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);
            tagContainer.setEnabled(false);

            if (offer.getTags().isEmpty()) {
                tagContainer.setVisibility(View.INVISIBLE);
            } else {
                LayoutInflater tag_inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (Tag tag : offer.getTags()) {
                    final Tag tmp = tag;
                    View mytagView = tag_inflater.inflate(R.layout.taglayout, null);
                    mytagView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagContainer.removeView(v);
                            Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                            existent.remove(tmp.getTag().toLowerCase().trim());
                        }
                    });
                    mytagView.setEnabled(false);

                    TextView tag_tv = (TextView) mytagView.findViewById(R.id.tag_tv);
                    tag_tv.setText(tag.getTag());
                    tagContainer.addView(mytagView);
                    existent.add(tag.getTag().toLowerCase().trim());
                }
            }
        }

        switchEditMode();
        return root;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            Boolean tmp_state = savedInstanceState.getBoolean("editMode");

            if (tmp_state) {

                editMode = true;
                editObject.setText(savedInstanceState.getString("objectOffer"));
                fieldSpinner.setSelection(savedInstanceState.getInt("workField"));
                places.setText(savedInstanceState.getString("places"));
                contractSpinner.setSelection(savedInstanceState.getInt("contract"));
                validity.setText(savedInstanceState.getString("validity"));
                termSpinner.setSelection(savedInstanceState.getInt("term"));
                nation.setText(savedInstanceState.getString("nation"));
                city.setText(savedInstanceState.getString("city"));
                Integer pos = savedInstanceState.getInt("posSalary");
                salarySpinner.setSelection(pos);

                if (pos != 0) {

                    editSalary.setText(savedInstanceState.getString("salary"));
                } else editSalary.setText("");

                editDescriptionText.setText(savedInstanceState.getString("description"));
                ArrayList<String> tmp = savedInstanceState.getStringArrayList("taglist");

                final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);
                LayoutInflater tag_inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (String tag : tmp) {

                    final String text_tag = tag;
                    View mytagView = tag_inflater.inflate(R.layout.taglayout, null);
                    mytagView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagContainer.removeView(v);
                            Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
                            existent.remove(text_tag.toLowerCase().trim());
                        }
                    });


                    TextView tag_tv = (TextView) mytagView.findViewById(R.id.tag_tv);
                    tag_tv.setText(tag);
                    tagContainer.addView(mytagView);
                    existent.add(tag.toLowerCase().trim());
                }


            }
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("editMode", editMode);

        outState.putString("objectOffer", editObject.getText().toString());
        outState.putInt("workField", fieldSpinner.getSelectedItemPosition());
        outState.putString("places", places.getText().toString());
        outState.putInt("contract", contractSpinner.getSelectedItemPosition());
        outState.putString("validity", validity.getText().toString());
        outState.putInt("term", termSpinner.getSelectedItemPosition());
        outState.putString("nation", nation.getText().toString());
        outState.putString("city", city.getText().toString());
        outState.putInt("posSalary", salarySpinner.getSelectedItemPosition());
        outState.putString("salary", editSalary.getText().toString());
        outState.putString("description", editDescriptionText.getText().toString());

        //elimino i tag salvati

        ArrayList<String> tmp = new ArrayList<>();

        final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);

        int n_tags = tagContainer.getChildCount();
        for (int i = 0; i < n_tags; i++) {
            View tv = tagContainer.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);

            Tag tag = retriveTag.get(t.getText().toString().toLowerCase().trim());
            tmp.add(tag.getTag());
            offer.getTags().remove(tag);
            existent.remove(tag.getTag().toLowerCase().trim());

        }


        tagContainer.removeAllViews();

        outState.putStringArrayList("taglist", tmp);

    }



    @Override
    public void onDataSet(GregorianCalendar gc) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateFormat.format(gc.getTime());

        validity.setText(dateFormat.format(gc.getTime()).toString());

    }



    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- MENU CALLBACKS ------------------------------------------------------------------------------ */
    /* --------------------------------------------------------------------------------------------------------------------- */



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_offer, menu);

        saveToolbarButton = (MenuItem) menu.findItem(R.id.action_save);

        if (!editMode) {
            saveToolbarButton.setVisible(false);
            saveToolbarButton.setEnabled(false);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_save) {

            saveChanges();
        }
        return true;
    }



    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- AUXILIARY METHODS --------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    // ----------------- edit mode switching ------------------------------------------------------
    private void switchEditMode() {

        fieldSpinner.setEnabled(editMode);
        contractSpinner.setEnabled(editMode);
        salarySpinner.setEnabled(editMode);
        termSpinner.setEnabled(editMode);

        editDescriptionText.setEnabled(editMode);
        textView.setEnabled(editMode);
        editObject.setEnabled(editMode);
        editSalary.setEnabled(editMode && salarySpinner.getSelectedItemPosition() != 0);
        dateButton.setEnabled(editMode);
        validity.setEnabled(editMode);
        places.setEnabled(editMode);
        nation.setEnabled(editMode);
        city.setEnabled(editMode);

        CardView cardView = (CardView) root.findViewById(R.id.card_AddTag);

        cardView.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
        addtag.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
        addtag.setEnabled(editMode);
        tagsView.setEnabled(editMode);

        final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);
        tagContainer.setEnabled(editMode);
        tagContainer.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);

        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            tagContainer.getChildAt(i).setEnabled(editMode);
        }

        configureFloatingButtons();
    }


    // -------------- setting floating action menu ------------------------------------------------
    private void configureFloatingButtons() {

        if(saveToolbarButton != null){

            saveToolbarButton.setVisible(editMode);
            saveToolbarButton.setEnabled(editMode);
        }

        saveFloatingButton.setEnabled(editMode);
        saveFloatingButton.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);


        // "view applications" button is disabled if the offer is not published yet or we are in editmode
        boolean flag1 = !(offer.getStatus().equals(CompanyOffer.STATUS_CREATED)
                || offer.getStatus().equals(CompanyOffer.STATUS_NEW)
                || editMode);

        viewAppliesFloatingButton.setEnabled(flag1);
        viewAppliesFloatingButton.setVisibility(flag1 ? View.VISIBLE : View.INVISIBLE);


        // "publish" and "modify" must be disabled if the offer is already published
        boolean flag2 = !(offer.getStatus().equals(CompanyOffer.STATUS_PUBLISHED) || editMode);

        publishFloatingButton.setEnabled(flag2);
        publishFloatingButton.setVisibility(flag2 ? View.VISIBLE : View.INVISIBLE);

        modifyFloatingButton.setEnabled(flag2);
        modifyFloatingButton.setVisibility(flag2 ? View.VISIBLE : View.INVISIBLE);

    }


    // --------------- offer creation and save ----------------------------------------------------
    public void saveChanges() {

        boolean correct = true;
        String errorMessage = getString(R.string.error) + ":\n";
        // ****** preliminary checks ********
        if (editObject.getText().length() == 0 ||
                fieldSpinner.getSelectedItemPosition() == 0 ||
                places.getText().length() == 0 ||
                validity.getText().length() == 0 ||
                contractSpinner.getSelectedItemPosition() == 0 ||
                termSpinner.getSelectedItemPosition() == 0 ||
                nation.getText().length() != 0 && city.getText().length() == 0 ||
                nation.getText().length() == 0 && city.getText().length() != 0) {

            correct = false;
            errorMessage += getString(R.string.new_offer_missing_field_error) + "\n";
        }

        // ****** correction checks ********
        Integer p = null;
        Integer s = null;
        Date d = null;

        try {

            p = Integer.parseInt(places.getText().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            d = dateFormat.parse(validity.getText().toString());
            Date today = Calendar.getInstance().getTime();

            if (d.compareTo(today) <= 0) {

                correct = false;
                errorMessage += getString(R.string.new_offer_wrong_date_error) + "\n";
            }

            if (salarySpinner.getSelectedItemPosition() != 0) {
                s = Integer.parseInt(editSalary.getText().toString());
            }

        }
        catch (Exception e) {

            errorMessage += getString(R.string.new_offer_wrong_type_error) + "\n";
        }

        // ********* if some error, show a dialog and terminate *********
        if(!correct){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            View view = getActivity().getLayoutInflater().inflate(R.layout.error_message_generic, null);
            LinearLayout container = (LinearLayout) view.findViewById(R.id.error_message_container);

            TextView message = new TextView(globalData);
            message.setTextColor(getResources().getColor(R.color.black_light_transparent));

            message.setText(errorMessage);
            container.addView(message);

            builder.setView(view);
            builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
            return;
        }


        // ****** if everything correct ********
        offer.setObject(editObject.getText().toString());
        offer.setWorkField(fieldSpinner.getSelectedItem().toString());
        offer.setPositions(p);
        offer.setContract(contractSpinner.getSelectedItem().toString());
        offer.setValidity(d);
        offer.setTerm(termSpinner.getSelectedItem().toString());

        if(offer.getStatus().equals(CompanyOffer.STATUS_NEW))
            offer.setStatus(CompanyOffer.STATUS_CREATED);

        Company company = (Company) globalData.getUserObject();
        offer.setCompany(company);

        if (salarySpinner.getSelectedItemPosition() != 0) {

            Integer salary = Integer.parseInt(editSalary.getText().toString());
            offer.setSalary(salary);
        }
        else {

            offer.setSalary(-1);
        }

        if (nation.getText().length() > 0 && city.getText().length() > 0) {

            //Creo il geopoint
            //make a geoPoint

            String geoAddress = nation.getText().toString() + ", " + city.getText().toString();
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addressList = null;

            try {
                addressList = geocoder.getFromLocationName(geoAddress, 5);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.isEmpty()) {
                //do nothing
            }
            else {
                Address a = addressList.get(0);
                ParseGeoPoint geoPoint = new ParseGeoPoint(a.getLatitude(), a.getLongitude());

                offer.setLocation(geoPoint);
                offer.setNation(nation.getText().toString());
                offer.setCity(city.getText().toString());
            }
        }


        if (editDescriptionText.getText().length() == 0)
            offer.setDescription("");
        else
            offer.setDescription(editDescriptionText.getText().toString());


        final GridLayout tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);
        int n_tags = tagContainer.getChildCount();

        for (int i = 0; i < n_tags; i++) {
            View tv = tagContainer.getChildAt(i);
            TextView t = (TextView) tv.findViewById(R.id.tag_tv);

            Tag tag = retriveTag.get(t.getText().toString().toLowerCase().trim());
            offer.addTag(tag);

        }

        final CompanyOffer obj = offer;

        offer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                obj.getCompany().addOffer(obj);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView message = new TextView(globalData);
        message.setTextColor(getResources().getColor(R.color.black_light_transparent));

        if(justCreated){

            message.setText(R.string.offer_detail_created_message);
            justCreated = false;
        }
        else
            message.setText(R.string.offer_detail_saved_message);


        builder.setView(message);
        builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editMode = false;
                switchEditMode();
            }
        });

        builder.create().show();

    }


    // ------------------ offer publication -----------------------------------------------------
    public void publish() {

        // 1) ---------- save offer status
        offer.setStatus(CompanyOffer.STATUS_PUBLISHED);
        offer.saveInBackground();


        // 2) ---------- send push notifications to students
        try {

            /* first find degrees inherent to the offer */
            ParseQuery degreeQuery = new ParseQuery("Degree");
            degreeQuery.whereEqualTo(Degree.STUDIES_FIELD, offer.getWorkField());
            ArrayList<Degree> degrees = new ArrayList<>();

            degrees.addAll(degreeQuery.find());


            if (!degrees.isEmpty()) {

                /* then find owners of degrees among students */
                ParseQuery studentsQuery = new ParseQuery("Student");
                studentsQuery.whereContainedIn("degrees", degrees);

                ArrayList<Student> studentsToNotify = new ArrayList<>();
                studentsToNotify.addAll(studentsQuery.find());


                if (!studentsToNotify.isEmpty()) {

                    /* then find users corresponding to students */
                    ParseQuery parseUserQuery = new ParseQuery("_User");
                    parseUserQuery.whereContainedIn("student", studentsToNotify);
                    ArrayList<ParseUser> usersToNotify = new ArrayList<>();
                    usersToNotify.addAll(parseUserQuery.find());

                    /* finally, find devices for push notifications */
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereContainedIn(Installation.USER_FIELD, usersToNotify);


                    /* proceed to send push notifications */
                    Company company = null;
                    company = offer.getCompany().fetchIfNeeded();

                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
                    push.setMessage("" + getString(R.string.Message_theCompany) + " " + company.getName() + " " + getString(R.string.Message_newOffer));
                    push.sendInBackground();
                }
            }

            News news = new News();
            news.createNews(0, offer, null, null, globalData);

                            /* Invio notifica push a studenti */
            ParsePush push = new ParsePush();
            push.setChannel(User.TYPE_STUDENT);
            push.setMessage(globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.new_job_offer_message) + " \"" + offer.getOfferObject() + "\"");
            push.sendInBackground();

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        // 3) ---------- make sure the user understands by means of a modal window
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView message = new TextView(globalData);
        message.setTextColor(getResources().getColor(R.color.black_light_transparent));


        message.setText(R.string.offer_detail_published_message);


        builder.setView(message);
        builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                configureFloatingButtons();
            }
        });

        builder.create().show();
    }



    // ------------------- offer deletion --------------------------------------------------------
    public void deleteOffer() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.warning_message_generic, null);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.warning_message_container);


        TextView warningMessage = new TextView(globalData);
        warningMessage.setTextColor(getResources().getColor(R.color.black_light_transparent));

        warningMessage.setText(R.string.offer_detail_delete_warning);
        container.addView(warningMessage);


        builder.setView(view);
        builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (offer.getStatus().equals(CompanyOffer.STATUS_NEW)) {

                    Toast.makeText(getActivity(), R.string.offer_detail_not_created_message, Toast.LENGTH_SHORT).show();
                    offer.deleteInBackground();
                }
                else {

                    if (offer.getApplications().isEmpty()) {

                        offer.deleteInBackground();
                        Toast.makeText(getActivity(), R.string.offer_detail_deleted_message, Toast.LENGTH_SHORT).show();
                    }
                    else {

                        offer.setStatus(CompanyOffer.STATUS_REVOKED);
                    }
                }

                News news = new News();
                news.createNews(News.TYPE_OFFER_DELETED, offer, null, null, globalData);

                /* Invio notifica push a studenti */
                ParsePush push = new ParsePush();
                push.setChannel(User.TYPE_STUDENT);
                push.setMessage(globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.deleted_job_offer_message) + "\"" + offer.getOfferObject() + "\"");
                push.sendInBackground();

                // return to home
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TabHomeCompanyFragment fragment = TabHomeCompanyFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack("Students")
                        .commit();
            }
        });

        builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }


    // ------------- view applications ----------------------------------------------------
    public void viewAppliedStudents() {

        ArrayList<StudentApplication> applications = offer.getApplications();

        if (!applications.isEmpty()) {

            ArrayList<Student> students = new ArrayList<Student>();
            for (StudentApplication a : applications){

                try {
                    a.getStudent().fetchIfNeeded();
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                students.add(a.getStudent());
            }

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            //New Fragment
            ApplicationsListFragment fragment = ApplicationsListFragment.newInstance(offer);
            // Insert the fragment by replacing any existing fragment

            fragmentManager.beginTransaction()
                    .replace(R.id.tab_Home_container, fragment)
                    .addToBackStack("Students")
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            Toolbar toolbar = globalData.getToolbar();
            toolbar.setTitle(offer.getOfferObject());

        }
        else
            Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_NoStudents), Toast.LENGTH_SHORT).show();

    }


    public Boolean isInEditMode() {
        return editMode;
    }



    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- ADAPTERS ------------------------------------------------------------------------------------ */
    /* --------------------------------------------------------------------------------------------------------------------- */


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

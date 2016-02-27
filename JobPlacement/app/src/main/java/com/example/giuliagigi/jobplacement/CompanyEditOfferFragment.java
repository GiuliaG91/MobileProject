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
import android.util.Log;
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

public class CompanyEditOfferFragment extends Fragment implements DatePickerFragment.OnDataSetListener {

    private static final String BUNDLE_KEY_OFFER = "Offer";
    private static final String BUNDLE_KEY_OBJECT = "Object";
    private static final String BUNDLE_KEY_FIELD = "Field";
    private static final String BUNDLE_KEY_PLACES = "Places";
    private static final String BUNDLE_KEY_VALIDITY = "Validity";
    private static final String BUNDLE_KEY_TERM = "Term";
    private static final String BUNDLE_KEY_CITY = "City";
    private static final String BUNDLE_KEY_NATION = "Nation";
    private static final String BUNDLE_KEY_CONTRACT = "Contract";
    private static final String BUNDLE_KEY_SALARY_TYPE = "Salary_Type";
    private static final String BUNDLE_KEY_SALARY = "Salary";
    private static final String BUNDLE_KEY_DESCRIPTION = "Description";
    private static final String BUNDLE_KEY_TAGS = "Tags";
    private static final String BUNDLE_KEY_EDIT_MODE = "Edit_Mode";

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
    private TextView descriptionCountTextView = null;
    private EditText editObject = null;
    private EditText editSalary = null;
    private Button dateButton = null;
    private TextView validity = null;
    private GridLayout tagContainer = null;
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
    private ImageButton addTagButton = null;


    private HashMap<String, Tag> allTags;
    private HashSet<String> currentTags;

    private MultiAutoCompleteTextView tagsView = null;


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
//            fragment.offer.saveInBackground();
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

        allTags = globalData.getTags();
        currentTags = new HashSet<>();

        if (savedInstanceState != null) {

            String tail = savedInstanceState.getString(BUNDLE_IDENTIFIER_TAIL_KEY);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER_HEADER + tail);

            if(bundle != null){

                Log.println(Log.ASSERT, "NEW OFFER", "onCreate with bundle: " + BUNDLE_IDENTIFIER_HEADER + tail);
                offer = (CompanyOffer)bundle.get(BUNDLE_KEY_OFFER);
                editMode = bundle.getBoolean(BUNDLE_KEY_EDIT_MODE);
            }
        }


        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteNewOffer);
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteNewOffer));

        root = inflater.inflate(R.layout.fragment_new_offer, container, false);


        /* --------------- object --------------------------------------------------------------- */
        editObject = (EditText) root.findViewById(R.id.offerObject);
        editObject.addTextChangedListener(new TextWatcher() {

            boolean complete = false;

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!complete) {
                    if (s.length() > 0) {
                        complete = true;
                        editObject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                    }
                }
                if (s.length() == 0) {
                    complete = false;
                    editObject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

        });


        /* --------------- field places contract ------------------------------------------------ */
        fieldSpinner = (Spinner) root.findViewById(R.id.fieldOffer);
        fieldSpinner.setAdapter(new StringAdapter(CompanyOffer.TYPE_WORK_FIELD_TRANSLATED));

        places = (EditText) root.findViewById(R.id.offerAvailability);

        contractSpinner = (Spinner) root.findViewById(R.id.typeContract);
        contractSpinner.setAdapter(new StringAdapter(CompanyOffer.TYPE_CONTRACT_FIELD_TRANSLATED));


        /* ------------------ date -------------------------------------------------------------- */
        dateButton = (Button) root.findViewById(R.id.dateButton);
        validity = (TextView) root.findViewById(R.id.validity_tv);

        final Fragment tf = this;
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datapicker = new DatePickerFragment();
                datapicker.setTargetFragment(tf, 0);
                datapicker.show(getFragmentManager(), "datapicker");
            }
        });

        /* ------------------- salary ------------------------------------------------------------*/
        editSalary = (EditText) root.findViewById(R.id.offerSalary);
        salarySpinner = (Spinner) root.findViewById(R.id.spinnerSalary);
        salarySpinner.setAdapter(new StringAdapter(CompanyOffer.TYPE_SALARY_TYPE_FIELD_TRANSLATED));

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

        /* ------------------- term nation city --------------------------------------------------*/
        termSpinner = (Spinner) root.findViewById(R.id.spinnerDuration);
        termSpinner.setAdapter(new StringAdapter(CompanyOffer.TYPE_TERM_FIELD_TRANSLATED));

        nation = (EditText) root.findViewById(R.id.new_offer_edit_nation);
        city = (EditText) root.findViewById(R.id.new_offer_edit_city);


        /* --------------- description ---------------------------------------------------------- */
        editDescriptionText = (EditText) root.findViewById(R.id.DescriptionText);
        descriptionCountTextView = (TextView) root.findViewById(R.id.countCharacter);

        editDescriptionText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                descriptionCountTextView.setText(String.valueOf(s.length()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });


        /* ----------------- tags --------------------------------------------------------------- */

        // 1) multiautocompleteview ----
        tagsView = (MultiAutoCompleteTextView) root.findViewById(R.id.tagAutoComplete_tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<String>(allTags.keySet()));

        tagsView.setAdapter(adapter);
        tagsView.setTokenizer(new SpaceTokenizer());
        tagsView.setThreshold(1);

        tagsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // when a tag is selected, highlight the button
                addTagButton.setBackgroundColor(Color.BLUE);
            }
        });


        // 2) add tag button ----
        addTagButton = (ImageButton) root.findViewById(R.id.addTagButton);
        tagContainer = (GridLayout) root.findViewById(R.id.tagContainder);

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allTags.keySet().contains((String) tagsView.getText().toString().trim())) {

                    if (!currentTags.contains(tagsView.getText().toString().trim())) {

                        addTag(tagsView.getText().toString().trim());

                        // cleaning
                        tagsView.setText("");
                        addTagButton.setBackgroundColor(Color.DKGRAY);
                        Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_added), Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.new_offer_fragment_existenTtag), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.new_offer_fragment_WrongTag), Toast.LENGTH_SHORT).show();

            }
        });


        /* ------------------- floating action menu ----------------------------------------------*/
        floatingActionMenu = (FloatingActionsMenu) root.findViewById(R.id.new_offer_floating_menu);


        // 1) publish ----
        publishFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_publish);
        publishFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                publish();
            }
        });


        // 2) modify ----
        modifyFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_modify);
        modifyFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = true;
                switchEditMode();
            }
        });


        // 3) delete ----
        deleteFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_delete);
        deleteFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOffer();
            }
        });


        // 4) view applications ----
        viewAppliesFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_seeApplications);
        viewAppliesFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewAppliedStudents();
            }
        });


        // 5) save ----
        saveFloatingButton = (FloatingActionButton) root.findViewById(R.id.floatingButton_save);
        saveFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
            }
        });


        /* -------------- setting view content -------------------------------------------------- */
        if(savedInstanceState == null && !offer.getStatus().equals(CompanyOffer.STATUS_NEW))
            setViewContentWithAvailableData();

        else if (savedInstanceState != null)
            restoreViews(savedInstanceState);


        switchEditMode();
        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        String tail = offer.toString();
        outState.putString(BUNDLE_IDENTIFIER_TAIL_KEY, tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER_HEADER + tail);
        Log.println(Log.ASSERT, "NEW OFFER", "saving to bundle: " + BUNDLE_IDENTIFIER_HEADER + tail);

        bundle.put(BUNDLE_KEY_OFFER, offer);
        bundle.putBoolean(BUNDLE_KEY_EDIT_MODE, editMode);

        bundle.putString(BUNDLE_KEY_OBJECT, editObject.getText().toString());
        bundle.putInt(BUNDLE_KEY_FIELD, fieldSpinner.getSelectedItemPosition());
        bundle.putString(BUNDLE_KEY_PLACES, places.getText().toString());
        bundle.putString(BUNDLE_KEY_VALIDITY, validity.getText().toString());
        bundle.putInt(BUNDLE_KEY_CONTRACT, contractSpinner.getSelectedItemPosition());
        bundle.putInt(BUNDLE_KEY_TERM, termSpinner.getSelectedItemPosition());
        bundle.putString(BUNDLE_KEY_NATION, nation.getText().toString());
        bundle.putString(BUNDLE_KEY_CITY, city.getText().toString());
        bundle.putInt(BUNDLE_KEY_SALARY_TYPE, salarySpinner.getSelectedItemPosition());
        bundle.putString(BUNDLE_KEY_SALARY, editSalary.getText().toString());
        bundle.putString(BUNDLE_KEY_DESCRIPTION, editDescriptionText.getText().toString());

        bundle.putList(BUNDLE_KEY_TAGS, new ArrayList<String>(currentTags));
        tagContainer.removeAllViews();
        currentTags.clear();
    }



    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- DATE PICKER IMPLEMENTATION ------------------------------------------------------------------ */
    /* --------------------------------------------------------------------------------------------------------------------- */

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

    // ---------------- at view creation, if the offer already exists ------------------------------
    private void setViewContentWithAvailableData(){

        Log.println(Log.ASSERT, "NEW OFFER", "offer not new. no rotation");

        if(offer == null){

            Log.println(Log.ASSERT, "NEW OFFER", "Attention: trying to set view content with a null offer object");
            return;
        }

        if(offer.getOfferObject() != null)
            editObject.setText(offer.getOfferObject());

        if(offer.getWorkField() != null)
            fieldSpinner.setSelection(CompanyOffer.getWorkFieldIndex(offer.getWorkField()));

        if(offer.getnPositions() != null)
            places.setText(String.format("%d", offer.getnPositions()));

        if(offer.getContract() != null)
            contractSpinner.setSelection(CompanyOffer.getContractFieldIndex(offer.getContract()));

        if(offer.getTerm() != null)
            termSpinner.setSelection(CompanyOffer.getTermFieldIndex(offer.getTerm()));

        if(offer.getNation() != null)
            nation.setText(offer.getNation());

        if(offer.getCity() != null)
            city.setText(offer.getCity());

        if(offer.getSalaryType() != null){

            salarySpinner.setSelection(CompanyOffer.getSalaryTypeFieldIndex(offer.getSalaryType()));

            if(offer.getSalary() != null)
                editSalary.setText(String.format("%d", offer.getSalary()));
        }

        if(offer.getDescription() != null)
            editDescriptionText.setText(offer.getDescription());

        if(offer.getTags() != null)
            for(Tag t: offer.getTags())
                addTag(t.getTag());

        if(offer.getValidity() != null){

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormat.format(offer.getValidity());
            validity.setText(date);
        }
    }



    // ------------------ restoring view content after rotation ------------------------------------
    private void restoreViews(Bundle savedInstanceState){

        Log.println(Log.ASSERT, "NEW OFFER", "restoring after rotation");

        if(savedInstanceState == null)
            return;

        String tail = savedInstanceState.getString(BUNDLE_IDENTIFIER_TAIL_KEY);
        MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER_HEADER + tail);

        if(bundle == null)
            return;

        editObject.setText(bundle.getString(BUNDLE_KEY_OBJECT));
        fieldSpinner.setSelection(bundle.getInt(BUNDLE_KEY_FIELD));
        places.setText(bundle.getString(BUNDLE_KEY_PLACES));
        contractSpinner.setSelection(bundle.getInt(BUNDLE_KEY_CONTRACT));
        termSpinner.setSelection(bundle.getInt(BUNDLE_KEY_TERM));
        nation.setText(bundle.getString(BUNDLE_KEY_NATION));
        city.setText(bundle.getString(BUNDLE_KEY_CITY));
        salarySpinner.setSelection(bundle.getInt(BUNDLE_KEY_SALARY_TYPE));
        editSalary.setText(bundle.getString(BUNDLE_KEY_SALARY));
        editDescriptionText.setText(bundle.getString(BUNDLE_KEY_DESCRIPTION));
        validity.setText(bundle.getString(BUNDLE_KEY_VALIDITY));

        ArrayList<String> retrievedTags = new ArrayList<>();
        for (Object o : bundle.getList(BUNDLE_KEY_TAGS))
            retrievedTags.add((String)o);

        for (String t: retrievedTags)
            if(!currentTags.contains(t))
                addTag(t);
    }


    // ----------------- edit mode switching -------------------------------------------------------
    private void switchEditMode() {

        Log.println(Log.ASSERT, "NEW OFFER", "setting editmode = " + editMode);

        fieldSpinner.setEnabled(editMode);
        contractSpinner.setEnabled(editMode);
        salarySpinner.setEnabled(editMode);
        termSpinner.setEnabled(editMode);

        editDescriptionText.setEnabled(editMode);
        descriptionCountTextView.setEnabled(editMode);
        editObject.setEnabled(editMode);
        editSalary.setEnabled(editMode && salarySpinner.getSelectedItemPosition() != 0);
        dateButton.setEnabled(editMode);
        validity.setEnabled(editMode);
        places.setEnabled(editMode);
        nation.setEnabled(editMode);
        city.setEnabled(editMode);

        CardView cardView = (CardView) root.findViewById(R.id.card_AddTag);

        cardView.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
        addTagButton.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
        addTagButton.setEnabled(editMode);
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


    // ---------------- adding tag to view ---------------------------------------------------------
    private void addTag(final String tagName){

        Log.println(Log.ASSERT, "NEW OFFER", "adding tag: " + tagName);

        // creating tad view
        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mytagView = inflater.inflate(R.layout.taglayout, null);

        // setting tag view
        TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
        t.setText(tagName);
        mytagView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView tv = (TextView) v.findViewById(R.id.tag_tv);
                currentTags.remove(tv.getText().toString().trim());
                tagContainer.removeView(v);
                Toast.makeText(getActivity(), getString(R.string.new_offer_fragment_removed), Toast.LENGTH_SHORT).show();
            }
        });

        // adding tag view
        currentTags.add(tagName);
        tagContainer.addView(mytagView);
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
                (nation.getText().length() == 0 && city.getText().length() == 0)) {

            correct = false;
            errorMessage += getString(R.string.new_offer_missing_field_error) + "\n";
        }

        // ****** correction checks ********
        Integer p = null;
        Integer s = null;
        Date d = null;

        try {

            // check places number
            p = Integer.parseInt(places.getText().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // date must be after today
            d = dateFormat.parse(validity.getText().toString());
            Date today = Calendar.getInstance().getTime();

            if (d.compareTo(today) <= 0) {

                correct = false;
                errorMessage += getString(R.string.new_offer_wrong_date_error) + "\n";
            }

            // check salary
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
        offer.setCompany(company);

        offer.setObject(editObject.getText().toString());
        offer.setWorkField(CompanyOffer.TYPE_WORK_FIELD[fieldSpinner.getSelectedItemPosition()]);
        offer.setPositions(p);
        offer.setContract(CompanyOffer.TYPE_CONTRACT_FIELD[contractSpinner.getSelectedItemPosition()]);
        offer.setValidity(d);
        offer.setTerm(CompanyOffer.TYPE_TERM_FIELD[termSpinner.getSelectedItemPosition()]);

        if(offer.getStatus().equals(CompanyOffer.STATUS_NEW))
            offer.setStatus(CompanyOffer.STATUS_CREATED);


        if (salarySpinner.getSelectedItemPosition() != 0) {

            Integer salary = Integer.parseInt(editSalary.getText().toString());
            offer.setSalary(salary);
            offer.setSalaryType(CompanyOffer.TYPE_SALARY_TYPE_FIELD[salarySpinner.getSelectedItemPosition()]);
        }
        else {

            offer.setSalary(-1);
        }

        if(!nation.getText().toString().trim().isEmpty())
            offer.setNation(nation.getText().toString());

        if(!nation.getText().toString().trim().isEmpty())
            offer.setCity(city.getText().toString());

        // create a geo point only having nation and city
        if (!nation.getText().toString().trim().isEmpty() && !city.getText().toString().trim().isEmpty()) {

            String geoAddress = nation.getText().toString() + ", " + city.getText().toString();
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addressList = null;

            try {

                addressList = geocoder.getFromLocationName(geoAddress, 5);
                if (!addressList.isEmpty()) {

                    Address a = addressList.get(0);
                    ParseGeoPoint geoPoint = new ParseGeoPoint(a.getLatitude(), a.getLongitude());
                    offer.setLocation(geoPoint);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (editDescriptionText.getText().length() == 0)
            offer.setDescription("");
        else
            offer.setDescription(editDescriptionText.getText().toString());


        for (String tagName: currentTags){

            Tag t = allTags.get(tagName);

            if(!offer.getTags().contains(t))
                offer.addTag(allTags.get(tagName));
        }

        offer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){

                    company.addOffer(offer);
                    company.saveInBackground();
                }
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
            news.createNews(News.TYPE_NEW_OFFER, offer, null, null, globalData);

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
            ApplicationsListFragment fragment = ApplicationsListFragment.newInstance(offer);

            //clear backstack
            int count = fragmentManager.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fragmentManager.popBackStack();
            }

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

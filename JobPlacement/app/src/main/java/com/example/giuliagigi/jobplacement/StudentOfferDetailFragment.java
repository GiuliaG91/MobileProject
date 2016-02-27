package com.example.giuliagigi.jobplacement;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class StudentOfferDetailFragment extends  Fragment {

    View root;
    CompanyOffer offer;
    GlobalData globalData;
    FragmentActivity activity;
    Student student;
    Company company;

    boolean applied = false;
    private FloatingActionButton applyButton, revokeButton;


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS -------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    public static StudentOfferDetailFragment newInstance(CompanyOffer offer, Student student) {
        StudentOfferDetailFragment fragment = new StudentOfferDetailFragment();
        fragment.offer = offer;
        fragment.student = student;
        return fragment;
    }

    public StudentOfferDetailFragment() {}


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        globalData=(GlobalData)getActivity().getApplication();
        globalData.getToolbar().setTitle(getResources().getString(R.string.ToolbarTilteOffer));

        if(offer==null){

            getFragmentManager().popBackStackImmediate();
        }

        try {
            offer.fetchIfNeeded();
            company=offer.getCompany().fetchIfNeeded();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<CompanyOffer> currentAppliedOffers = new ArrayList<>();
        for(StudentApplication a : student.getApplications())
            currentAppliedOffers.add(a.getOffer());

        if(currentAppliedOffers.contains(offer)) applied = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Toolbar toolbar=globalData.getToolbar();
        toolbar.setTitle(R.string.ToolbarTilteOffer);

        globalData.setToolbarTitle(getString(R.string.ToolbarTilteOffer));
        activity=getActivity();

        root=inflater.inflate(R.layout.student_offer_detail_fragment,container,false);

        // ------- object -----------------------------------------------------------------------
        LinearLayout linearLayout=(LinearLayout)root.findViewById(R.id.object_row);
        TextView hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        TextView content=(TextView)linearLayout.findViewById(R.id.content_tv);
        ImageView icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_object));
        content.setText(offer.getOfferObject());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_objectoffer));


        // ------ mission -----------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.mission_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_mission));
        content.setText(offer.getOfferObject());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_mission));


        // ------- field ------------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.field_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_field));
        content.setText(offer.getWorkField());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_field));


        // ------- places --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.places_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_places));
        content.setText(String.valueOf(offer.getnPositions()));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_places));


        // ------- validity --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.validity_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_validity));
        SimpleDateFormat dateformat=new SimpleDateFormat("dd/MM/yyyy");
        String date=dateformat.format(offer.getValidity());
        content.setText(date);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_validity));



        // ------- contract --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.contract_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_contract));
        content.setText(offer.getContract());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_contract));


        // ------- terms --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.term_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_term));
        content.setText(offer.getTerm());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_term));


        // ------- location --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.location_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);
        hint.setText(getString(R.string.offer_detail_hint_location));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location));

        if(offer.getLocation()!=null && !offer.getLocation().equals(""))
        content.setText(offer.getOfferObject());
        else content.setText(getString(R.string.offer_detail_hint_Nodefined));


        // ------- salary --------------------------------------------------------------------
        linearLayout=(LinearLayout)root.findViewById(R.id.salary_row);
        hint=(TextView)linearLayout.findViewById(R.id.hint_tv);
        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        icon=(ImageView)linearLayout.findViewById(R.id.rowIcon);

        hint.setText(getString(R.string.offer_detail_hint_salary));
        Integer salary=offer.getSalary();

        if(salary!=-1) {

            content.setText(String.valueOf(salary).trim());
        }
        else {

            content.setText(getString(R.string.offer_detail_hint_Nodefined));
        }

        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_salary));


        // ---- status and icon --------------------------------------------------------------------
        linearLayout = (LinearLayout)root.findViewById(R.id.status_row);
        hint = (TextView)linearLayout.findViewById(R.id.hint_tv);
        hint.setText(R.string.offer_detail_status);

        content=(TextView)linearLayout.findViewById(R.id.content_tv);
        content.setText(offer.getStatus());


        // ---- company info ------------------------------------------------------------------
        ImageView logo = (ImageView)root.findViewById(R.id.circleView_logo);
        TextView companyName = (TextView)root.findViewById(R.id.company_name_tv);
        TextView companyMail = (TextView)root.findViewById(R.id.Company_email);

        TextView description=(TextView)root.findViewById(R.id.DescriptionText_tv);
        description.setText(offer.getDescription());

        if(company.getProfilePhoto() != null){

            logo.setImageBitmap(company.getProfilePhoto());
        }

        companyName.setText(company.getName());
        companyMail.setText(company.getMail());


        // --------- floating actions menu --------------------------------------------------------


        /* --- apply --- */
        applyButton=(FloatingActionButton)root.findViewById(R.id.action_apply);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<Student> students = new ArrayList<Student>();
                for (StudentApplication a : offer.getApplications())
                    students.add(a.getStudent());

                // if is possible perform apply
                if (!students.contains(student)) {

                    Log.println(Log.ASSERT, "OFFER", "validity: " + offer.getValidity().toString() + " today: " + Calendar.getInstance().getTime().toString());

                    if (offer.getnPositions() > 0 && (offer.getValidity().after(Calendar.getInstance().getTime()))) {

                        offer.saveInBackground();
                        final StudentApplication studentApplication = new StudentApplication();
                        studentApplication.setOffer(offer);
                        studentApplication.setStudent(student);
                        studentApplication.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                offer.addApplication(studentApplication);
                                offer.saveInBackground();

                                student.addApplication(studentApplication);
                                student.saveInBackground();

                            }
                        });

                        if (!student.getFavouriteOffers().contains(offer)) {

                            student.addFavouriteOffer(offer);
                            student.saveInBackground();
                        }


                        // --- push notification to publisher ----------
                        Company company = null;

                        try {
                            company = offer.getCompany().fetchIfNeeded();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("User", company.getParseUser());

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setMessage("" + student.getName() + " " + student.getSurname() + " " + getString(R.string.Message_Apply) + " " + offer.getOfferObject());
                        push.sendInBackground();

                        News news = new News();
                        news.createNews(News.TYPE_OFFER_APPLICATION, offer, (Student) globalData.getUserObject(), null, globalData);

                        applied = true;
                        configureFloatingButtons();
                        Toast.makeText(getActivity(), getString(R.string.Done), Toast.LENGTH_SHORT).show();

                    }
                    else {

                        Toast.makeText(getActivity(), getString(R.string.offer_detail_NoApply), Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(getActivity(), getString(R.string.offer_detail_AlreadyApplied), Toast.LENGTH_SHORT).show();
                }

            }
        });


        /* --- contact button --- */
        final FloatingActionButton contactButton=(FloatingActionButton)root.findViewById(R.id.action_contact_company);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                ArrayList<ParseUserWrapper> recipients = new ArrayList<ParseUserWrapper>();
                recipients.add(company.getParseUser());
                Fragment fragment = MailBoxNewFragment.newInstance(recipients,null,null);

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(activity.getResources().getString(R.string.new_message_toolbar_title))
                        .commit();

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
                toolbar.setTitle(activity.getResources().getString(R.string.new_message_toolbar_title));

                ((FloatingActionsMenu) root.findViewById(R.id.new_offer_floating_menu)).collapse();
            }
        });

        /* --- revoke application --- */
        revokeButton = (FloatingActionButton)root.findViewById(R.id.action_revoke_application);
        revokeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.offer_detail_revoke_apply_warning);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (StudentApplication a : student.getApplications()) {

                            if (a.getOffer().equals(offer)) {

                                offer.removeApplication(a);

                                if(a.getStatus().equals(StudentApplication.TYPE_ACCEPTED))
                                    offer.setPositions(offer.getnPositions() + 1);

                                offer.saveInBackground();

                                student.removeApplication(a);
                                student.saveInBackground();

                                a.deleteInBackground();

                                News news = new News();
                                news.createNews(News.TYPE_APPLICATION_DELETED, offer, student, a, globalData);

                                ParseQuery pushQuery = ParseInstallation.getQuery();
                                pushQuery.whereEqualTo("User", company.getParseUser());

                                ParsePush push = new ParsePush();
                                push.setQuery(pushQuery);
                                push.setMessage("" + student.getName() + " " + student.getSurname() + " " + getString(R.string.deleted_application_message) + " " + offer.getOfferObject());
                                push.sendInBackground();

                                applied = false;
                                configureFloatingButtons();
                                Toast.makeText(getActivity(), getString(R.string.Done), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), R.string.operation_canceled, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create().show();
            }
        });


        /* --- favourites --- */
        final FloatingActionButton favourites = (FloatingActionButton)root.findViewById(R.id.action_favourites);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!student.getFavouriteOffers().contains(offer)) {

                    student.addFavouriteOffer(offer);
                    student.saveInBackground();

                    Toast.makeText(getActivity(), R.string.offer_detail_favourite_added, Toast.LENGTH_SHORT).show();
                }
                else {

                    if(applied){

                        Toast.makeText(getContext(), R.string.offer_detail_favourites_warning, Toast.LENGTH_LONG).show();
                    }
                    else {

                        student.removeFavouriteOffer(offer);
                        student.saveInBackground();

                        Toast.makeText(getActivity(), R.string.offer_detail_favourite_removed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        ((Toolbar) activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.job_offer));

        configureFloatingButtons();

        return root;
    }


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- AUXILIARY METHODS --------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    private void configureFloatingButtons(){


        applyButton.setEnabled(!applied);
        applyButton.setVisibility(applied ? View.INVISIBLE : View.VISIBLE);

        revokeButton.setEnabled(applied);
        revokeButton.setVisibility(applied ? View.VISIBLE : View.INVISIBLE);
    }

}

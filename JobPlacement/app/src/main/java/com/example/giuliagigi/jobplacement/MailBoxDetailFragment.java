package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.YEAR;

/**
 * Created by Silvia on 11/05/2015.
 */
public class MailBoxDetailFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
    ArrayList<User> recipients;
    User sender;

    FragmentActivity activity;


    public static MailBoxDetailFragment newInstance() {
        MailBoxDetailFragment fragment = new MailBoxDetailFragment();
        return fragment;
    }

    public MailBoxDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

        message = globalData.getCurrentViewMessage();
        if(message == null)
        {
            getFragmentManager().popBackStackImmediate();
        }

        sender = message.getSender();
        sender.fetchIfNeededInBackground();

        recipients = message.getRecipients();
        for(User r: recipients){
            r.fetchIfNeededInBackground();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mail_box_detail,container,false);


        //set profile photo
        ImageView img = (ImageView) root.findViewById(R.id.sender_img);
        img.setImageBitmap(sender.getProfilePhoto());

        //set sender name
        TextView tv = (TextView)root.findViewById(R.id.sender_tv);
        if(sender instanceof Student)
            tv.setText(((Student)sender).getName());
        else
            tv.setText(((Company)sender).getName());


        //set list of recipients in the spinner
        Spinner sp = (Spinner)root.findViewById(R.id.recipients_list);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.globalData, R.layout.row_spinner);
        ArrayList<String> name_recipients = new ArrayList<String>();
        for(User u: this.recipients)
            if(u instanceof Student)
                name_recipients.add(((Student)u).getName());
            else
                name_recipients.add(((Company)u).getName());

        spinnerAdapter.addAll(name_recipients);
        sp.setAdapter(spinnerAdapter);


        //set date
        tv = (TextView) root.findViewById(R.id.date_tv);
        Date date = this.message.getDate();
        String format = date.getDay() + " " + globalData.getResources().getStringArray(R.array.months)[date.getMonth()];

        format = format + " " + date.getYear() + " " + date.getHours() + ":" + date.getMinutes();
        tv.setText(format);

        //set object
        tv = (TextView) root.findViewById(R.id.object_tv);
        tv.setText(message.getObject());

        //set body
        tv = (TextView) root.findViewById(R.id.body_tv);
        tv.setText(message.getBodyMessage());

        // aggiungo listener a bottone di prova per triggerare fragment MailBoxRespondFragment
        ImageButton button = (ImageButton) root.findViewById(R.id.respond_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxRespondFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .commit();

                Toolbar toolbar = (Toolbar) globalData.getToolbar();
            }
        });

        return root;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menu.clear();
//        menuInflater.inflate(R.menu.menu_mail_box_detail, menu);

    }

//    public boolean onOptionsItemSelected(MenuItem item){
//
//        switch(item.getItemId()){
//
//            case R.id.respond_action_button:    FragmentManager fragmentManager = ((FragmentActivity)this.getActivity()).getSupportFragmentManager();
//
//                Fragment fragment = MailBoxRespondFragment.newInstance();
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.tab_Home_container, fragment)
//                        .commit();
//
//                Toolbar toolbar = (Toolbar) globalData.getToolbar();
//
//                // ---> eliminare action button precedenti dalla toolbar
//
//                return true;
//
//            default:    return false;
//
//        }
//
//    }

    // implementare un botttone Up che punti a mailBoxFragment con la vista di tutti i messaggi
    // resettare di conseguenza il contenuto di globalData.currentViewMessage

}

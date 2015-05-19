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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.YEAR;

/**
 * Created by Silvia on 11/05/2015.
 */
public class MailBoxDetailFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
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

        if(message.getSender().equals(globalData.getUserObject().getMail()))
            sender = globalData.getUserObject();
        else{

            ParseQuery<User> query = ParseQuery.getQuery("User");
            query.whereEqualTo(User.MAIL_FIELD, message.getSender());
            try {
                List<User> users = query.find();
                if(users != null && users.size() > 0)
                    sender = users.get(0);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mail_box_detail,container,false);


        //set profile photo
        if(sender.getProfilePhoto() != null) {
            ImageView img = (ImageView) root.findViewById(R.id.sender_img);
            img.setImageBitmap(sender.getProfilePhoto());
        }

        //set sender name
        TextView tv = (TextView)root.findViewById(R.id.sender_tv);
        tv.setText(sender.getName());


        //set list of recipients in the spinner
        Spinner sp = (Spinner)root.findViewById(R.id.recipients_list);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.globalData, R.layout.row_spinner);
        spinnerAdapter.addAll(message.getRecipients());
        sp.setAdapter(spinnerAdapter);

        //sp.setAdapter(new StringAdapter((String[])(message.getRecipients().toArray())));


        //set date
        tv = (TextView) root.findViewById(R.id.date_tv);
        Date date = this.message.getDate();
        Date now = new Date();
        String format = "";
        if(date.getDay() == now.getDay() && date.getMonth() == now.getMonth() && date.getYear() == now.getYear()){

            if(date.getHours() == now.getHours())
                if(now.getMinutes() - date.getMinutes() == 1)
                    format = date.getHours() + ":" + date.getMinutes() + " (" + (now.getMinutes() - date.getMinutes()) +" " + globalData.getResources().getString(R.string.minute_ago) + ")";
                else
                    format = date.getHours() + ":" + date.getMinutes() + " (" + (now.getMinutes() - date.getMinutes()) +" " + globalData.getResources().getString(R.string.minutes_ago) + ")";
            else
                if(now.getHours() - date.getHours() == 1)
                    format = date.getHours() + ":" + date.getMinutes() + " (" + (now.getHours() - date.getHours()) + " " + globalData.getResources().getString(R.string.hour_ago) + ")";
                else
                    format = date.getHours() + ":" + date.getMinutes() + " (" + (now.getHours() - date.getHours()) + " " + globalData.getResources().getString(R.string.hours_ago) + ")";
        }else if(date.getMonth() == now.getMonth() && date.getYear() == now.getYear()){

            if(now.getDay() - date.getDay() <= 13)
                if(now.getDay() - date.getDay() == 1)
                    format = date.getDay() + " " + globalData.getResources().getStringArray(R.array.months)[date.getMonth()] + " (" + (now.getDay() - date.getDay()) + " " + globalData.getResources().getString(R.string.day_ago) + ")";
                else
                    format = date.getDay() + " " + globalData.getResources().getStringArray(R.array.months)[date.getMonth()] + " (" + (now.getDay() - date.getDay()) + " " + globalData.getResources().getString(R.string.days_ago) + ")";

        }else if(date.getYear() == now.getYear()){

            format = date.getDay() + " " + globalData.getResources().getStringArray(R.array.months)[date.getMonth()];

        }else {
            format = date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
        }

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
                        .addToBackStack(message.getObject())
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


/*
    private class StringAdapter extends BaseAdapter {

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
    */

}

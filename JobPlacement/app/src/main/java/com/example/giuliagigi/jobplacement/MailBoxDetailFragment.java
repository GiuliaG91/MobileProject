package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.res.Resources;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.YEAR;

/**
 * Created by Silvia on 11/05/2015.
 */
public class MailBoxDetailFragment extends Fragment {

    protected static final String RECIPIENTS_KEY = "recipients";
    protected static final String OBJECT_KEY = "object";
    protected static final String OLD_MESSAGE_KEY = "old_message";
    public static final String BUNDLE_IDENTIFIER = "MAILBOX";

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

        setHasOptionsMenu(true);

        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

        message = globalData.getCurrentViewMessage();

        if(message == null){
            getFragmentManager().popBackStackImmediate();
        }

//        if(message.getSender().equals(globalData.getUserObject().getMail()))
//            sender = globalData.getUserObject();
//        else{
//
//            ParseQuery<User> query = ParseQuery.getQuery("User");
//            query.whereEqualTo(User.MAIL_FIELD, message.getSender());
//            try {
//                List<User> users = query.find();
//                if(users != null && users.size() > 0)
//                    sender = users.get(0);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

//        ParseQuery<ParseUser> parseUserQuery = ParseQuery.getQuery("_User");
//        parseUserQuery.whereEqualTo("email", message.getSender());
//
//        try {
//
//            List<ParseUser> parseUsers = parseUserQuery.find();
//
//            if(!parseUsers.isEmpty()){
//
//                ParseUserWrapper parseUserWrapper = (ParseUserWrapper)parseUsers.get(0);
//                sender = parseUserWrapper.getUser().fetchIfNeeded();
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        sender = message.getSender().getUser();
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
        if(sender.getMail().equals(globalData.getUserObject().getMail()))
            tv.setText(globalData.getResources().getString(R.string.me));
        else
            tv.setText(sender.getName());


        //set list of recipients in the spinner
        Spinner sp = (Spinner)root.findViewById(R.id.recipients_list);

//        String[] recipients = new String[message.getRecipients().size()];
//        if(sender.getMail().equals(globalData.getUserObject().getMail()))
//            recipients[0] = message.getRecipients().get(0);
//        else
//            recipients[0] = globalData.getResources().getString(R.string.me);
//        for(int i = 1; i < message.getRecipients().size(); i++)
//            if(message.getRecipients().get(i).equals(globalData.getUserObject().getMail()))
//                continue;
//            else
//                recipients[i] = message.getRecipients().get(i);


        ArrayList<String> recipients = new ArrayList<String>();

        for(ParseUserWrapper p: message.getRecipients()){

            recipients.add(p.getEmail());
        }

        sp.setAdapter(new StringAdapter((String[])recipients.toArray()));

        //set date
        tv = (TextView) root.findViewById(R.id.date_tv);
        try {
            Calendar date = message.getDate();
            Calendar now = Calendar.getInstance();
            String format = "";
            if (date.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) && date.get(Calendar.MONTH) == now.get(Calendar.MONTH) && date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {

                if (date.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY))
                    if (now.get(Calendar.MINUTE) - date.get(Calendar.MINUTE) == 1)
                        format = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + " (" + "1 " + globalData.getResources().getString(R.string.minute_ago) + ")";
                    else
                        format = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + " (" + (now.get(Calendar.MINUTE) - date.get(Calendar.MINUTE)) + " " + globalData.getResources().getString(R.string.minutes_ago) + ")";
                else if (now.get(Calendar.HOUR_OF_DAY) - date.get(Calendar.HOUR_OF_DAY) == 1)
                    format = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + " (" + "1 " + globalData.getResources().getString(R.string.hour_ago) + ")";
                else
                    format = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + " (" + (now.get(Calendar.HOUR_OF_DAY) - date.get(Calendar.HOUR_OF_DAY)) + " " + globalData.getResources().getString(R.string.hours_ago) + ")";

            } else if (date.get(Calendar.MONTH) == now.get(Calendar.MONTH) && date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {

                if (now.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH) <= 13)
                    if (now.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH) == 1)
                        format = date.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[date.get(Calendar.MONTH)] + " (1 " + globalData.getResources().getString(R.string.day_ago) + ")";
                    else
                        format = date.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[date.get(Calendar.MONTH)] + " (" + (now.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH)) + " " + globalData.getResources().getString(R.string.days_ago) + ")";

                format = date.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[date.get(Calendar.MONTH)];

            } else if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {

                format = date.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[date.get(Calendar.MONTH)];

            } else {
                format = date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR);
            }

            tv.setText(format);
        }catch(JSONException e){
            e.printStackTrace();
        }

        //set body
        tv = (TextView) root.findViewById(R.id.body_tv);
        tv.setText(message.getBodyMessage());

        /*Attach on click listener to button menu */

        FloatingActionsMenu actionsMenu = (FloatingActionsMenu) root.findViewById(R.id.multiple_actions_mailbox);

        if(message.getSender().equals(globalData.getUserObject().getMail())) {
            actionsMenu.setEnabled(false);
            actionsMenu.setVisibility(View.INVISIBLE);
        }

        final FloatingActionButton respondSenderButton = (FloatingActionButton)root.findViewById(R.id.action_respond_sender);
        respondSenderButton.setIcon(R.drawable.ic_person_outline_white_36dp);
        respondSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ParseUserWrapper> recipients = new ArrayList<ParseUserWrapper>();
                recipients.add(message.getSender());

                String oldMessage = "";
                try {
                    Calendar date = message.getDate();
                    Resources res = globalData.getResources();
                    oldMessage = res.getString(R.string.on) + " " + date.get(Calendar.DAY_OF_MONTH) + " " + res.getStringArray(R.array.months)[date.get(Calendar.MONTH)] + " " + date.get(Calendar.YEAR) + ", ";
                    oldMessage = oldMessage + message.getSender().getEmail() + " <" + message.getSender().getEmail() + "> " + res.getString(R.string.wrote) + ":\n\"";
                    oldMessage = oldMessage + message.getBodyMessage() + "\"\n\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxNewFragment.newInstance(recipients,message.getObject(),oldMessage);

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(message.getObject())
                        .commit();

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
                toolbar.setTitle(message.getObject());
            }
        });


        final FloatingActionButton respondAllButton = (FloatingActionButton)root.findViewById(R.id.action_respond_all);
        respondAllButton.setIcon(R.drawable.ic_people_outline_white_36dp);
        respondAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ParseUserWrapper> recipients = new ArrayList<ParseUserWrapper>();
                recipients.add(message.getSender());

                for(ParseUserWrapper p: message.getRecipients()){

                    if(p.equals(globalData.getCurrentUser()))
                        continue;
                    recipients.add(p);
                }

                String oldMessage = null;

                try {
                    Calendar date = message.getDate();
                    Resources res = globalData.getResources();
                    oldMessage = res.getString(R.string.on) + " " + date.get(Calendar.DAY_OF_MONTH) + " " + res.getStringArray(R.array.months)[date.get(Calendar.MONTH)] + " " + date.get(Calendar.YEAR) + ", ";
                    oldMessage = oldMessage + message.getSender().getEmail() + " <" + message.getSender().getEmail() + "> " + res.getString(R.string.wrote) + ":\n\"";
                    oldMessage = oldMessage + message.getBodyMessage() + "\"\n\n";
                }catch(JSONException e){
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxNewFragment.newInstance(recipients,message.getObject(),oldMessage);

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(message.getObject())
                        .commit();

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
                toolbar.setTitle(message.getObject());
            }
        });

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(message.getObject());

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


}

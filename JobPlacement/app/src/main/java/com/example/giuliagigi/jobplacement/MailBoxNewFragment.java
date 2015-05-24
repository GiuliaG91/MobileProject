package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class MailBoxNewFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
    FragmentActivity activity;
    Boolean send_flag;
    Boolean flag;


    public static MailBoxNewFragment newInstance(Bundle data) {
        MailBoxNewFragment fragment = new MailBoxNewFragment();

        fragment.message = new InboxMessage();
        fragment.send_flag = false;
        fragment.flag = true;

        if(data.getStringArrayList(MailBoxDetailFragment.RECIPIENTS_KEY) != null)
            fragment.message.setRecipients(data.getStringArrayList(MailBoxDetailFragment.RECIPIENTS_KEY));
        else
            fragment.message.setRecipients(new ArrayList<String>());

        if(data.getString(MailBoxDetailFragment.OBJECT_KEY) != null)
            fragment.message.setObject(data.getString(MailBoxDetailFragment.OBJECT_KEY));
        else
            fragment.message.setObject("");

        if(data.getString(MailBoxDetailFragment.OLD_MESSAGE_KEY) != null)
            fragment.message.setBodyMessage(data.getString(MailBoxDetailFragment.OLD_MESSAGE_KEY));
        else
            fragment.message.setBodyMessage("");

        return fragment;
    }

    public MailBoxNewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

        message.setSender(globalData.getUserObject().getMail());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        globalData.setToolbarTitle(getString(R.string.new_message_toolbar_title));
        root = inflater.inflate(R.layout.fragment_mail_box_new,container,false);

        if(message.getRecipients().size() > 0){
            EditText ed = (EditText)root.findViewById(R.id.recipients_list_new_message);
            String list_rec = "";
            for(String r: message.getRecipients()){
                list_rec = list_rec + r + "   ";
            }
            ed.setText(list_rec);
        }

        if(message.getObject().length() > 0){
            EditText ed = (EditText) root.findViewById(R.id.object_new_message);
            ed.setText(message.getObject());
        }

        if(message.getBodyMessage().length() > 0){
            EditText ed = (EditText) root.findViewById(R.id.body_new_message);
            ed.setText(message.getBodyMessage());
        }

        final Button send = (Button) root.findViewById(R.id.send_new_message_btn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String recipientsList = ((EditText) root.findViewById(R.id.recipients_list_new_message)).getText().toString();
                String object = ((EditText) root.findViewById(R.id.object_new_message)).getText().toString();

                StringTokenizer st = new StringTokenizer(recipientsList, ", ;");
                while (st.hasMoreTokens())
                    message.getRecipients().add(st.nextToken());

                //control that recipients filed is not empty
                if (message.getRecipients().size() == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    View view = activity.getLayoutInflater().inflate(R.layout.error_message_mail_box, null);
                    builder.setView(view);

                    builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            send_flag = false;
                        }
                    });
                    builder.create().show();

                } else if (object.length() == 0 && flag) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    View view = activity.getLayoutInflater().inflate(R.layout.warning_message_mail_box, null);
                    builder.setView(view);


                    builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                send_flag = true;
                                flag = false;
                                send.callOnClick();
                        }
                    });

                    builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                send_flag = false;
                                message.getRecipients().clear();
                        }
                    });

                    builder.create().show();

                } else {
                    send_flag = true;
                }



                if (send_flag) {

                    message.setObject(object);

                    String bodyMessage = ((EditText) root.findViewById(R.id.body_new_message)).getText().toString();
                    message.setBodyMessage(bodyMessage);

                    message.setDate(Calendar.getInstance());

                    message.setIsPreferred(false);
                    message.setIsRead(false);
                    message.setIsDeleting(false);


                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                                Toast.makeText(globalData, globalData.getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
                        }
                    });

                    for (String recipient : message.getRecipients()) {

                        InboxMessageReceived mr = new InboxMessageReceived();

                        mr.setObject(message.getObject());
                        mr.setSender(message.getSender());
                        mr.setRecipients(message.getRecipients());
                        mr.setBodyMessage(message.getBodyMessage());
                        mr.setIsPreferred(false);
                        mr.setIsRead(false);
                        try {
                            mr.setDate(message.getDate());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mr.setIsDeleting(false);
                        mr.setRecipient(recipient);
                        mr.setNameSender(globalData.getUserObject().getName());

//                        if (globalData.getUserObject().getProfilePhoto() != null)
//                            mr.setPhotoSender(globalData.getUserObject().getProfilePhoto());

                        mr.saveInBackground();


                        ParseQuery inner= new ParseQuery("_User");
                         inner.whereContainedIn("email", Arrays.asList(recipient));

                          List<ParseUser> users=null;

                        try {
                            users=inner.find();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereContainedIn("User",users);


                        ParsePush push = new ParsePush();
                                        push.setQuery(pushQuery);
                                        push.setMessage(""+getString(R.string.Message_newMessage) + globalData.getUserObject().getMail());
                                        push.sendInBackground();

                                    }


                    }

                    FragmentManager fragmentManager = activity.getSupportFragmentManager();

                    Fragment fragment = MailBoxFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(globalData.getResources().getStringArray(R.array.Menu_items_student)[4])
                            .commit();

                    globalData.getToolbar().setTitle(globalData.getResources().getStringArray(R.array.Menu_items_student)[4]);

                }

        });

        if(savedInstanceState != null){

            // setto lista recipients col contenuto già inserito dall'utente
            EditText ed = (EditText)root.findViewById(R.id.recipients_list_new_message);
            ed.setText(savedInstanceState.getString("recipients"));

            // setto object col contenuto già inserito dall'utente
            ed = (EditText)root.findViewById(R.id.object_new_message);
            ed.setText(savedInstanceState.getString("object"));

            //setto body message con il contenuto già inserito dall'utente
            ed = (EditText)root.findViewById(R.id.body_new_message);
            ed.setText(savedInstanceState.getString("bodyMessage"));

        }

        ((Toolbar)activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.new_message_toolbar_title));

        return root;
    }

    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString("recipients", ((EditText)root.findViewById(R.id.recipients_list_new_message)).getText().toString());
        savedInstanceState.putString("object", ((EditText)root.findViewById(R.id.object_new_message)).getText().toString());
        savedInstanceState.putString("bodyMessage", ((EditText)root.findViewById(R.id.body_new_message)).getText().toString());

    }

    private static boolean isMailPresent(String mail, List<ParseObject> userList){

        for(ParseObject u: userList){
            if(((User)u).getMail().equals(mail)) return true;
        }

        return false;
    }

}

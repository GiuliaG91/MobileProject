package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class MailBoxNewFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
    FragmentActivity activity;


    public static MailBoxNewFragment newInstance(Bundle data) {
        MailBoxNewFragment fragment = new MailBoxNewFragment();

        fragment.message = new InboxMessage();

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

        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

        message.setSender(globalData.getUserObject().getMail());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        Button button = (Button) root.findViewById(R.id.send_new_message_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String object = ((EditText)root.findViewById(R.id.object_new_message)).getText().toString();
                message.setObject(object);

                String bodyMessage = ((EditText)root.findViewById(R.id.body_new_message)).getText().toString();
                message.setBodyMessage(bodyMessage);

                message.setDate(new Date());
                message.setIsPreferred(false);
                message.setIsRead(false);
                message.setIsDeleting(false);

                String recipientsList = ((EditText)root.findViewById(R.id.recipients_list_new_message)).getText().toString();
                StringTokenizer st = new StringTokenizer(recipientsList, ", ;");

                while(st.hasMoreTokens())
                    message.getRecipients().add(st.nextToken());

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                            Toast.makeText(globalData, globalData.getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
                    }
                });

                for(String recipient: message.getRecipients()){

                    InboxMessageReceived mr = new InboxMessageReceived();

                    mr.setObject(message.getObject());
                    mr.setSender(message.getSender());
                    mr.setRecipients(message.getRecipients());
                    mr.setBodyMessage(message.getBodyMessage());
                    mr.setIsPreferred(false);
                    mr.setIsRead(false);
                    mr.setDate(message.getDate());
                    mr.setIsDeleting(false);
                    mr.setRecipient(recipient);
                    mr.setNameSender(globalData.getUserObject().getName());

                    if(globalData.getUserObject().getProfilePhoto() != null)
                        mr.setPhotoSender(globalData.getUserObject().getProfilePhoto());

                    mr.saveInBackground();

                    ParseQuery<User> query = ParseQuery.getQuery("User");
                    query.whereEqualTo(User.MAIL_FIELD, recipient.trim());
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> list, ParseException e) {
                            if(list != null){
                                if(list.get(0).getType().equals(User.TYPE_STUDENT)){

                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo(User.MAIL_FIELD, list.get(0).getMail());

                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
                                    push.setMessage("Nuovo messaggio da: " + globalData.getUserObject().getMail());
                                    push.sendInBackground();

                                }
                            }
                        }

                    });

                }

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(globalData.getResources().getStringArray(R.array.Menu_items_student)[4])
                        .commit();

                globalData.getToolbar().setTitle(globalData.getResources().getStringArray(R.array.Menu_items_student)[4]);

                /*
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                query.whereContainedIn(User.MAIL_FIELD, recipients);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if(e == null){

                            for(ParseObject u: parseObjects){
                                message.getRecipients().add((User)u);
                            }

                            message.saveInBackground();


                            for(ParseObject u: parseObjects){
                                InboxMessageReceived mr = new InboxMessageReceived();
                                mr.setObject(message.getObject());
                                mr.setSender(message.getSender());
                                mr.setRecipients(message.getRecipients());
                                mr.setBodyMessage(message.getBodyMessage());
                                mr.setIsPreferred(false);
                                mr.setIsRead(false);
                                mr.setDate(message.getDate());
                                mr.setIsDeleting(false);
                                mr.setRecipient((User)u);

                                mr.saveInBackground();


                                if(u instanceof Student) {
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo(User.MAIL_FIELD, ((User) u).getMail());

                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
                                    push.setMessage("Nuovo messaggio da: " + globalData.getUserObject().getMail());
                                    push.sendInBackground();
                                }
                            }

                            Toast.makeText(globalData, globalData.getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();

                            if(parseObjects.size() != recipients.size()){
                                for(String m: recipients) {
                                    if (!MailBoxNewFragment.isMailPresent(m, parseObjects))
                                        wrongRecipients.add(m);
                                }

                                String messageEmailsWrong = globalData.getResources().getString(R.string.message_emails_wrong) + "\n";
                                for(String s: wrongRecipients)
                                    messageEmailsWrong = messageEmailsWrong + s + "\n";

                                Toast.makeText(globalData, messageEmailsWrong, Toast.LENGTH_SHORT).show();

                            }

                            // PUSH notifications and update the page in real time for the recipients

                            FragmentManager fragmentManager = activity.getSupportFragmentManager();

                            Fragment fragment = MailBoxFragment.newInstance();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.tab_Home_container, fragment)
                                    .addToBackStack(globalData.getResources().getStringArray(R.array.Menu_items_student)[4])
                                    .commit();

                            globalData.getToolbar().setTitle(globalData.getResources().getStringArray(R.array.Menu_items_student)[4]);

                        } else {

                            Toast.makeText(globalData, globalData.getResources().getString(R.string.message_all_recipients_wrong), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                */

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

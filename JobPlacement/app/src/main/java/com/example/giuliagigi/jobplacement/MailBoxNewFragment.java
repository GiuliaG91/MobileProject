package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


public class MailBoxNewFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER_HEADER = "mailboxNew_bundle";
    private static final String RECIPIENTS_KEY = "mailboxNew_bundle_recipients";
    private static final String OBJECT_KEY = "mailboxNew_bundle_object";
    private static final String MESSAGE_TEXT_KEY = "mailboxNew_bundle_messageText";
    private static final String BUNDLE_IDENTIFIER_TAIL_KEY = "mailboxNew_bundle_id_tail";

    View root;
    InboxMessage message;
    GlobalData globalData;
    FragmentActivity activity;
    Boolean sendFlag;
    String bundleIdentifierTail;


    EditText recipientsEdit, objectEdit, messageTextEdit;

    ArrayList<ParseUserWrapper> recipients;
    String object, messageText;

    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS, SETTERS ---------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    public static MailBoxNewFragment newInstance(){

        return MailBoxNewFragment.newInstance(null,null,null);
    }

    public static MailBoxNewFragment newInstance(ArrayList<ParseUserWrapper> recipients, String object, String oldMessage) {
        MailBoxNewFragment fragment = new MailBoxNewFragment();

        fragment.message = new InboxMessageSent();
        fragment.bundleIdentifierTail = fragment.message.toString();
        fragment.sendFlag = false;

        fragment.recipients = recipients;

        if(object != null)
            fragment.object = object;
        else
            fragment.object = "";

        if(oldMessage != null)
            fragment.messageText = oldMessage;
        else
            fragment.messageText = "";

        return fragment;
    }

    public MailBoxNewFragment() {}


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.println(Log.ASSERT, "MAILBOXNEW", "onCreate");

        setHasOptionsMenu(true);
        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 1) finding UI elements ----------------------------------------------------------------------------------------
        globalData.setToolbarTitle(getString(R.string.new_message_toolbar_title));
        root = inflater.inflate(R.layout.fragment_mail_box_new,container,false);
        recipientsEdit = (EditText)root.findViewById(R.id.recipients_list_new_message);
        objectEdit = (EditText) root.findViewById(R.id.object_new_message);
        messageTextEdit = (EditText) root.findViewById(R.id.body_new_message);

        // 2) setting view -----------------------------------------------------------------------------------------------
        if(savedInstanceState != null){ // restoring state after rotation //////////////////////////////////////

            bundleIdentifierTail = savedInstanceState.getString(BUNDLE_IDENTIFIER_TAIL_KEY);
            Log.println(Log.ASSERT, "MAILBOXNEW", "looking for a bundle with key: " + BUNDLE_IDENTIFIER_HEADER +bundleIdentifierTail);
            MyBundle b = globalData.getBundle(BUNDLE_IDENTIFIER_HEADER + bundleIdentifierTail);

            if(b!= null){

                Log.println(Log.ASSERT, "MAILBOXNEW", "bundle found");
                recipientsEdit.setText(b.getString(RECIPIENTS_KEY));
                objectEdit.setText(b.getString(OBJECT_KEY));
                messageTextEdit.setText(b.getString(MESSAGE_TEXT_KEY));
            }
        }
        else { // view is initialized for the first time (using constructor parameters) //////

            if(recipients != null && recipients.size() > 0){

                String recipientsMails = "";
                for(ParseUserWrapper p: recipients)
                    recipientsMails = recipientsMails + p.getEmail() + "   ";

                recipientsEdit.setText(recipientsMails);
            }

            if(object != null && object.length() > 0)
                objectEdit.setText(object);

            if(messageText != null && messageText.length() > 0)
                messageTextEdit.setText(messageText);
        }


        // 3) setting buttons ---------------------------------------------------------------------------------------------
        final Button send = (Button) root.findViewById(R.id.send_new_message_btn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<String> recipientsMails = new ArrayList<String>();
                String recipientsList = ((EditText) root.findViewById(R.id.recipients_list_new_message)).getText().toString();
                String object = ((EditText) root.findViewById(R.id.object_new_message)).getText().toString();

                StringTokenizer st = new StringTokenizer(recipientsList, ", ;");
                int validRecipients = 0;

                while (st.hasMoreTokens())
                    recipientsMails.add(st.nextToken());

                for(String r: recipientsMails){

                    try {

                        if(r.equals(globalData.getCurrentUser().getEmail())){

                            Log.println(Log.ASSERT, "MAILBOXNEW", "trying to send a message to myself! not a valid recipient");
                            continue;
                        }

                        message.addRecipient(r);
                        validRecipients++;
                    }
                    catch (InboxMessage.UnknownRecipientException e) {


                        View view = activity.getLayoutInflater().inflate(R.layout.error_message_mail_box, null);
                        TextView tv = (TextView)view.findViewById(R.id.textView_error_message);
                        tv.setText("some of recipients do not exist. Remove or correct the recipients name and try again");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(view);

                        builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendFlag = false;
                            }
                        });

                        builder.create().show();
                        e.printStackTrace();
                    }
                }

                /* ---------------- performing some checks before sending the message --------------- */
                if (validRecipients == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    View view = activity.getLayoutInflater().inflate(R.layout.error_message_mail_box, null);
                    builder.setView(view);

                    builder.setPositiveButton(globalData.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendFlag = false;
                        }
                    });
                    builder.create().show();

                } else if (object.length() == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    View view = activity.getLayoutInflater().inflate(R.layout.warning_message_mail_box, null);
                    builder.setView(view);


                    builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                sendFlag = true;
                                send.callOnClick();
                        }
                    });

                    builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                sendFlag = false;
                                message.getRecipients().clear();
                        }
                    });

                    builder.create().show();

                } else {
                    sendFlag = true;
                }


                // if checks are ok, send the message
                if (sendFlag) {

                    /* ------------------ saving message in sent messages (for sender) -------------- */
                    message.setSender(globalData.getCurrentUser());
                    message.setType(InboxMessage.TYPE_SENT);
                    message.setObject(object);
                    String bodyMessage = ((EditText) root.findViewById(R.id.body_new_message)).getText().toString();
                    message.setBodyMessage(bodyMessage);
                    message.setDate(Calendar.getInstance());
                    message.setIsPreferred(false);
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                            /* ------------ saving message in received messages (one per recipient) --------- */
                            if(e == null){

                                for (ParseUserWrapper recipient : message.getRecipients()) {

                                    InboxMessageReceived mr = new InboxMessageReceived();

                                    mr.setSender(globalData.getCurrentUser());
                                    mr.setOwner(recipient);

                                    for(ParseUserWrapper p : message.getRecipients())
                                        mr.addRecipient(p);

                                    mr.setType(InboxMessage.TYPE_RECEIVED);
                                    mr.setObject(message.getObject());
                                    mr.setBodyMessage(message.getBodyMessage());
                                    mr.setIsPreferred(false);
                                    mr.setIsRead(false);

                                    try {
                                        mr.setDate(message.getDate());
                                    }
                                    catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    mr.saveInBackground();

                                    /* ------------------ push notifications -------------------------------------*/
                                    ParseQuery inner= new ParseQuery("_User");
                                    inner.whereContainedIn("email", Arrays.asList(recipient));

                                    List<ParseUser> users=null;

                                    try {
                                        users=inner.find();
                                    } catch (ParseException e2) {
                                        e2.printStackTrace();
                                    }

                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereContainedIn("User",users);


                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
                                    push.setMessage(""+getString(R.string.Message_newMessage) + globalData.getUserObject().getMail());
                                    push.sendInBackground();

                                }

                                Toast.makeText(globalData,"Message sent",Toast.LENGTH_SHORT).show();

                                /* -------------- opening mailbox main view -------------------------------------*/
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                Fragment fragment = MailBoxDisplayFragment.newInstance();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.tab_Home_container, fragment)
                                        .addToBackStack(globalData.getResources().getStringArray(R.array.Menu_items_student)[4])
                                        .commit();

                                globalData.getToolbar().setTitle(globalData.getResources().getStringArray(R.array.Menu_items_student)[4]);
                            }
                            else {

                                Toast.makeText(globalData,"Some error occured while sending the message. The message was eliminated", Toast.LENGTH_SHORT).show();
                                message.deleteEventually();
                            }
                        }
                    });
                }
            }
        });

        ((Toolbar)activity.findViewById(R.id.toolbar)).setTitle(activity.getResources().getString(R.string.new_message_toolbar_title));
        return root;
    }


    public void onSaveInstanceState(Bundle savedInstanceState){

        Log.println(Log.ASSERT,"MAILBOXNEW", "saving to bundle with key: " + BUNDLE_IDENTIFIER_HEADER + bundleIdentifierTail);
        MyBundle b = globalData.addBundle(BUNDLE_IDENTIFIER_HEADER + bundleIdentifierTail);
        b.putString(OBJECT_KEY, objectEdit.getText().toString());
        b.putString(MESSAGE_TEXT_KEY, messageTextEdit.getText().toString());
        b.putString(RECIPIENTS_KEY, recipientsEdit.getText().toString());

        savedInstanceState.putString(BUNDLE_IDENTIFIER_TAIL_KEY, bundleIdentifierTail);
    }

}

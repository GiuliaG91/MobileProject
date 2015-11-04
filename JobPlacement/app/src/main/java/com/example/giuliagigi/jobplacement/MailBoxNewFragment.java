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
import android.widget.LinearLayout;
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

    private static final int WARNING_NO_OBJECT = 0;
    private static final int WARNING_UNKNOWN_RECIPIENT = 1;
    private static final int WARNING_SEND_MYSELF = 2;

    private static final int ERROR_NO_RECIPIENTS = 0;
    private static final int ERROR_NO_TEXT = 1;

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


        // 3) send button ---------------------------------------------------------------------------------------------
        final Button send = (Button) root.findViewById(R.id.send_new_message_btn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<Integer> warnings = new ArrayList<Integer>();
                ArrayList<Integer> errors = new ArrayList<Integer>();

                object = ((EditText) root.findViewById(R.id.object_new_message)).getText().toString();
                String recipientsList = ((EditText) root.findViewById(R.id.recipients_list_new_message)).getText().toString();
                messageText = ((EditText)root.findViewById(R.id.body_new_message)).getText().toString();

                StringTokenizer st = new StringTokenizer(recipientsList, ", ;");
                ArrayList<String> recipientsMails = new ArrayList<String>();
                int validRecipients = 0;

                while (st.hasMoreTokens())
                    recipientsMails.add(st.nextToken());

                for(String r: recipientsMails){

                    try {

                        if(r.equals(globalData.getCurrentUser().getEmail())){

                            if(!warnings.contains(WARNING_SEND_MYSELF))
                                warnings.add(WARNING_SEND_MYSELF);
                            continue;
                        }

                        message.addRecipient(r);
                        validRecipients++;
                    }
                    catch (InboxMessage.UnknownRecipientException e) {

                        if(!warnings.contains(WARNING_UNKNOWN_RECIPIENT))
                            warnings.add(WARNING_UNKNOWN_RECIPIENT);
                    }
                }

                /* ---------------- performing some checks before sending the message --------------- */
                if (validRecipients == 0)
                    errors.add(ERROR_NO_RECIPIENTS);

                if(messageText.trim().length() == 0)
                    errors.add(ERROR_NO_TEXT);

                if (object.trim().length() == 0)
                    warnings.add(WARNING_NO_OBJECT);


                /* ---------------- send the message only if everything is ok ----------------------- */
                if(errors.size()!=0){

                    message = new InboxMessage();
                    showError(errors);
                }
                else if(warnings.size()!=0) showWarning(warnings);
                else                        sendMessage();

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



    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- WARNING AND ERRORS ------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    private void showWarning(ArrayList<Integer> warnings){


        Log.println(Log.ASSERT,"MAILBOXNEW", "showing warnings");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = activity.getLayoutInflater().inflate(R.layout.warning_message_mail_box, null);
        LinearLayout container = (LinearLayout)view.findViewById(R.id.warning_message_container);

        for(Integer code: warnings){

            TextView et = new TextView(globalData);

            switch (code){

                case WARNING_NO_OBJECT:

                    et.setText(globalData.getResources().getString(R.string.no_object_message_warning));

                    break;

                case WARNING_UNKNOWN_RECIPIENT:

                    et.setText(globalData.getResources().getString(R.string.unknown_recipient_message_warning));
                    break;

                case WARNING_SEND_MYSELF:

                    et.setText(globalData.getResources().getString(R.string.send_myself_message_warning));
                    break;

                default: break;
            }

            container.addView(et);
        }

        builder.setView(view);


        builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sendMessage();
            }
        });

        builder.setNegativeButton(globalData.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.create().show();
    }



    private void showError(ArrayList<Integer> errors){

        Log.println(Log.ASSERT,"MAILBOXNEW", "showing errors");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = activity.getLayoutInflater().inflate(R.layout.error_message_mail_box, null);
        LinearLayout container = (LinearLayout)view.findViewById(R.id.error_message_container);

        for(Integer code: errors){

            TextView et = new TextView(globalData);

            switch (code){

                case ERROR_NO_RECIPIENTS:

                    et.setText(globalData.getResources().getString(R.string.no_recipients_message_error));

                    break;

                case ERROR_NO_TEXT:

                    et.setText(globalData.getResources().getString(R.string.no_text_message_error));
                    break;

                default: break;
            }

            container.addView(et);
        }

        builder.setView(view);


        builder.setPositiveButton(globalData.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.create().show();
    }


    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- SEND MESSAGE ------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    public void sendMessage() {

        Log.println(Log.ASSERT, "MAILBOXNEW", "sending message");

        /* ------------------ saving message in sent messages (for sender) -------------- */
        message.setSender(globalData.getCurrentUser());
        message.setType(InboxMessage.TYPE_SENT);
        message.setObject(object);
        message.setBodyMessage(messageText);
        message.setDate(Calendar.getInstance());
        message.setIsPreferred(false);
        message.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                /* ------------ saving message in received messages (one per recipient) --------- */
                if (e == null) {

                    for (ParseUserWrapper recipient : message.getRecipients()) {

                        InboxMessageReceived mr = new InboxMessageReceived();

                        mr.setSender(globalData.getCurrentUser());
                        mr.setOwner(recipient);

                        for (ParseUserWrapper p : message.getRecipients())
                            mr.addRecipient(p);

                        mr.setType(InboxMessage.TYPE_RECEIVED);
                        mr.setObject(object);
                        mr.setBodyMessage(messageText);
                        mr.setIsPreferred(false);
                        mr.setIsRead(false);

                        try {
                            mr.setDate(message.getDate());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        mr.saveInBackground();

                        /* ------------------ push notifications -------------------------------------*/
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("_User", recipient);
                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setMessage("" + getString(R.string.Message_newMessage) + globalData.getUserObject().getMail());
                        push.sendInBackground();
                    }

                    Toast.makeText(globalData, "Message sent", Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(globalData, "Some error occured while sending the message. The message was eliminated", Toast.LENGTH_SHORT).show();
                    message.deleteEventually();
                }
            }
        });
    }
}


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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class MailBoxNewFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
    ArrayList<String> recipients;
    ArrayList<String> wrongRecipients;
    FragmentActivity activity;


    public static MailBoxNewFragment newInstance() {
        MailBoxNewFragment fragment = new MailBoxNewFragment();
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

        message = new InboxMessage();
        message.setRecipients(new ArrayList<User>());
        message.setSender(globalData.getUserObject());

        recipients = new ArrayList<String>();
        wrongRecipients = new ArrayList<String>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mail_box_new,container,false);

        Button button = (Button) root.findViewById(R.id.send_new_message_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String recipientsList = ((EditText)root.findViewById(R.id.recipients_list_new_message)).getText().toString();
                StringTokenizer st = new StringTokenizer(recipientsList, ", ;");

                while(st.hasMoreTokens())
                    recipients.add(st.nextToken());

                ArrayList<User> user_rec = new ArrayList<User>();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

                for(final String s: recipients){
                    query.whereEqualTo(User.MAIL_FIELD, s);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if(e == null){
                                message.getRecipients().add((User)parseObjects.get(0));
                            } else {
                                wrongRecipients.add(s);
                            }
                        }
                    });
                }

                String object = ((EditText)root.findViewById(R.id.object_new_message)).getText().toString();
                message.setObject(object);

                String bodyMessage = ((EditText)root.findViewById(R.id.body_new_message)).getText().toString();
                message.setBodyMessage(bodyMessage);

                message.setDate(new Date());
                message.setIsPreferred(false);
                message.setIsRead(false);

                message.saveEventually();

                // Invio notifica Push solo se è la Company ad inviare il messaggio
                if(globalData.getUserObject() instanceof Company){
                    for(String s: recipients) {
                        ParseQuery userQuery = ParseQuery.getQuery("User");
                        userQuery.whereEqualTo(User.MAIL_FIELD, s);

                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereMatchesQuery("user", userQuery);


                        JSONObject company = globalData.getUserObject().getJSONObject("company");

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setData(company);
                        String m = ((Company) globalData.getUserObject()).getName() + ": " + message.getObject();
                        push.setMessage(m);
                        push.sendInBackground();
                    }
                }

                Toast.makeText(globalData, globalData.getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();

                String messageEmailsWrong = globalData.getResources().getString(R.string.message_emails_wrong) + "\n";
                for(String s: wrongRecipients)
                    messageEmailsWrong = messageEmailsWrong + s + "\n";

                Toast.makeText(globalData, messageEmailsWrong, Toast.LENGTH_SHORT).show();

                // PUSH notifications and update the page in real time for the recipients

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
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

        return root;
    }

    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString("recipients", ((EditText)root.findViewById(R.id.recipients_list_new_message)).getText().toString());
        savedInstanceState.putString("object", ((EditText)root.findViewById(R.id.object_new_message)).getText().toString());
        savedInstanceState.putString("bodyMessage", ((EditText)root.findViewById(R.id.body_new_message)).getText().toString());

    }

}

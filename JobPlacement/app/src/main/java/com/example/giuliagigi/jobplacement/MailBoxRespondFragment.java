package com.example.giuliagigi.jobplacement;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Silvia on 12/05/2015.
 */
public class MailBoxRespondFragment extends Fragment {

    View root;
    InboxMessage message;
    GlobalData globalData;
    FragmentActivity activity;
    User recipient;
    User sender;


    public static MailBoxRespondFragment newInstance() {
        MailBoxRespondFragment fragment = new MailBoxRespondFragment();
        return fragment;
    }

    public MailBoxRespondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalData = (GlobalData)getActivity().getApplication();
        activity = this.getActivity();

        message = new InboxMessage();
        message.setRecipients(new ArrayList<User>());
        // setto il destinatario del messaggio prendendo il mittente dal messaggio della vista dettaglio da cui provengo
        message.getRecipients().add(globalData.getCurrentViewMessage().getSender());

        message.setSender(globalData.getUserObject());
        message.setObject(globalData.getCurrentViewMessage().getObject());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mail_box_respond,container,false);

        Button button = (Button) root.findViewById(R.id.send_response_message_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String bodyMessage = ((TextView)root.findViewById(R.id.old_message)).getText().toString();
                bodyMessage = bodyMessage + "\n\n" + ((EditText)root.findViewById(R.id.new_message)).getText().toString();
                message.setBodyMessage(bodyMessage);

                message.setDate(new Date());
                message.setIsPreferred(false);
                message.setIsRead(false);

                message.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                            Toast.makeText(globalData, globalData.getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
                    }
                });

                // PUSH notifications and update the page in real time for the recipient

                FragmentManager fragmentManager = activity.getSupportFragmentManager();

                Fragment fragment = MailBoxFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .commit();

                globalData.getToolbar().setTitle(globalData.getResources().getStringArray(R.array.Menu_items_student)[4]);

            }
        });

        TextView tv = (TextView) root.findViewById(R.id.recipient);
        if(globalData.getCurrentViewMessage().getSender() instanceof Student)
            tv.setText(((Student)globalData.getCurrentViewMessage().getSender()).getName());
        else
            tv.setText(((Company) globalData.getCurrentViewMessage().getSender()).getName());

        tv = (TextView) root.findViewById(R.id.old_message);
        Date date = globalData.getCurrentViewMessage().getDate();
        Resources res = globalData.getResources();
        String oldMessage = res.getString(R.string.on) + " " + date.getDay() + " " + res.getStringArray(R.array.months)[date.getMonth()];
        oldMessage = oldMessage + " " + date.getYear() + " " + globalData.getCurrentViewMessage().getSender().getMail() + res.getString(R.string.wrote) + ":\n";
        oldMessage = oldMessage + globalData.getCurrentViewMessage().getBodyMessage() + "\n";
        tv.setText(oldMessage);
        tv.setTextColor(0x0D47A1);

        if(savedInstanceState != null){
            EditText et = (EditText)root.findViewById(R.id.new_message);
            et.setText(savedInstanceState.getString("newMessage"));
        }


        return root;
    }

    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putString("newMessage", ((EditText)root.findViewById(R.id.new_message)).getText().toString());

    }

}











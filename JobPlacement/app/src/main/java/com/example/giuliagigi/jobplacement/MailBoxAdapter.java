package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Silvia on 10/05/2015.
 */
public class MailBoxAdapter extends RecyclerView.Adapter<MailBoxAdapter.MailboxViewHolder> implements View.OnClickListener{

    private FragmentActivity activity;
    private ArrayList<InboxMessage> messageList;
    private GlobalData globalData;

    public MailBoxAdapter(FragmentActivity a){

        activity = a;
        messageList = new ArrayList<InboxMessage>();
        globalData = (GlobalData) activity.getApplication();
    }

    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- VIEW HOLDER -------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    /* ----------------------- class definition --------------------------- */

    public static class MailboxViewHolder extends RecyclerView.ViewHolder {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        private int position; // the item must know its index inside the list

        private CheckBox cancel;
        private TextView recipients;
        private TextView sender;
        private TextView object;
        private CheckBox pref;
        private CardView cardLayout;
        private LinearLayout rowLayout;
        private TextView date;


        public MailboxViewHolder(View v) {
            super(v);
            v.setTag(this);

            object = (TextView)v.findViewById(R.id.object_message);
            sender = (TextView)v.findViewById(R.id.sender);
            recipients = (TextView)v.findViewById(R.id.recipients_list);
            pref = (CheckBox)v.findViewById(R.id.star_message);
            cancel = (CheckBox)v.findViewById(R.id.delete_message);
            cardLayout = (CardView) v.findViewById(R.id.card_view_mail_box);
            rowLayout = (LinearLayout) v.findViewById(R.id.row_message);
            position = -1;
            date = (TextView) v.findViewById(R.id.date_message);
        }
    }


    /* ----------------------- onCreate --------------------------- */
    @Override
    public MailboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create new views (invoked by the layout manager)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_message_row, parent, false);
        v.setClickable(true);
        v.setOnClickListener(MailBoxAdapter.this);

        MailboxViewHolder vh = new MailboxViewHolder(v);
        return vh;
    }


    /* ----------------------- onBind --------------------------- */
    @Override
    public void onBindViewHolder(final MailboxViewHolder holder, final int position) {

        // Replace the contents of a view (invoked by the layout manager)

        // 1) displaying sender and recipients mails
        if(messageList.get(position).getSender().getEmail().length() < 22)
            holder.sender.setText(messageList.get(position).getSender().getEmail());
        else
            holder.sender.setText(messageList.get(position).getSender().getEmail().substring(0, 19) + "...");

        String recipients = "";
        for(ParseUserWrapper u : messageList.get(position).getRecipients())
            recipients += u.getEmail() + ", ";

        if(recipients.length() < 22)
            holder.recipients.setText(recipients);
        else
            holder.recipients.setText(recipients.substring(0, 19) + "...");

        // 2) displaying mail's object
        if(messageList.get(position).getObject().length() < 8)
            holder.object.setText(messageList.get(position).getObject());
        else
            holder.object.setText(messageList.get(position).getObject().substring(0, 5) + "...");

        // 3) setting "preferred" checkbox
        holder.pref.setChecked(messageList.get(position).getIsPreferred());
        holder.pref.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(((CheckBox)v).isChecked()) {
                    messageList.get(position).setIsPreferred(true);
                    messageList.get(position).saveInBackground();

                }else {
                    messageList.get(position).setIsPreferred(false);
                    messageList.get(position).saveInBackground();

                }
            }
        });

        // 4) setting "delete" checkbox
        holder.cancel.setChecked(messageList.get(position).isDelete());
        holder.cancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                messageList.get(position).deleteMessage(((CheckBox)v).isChecked());

                if(((CheckBox)v).isChecked()) {

                    holder.cardLayout.setCardBackgroundColor(0xFFE0B2);
                    holder.rowLayout.setBackgroundColor(0xFFE0B2);

                }
                else if(messageList.get(position).getType().equals(InboxMessage.TYPE_RECEIVED)){

                    // only if the message is a received message, we must distinguish
                    // if it was read or not
                    InboxMessageReceived m = (InboxMessageReceived)messageList.get(position);

                    if(m.getIsRead()) {
                        holder.cardLayout.setCardBackgroundColor(0xB2EBF2);
                        holder.rowLayout.setBackgroundColor(0xB2EBF2);
                    }else {
                        holder.cardLayout.setCardBackgroundColor(0xFFFFFF);
                        holder.rowLayout.setBackgroundColor(0xFFFFFF);
                    }
                }
            }

        });

        // 6) displaying the message text (if any)
        holder.position = position;
        if(messageList.get(position).getType().equals(InboxMessage.TYPE_RECEIVED)){

            InboxMessageReceived m = (InboxMessageReceived)messageList.get(position);

            if(m.getIsRead()){

                holder.cardLayout.setCardBackgroundColor(0xB2EBF2);
                holder.rowLayout.setBackgroundColor(0xB2EBF2);
            }
        }

        if(messageList.get(position).isDelete()){
            holder.cardLayout.setCardBackgroundColor(0xFFE0B2);
            holder.rowLayout.setBackgroundColor(0xFFE0B2);
        }

        try {
            Calendar dateMessage = messageList.get(position).getDate();
            Calendar now = Calendar.getInstance();

            if(dateMessage.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) && dateMessage.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dateMessage.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                holder.date.setText(dateMessage.get(Calendar.HOUR_OF_DAY) + ":" + dateMessage.get(Calendar.MINUTE));
            else if(dateMessage.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                holder.date.setText(dateMessage.get(Calendar.DAY_OF_MONTH) + " " + globalData.getResources().getStringArray(R.array.months)[dateMessage.get(Calendar.MONTH)]);
            else
                holder.date.setText(dateMessage.get(Calendar.DAY_OF_MONTH) + "/" + dateMessage.get(Calendar.MONTH) + "/" + dateMessage.get(Calendar.YEAR));

        }catch(JSONException e){
            e.printStackTrace();
        }

    }



    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- LIST MANAGEMENT ---------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessageToList(InboxMessage message){
        Log.println(Log.ASSERT, "MREC ADAPTER", "updating message list");
        messageList.add(message);
        Collections.sort(messageList);
    }

    public void resetMessageList(){
        messageList.clear();
    }

    public void removeMessage(int index){

        messageList.remove(index);
    }

    public ArrayList<InboxMessage> getMessageList(){

        return this.messageList;
    }



    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- ITEM CLICK --------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onClick(View v) {

        // 1) obtain message
        MailboxViewHolder vh = (MailboxViewHolder)v.getTag();
        MailBoxDetailFragment fragment = MailBoxDetailFragment.newInstance(messageList.get(vh.position));

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        //clear backstack
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fragmentManager.popBackStack();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack((String)vh.object.getText())
                .commit();

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(vh.object.getText());
    }
}

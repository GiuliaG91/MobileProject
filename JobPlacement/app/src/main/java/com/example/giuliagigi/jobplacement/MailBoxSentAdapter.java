package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Silvia on 20/05/2015.
 */
public class MailBoxSentAdapter extends RecyclerView.Adapter<MailBoxSentAdapter.ViewHolder> implements View.OnClickListener, MailBoxAdapter{

    private FragmentActivity context;
    private ArrayList<InboxMessageSent> messageList;
    private GlobalData globalData;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cancel;
        private ImageView image;
        private TextView name;
        private TextView object;
        private CheckBox pref;
        private TextView message;
        private int position;
        private CardView cardLayout;
        private LinearLayout rowLayout;
        private TextView date;


        public ViewHolder(View v) {
            super(v);
            v.setTag(this);

            image = (ImageView)v.findViewById(R.id.sender_img);
            object = (TextView)v.findViewById(R.id.object_message);
            name = (TextView)v.findViewById(R.id.sender);
            pref = (CheckBox)v.findViewById(R.id.star_message);
            cancel = (CheckBox)v.findViewById(R.id.delete_message);
            message = (TextView)v.findViewById(R.id.body_message);
            cardLayout = (CardView) v.findViewById(R.id.card_view_mail_box);
            rowLayout = (LinearLayout) v.findViewById(R.id.row_message);
            position = -1;
            date = (TextView) v.findViewById(R.id.date_message);
        }
    }

    public MailBoxSentAdapter(FragmentActivity c)
    {
        context = c;
        messageList = new ArrayList<InboxMessageSent>();
        globalData = (GlobalData)context.getApplication();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public  MailBoxSentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_message_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(MailBoxSentAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String recipients = "";
        for(ParseUserWrapper s: messageList.get(position).getRecipients())
            recipients = recipients + s.getEmail() + ", ";
        recipients = recipients.substring(0, recipients.length()-2);

        if(recipients.length() < 15)
            holder.name.setText(globalData.getResources().getString(R.string.to) + " " + recipients);
        else{
            holder.name.setText(globalData.getResources().getString(R.string.to) + " " + recipients.substring(0, 14) + "...");
        }

        if(globalData.getUserObject().getProfilePhoto() != null)
            holder.image.setImageBitmap(globalData.getUserObject().getProfilePhoto());


        if(messageList.get(position).getObject().length() < 20)
            holder.object.setText(messageList.get(position).getObject());
        else
            holder.object.setText(messageList.get(position).getObject().substring(0, 19) + "...");


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

        holder.cancel.setChecked(messageList.get(position).getIsDeleting());

        holder.cancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(((CheckBox)v).isChecked()) {
                    messageList.get(position).setIsDeleting(true);
                    messageList.get(position).saveInBackground();
                    holder.cardLayout.setCardBackgroundColor(0xFFE0B2);
                    holder.rowLayout.setBackgroundColor(0xFFE0B2);
                }else {
                    messageList.get(position).setIsDeleting(false);
                    messageList.get(position).saveInBackground();
//                    if(messageList.get(position).getIsRead()) {
//                        holder.cardLayout.setCardBackgroundColor(0xB2EBF2);
//                        holder.rowLayout.setBackgroundColor(0xB2EBF2);
//                    }else {
//                        holder.cardLayout.setCardBackgroundColor(0xFFFFFF);
//                        holder.rowLayout.setBackgroundColor(0xFFFFFF);
//                    }
                }

            }

        });

        if(holder.message != null) {

            String message = messageList.get(position).getBodyMessage();
            if (message.length() < 25) {
                holder.message.setText(message);
            } else {
                holder.message.setText(message.substring(0, 24) + "...");
            }
        }

        holder.position = position;

//        if(messageList.get(position).getIsRead()){
//            //holder.name.setTextColor(0x424242);
//            //holder.object.setTextColor(0x424242);
//            holder.cardLayout.setCardBackgroundColor(0xB2EBF2);
//            holder.rowLayout.setBackgroundColor(0xB2EBF2);
//        }

        if(messageList.get(position).getIsDeleting()){
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public void updateMyDataset(List<InboxMessageSent> messages){

        messageList.addAll(messages);

    }

    public void resetMyDataset(){
        messageList.clear();
    }

    public void removeMessageFromMyDataset(int index){

        messageList.remove(index);

    }


    @Override
    public void onClick(View v) {


        ViewHolder vh = (ViewHolder)v.getTag();

//        messageList.get(vh.position).setIsRead(true);
        messageList.get(vh.position).saveInBackground();

        globalData.setCurrentViewMessage(messageList.get(vh.position));

        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        MailBoxDetailFragment fragment = MailBoxDetailFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack((String)vh.object.getText())
                .commit();


        globalData.getToolbar().setTitle(vh.object.getText());

    }

    public ArrayList<InboxMessageSent> getMessageList(){

        return this.messageList;

    }




}

package com.example.giuliagigi.jobplacement;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silvia on 10/05/2015.
 */
public class MailBoxAdapter extends RecyclerView.Adapter<MailBoxAdapter.ViewHolder> implements View.OnClickListener{

    private FragmentActivity context;
    private ArrayList<InboxMessage> mDataset;
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
        private LinearLayout rowLayout;


        public ViewHolder(View v) {
            super(v);
            v.setTag(this);

            image = (ImageView)v.findViewById(R.id.sender_img);
            object = (TextView)v.findViewById(R.id.object_message);
            name = (TextView)v.findViewById(R.id.sender);
            pref = (CheckBox)v.findViewById(R.id.star_message);
            cancel = (CheckBox)v.findViewById(R.id.delete_message);
            message = (TextView)v.findViewById(R.id.body_message);
            rowLayout = (LinearLayout) v.findViewById(R.id.row_message);
            position = -1;
        }
    }

    public MailBoxAdapter(FragmentActivity c)
    {
        context = c;
        mDataset = new ArrayList<InboxMessage>();
        globalData = (GlobalData)context.getApplication();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public  MailBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_message_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(MailBoxAdapter.this);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.image.setImageBitmap(mDataset.get(position).getSender().getProfilePhoto());

        holder.object.setText(mDataset.get(position).getObject());

        User user = mDataset.get(position).getSender();

        if(user instanceof Student)
            holder.name.setText(((Student)mDataset.get(position).getSender()).getName());
        else
            holder.name.setText(((Company)mDataset.get(position).getSender()).getName());


        holder.pref.setChecked(mDataset.get(position).getIsPreferred());
        holder.cancel.setChecked(mDataset.get(position).getIsDeleting());

        holder.cancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(((CheckBox)v).isChecked())
                    mDataset.get(position).setIsDeleting(true);
                else
                    mDataset.get(position).setIsDeleting(false);

            }

        });

        if(holder.message != null) {

            String message = mDataset.get(position).getBodyMessage();
            if (message.isEmpty()) {
                holder.message.setText("");
            } else {

                if (message.length() < 15) {
                    holder.message.setText(message);
                } else {
                    holder.message.setText(message.substring(0, 15) + "...");
                }
            }

        }

        holder.position = position;

        if(mDataset.get(position).getIsRead()){
            holder.name.setTextColor(0x424242);
            holder.object.setTextColor(0x424242);
            holder.rowLayout.setBackgroundColor(0xB2EBF2);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void updateMyDataset(List<InboxMessage> messages)
    {
        mDataset.addAll(messages);

    }

    public void removeMessageFromMyDataset(int index){

        mDataset.remove(index);

    }


    @Override
    public void onClick(View v) {


        ViewHolder vh = (ViewHolder)v.getTag();

        mDataset.get(vh.position).setIsRead(true);

        globalData.setCurrentViewMessage(mDataset.get(vh.position));

        FragmentManager fragmentManager = context.getSupportFragmentManager();

        //New Fragment
        MailBoxDetailFragment fragment = MailBoxDetailFragment.newInstance();

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("MailBox")
                .commit();


        // 1) --> aggiungere al backstack il fragment MailBoxFragment oppure implementare un tasto up
        // nella action bar per ritornare alla vista precedente anche tramite la action bar

        // 2) --> annullare l'attuale toolbar in modo tale da caricare gli action button
        // per il fragment MailBoxDetailFragment

        Toolbar toolbar = globalData.getToolbar();
        toolbar.setTitle(vh.object.getText());


    }

    public ArrayList<InboxMessage> getMyDataset(){

        return this.mDataset;

    }


}

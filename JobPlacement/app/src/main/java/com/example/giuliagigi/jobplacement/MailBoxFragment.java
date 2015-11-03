package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;


public class MailBoxFragment extends Fragment {

    public static final String BUNDLE_IDENTIFIER = "mailbox_bundle";
    public static final String BUNDLE_KEY_MODE = "mode";
    public static final String BUNDLE_KEY_MESSAGE = "message";

    public static final int MODE_DISPLAY = 1;
    public static final int MODE_NEW = 2;
    public static final int MODE_DETAIL = 3;

    protected View root;
    private GlobalData globalData;
    private OnFragmentInteractionListener mListener;

    private RecyclerView messageRecyclerView;
    private MailBoxAdapter adapterReceived, adapterSent;
    private LinearLayoutManager mLayoutManager;
    private ParseQueryAdapter<InboxMessageReceived> queryAdapterReceived;
    private ParseQueryAdapter<InboxMessageSent> queryAdapterSent;

    private int currentMode = 1;


    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTORS, SETTERS ---------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    public static MailBoxFragment newInstance() {
        MailBoxFragment fragment = new MailBoxFragment();
        return fragment;
    }

    public MailBoxFragment() {}



    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- STANDARD CALLBACKS -------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.println(Log.ASSERT, "MAILBOX", "onCreate");

        globalData=(GlobalData)getActivity().getApplication();
        setHasOptionsMenu(true);
        mListener = (OnFragmentInteractionListener)this.getActivity();

        MyBundle b = globalData.getBundle(BUNDLE_IDENTIFIER);
        if(b != null){

            Log.println(Log.ASSERT, "MAILBOX", "found a bundle");
            currentMode = b.getInt(BUNDLE_KEY_MODE);
//            this.currentMode = savedInstanceState.getInt("currentMode");
            switch(currentMode){

                case MODE_DISPLAY:  // MessageBox principale

                    // TODO: aprire messaggi ricevuti o inviati

                    break;

                case MODE_NEW:  // MessageBox creazione nuovo messaggio

                    FragmentManager fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();
                    Fragment fragment = MailBoxNewFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title))
                            .commit();

                    Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
                    toolbar.setTitle(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title));

                    break;

                case MODE_DETAIL:  // MessageBox vista dettaglio

                    fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();
                    fragment = MailBoxDetailFragment.newInstance((InboxMessage)b.get(BUNDLE_KEY_MESSAGE));

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(((GlobalData)((Activity)mListener).getApplication()).getCurrentViewMessage().getObject())
                            .commit();

                    toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
                    toolbar.setTitle((CharSequence)((GlobalData)((Activity)mListener).getApplication()).getCurrentViewMessage().getObject());

                    break;

                default: break;
            }
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        globalData.setToolbarTitle(getString(R.string.ToolbarTilteMailBox));
        root = inflater.inflate(R.layout.fragment_mailbox, container, false);

        // 1) ---------- setting recyclerView parameters -----------------------------------

        messageRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_student_mailbox);
        messageRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        messageRecyclerView.setLayoutManager(mLayoutManager);
        messageRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));


        // 2) ---------- creating queries -------------------------------------------------

        ParseQueryAdapter.QueryFactory<InboxMessageReceived> factoryReceived =
                new ParseQueryAdapter.QueryFactory<InboxMessageReceived>() {
                    public ParseQuery create() {

                        ParseQuery<InboxMessageReceived> query = new ParseQuery<InboxMessageReceived>(InboxMessageReceived.class);
                        query.whereEqualTo(InboxMessageReceived.OWNER, globalData.getCurrentUser());
                        return query;
                    }
                };

        final ParseQueryAdapter.QueryFactory<InboxMessageSent> factorySent =
                new ParseQueryAdapter.QueryFactory<InboxMessageSent>() {
                    public ParseQuery create() {

                        ParseQuery<InboxMessageSent> query = new ParseQuery<InboxMessageSent>(InboxMessageSent.class);
                        query.whereEqualTo(InboxMessage.SENDER, globalData.getCurrentUser());
                        return query;
                    }
                };


        // 3) ---------- setting paged queries parameters -------------------------------

        queryAdapterReceived = new ParseQueryAdapter<>(getActivity(), factoryReceived);
        queryAdapterReceived.setObjectsPerPage(15);
        queryAdapterReceived.addOnQueryLoadListener(new OnQueryLoadListener());
        adapterReceived = new MailBoxAdapter(this.getActivity());

        queryAdapterSent = new ParseQueryAdapter<>(getActivity(), factorySent);
        queryAdapterSent.setObjectsPerPage(15);
        queryAdapterSent.addOnQueryLoadListener(new OnQueryLoadListenerSent());
        adapterSent = new MailBoxAdapter(this.getActivity());


        // 4) ---------- setting default view behaviour (received messages) ------------

        messageRecyclerView.setAdapter(adapterReceived);
        queryAdapterReceived.loadObjects();
        ((Toolbar) ((FragmentActivity) mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity) mListener).getResources().getString(R.string.received_mail));


        // 5) ---------- paged queries management --------------------------------------

        messageRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int total = mLayoutManager.getItemCount();

                if (mLayoutManager.findLastVisibleItemPosition() == total - 1) {

                    if (((MailBoxAdapter) messageRecyclerView.getAdapter()).equals(adapterReceived))
                        queryAdapterReceived.loadNextPage();
                    else
                        queryAdapterSent.loadNextPage();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        // 6) ---------- opening new message -------------------------------------------------------

        final FloatingActionButton newMessageButton = (FloatingActionButton) root.findViewById(R.id.action_new_message);
        newMessageButton.setIcon(R.drawable.ic_mail);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) mListener).getSupportFragmentManager();

                Fragment fragment = MailBoxNewFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(((FragmentActivity) mListener).getResources().getString(R.string.new_message_toolbar_title))
                        .commit();

                Toolbar toolbar = (Toolbar) ((Activity) mListener).findViewById(R.id.toolbar);
                toolbar.setTitle(((Activity) mListener).getResources().getString(R.string.new_message_toolbar_title));
                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });


        // 7) ---------- cancel selected mails -----------------------------------------------------

        final FloatingActionButton cancelButton = (FloatingActionButton) root.findViewById(R.id.action_cancel);
        cancelButton.setIcon(R.drawable.ic_delete);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MailBoxFragment.deleteMessages(root, messageRecyclerView);
                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions_mailbox)).collapse();

            }
        });


        // 8) ---------- opening received mails ----------------------------------------------------

        final FloatingActionButton receivedMailsButton = (FloatingActionButton) root.findViewById(R.id.action_received_mail);
        receivedMailsButton.setIcon(R.drawable.ic_comment_white_36dp);
        receivedMailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MailBoxAdapter) messageRecyclerView.getAdapter()).equals(adapterSent)) {

                    adapterSent.resetMessageList();
                    messageRecyclerView.setAdapter(adapterReceived);
                    queryAdapterReceived.loadObjects();

                    ((Toolbar) ((FragmentActivity) mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity) mListener).getResources().getString(R.string.received_mail));

                }
                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });


        // 9) ---------- opening sent mails --------------------------------------------------------

        final FloatingActionButton sentMailsButton = (FloatingActionButton) root.findViewById(R.id.action_sent_mail);
        sentMailsButton.setIcon(R.drawable.ic_send_white_36dp);
        sentMailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MailBoxAdapter) messageRecyclerView.getAdapter()).equals(adapterReceived)) {

                    adapterReceived.resetMessageList();
                    messageRecyclerView.setAdapter(adapterSent);
                    queryAdapterSent.loadObjects();

                    ((Toolbar) ((FragmentActivity) mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity) mListener).getResources().getString(R.string.sent_mail));
                }
                ((FloatingActionsMenu) root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });


        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        MyBundle b = globalData.addBundle(BUNDLE_IDENTIFIER);
        b.putInt(BUNDLE_KEY_MODE,currentMode);

//        savedInstanceState.putInt("currentMode", this.currentMode);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- ACTIVITY INTERFACE ------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- QUERY INTERFACE ---------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<InboxMessageReceived> {

        public void onLoading() {}

        @Override
        public void onLoaded(List<InboxMessageReceived> inboxMessages, Exception e) {

            Log.println(Log.ASSERT, "MAILBOX", "received message query loading finished");

            if(e == null)
                for(InboxMessage m:inboxMessages)
                    ((MailBoxAdapter) messageRecyclerView.getAdapter()).addMessageToList(m);
            else
                Log.println(Log.ASSERT,"MAILBOX", "Error loading query results");

            ((MailBoxAdapter) messageRecyclerView.getAdapter()).notifyDataSetChanged();

        }
    }

    public class OnQueryLoadListenerSent implements ParseQueryAdapter.OnQueryLoadListener<InboxMessageSent> {

        public void onLoading() {}

        @Override
        public void onLoaded(List<InboxMessageSent> inboxMessages, Exception e) {

            Log.println(Log.ASSERT, "MAILBOX", "sent message query loading finished");

            if(e == null)
                for(InboxMessage m:inboxMessages)
                    ((MailBoxAdapter) messageRecyclerView.getAdapter()).addMessageToList(m);
            else
                Log.println(Log.ASSERT,"MAILBOX", "Error loading query results");

            ((MailBoxAdapter) messageRecyclerView.getAdapter()).notifyDataSetChanged();
        }
    }


    /* -------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- AUXILIARY METHODS -------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------------------------------------- */

    public static void deleteMessages(View root, RecyclerView mRecyclerView){

        MailBoxAdapter adapter = (MailBoxAdapter)mRecyclerView.getAdapter();
        ArrayList<InboxMessage> listMessages = adapter.getMessageList();

        for(int i = 0; i < listMessages.size(); i++){

            if(listMessages.get(i).isDelete()){

                adapter.removeMessage(i);
                listMessages.get(i).deleteInBackground();
            }
        }

        adapter.notifyDataSetChanged();

    }





}

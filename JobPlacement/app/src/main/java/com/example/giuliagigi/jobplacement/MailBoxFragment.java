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

    View root;
    private RecyclerView mRecyclerView;
    private MailBoxSentAdapter adapterSent;
    private MailBoxReceivedAdapter adapterReceived;
    private LinearLayoutManager mLayoutManager;
    private ParseQueryAdapter<InboxMessageReceived> queryAdapterReceived;
    private ParseQueryAdapter<InboxMessageSent> queryAdapterSent;
    private int currentMailBoxFragment;
    private GlobalData globalData;

    private boolean loading = true;

    private OnFragmentInteractionListener mListener;


    public static MailBoxFragment newInstance() {
        MailBoxFragment fragment = new MailBoxFragment();
        fragment.currentMailBoxFragment = 1;
        return fragment;
    }

    public MailBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalData=(GlobalData)getActivity().getApplication();
        setHasOptionsMenu(true);

        mListener = (OnFragmentInteractionListener)this.getActivity();

        if(savedInstanceState != null){

            this.currentMailBoxFragment = savedInstanceState.getInt("currentMailBoxFragment");
            switch(currentMailBoxFragment){

                case 1:  // MessageBox principale
                    break;

                case 2:  // MessageBox creazione nuovo messaggio
                    FragmentManager fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                    Fragment fragment = MailBoxNewFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title))
                            .commit();

                    Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
                    toolbar.setTitle(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title));

                    break;

                case 3:  // MessageBox vista dettaglio
                    fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                    fragment = MailBoxDetailFragment.newInstance();

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
        // Inflate the layout for this fragment
        globalData.setToolbarTitle(getString(R.string.ToolbarTilteMailBox));

        root = inflater.inflate(R.layout.fragment_mailbox, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_student_mailbox);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*ParseQueryAdapter */

        ParseQueryAdapter.QueryFactory<InboxMessageReceived> factoryReceived =
                new ParseQueryAdapter.QueryFactory<InboxMessageReceived>() {
                    public ParseQuery create() {

                        ParseQuery query = new ParseQuery("InboxMessageReceived");
                        query.whereEqualTo(InboxMessageReceived.OWNER, globalData.getCurrentUser());
                        return query;
                    }
                };

        final ParseQueryAdapter.QueryFactory<InboxMessageSent> factorySent =
                new ParseQueryAdapter.QueryFactory<InboxMessageSent>() {
                    public ParseQuery create() {

                        ParseQuery query = new ParseQuery("InboxMessage");
                        query.whereEqualTo(InboxMessageSent.SENDER, globalData.getCurrentUser());
                        return query;
                    }
                };

        queryAdapterReceived = new ParseQueryAdapter<>(getActivity(), factoryReceived);
        queryAdapterReceived.setObjectsPerPage(15);
        queryAdapterReceived.addOnQueryLoadListener(new OnQueryLoadListenerReceived());

        queryAdapterSent = new ParseQueryAdapter<>(getActivity(), factorySent);
        queryAdapterSent.setObjectsPerPage(15);
        queryAdapterSent.addOnQueryLoadListener(new OnQueryLoadListenerSent());


        adapterReceived = new MailBoxReceivedAdapter(this.getActivity());
        adapterSent = new MailBoxSentAdapter(this.getActivity());

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapterReceived);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int total=   mLayoutManager.getItemCount();
                if(mLayoutManager.findLastVisibleItemPosition()== total-1)
                {
                    if(mRecyclerView.getAdapter() instanceof MailBoxReceivedAdapter)
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

        queryAdapterReceived.loadObjects();

        /*Attach on click listener to button menu */

        final FloatingActionButton newMessageButton = (FloatingActionButton)root.findViewById(R.id.action_new_message);
        newMessageButton.setIcon(R.drawable.ic_mail);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                Fragment fragment = MailBoxNewFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title))
                        .commit();

                Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
                toolbar.setTitle(((Activity)mListener).getResources().getString(R.string.new_message_toolbar_title));
                ((FloatingActionsMenu)root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });

        final FloatingActionButton sentMailsButton = (FloatingActionButton)root.findViewById(R.id.action_sent_mail);
        sentMailsButton.setIcon(R.drawable.ic_send_white_36dp);
        sentMailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRecyclerView.getAdapter() instanceof MailBoxReceivedAdapter) {
                    adapterReceived.resetMessageList();

                    mRecyclerView.setAdapter(adapterSent);
                    queryAdapterSent.loadObjects();

                    ((Toolbar)((FragmentActivity)mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity)mListener).getResources().getString(R.string.sent_mail));
                }
                ((FloatingActionsMenu)root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });

        final FloatingActionButton cancelButton = (FloatingActionButton)root.findViewById(R.id.action_cancel);
        cancelButton.setIcon(R.drawable.ic_delete);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MailBoxFragment.deleteMessages(root, mRecyclerView);
                ((FloatingActionsMenu)root.findViewById(R.id.multiple_actions_mailbox)).collapse();

            }
        });

        final FloatingActionButton receivedMailsButton = (FloatingActionButton)root.findViewById(R.id.action_received_mail);
        receivedMailsButton.setIcon(R.drawable.ic_comment_white_36dp);
        receivedMailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRecyclerView.getAdapter() instanceof MailBoxSentAdapter){
                    adapterSent.resetMyDataset();

                    mRecyclerView.setAdapter(adapterReceived);
                    queryAdapterReceived.loadObjects();

                    ((Toolbar)((FragmentActivity)mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity)mListener).getResources().getString(R.string.received_mail));

                }
                ((FloatingActionsMenu)root.findViewById(R.id.multiple_actions_mailbox)).collapse();
            }
        });

        ((Toolbar)((FragmentActivity)mListener).findViewById(R.id.toolbar)).setTitle(((FragmentActivity)mListener).getResources().getString(R.string.received_mail));

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class OnQueryLoadListenerReceived implements ParseQueryAdapter.OnQueryLoadListener<InboxMessageReceived> {

        public void onLoading() {
            //rotellina
        }

        @Override
        public void onLoaded(List<InboxMessageReceived> inboxMessages, Exception e) {

            ((MailBoxReceivedAdapter)mRecyclerView.getAdapter()).updateMyDataset(inboxMessages);
            ((MailBoxReceivedAdapter)mRecyclerView.getAdapter()).notifyDataSetChanged();

        }
    }

    public class OnQueryLoadListenerSent implements ParseQueryAdapter.OnQueryLoadListener<InboxMessageSent> {

        public void onLoading() {
            //rotellina
        }

        @Override
        public void onLoaded(List<InboxMessageSent> inboxMessages, Exception e) {

            ((MailBoxSentAdapter)mRecyclerView.getAdapter()).updateMyDataset(inboxMessages);
            ((MailBoxSentAdapter)mRecyclerView.getAdapter()).notifyDataSetChanged();

        }
    }


    public static void deleteMessages(View root, RecyclerView mRecyclerView){

        ArrayList listMessages = null;
        if(mRecyclerView.getAdapter() instanceof  MailBoxReceivedAdapter){

            listMessages = ((MailBoxReceivedAdapter)mRecyclerView.getAdapter()).getMessageList();
        }
        else{

            listMessages = ((MailBoxSentAdapter)mRecyclerView.getAdapter()).getMessageList();

        }

        for(int i = 0; i < listMessages.size(); i++){

            InboxMessage m = (InboxMessage)listMessages.get(i);

            if(m.getIsDeleting()){

                if(mRecyclerView.getAdapter() instanceof MailBoxReceivedAdapter)
                    ((MailBoxReceivedAdapter)mRecyclerView.getAdapter()).removeMessage(i);
                else
                    ((MailBoxSentAdapter)mRecyclerView.getAdapter()).removeMessageFromMyDataset(i);

                m.deleteInBackground();
            }
        }

        if(mRecyclerView.getAdapter() instanceof  MailBoxReceivedAdapter)
            ((MailBoxReceivedAdapter)mRecyclerView.getAdapter()).notifyDataSetChanged();
        else
            ((MailBoxSentAdapter)mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    public int getCurrentMailBoxFragment(){
        return this.currentMailBoxFragment;
    }

    public void setCurrentMailBoxFragment(int i){
        this.currentMailBoxFragment = i;
    }

    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putInt("currentMailBoxFragment", this.currentMailBoxFragment);

    }

}

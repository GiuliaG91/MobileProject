package com.example.giuliagigi.jobplacement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;


public class MailBoxFragment extends Fragment {

    View root;
    private RecyclerView mRecyclerView;
    private MailBoxAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private ParseQueryAdapter<InboxMessageReceived> queryAdapter;
    private int currentMailBoxFragment;

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
                            .addToBackStack(((FragmentActivity)mListener).getResources().getStringArray(R.array.Menu_items_student)[4])
                            .commit();

                    Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);

                    //toolbar.setTitle(((Activity) mListener).getResources().getString(R.string.new_message_toolbar_title));

                    break;

                case 3:  // MessageBox vista dettaglio
                    fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                    fragment = MailBoxDetailFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(((FragmentActivity)mListener).getResources().getStringArray(R.array.Menu_items_student)[4])
                            .commit();

                    toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);

                    //toolbar.setTitle((CharSequence)((GlobalData)((Activity)mListener).getApplication()).getCurrentViewMessage().getObject());

                    break;

                case 4:  // MessageBox rispondi al mittente
                    fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                    fragment = MailBoxRespondFragment.newInstance();

                    fragmentManager.beginTransaction()
                            .replace(R.id.tab_Home_container, fragment)
                            .addToBackStack(((FragmentActivity)mListener).getResources().getStringArray(R.array.Menu_items_student)[4])
                            .commit();

                    toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);

                    //toolbar.setTitle((CharSequence)((GlobalData)((Activity)mListener).getApplication()).getCurrentViewMessage().getObject());

                    break;

                default: break;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        // Instantiate a QueryFactory to define the ParseQuery to be used for fetching items in this
        // Adapter.
        ParseQueryAdapter.QueryFactory<InboxMessageReceived> factory =
                new ParseQueryAdapter.QueryFactory<InboxMessageReceived>() {
                    public ParseQuery create() {

                        ParseQuery query = new ParseQuery("InboxMessageReceived");
                        query.whereEqualTo(InboxMessageReceived.RECIPIENT, ((GlobalData)((FragmentActivity)mListener).getApplication()).getUserObject().getMail());
                        return query;
                    }
                };

        // Pass the factory into the ParseQueryAdapter's constructor.

        queryAdapter = new ParseQueryAdapter<>(getActivity(), factory);
        queryAdapter.setObjectsPerPage(15);
        queryAdapter.addOnQueryLoadListener(new OnQueryLoadListener());


        adapter = new MailBoxAdapter(this.getActivity());

        /*********************/

        // specify an adapter
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                int total=   mLayoutManager.getItemCount();
                if(mLayoutManager.findLastVisibleItemPosition()== total-1)
                {
                    queryAdapter.loadNextPage();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        queryAdapter.loadObjects();

        // aggiungo listener a bottone di prova per triggerare nuovi messaggi
        Button button = (Button) root.findViewById(R.id.new_btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                FragmentManager fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();

                Fragment fragment = MailBoxNewFragment.newInstance();

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(((FragmentActivity)mListener).getResources().getString(R.string.new_message_toolbar_title))
                        .commit();

                Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
                toolbar.setTitle(((Activity)mListener).getResources().getString(R.string.new_message_toolbar_title));

            }
        });

        /*
        button = (Button) root.findViewById(R.id.mails_sent_btn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ParseQueryAdapter.QueryFactory<InboxMessage> factory =
                        new ParseQueryAdapter.QueryFactory<InboxMessage>() {
                            public ParseQuery create() {

                                ParseQuery query = new ParseQuery("InboxMessage");
                                query.whereEqualTo(InboxMessageReceived.SENDER, ((GlobalData)((FragmentActivity)mListener).getApplication()).getUserObject().getMail());
                                return query;
                            }
                        };
                queryAdapter = new ParseQueryAdapter<>(getActivity(), factory);
                queryAdapter.loadObjects();
            }
        });
        */

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

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<InboxMessageReceived> {

        public void onLoading() {
            //rotellina
        }

        @Override
        public void onLoaded(List<InboxMessageReceived> inboxMessages, Exception e) {

            adapter.updateMyDataset(inboxMessages);
            adapter.notifyDataSetChanged();

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){

        menu.clear();

//        menuInflater.inflate(R.menu.menu_mailbox, menu);

    }


    /*
    @Override
    public void onPrepareOptionsMenu(Menu menu){

        MenuInflater inflater = ((Activity)mListener).getMenuInflater();

        inflater.inflate(R.menu.menu_mailbox, menu);

    }
    */

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public boolean onOptionsItemSelected(MenuItem item){
//
//        switch(item.getItemId()){
//
//            case R.id.new_message_action_button:     FragmentManager fragmentManager = ((FragmentActivity)mListener).getSupportFragmentManager();
//
//                Fragment fragment = MailBoxNewFragment.newInstance();
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.tab_Home_container, fragment)
//                        .commit();
//
//                Toolbar toolbar = (Toolbar) ((Activity)mListener).findViewById(R.id.toolbar);
//
//                //toolbar.setTitle(((Activity)mListener).getResources().getString(R.string.new_message_toolbar_title));
//
//                return true;
//
//            case R.id.delete_messages_action_button: MailBoxFragment.deleteMessages(root, adapter);
//                return true;
//
//            default: return onOptionsItemSelected(item);
//
//        }
//    }

    public static void deleteMessages(View root, MailBoxAdapter adapter){

        ArrayList<InboxMessageReceived> listMessages = adapter.getMyDataset();

        for(int i = 0; i < listMessages.size(); i++){

            InboxMessage m = listMessages.get(i);

            if(m.getIsDeleting()){

                adapter.removeMessageFromMyDataset(i);

                m.deleteInBackground();
            }
        }

        adapter.notifyDataSetChanged();
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

package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileManagementTagsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Tags";

    private User currentUser;

    private ImageButton addTag;
    private GridLayout tagContainer;
    private MultiAutoCompleteTextView tagsText;
    private HashMap<String,Tag> userTags;
    private ArrayList<View> tagViews;

    /* ---------------------------- CONTRUCTIORS GETTERS SETTERS ---------------------------------*/

    public ProfileManagementTagsFragment() {}
    public static ProfileManagementTagsFragment newInstance() {
        ProfileManagementTagsFragment fragment = new ProfileManagementTagsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public String getTitle(){
        return TITLE;
    }


    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentUser = application.getUserObject();
        userTags = currentUser.getTags();
        tagViews = new ArrayList<View>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile_management_tags, container, false);

        tagsText = (MultiAutoCompleteTextView) root.findViewById(R.id.tagsFragment_tagAutoComplete_text);
        addTag = (ImageButton) root.findViewById(R.id.tagsFragment_addTagButton);
        tagContainer = (GridLayout)root.findViewById(R.id.tagsFragment_tagContainer);

        for(String t: userTags.keySet()){

            final View mytagView = inflater.inflate(R.layout.taglayout, null);
            final TextView tagTextView = (TextView) mytagView.findViewById(R.id.tag_tv);
            tagTextView.setText(t);

            mytagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tagContainer.removeView(v);
                    currentUser.removeTag(userTags.get(tagTextView.getText().toString().trim()));
                    userTags.remove(tagTextView.getText().toString().trim());
                    tagViews.remove(mytagView);
                    Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                    hasChanged = true;
                }
            });

            tagContainer.addView(mytagView);
            tagViews.add(mytagView);
        }

        final String[] tagNames = new String[application.getTags().size()];

        int i =0;
        for (String t: application.getTags().keySet())
            tagNames[i++] = t;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, tagNames);

        tagsText.setAdapter(adapter);
        tagsText.setTokenizer(new SpaceTokenizer());
        tagsText.setThreshold(1);

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> support = new ArrayList<String>();
                for (String t: tagNames)
                    support.add(t.toLowerCase().trim());

                ArrayList<String> existent = new ArrayList<String>();
                for(String t: userTags.keySet())
                    existent.add(t.toLowerCase().trim());

                if(support.contains(tagsText.getText().toString().toLowerCase().trim()))
                {
                    if(!existent.contains(tagsText.getText().toString().toLowerCase().trim())) {

                        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View mytagView = inflater.inflate(R.layout.taglayout, null);
                        final TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);

                        tagContainer.addView(mytagView);
                        tagViews.add(mytagView);
                        userTags.put(tagsText.getText().toString().trim(), application.getTags().get(tagsText.getText().toString().trim()));
                        t.setText(tagsText.getText().toString());

                        mytagView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                tagContainer.removeView(v);
                                currentUser.removeTag(userTags.get(t.getText().toString().trim()));
                                userTags.remove(t.getText().toString().trim());
                                tagViews.remove(mytagView);
                                Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        tagsText.setText("");
                        Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT ).show();
                        hasChanged = true;

                    }else Toast.makeText(getActivity().getApplicationContext(),"Existent tag",Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(getActivity().getApplicationContext(),"Wrong tag",Toast.LENGTH_SHORT).show();

            }
        });

//        setEnable(host.isEditMode());

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* ----------------------- AUXILIARY METHODS -------------------------------------------------*/

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);

        addTag.setEnabled(enable);
        tagsText.setEnabled(enable);

        for(View tag:tagViews)
            tag.setEnabled(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();

        Log.println(Log.ASSERT, "TAGS FRAG", "lista tag: ");

        for(Tag t: userTags.values()){

            Log.println(Log.ASSERT, "TAGS FRAG", "tag: " + t.getTag());
            currentUser.addTag(t);
        }

        currentUser.saveEventually();
    }
}

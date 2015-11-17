package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileManagementTagsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Tags";
    public static final String BUNDLE_IDENTIFIER = "PROFILEMANAGEMENTTAGS";

    private ImageButton addTag;
    private GridLayout tagContainer;
    private MultiAutoCompleteTextView tagsText;
    private HashMap<String,Tag> userTags;
    private ArrayList<View> tagViews;

    /* ---------------------------- CONTRUCTIORS GETTERS SETTERS ---------------------------------*/

    public ProfileManagementTagsFragment() {}
    public static ProfileManagementTagsFragment newInstance(User user) {
        ProfileManagementTagsFragment fragment = new ProfileManagementTagsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUser(user);
        return fragment;
    }

    public String getTitle(){
        return TITLE;
    }


    public void setUser(User user){

        this.user = user;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }
    /* ---------------------------- STANDARD CALLBACKS -------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userTags = user.getTags();
        tagViews = new ArrayList<View>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root == null)
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
                    user.removeTag(userTags.get(tagTextView.getText().toString().trim()));
                    userTags.remove(tagTextView.getText().toString().trim());
                    tagViews.remove(mytagView);
                    Toast.makeText(getActivity(), GlobalData.getContext().getString(R.string.string_tags_removed), Toast.LENGTH_SHORT).show();
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
                                user.removeTag(userTags.get(t.getText().toString().trim()));
                                userTags.remove(t.getText().toString().trim());
                                tagViews.remove(mytagView);
                                Toast.makeText(getActivity(), GlobalData.getContext().getString(R.string.string_tags_removed), Toast.LENGTH_SHORT).show();
                            }
                        });

                        tagsText.setText("");
                        Toast.makeText(getActivity(),GlobalData.getContext().getString(R.string.string_tags_added),Toast.LENGTH_SHORT ).show();
                        hasChanged = true;

                    }else Toast.makeText(getActivity().getApplicationContext(),GlobalData.getContext().getString(R.string.string_tags_existent),Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(getActivity().getApplicationContext(),GlobalData.getContext().getString(R.string.string_tags_wrong),Toast.LENGTH_SHORT).show();

            }
        });

        Button tagsExplanation = (Button)root.findViewById(R.id.tagsFragment_tagExplanation_Button);
        tagsExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.tags_description, null);
                builder.setView(view);
                //builder.setTitle(GlobalData.getContext().getString(R.string.string_tags_what_is));
                TextView explanationText = (TextView)view.findViewById(R.id.tags_description_text);
                if(application.getUserObject().getType().equals(User.TYPE_STUDENT))
                    explanationText.setText(GlobalData.getContext().getString(R.string.string_tags_description_student));
                else
                    explanationText.setText(GlobalData.getContext().getString(R.string.string_tags_description_company));
                builder.create().show();
            }
        });

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

        for(Tag t: userTags.values())
            user.addTag(t);

        user.saveEventually();
    }
}

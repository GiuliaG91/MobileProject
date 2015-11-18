package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;


public class StudentProfileManagementLanguageFragment extends ProfileManagementFragment {

    private static final String TITLE = "Language";
    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILELANGUAGE";
    public static final String BUNDLE_KEY_LANGUAGE = "bundle_language";
    public static final String BUNDLE_KEY_STUDENT = "bundle_student";
    public static final String BUNDLE_KEY_LEVEL = "bundle_level";
    public static final String BUNDLE_KEY_DESCRIPTION = "bundle_description";
    public static final String BUNDLE_KEY_PARENT = "bundle_parent";

    private LanguageFragmentInterface parent;
    private Student student;
    private Language language;

    private Spinner languageLevel;
    private EditText languageDesc;
    Button  delete;

    private int currentLevel = 0;
    private String currentDescription = null, parentName = null;

    private boolean isRemoved;


    /* ----------------- CONSTRUCTORS GETTERS SETTERS ------------------------------------------- */

    public StudentProfileManagementLanguageFragment() { super(); }

    public static StudentProfileManagementLanguageFragment newInstance(LanguageFragmentInterface parent, Language language,Student student) {

        StudentProfileManagementLanguageFragment fragment = new StudentProfileManagementLanguageFragment();

        fragment.setLanguage(language);
        fragment.setStudent(student);
        fragment.setUser(student);
        fragment.parent = parent;

        ProfileManagementFragment f = (ProfileManagementFragment)parent;
        fragment.parentName = f.bundleIdentifier();

        return fragment;
    }

    public void setLanguage(Language language){
        this.language = language;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    public String bundleIdentifier(){

        return BUNDLE_IDENTIFIER;
    }

    /* ----------------- STANDARD CALLBACKS ---------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isRemoved = false;

        if(language != null){

            if(language.getLevel() != null)
                currentLevel = Language.getLevelID(language.getLevel());
            currentDescription = language.getDescription();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_student_profile_management_language, container, false);

        languageLevel = (Spinner) root.findViewById(R.id.language_management_spinnerLevel);
        languageLevel.setAdapter(new StringAdapter(Language.LEVELS));
        languageLevel.setSelection(currentLevel);

        delete = (Button) root.findViewById(R.id.language_management_delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(language.getObjectId() != null){

                    language.deleteEventually(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e == null){

                                student.removeLanguage(language);
                                student.saveEventually();
                                root.setVisibility(View.INVISIBLE);
                                parent.onLanguageDelete(StudentProfileManagementLanguageFragment.this);
                                isRemoved = true;

                            }
                            else {

                                Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.profile_object_delete_failure), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {

                    root.setVisibility(View.INVISIBLE);
                    parent.onLanguageDelete(StudentProfileManagementLanguageFragment.this);
                    isRemoved = true;
                }


            }
        });

        languageDesc = (EditText) root.findViewById(R.id.language_management_description_area);
        if (currentDescription != null)
            languageDesc.setText(String.valueOf(currentDescription));
        else
            languageDesc.setText(INSERT_FIELD);

        textFields.add(languageDesc);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for (EditText et : textFields)
            et.addTextChangedListener(hasChangedListener);

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* ----------------- AUXILIARY METHODS ----------------------------------------------------- */

    @Override
    protected void saveStateInBundle(Bundle outstate) {
        super.saveStateInBundle(outstate);

        bundle.put(BUNDLE_KEY_LANGUAGE, language);
        bundle.put(BUNDLE_KEY_STUDENT, student);

        currentLevel = languageLevel.getSelectedItemPosition();
        currentDescription = languageDesc.getText().toString();
        bundle.putString(BUNDLE_KEY_DESCRIPTION, currentDescription);
        bundle.putInt(BUNDLE_KEY_LEVEL,currentLevel);
        bundle.putString(BUNDLE_KEY_PARENT,parentName);

    }

    @Override
    protected void restoreStateFromBundle(Bundle savedInstanceState) {
        super.restoreStateFromBundle(savedInstanceState);

        if(bundle != null){

            student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
            language = (Language)bundle.get(BUNDLE_KEY_LANGUAGE);
            currentLevel = bundle.getInt(BUNDLE_KEY_LEVEL);
            currentDescription = bundle.getString(BUNDLE_KEY_DESCRIPTION);

            parentName = bundle.getString(BUNDLE_KEY_PARENT);
            parent = (LanguageFragmentInterface)application.getFragment(parentName);
        }
    }

    @Override
    public void saveChanges(){

        super.saveChanges();

        if(!isRemoved){

            language.setLevel((String) languageLevel.getSelectedItem());
            if(!languageDesc.getText().toString().equals(INSERT_FIELD)) language.setDescription((String) languageDesc.getText().toString());
            language.setStudent(student);
            language.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e==null){

                        student.addLanguage(language);
                        student.saveEventually();
                    }

                }
            });

        }
    }

    @Override
    public void setEnable(boolean enable) {
        super.setEnable(enable);

        languageLevel.setEnabled(enable);
        languageDesc.setEnabled(enable);

        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        delete.setVisibility(visibility);
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ----------------- PARENT FRAGMENT INTERFACE ---------------------------------------------- */
    /* ------------------------------------------------------------------------------------------ */

    public interface LanguageFragmentInterface{

        public void onLanguageDelete(StudentProfileManagementLanguageFragment toRemove);
    }
}

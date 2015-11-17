package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
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
    private static String BUNDLE_DESCRIPTION = "bundle_description";
    private static String BUNDLE_LEVEL = "bundle_level";
    private static String BUNDLE_HASCHANGED = "bundle_recycled";

    private LanguageFragmentInterface parent;
    private Student student;
    private Language language;

    private Spinner languageLevel;
    private EditText languageDesc;
    Button  delete;

    private boolean isRemoved;


    /* ----------------- CONSTRUCTORS GETTERS SETTERS ------------------------------------------- */

    public StudentProfileManagementLanguageFragment() { super(); }

    public static StudentProfileManagementLanguageFragment newInstance(LanguageFragmentInterface parent, Language language,Student student) {
        StudentProfileManagementLanguageFragment fragment = new StudentProfileManagementLanguageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setLanguage(language);
        fragment.setStudent(student);
        fragment.parent = parent;
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

    /* ----------------- STANDARD CALLBACKS ---------------------------------------------------- */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isNestedFragment = true;
        isRemoved = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int level;
        String description;
        if (getArguments().getBoolean(BUNDLE_HASCHANGED)) {

            level = getArguments().getInt(BUNDLE_LEVEL);
            description = getArguments().getString(BUNDLE_DESCRIPTION);

        } else {

            if (language.getLevel() != null)
                level = Language.getLevelID(language.getLevel());
            else
                level = 0;

            if (language.getDescription() != null)
                description = language.getDescription();
            else
                description = null;
        }

        root = inflater.inflate(R.layout.fragment_student_profile_management_language, container, false);

        languageLevel = (Spinner) root.findViewById(R.id.language_management_spinnerLevel);
        languageLevel.setAdapter(new StringAdapter(Language.LEVELS));
        languageLevel.setSelection(level);


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
        if (description != null)
            languageDesc.setText(String.valueOf(description));
        else
            languageDesc.setText(INSERT_FIELD);

        textFields.add(languageDesc);

        OnFieldChangedListener hasChangedListener = new OnFieldChangedListener();
        for (EditText et : textFields)
            et.addTextChangedListener(hasChangedListener);

//        setEnable(listener.isEditMode());
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        getArguments().putBoolean(BUNDLE_HASCHANGED,hasChanged);

        if(hasChanged){

            getArguments().putInt(BUNDLE_LEVEL,languageLevel.getSelectedItemPosition());
            getArguments().putString(BUNDLE_DESCRIPTION, languageDesc.getText().toString());
        }
    }

    /* ----------------- AUXILIARY METHODS ----------------------------------------------------- */

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

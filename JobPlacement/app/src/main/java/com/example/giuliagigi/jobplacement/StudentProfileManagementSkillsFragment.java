package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment
        implements StudentProfileManagementCertificateFragment.CertificateFragmentInterface, StudentProfileManagementDegreeFragment.DegreeFragmentInterface, StudentProfileManagementLanguageFragment.LanguageFragmentInterface {

    private static final String TITLE = GlobalData.getContext().getString(R.string.profile_skills_tab);
    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILESKILLS";
    private static final String BUNDLE_KEY_STUDENT = "BUNDLE_KEY_STUDENT";

    private Student student;
    private boolean editable;
    Button addDegree, addLanguage,addCertificate, curriculum;
    TextView curriculumName;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;
    ArrayList<StudentProfileManagementLanguageFragment> languageFragments;
    ArrayList<StudentProfileManagementCertificateFragment> certificateFragments;

    /*----------------------- CONSTRUCTORS ------------------------------------------------------*/

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance(Student student, boolean editable) {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setStudent(student);
        fragment.setUser(student);
        fragment.setEditable(editable);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /*----------------------- STANDARD CALLBACKS -------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        degreeFragments = new ArrayList<StudentProfileManagementDegreeFragment>();
        languageFragments = new ArrayList<StudentProfileManagementLanguageFragment>();
        certificateFragments = new ArrayList<StudentProfileManagementCertificateFragment>();
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


        if(root == null)
            root = inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);

        addDegree = (Button)root.findViewById(R.id.skills_add_degree);
        addDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementDegreeFragment dmf;
                dmf = StudentProfileManagementDegreeFragment.newInstance(StudentProfileManagementSkillsFragment.this, new Degree(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_degreeList_container,dmf);
                ft.commit();
                degreeFragments.add(dmf);
            }
        });

        addLanguage = (Button)root.findViewById(R.id.skills_add_language);
        addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementLanguageFragment lmf;
                lmf = StudentProfileManagementLanguageFragment.newInstance(StudentProfileManagementSkillsFragment.this,new Language(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_languageList_container,lmf);
                ft.commit();
                languageFragments.add(lmf);
            }
        });

        addCertificate = (Button)root.findViewById(R.id.skills_add_certificate);
        addCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementCertificateFragment cmf;
                cmf = StudentProfileManagementCertificateFragment.newInstance(StudentProfileManagementSkillsFragment.this, new Certificate(), student);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_certificateList_container,cmf);
                ft.commit();
                certificateFragments.add(cmf);
            }
        });

        curriculum = (Button)root.findViewById(R.id.skills_down_upload_cv);
        curriculumName = (TextView)root.findViewById(R.id.student_cv_name);

        if(student.getCurriculum()!= null)
            curriculumName.setText("Curriculum uploaded!");

        if(editable){

            curriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickCurriculumFile();
                }
            });
        }
        else {

            curriculum.setBackgroundResource(R.drawable.ic_download);
            curriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    downloadCurriculumFile();
                }
            });
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        int max = Math.max(degreeFragments.size(), student.getDegrees().size());
        for(int i=0;i<max;i++){

            if(i>=degreeFragments.size()){

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(this, student.getDegrees().get(i), student);
                degreeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_degreeList_container, degreeFragments.get(i));
            ft.commit();

        }

        max = Math.max(languageFragments.size(), student.getLanguages().size());
        for(int j=0;j<max;j++){

            if(j>=languageFragments.size()){

                StudentProfileManagementLanguageFragment lmf;
                lmf = StudentProfileManagementLanguageFragment.newInstance(this, student.getLanguages().get(j), student);
                languageFragments.add(lmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_languageList_container, languageFragments.get(j));
            ft.commit();

        }

        max = Math.max(certificateFragments.size(), student.getCertificates().size());
        for(int j=0;j<max;j++){

            if(j>=certificateFragments.size()){

                StudentProfileManagementCertificateFragment cmf;
                cmf = StudentProfileManagementCertificateFragment.newInstance(StudentProfileManagementSkillsFragment.this,student.getCertificates().get(j), student);
                certificateFragments.add(cmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_certificateList_container, certificateFragments.get(j));
            ft.commit();

        }


        setEnable(listener.isEditMode());
    }

    @Override
    public void onPause() {
        super.onPause();

        for(Fragment f: degreeFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

        for(Fragment f: languageFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

        for(Fragment f: certificateFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementFragment f: degreeFragments)
            host.removeOnActivityChangedListener(f);
        for (ProfileManagementFragment f: languageFragments)
            host.removeOnActivityChangedListener(f);
        for (ProfileManagementFragment f: certificateFragments)
            host.removeOnActivityChangedListener(f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CONTENT_GET && resultCode == Activity.RESULT_OK){

            Uri fileUri = data.getData();

            if(fileUri != null){

                try {

                    InputStream is = getActivity().getContentResolver().openInputStream(fileUri);
                    BufferedInputStream buffer = new BufferedInputStream(is);

                    int length = 0;
                    while(buffer.read()!= -1)
                        length++;
                    is.close();

                    Log.println(Log.ASSERT,"SKILLS FRAG", "length: " + length);

                    is = getActivity().getContentResolver().openInputStream(fileUri);
                    buffer = new BufferedInputStream(is);
                    byte[] byteArray = new byte[length];
                    buffer.read(byteArray,0,length);
                    is.close();

                    Log.println(Log.ASSERT,"SKILLS FRAG", "file read. proceed uploading");
                    curriculumName.setText("Curriculum uploaded");
                    student.setCurriculum(byteArray);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.println(Log.ASSERT,"SKILLS", "curriculum file not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.println(Log.ASSERT,"SKILLS", "error while reading file");
                }

            }

        }
    }

    /*----------------------- AUXILIARY METHODS ------------------------------------------------------*/

    private void downloadCurriculumFile(){


        Log.println(Log.ASSERT,"SKILLS FRAG", "download curriculum");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to download the curriculum?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    byte[] bytes = student.getCurriculum();

                    if(bytes!= null){

                        File appDirectory = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                "TheJobProvider");
                        appDirectory.mkdirs();

                        File newFile = new File(appDirectory.getPath() + File.separator + "curriculum" + student.getName() + student.getSurname() + ".pdf");
                        newFile.createNewFile();

                        Log.println(Log.ASSERT,"SKILLS", "writing file...");
//                        FileOutputStream outputStream = getActivity().getApplicationContext().openFileOutput("curriculum" + student.getName() + student.getSurname(), Context.MODE_PRIVATE);
                        FileOutputStream outputStream = new FileOutputStream(newFile.getPath());
                        BufferedOutputStream buffer = new BufferedOutputStream(outputStream);

                        buffer.write(bytes,0,bytes.length);

                        buffer.close();
                        outputStream.close();
                        Log.println(Log.ASSERT,"SKILLS", "done");
                        Toast.makeText(getActivity(),"File available in Downloads inside folder TheJobProvider",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getActivity(),"No curriculum available",Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.println(Log.ASSERT,"SKILLS FRAG", "output file not found");
                    Toast.makeText(getActivity(),"Error saving file",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.println(Log.ASSERT,"SKILLS FRAG", "Error writing file");
                    Toast.makeText(getActivity(),"Error saving file",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.create().show();

    }

    private void pickCurriculumFile(){

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf/*");
        getActivity().startActivityForResult(i,REQUEST_CONTENT_GET);

    }

    @Override
    protected void restoreStateFromBundle() {
        super.restoreStateFromBundle();

        if(bundle!=null)
            student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
    }

    @Override
    protected void saveStateInBundle() {
        super.saveStateInBundle();

        if(bundle!=null)
            bundle.put(BUNDLE_KEY_STUDENT,student);
    }

    @Override
    protected void setEnable(boolean enable) {

        super.setEnable(enable);
        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        addCertificate.setVisibility(visibility);
        addDegree.setVisibility(visibility);
        addLanguage.setVisibility(visibility);

        if(editable) curriculum.setEnabled(enable);
        else         curriculum.setEnabled(true);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }



    /*--------------------------------------------------------------------------------------------*/
    /*----------------------- NESTED FRAGMENTS INTERFACE -----------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onCertificateDelete(StudentProfileManagementCertificateFragment toRemove) {

        host.removeOnActivityChangedListener(toRemove);
        certificateFragments.remove(toRemove);


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(toRemove);
        ft.commit();
    }

    @Override
    public void onDegreeDelete(StudentProfileManagementDegreeFragment toRemove) {

        host.removeOnActivityChangedListener(toRemove);
        degreeFragments.remove(toRemove);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(toRemove);
        ft.commit();
    }

    @Override
    public void onLanguageDelete(StudentProfileManagementLanguageFragment toRemove) {

        host.removeOnActivityChangedListener(toRemove);
        languageFragments.remove(toRemove);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(toRemove);
        ft.commit();
    }
}

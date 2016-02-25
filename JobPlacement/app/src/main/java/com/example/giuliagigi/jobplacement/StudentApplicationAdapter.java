package com.example.giuliagigi.jobplacement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Unieuro on 22/02/2016.
 */
public class StudentApplicationAdapter extends RecyclerView.Adapter<StudentApplicationAdapter.ViewHolder> implements OnClickListener {

    public static final int MODE_STUDENT_VIEW = 0;
    public static final int MODE_COMPANY_VIEW = 1;
    public static final int MODE_SIMPLE_VIEW = 2;


    private GlobalData globalData;
    private FragmentActivity context;

    private Student student = null;
    private Company company = null;
    private ArrayList<StudentApplication> applications;
    private int mode;

    private Integer currentPosition = 0;
    private LinearLayoutManager mLayoutManager;


    /* --------------------------------------------------------------------------------------------------------------------- */
    /* ----------------------- CONSTRUCTOR --------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------------------------------------------- */


    public StudentApplicationAdapter(FragmentActivity c,
                                     ArrayList<StudentApplication> applications,
                                     int mode,
                                     Integer pos,
                                     LinearLayoutManager linearLayoutManager){

        context = c;
        globalData = (GlobalData)context.getApplication();
        this.applications = applications;
        this.mode = mode;
        currentPosition = pos;
        mLayoutManager = linearLayoutManager;


        switch (mode){

            case MODE_STUDENT_VIEW:

                student = (Student)globalData.getUserObject();

                break;

            case MODE_COMPANY_VIEW:

                company = (Company)globalData.getUserObject();
                break;

            default:

                break;
        }


    }



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- VIEW HOLDER ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView logo;
        private TextView object;
        private TextView descriprion;
        private TextView status;
        private Button replyButton;

        public ViewHolder(View v) {

            super(v);
            v.setTag(this);
            logo=(ImageView)v.findViewById(R.id.logo_img);
            object=(TextView)v.findViewById(R.id.textView_title);
            descriprion=(TextView)v.findViewById(R.id.textView_description);
            status=(TextView)v.findViewById(R.id.status_tv);
            replyButton = (Button)v.findViewById(R.id.application_reply_button);
        }
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public StudentApplicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_student_application, parent, false);
        v.setClickable(true);
        v.setOnClickListener(StudentApplicationAdapter.this);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentApplicationAdapter.ViewHolder holder, final int position) {


        /* ----------------------- retrieving data ---------------------------------------------- */
        CompanyOffer offer = null;
        Company publisher = null;
        Student applicant = null;


        try {
            offer = applications.get(position).getOffer().fetchIfNeeded();

            if(company == null)
                publisher = offer.getCompany().fetchIfNeeded();

            else if(student == null)
                applicant = applications.get(position).getStudent().fetchIfNeeded();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if(offer == null)
            return;


        /* ---------------- logo ---------------------------------------------------------------- */
        holder.logo.setImageResource(R.drawable.ic_profile);

        if(company == null && publisher != null){

            if(publisher.getProfilePhoto() != null)
                holder.logo.setImageBitmap(publisher.getProfilePhoto());
        }
        if(student == null && applicant != null){

            if(applicant.getProfilePhoto() != null)
                holder.logo.setImageBitmap(applicant.getProfilePhoto());
        }


        /* ----------- name and status ---------------------------------------------------------- */
        String studentName = applications.get(position).getStudent().getName() + " " + applications.get(position).getStudent().getSurname();
        holder.object.setText(mode == MODE_STUDENT_VIEW? offer.getOfferObject() : studentName);

        holder.status.setText(applications.get(position).getStatus());


        /* ---------------- description (student or offer) -------------------------------------- */
        String description = "";


        if(company == null)                             // a students wants to see the offer description
            description = offer.getDescription();

        else if(student == null && applicant != null){  // a company wants to see the student's info

            try {

                if(applicant.getDegrees() != null && !applicant.getDegrees().isEmpty()){

                    for(Degree d: applicant.getDegrees()){

                        d.fetchIfNeeded();
                        description += (d.getType() + " " + d.getStudies() + ", ");
                    }
                }
                else
                    description += context.getString(R.string.application_no_graduation) + ". ";

            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if(description == null || description.isEmpty() || description.equals(""))
            holder.descriprion.setText("...");

        else {

            if(description.length()<30)
                holder.descriprion.setText(description);

            else
                holder.descriprion.setText(offer.getDescription().substring(0,29)+"...");
        }


        /* ---------------- reply (only if company) --------------------------------------------- */
        holder.replyButton.setEnabled(!(mode == MODE_STUDENT_VIEW));
        holder.replyButton.setVisibility(mode == MODE_STUDENT_VIEW ? View.INVISIBLE : View.VISIBLE);

        holder.replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(     applications.get(position).getStatus().equals(StudentApplication.TYPE_ACCEPTED)
                        || applications.get(position).getStatus().equals(StudentApplication.TYPE_REFUSED)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.application_dialog_already_replied);

                    TextView tv = new TextView(context);
                    String message = context.getString(R.string.application_status_applyStatus) + applications.get(position).getStatus();
                    tv.setText(message);
                    builder.setView(tv);

                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });

                    builder.create().show();
                }
                else {

                    ApplicationReplyDialogFragment fragment = ApplicationReplyDialogFragment.newInstance(applications.get(position));
                    fragment.show(context.getFragmentManager(),"ApplicationReply");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return applications.size();
    }



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- ITEM CLICK -----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {

        ViewHolder vh=(ViewHolder)v.getTag();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Fragment fragment = null;

        String toolbarTitle = "";

        if(mode == MODE_STUDENT_VIEW){

            fragment = StudentOfferDetailFragment.newInstance(applications.get(vh.getPosition()).getOffer(), (Student)globalData.getUserObject());
            toolbarTitle = context.getString(R.string.ToolbarTilteOffer);
        }
        else if(mode == MODE_COMPANY_VIEW){

            fragment = ProfileManagement.newInstance(false,applications.get(vh.getPosition()).getStudent());
            toolbarTitle = context.getString(R.string.ToolbarTilteProfile);
        }


        if(fragment == null){

            Toast.makeText(context, "We are sorry. An application error occurred. Impossible to perform operation", Toast.LENGTH_LONG).show();
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.tab_Home_container, fragment)
                .addToBackStack("ApplicationsList")
                .commit();


        Toolbar toolbar= globalData.getToolbar();
        toolbar.setTitle(toolbarTitle);
    }


}

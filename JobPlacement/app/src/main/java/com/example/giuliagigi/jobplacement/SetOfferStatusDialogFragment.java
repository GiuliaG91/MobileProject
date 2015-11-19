package com.example.giuliagigi.jobplacement;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by pietro on 22/05/2015.
 */
public class SetOfferStatusDialogFragment extends DialogFragment {


  private  View root;
    private TextView title;
    private Spinner StatusSpinner;
    private  String[] items;

    private Button okButton;
    private Button cancelButton;
    private GlobalData globalData;
    private Student student;
    private int currentPos=0;
    private OfferStatus myStatus;

    public SetOfferStatusDialogFragment() {
    }

    public static SetOfferStatusDialogFragment newInstance(Student student ) {
        SetOfferStatusDialogFragment fragment = new SetOfferStatusDialogFragment();
        fragment.setStudent(student);
        return fragment;
    }

      private void setStudent(Student student){

          this.student=student;
      }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData=(GlobalData)getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_set_offer_status,container,false);


        title=(TextView)root.findViewById(R.id.SetStatusTitle);
        StatusSpinner=(Spinner)root.findViewById(R.id.StatusSpinner);
        title.setText(getString(R.string.OfferStatus_applyStatus));

        okButton = (Button) root.findViewById(R.id.ok_button);
        cancelButton = (Button) root.findViewById(R.id.cancel_button);

        items = new String[4];
        items[0]=getString(R.string.OfferStatus_ChooseItem);
        items[1]=getString(R.string.OfferStatus_Considering);
        items[2]=getString(R.string.OfferStatus_Accepted);
        items[3]=getString(R.string.OfferStatus_Refused);

       StatusSpinner.setAdapter(new StringAdapter(items));

        ParseQuery<OfferStatus> query=new ParseQuery("OfferStatus");
                query.whereEqualTo("student",student);
                query.whereEqualTo("offer",globalData.getCurrentViewOffer());
        List<OfferStatus> status=null;
        try {
           status = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int pos=0;

        if(!status.isEmpty())
        {
            myStatus=status.get(0);
            pos=OfferStatus.getTypeIndex(myStatus.getType());
            currentPos=pos;
        }

        StatusSpinner.setSelection(pos);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(v);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();

            }
        });

        return root;
    }



    public void setStatus(View v)
    {
                //check
               int pos=StatusSpinner.getSelectedItemPosition();

        if(pos>currentPos)
        {
            //perform change status
            myStatus.setType(StatusSpinner.getSelectedItem().toString());
            myStatus.saveInBackground();
            CompanyOffer offer=globalData.getCurrentViewOffer();
            News news = new News();
            news.createNews(2, globalData.getCurrentViewOffer(), student, myStatus, globalData);

            if(pos==2)
            {

                int places=offer.getnPositions();
                offer.setPositions(places-1);
                offer.saveInBackground();
            }

            ParseUser user=student.getParseUser();
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("User", student.getParseUser());


            ParsePush push = new ParsePush();
            push.setQuery(pushQuery);
            push.setMessage("" + getString(R.string.Message_Update) + " " + offer.getOfferObject() +
                    " " + getString(R.string.Message_UpdateEnd) + " " + globalData.getUserObject().getMail());
            push.sendInBackground();

            Toast.makeText(getActivity(),R.string.Done,Toast.LENGTH_SHORT).show();
            getDialog().dismiss();


        }else
            Toast.makeText(getActivity(),R.string.ErrorStatus,Toast.LENGTH_SHORT).show();



    }

    /**
     * ***************************STRING ADAPTER*************************
     */
    public class StringAdapter extends BaseAdapter {

        public String[] stringArray;

        public StringAdapter(String[] stringArray) {
            super();
            this.stringArray = stringArray;
        }

        @Override
        public int getCount() {
            return stringArray.length;
        }

        @Override
        public String getItem(int position) {
            return stringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_text_element, parent, false);

            TextView text = (TextView) convertView.findViewById(R.id.text_view);
            text.setTextSize(15);
            text.setText(stringArray[position]);
            return convertView;
        }
    }
}

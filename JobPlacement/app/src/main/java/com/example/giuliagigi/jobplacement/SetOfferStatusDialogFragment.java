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
import com.parse.ParseQuery;

import java.util.List;
import java.util.Set;

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
        title.setText(getString(R.string.StatusTextview));

        okButton = (Button) root.findViewById(R.id.ok_button);
        cancelButton = (Button) root.findViewById(R.id.cancel_button);


        items[0]=getString(R.string.StatusChooseItem);
        items[1]=getString(R.string.OfferStatus_Considering);
        items[2]=getString(R.string.OfferStatus_Accepted);
        items[3]=getString(R.string.OfferStatus_Refused);

       StatusSpinner.setAdapter(new StringAdapter(items));

        ParseQuery query=new ParseQuery("OfferStatus");
                query.whereEqualTo("student",student);
                query.whereEqualTo("offer",globalData.getCurrentViewOffer());
   List<OfferStatus> status=null;
        try {
         status=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int pos=0;
        if(!status.isEmpty())
        {
            myStatus=status.get(0);
            pos=OfferStatus.getTypeIndex(status.get(0).getType());
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

            Toast.makeText(getActivity(),R.string.Done,Toast.LENGTH_SHORT).show();
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

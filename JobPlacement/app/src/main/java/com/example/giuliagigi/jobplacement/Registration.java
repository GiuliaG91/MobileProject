package com.example.giuliagigi.jobplacement;

import android.app.FragmentTransaction;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;


public class Registration extends ActionBarActivity implements StudentRegistrationFragment.onInteractionListener, CompanyRegistrationFragment.OnInteractionListener {


    /* ---------------------- Standard callbacks ------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getFragmentManager().beginTransaction().add(R.id.container_register_fragment,StudentRegistrationFragment.newInstance()).commit();

        ListView typeList = (ListView)findViewById(R.id.type_list);
        typeList.setAdapter(new stringAdapter(User.TYPES));
        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                switch (User.TYPES[position]){

                    case User.TYPE_STUDENT:
                        if(findViewById(R.id.fragment_company_register)!= null)
                            ft.replace(R.id.container_register_fragment,StudentRegistrationFragment.newInstance());
                        break;
                    case User.TYPE_COMPANY:
                        if(findViewById(R.id.fragment_student_register)!= null)
                            ft.replace(R.id.container_register_fragment, CompanyRegistrationFragment.newInstance());
                        break;
                    default:
                        Log.println(Log.ASSERT,"REGISTRATION", "Error: type unknown");
                        break;
                }

                ft.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /* ---------------------------- Adapter for type list --------------------------------- */

    private class stringAdapter extends ArrayAdapter<String>{

        public String[] stringArray;

        public stringAdapter(String[] stringArray){
            super(getApplicationContext(),R.layout.list_text_element, Arrays.asList(User.TYPES));
            this.stringArray = stringArray;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_text_element,parent,false);

            TextView type = (TextView)convertView.findViewById(R.id.type_name);
            type.setText(stringArray[position]);
            return convertView;
        }
    }


}

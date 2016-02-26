package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

/**
 * Created by pietro on 22/05/2015.
 */
@ParseClassName("StudentApplication")
public class StudentApplication extends ParseObject {


    public static final String TYPE_START =  "Processing";
    public static final String TYPE_CONSIDERING ="Considering";
    public static final String TYPE_ACCEPTED = "Accepted";
    public static final String TYPE_REFUSED ="Refused";

    public static final String TYPE_START_TRANSLATED =  GlobalData.getContext().getString(R.string.application_status_Start);
    public static final String TYPE_CONSIDERING_TRANSLATED =  GlobalData.getContext().getString(R.string.application_status_Considering);
    public static final String TYPE_ACCEPTED_TRANSLATED =  GlobalData.getContext().getString(R.string.application_status_Accepted);
    public static final String TYPE_REFUSED_TRANSLATED =  GlobalData.getContext().getString(R.string.application_status_Refused);


    public static final HashMap<String,String> STATUS_TYPES_MAP = new HashMap<>();
    public static final String[] TYPES = new String[]{TYPE_START_TRANSLATED, TYPE_CONSIDERING_TRANSLATED, TYPE_ACCEPTED_TRANSLATED, TYPE_REFUSED_TRANSLATED};

    public static final String STATUS_FIELD = "status";
    public static final String OFFER_FIELD =  "offer";
    public static final String STUDENT_FIELD = "student";

    public StudentApplication(){super();}



    /*--------------------------------------------------------------------------------------------*/
    /* --------------------------- GETTERS AND SETTERS ------------------------------------------ */
    /*--------------------------------------------------------------------------------------------*/


    /*------------------ status ----------------------------------------------------------------- */
    public void setStatus(String type){

        String res = getEnglishType(type);

        if(res == null)
            this.put(STATUS_FIELD, type);
        else
            this.put(STATUS_FIELD, res);
    }

    public String getStatus(){

        return STATUS_TYPES_MAP.get(this.getString(STATUS_FIELD));
    }


    /*------------------ student ---------------------------------------------------------------- */
    public void setStudent(Student student){
        this.put(STUDENT_FIELD,student);
    }
    public Student getStudent(){
        return (Student)this.get(STUDENT_FIELD);
    }


    /*------------------ offer ------------------------------------------------------------------ */
    public void setOffer(CompanyOffer offer){
        this.put(OFFER_FIELD,offer);
    }
    public CompanyOffer getOffer(){
        return (CompanyOffer)this.get(OFFER_FIELD);
    }



    /*--------------------------------------------------------------------------------------------*/
    /* ----------------------------------- AUXILIARY METHODS ------------------------------------ */
    /*--------------------------------------------------------------------------------------------*/

    public static void initializeLangauges(){

        STATUS_TYPES_MAP.put(TYPE_START, TYPE_START_TRANSLATED);
        STATUS_TYPES_MAP.put(TYPE_CONSIDERING, TYPE_CONSIDERING_TRANSLATED);
        STATUS_TYPES_MAP.put(TYPE_REFUSED, TYPE_REFUSED_TRANSLATED);
        STATUS_TYPES_MAP.put(TYPE_ACCEPTED, TYPE_ACCEPTED_TRANSLATED);

    }



    public static int getTypeIndex(String type) {

        for(int i=0;i<TYPES.length;i++) {

            if(TYPES[i].equals(type)) {

                return i;
            }
        }

        return -1;
    }


    public static String getEnglishType(String type) {

        String res = (String)getKeyByValue(STATUS_TYPES_MAP,type);

        if(res == null)
            return type;
        else
            return res;
    }

    public static Object getKeyByValue(HashMap hm, Object value) {

        for (Object o : hm.keySet()) {

            if (hm.get(o).equals(value)) {
                return o;
            }
        }

        return null;
    }
}

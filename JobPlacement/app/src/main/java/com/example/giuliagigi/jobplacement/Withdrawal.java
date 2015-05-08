package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by MarcoEsposito90 on 08/05/2015.
 */

@ParseClassName("Withdrawal")
public class Withdrawal extends ParseObject{

    private static final String HINTS_FIELD = "hints";
    private static final String REASONS_FIELD = "reasons";
    private static final String OTHER_REASON_FIELD = "other_reason";

    public static final String REASON_FOUND_JOB = "found a job";
    public static final String REASON_BAD_APPLICATION = "app not good";
    public static final String REASON_FEW_USERS = "Few offers";
    public static final String REASON_OTHER = "Other";
    public static final String[] REASONS = new String[]{REASON_FOUND_JOB,REASON_BAD_APPLICATION,REASON_FEW_USERS,REASON_OTHER};


    public Withdrawal(){
        super();
    }

    public void setHints(String hints){

        this.put(HINTS_FIELD, hints);
    }

    public void setOtherReason(String otherReason){

        this.put(OTHER_REASON_FIELD,otherReason);
    }

    public void setReasons(ArrayList<String> reasons){

        for(String r:reasons)
            this.addUnique(REASONS_FIELD,r);
    }



}

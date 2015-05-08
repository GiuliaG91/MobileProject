package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by MarcoEsposito90 on 08/05/2015.
 */

@ParseClassName("Withdrawal")
public class Withdrawal extends ParseObject{

    private static final String HINTS_FIELD = "hints";

    public static final String REASON_FOUND_JOB = "found a job";
    public static final String REASON_BAD_APPLICATION = "app not good";
    public static final String REASON_FEW_USERS = "Few offers";
    public static final String REASON_OTHER = "Other";



}

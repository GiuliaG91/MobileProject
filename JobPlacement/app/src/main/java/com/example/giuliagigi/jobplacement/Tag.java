package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by pietro on 03/05/2015.
 */
@ParseClassName("Tag")
public class Tag extends ParseObject {

    private static final String TAG_FIELD = "tag";

    public String getTag() { return this.getString(TAG_FIELD); }
}
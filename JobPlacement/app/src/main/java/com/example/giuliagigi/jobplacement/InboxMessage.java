package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Silvia on 10/05/2015.
 */

@ParseClassName("InboxMessage")
public class InboxMessage extends ParseObject {

    protected static final String OBJECT = "object";
    protected static final String SENDER = "sender";
    private static final String  RECIPIENTS = "recipients";
    private static final String  BODY_MESSAGE = "body_message";
    private static final String  IS_PREFERRED = "is_preferred";
    private static final String  IS_READ = "is_read";
    private static final String  DATE = "date";
    private static final String IS_DELETING = "is_deleting";


    public InboxMessage(){
        super();
    }


    /*GETTER*/

    public String getSender() {
        return (String)this.get(SENDER);
    }

    public ArrayList<String> getRecipients() {
        return (ArrayList<String>)this.get(RECIPIENTS);
    }

    public String getObject() {
        return this.getString(OBJECT);
    }

    public Calendar getDate() throws JSONException {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime((Date)this.get(DATE));

        return calendar;
    }

    public String getBodyMessage() {
        return this.getString(BODY_MESSAGE);
    }

    public Boolean getIsPreferred() {
        return this.getBoolean(IS_PREFERRED);
    }

    public Boolean getIsRead() {
        return this.getBoolean(IS_READ);
    }

    public Boolean getIsDeleting() { return this.getBoolean(IS_DELETING); }

    /***************END GETTER****************/



    public void setObject(String object){

        this.put(OBJECT,object);
    }
    public void setSender(String sender){

        this.put(SENDER, sender);
    }
    public void setRecipients(ArrayList<String> recipients){

        this.put(RECIPIENTS, recipients);
    }

    public void setBodyMessage(String bodyMessage){

        this.put(BODY_MESSAGE, bodyMessage);
    }

    public void setIsPreferred(Boolean isPreferred){

        this.put(IS_PREFERRED, isPreferred);
    }

    public void setIsRead(Boolean isRead){

        this.put(IS_READ, isRead);
    }

    public void setDate(Calendar calendar){

        Date d = calendar.getTime();
        this.put(DATE, d);

    }

    public void setIsDeleting(Boolean flag){

        this.put(IS_DELETING, flag);
    }

    /*END SETTER METHODS*/

}

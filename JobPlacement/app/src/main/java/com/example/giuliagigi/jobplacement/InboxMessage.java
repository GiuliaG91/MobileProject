package com.example.giuliagigi.jobplacement;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Silvia on 10/05/2015.
 */

@ParseClassName("InboxMessage")
public class InboxMessage extends ParseObject {

    protected static final String OBJECT = "object";
    protected static final String SENDER = "sender";
    protected static final String RECIPIENTS = "recipients";
    protected static final String TYPE = "type";
    protected static final String BODY_MESSAGE = "body_message";
    protected static final String IS_PREFERRED = "is_preferred";
    protected static final String DATE = "date";
    protected static final String IS_DELETING = "is_deleting";

    public static final String TYPE_SENT = "sent";
    public static final String TYPE_RECEIVED = "received";

    public InboxMessage(){
        super();
    }


    /*GETTER*/

    public String getObject() {
        return this.getString(OBJECT);
    }

    public ParseUserWrapper getSender() {
        return (ParseUserWrapper)this.get(SENDER);
    }



    public List<ParseUserWrapper> getRecipients() {

        ParseRelation<ParseUserWrapper> tmp = getRelation(RECIPIENTS);
        List<ParseUserWrapper> results = null;

        try {
            results = tmp.getQuery().find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return results;
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

    public Boolean getIsDeleting() { return this.getBoolean(IS_DELETING); }

    public String getType(){

        return this.getString(TYPE);
    }

    /***************END GETTER****************/

    public void setObject(String object){

        this.put(OBJECT,object);
    }

    public void setSender(ParseUserWrapper sender){

        this.put(SENDER, sender);
    }

    public void addRecipient(String recipient) throws UnknownRecipientException {

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereEqualTo("email",recipient);
        try {

            List<ParseUser> users = userQuery.find();

            if(users.isEmpty()){

                throw new UnknownRecipientException();
            }
            else {

                getRelation(RECIPIENTS).add(users.get(0));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void addRecipient(ParseUserWrapper recipient) {

        getRelation(RECIPIENTS).add(recipient);
    }

    public void setBodyMessage(String bodyMessage){

        this.put(BODY_MESSAGE, bodyMessage);
    }

    public void setIsPreferred(Boolean isPreferred){

        this.put(IS_PREFERRED, isPreferred);
    }

    public void setDate(Calendar calendar){

        Date d = calendar.getTime();
        this.put(DATE, d);

    }

    public void setIsDeleting(Boolean flag){

        this.put(IS_DELETING, flag);
    }

    public void setType(String type){

        this.put(TYPE,type);
    }

    /*END SETTER METHODS*/

    public class UnknownRecipientException extends Exception{

        private static final String ERROR_MESSAGE = "Error: this recipient does not exist";

        public String getMessage(){
            return ERROR_MESSAGE;
        }
    }

}

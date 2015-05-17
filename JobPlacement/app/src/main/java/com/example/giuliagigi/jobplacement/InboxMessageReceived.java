package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;

/**
 * Created by Silvia on 16/05/2015.
 */
@ParseClassName("InboxMessageReceived")
public class InboxMessageReceived extends InboxMessage{

    protected static final String RECIPIENT = "recipient";
    //protected  static final String FROM_COMPANY = "from_company";

    public InboxMessageReceived(){
        super();
    }

    public String getRecipient(){
        return this.getString(InboxMessageReceived.RECIPIENT);
    }

    public void setRecipient(String recipient){
        this.put(InboxMessageReceived.RECIPIENT, recipient);
    }

    /*
    public boolean getFromCompany(){
        return this.getBoolean(InboxMessageReceived.FROM_COMPANY);
    }

    public void setFromCompany(boolean flag){
        this.put(InboxMessageReceived.FROM_COMPANY, flag);
    }
    */

}

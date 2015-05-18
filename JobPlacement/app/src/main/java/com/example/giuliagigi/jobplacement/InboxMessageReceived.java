package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;

import com.parse.ParseClassName;

/**
 * Created by Silvia on 16/05/2015.
 */
@ParseClassName("InboxMessageReceived")
public class InboxMessageReceived extends InboxMessage{

    protected static final String RECIPIENT = "recipient";
    protected static final String NAME_SENDER = "name_sender";
    protected  static final String PHOTO_SENDER = "photo_sender";
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

    public String getNameSender(){
        return this.getString(InboxMessageReceived.NAME_SENDER);
    }

    public void setNameSender(String name){
        this.put(InboxMessageReceived.NAME_SENDER, name);
    }

    public Bitmap getPhotoSender(){
        return (Bitmap)this.get(InboxMessageReceived.PHOTO_SENDER);
    }

    public void setPhotoSender(Bitmap photo){
        this.put(InboxMessageReceived.PHOTO_SENDER, photo);
    }

}

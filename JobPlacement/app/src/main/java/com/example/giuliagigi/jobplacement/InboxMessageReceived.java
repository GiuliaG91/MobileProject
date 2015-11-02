package com.example.giuliagigi.jobplacement;

import android.graphics.Bitmap;

import com.parse.ParseClassName;

/**
 * Created by Silvia on 16/05/2015.
 */
@ParseClassName("InboxMessageReceived")
public class InboxMessageReceived extends InboxMessage{

    protected static final String  IS_READ = "is_read";
    protected static final String  OWNER = "owner";

    public InboxMessageReceived(){
        super();
    }

    public Boolean getIsRead() {
        return this.getBoolean(IS_READ);
    }

    public void setIsRead(Boolean isRead){

        this.put(IS_READ, isRead);
    }

    public ParseUserWrapper getOwner(){

        return (ParseUserWrapper)get(OWNER);
    }

    public void setOwner(ParseUserWrapper owner){

        put(OWNER,owner);
    }

}

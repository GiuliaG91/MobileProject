package com.example.giuliagigi.jobplacement;

import android.util.JsonReader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MarcoEsposito90 on 05/11/2015.
 */
public class RoomsFileReader {

    public static final String ROOMS_JSON_FILE_NAME = "rooms.json";
    public static final String ROOM_NAME_FIELD = "name";
    public static final String ROOM_FLOOR_FIELD = "floor";
    public static final String ROOM_BUILDING_FIELD = "building";
    public static final String ROOM_START_HOUR_FIELD = "startHour";
    public static final String ROOM_START_MINUTE_FIELD = "startMinute";
    public static final String ROOM_END_HOUR_FIELD = "endHour";
    public static final String ROOM_END_MINUTE_FIELD = "endMinute";
    public static final String ROOM_X_FIELD = "xCoordinate";
    public static final String ROOM_Y_FIELD = "yCoordinate";

    private HashMap<String,Room> rooms;
    private ArrayList<String> roomNames;

    public RoomsFileReader(){

        rooms = new HashMap<String,Room>();
        roomNames = new ArrayList<String>();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- GETTER METHODS --------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<String> getRoomNames(){

        if(rooms.isEmpty() || !roomNames.isEmpty()) return this.roomNames;

        ArrayList<String> roomNames = new ArrayList<String>();

        for(Room r: rooms.values()){

            roomNames.add(r.getName());
        }

        this.roomNames = roomNames;
        return roomNames;
    }

    public Room getRoom(String roomName){

        if(!rooms.keySet().contains(roomName)) return null;

        return rooms.get(roomName);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- FILE READING METHODS --------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public void readRooms(){

        try{

            InputStream in = GlobalData.getAssetManager().open(ROOMS_JSON_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginArray();

            while (reader.hasNext()){

                Room r = readRoom(reader);
                rooms.put(r.getName(),r);
            }

            reader.endArray();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Room readRoom(JsonReader reader) throws IOException{


        Room r = new Room();
        int startHour = 0, startMinute = 0, endHour = 0, endMinute = 0;
        reader.beginObject();

        while (reader.hasNext()){

            String courseField = reader.nextName();

            if(courseField.equals(ROOM_NAME_FIELD))             r.setName(reader.nextString());
            else if(courseField.equals(ROOM_BUILDING_FIELD))    r.setBuilding(reader.nextString());
            else if(courseField.equals(ROOM_FLOOR_FIELD))       r.setFloor(reader.nextInt());
            else if(courseField.equals(ROOM_START_HOUR_FIELD))  startHour = reader.nextInt();
            else if(courseField.equals(ROOM_START_MINUTE_FIELD))startMinute = reader.nextInt();
            else if(courseField.equals(ROOM_END_HOUR_FIELD))    endHour = reader.nextInt();
            else if(courseField.equals(ROOM_END_MINUTE_FIELD))  endMinute = reader.nextInt();
            else if(courseField.equals(ROOM_X_FIELD))           r.setXCoordinate((float)reader.nextDouble());
            else if(courseField.equals(ROOM_Y_FIELD))           r.setYCoordinate((float)reader.nextDouble());
            else {

                reader.skipValue();
            }

        }

        if(startHour !=0){
            
            Schedule s = new Schedule(startHour,startMinute,endHour,endMinute);
            r.setTime(s);
        }

        reader.endObject();

        return r;
    }
}

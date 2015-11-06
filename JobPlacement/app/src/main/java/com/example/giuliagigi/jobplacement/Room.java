package com.example.giuliagigi.jobplacement;

/**
 * Created by MarcoEsposito90 on 05/11/2015.
 */
public class Room {

    private String name;
    private Schedule time;
    private int floor;
    private String building;
    private float xCoordinate;
    private float yCoordinate;

    public Room() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schedule getTime() {
        return time;
    }

    public void setTime(Schedule time) {
        this.time = time;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}

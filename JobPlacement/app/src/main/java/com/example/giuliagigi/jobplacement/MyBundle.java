package com.example.giuliagigi.jobplacement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by MarcoEsposito 90 on 17/05/2015.
 */
public class MyBundle {

    private HashMap<String,String> strings;
    private HashMap<String,Integer> integers;
    private HashMap<String,ArrayList<Object>> lists;
    private HashMap<String,Object> others;
    private HashMap<String,Boolean> booleans;


    public MyBundle(){

        strings = new HashMap<String,String>();
        integers = new HashMap<String,Integer>();
        lists = new HashMap<String,ArrayList<Object>>();
        others = new HashMap<String, Object>();
        booleans = new HashMap<String,Boolean>();
    }

    public void putString(String key, String value){

        strings.put(key,value);
    }

    public String getString(String key){

        return strings.get(key);
    }


    public void putInt(String key, Integer value){

        integers.put(key,value);
    }

    public Integer getInt(String key){

        return integers.get(key);
    }

    public void putList(String key, ArrayList<Object> list){

        lists.put(key, list);
    }
    public ArrayList<Object> getList(String key){

        return lists.get(key);
    }

    public void put(String key, Object value){

        others.put(key, value);
    }

    public Object get(String key){

        return others.get(key);
    }

    public void putBoolean(String key, Boolean value){

        booleans.put(key, value);
    }

    public Boolean getBoolean(String key){

        return booleans.get(key);
    }
}

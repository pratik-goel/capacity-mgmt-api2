package com.homeretailgroup.microservices.capacitymanagement;

import java.util.Date;

/**
 * 
 * @author pratik.goel
 *
 */
public class Availability  {

    private String id;
    private Date date;
    private String capacity;

    public Availability(Date date, String capacity){
        this.date = date;
        this.capacity = capacity;
    }

    /**
     * Creates an Availability object with an auto generated _id for use when a RequestBody
     * contains a list of Availabilities.
     */
    public Availability() {
        //TODO: _id generation
    }

    public String getId(){
        return id;
    }

    public Date getDate(){
        return date;
    }

    public String getCapacity(){
        return capacity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

}

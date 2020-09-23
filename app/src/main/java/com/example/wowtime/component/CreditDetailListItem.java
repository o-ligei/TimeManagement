package com.example.wowtime.component;

import java.sql.Timestamp;

public class CreditDetailListItem {

    private Timestamp timestamp;
    private Integer variation;
    private String eventName;

    public CreditDetailListItem() {
    }

    public CreditDetailListItem(Timestamp timestamp, Integer variation, String eventName){
        this.timestamp = timestamp;
        this.variation = variation;
        this.eventName = eventName;
    }

    public Timestamp getTimestamp() {return timestamp;}
    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}
    public Integer getVariation() {return variation;}
    public void setVariation(Integer variation) {this.variation = variation;}
    public String getEventName() {return eventName;}
    public void setEventName(String eventName) {this.eventName = eventName;}
}
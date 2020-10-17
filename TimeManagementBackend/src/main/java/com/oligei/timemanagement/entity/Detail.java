package com.oligei.timemanagement.entity;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tm_details")
public class Detail {

    private Integer detailId;
    private Integer userId;
    private String event;
    private Timestamp timestamp;

    public Detail() {}
    public Detail(Integer userId, String event, Timestamp timestamp) {
        this.userId = userId;
        this.event = event;
        this.timestamp = timestamp;
    }

    @Id
    @Column(name = "detailid")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Integer getDetailId() {return detailId;}
    public void setDetailId(Integer detailId) {this.detailId = detailId;}

    @Column(name = "userid")
    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}

    @Column(name = "event")
    public String getEvent() {return event;}
    public void setEvent(String event) {this.event = event;}

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {return timestamp;}
    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}
}

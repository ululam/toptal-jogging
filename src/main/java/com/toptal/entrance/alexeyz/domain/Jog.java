package com.toptal.entrance.alexeyz.domain;

import com.toptal.entrance.alexeyz.util.Utils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.text.DateFormat;
import java.util.Date;

/**
 * Represents a single training session
 *
 * @author alexey.zakharchenko@gmail.com
 */
@Entity
public class Jog {

    @Id
    @GeneratedValue
    private Long id;

    private int distance;
    private int time;
    private Date date;


    public Jog() {}

    public Jog(Date date, int distance, int time) {
        this.date = date;
        this.distance = distance;
        this.time = time;
    }

    public Long getId() {
        return id;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public Jog copyFrom(Jog jog) {
        this.distance = jog.distance;
        this.time = jog.time;
        this.date = jog.date;

        return this;
    }

    @Transient
    public float getAverageSpeed() {
        return (float) distance / time;
    }

    @Override
    public String toString() {
        return Utils.s(this);
    }
}

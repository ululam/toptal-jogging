package com.toptal.entrance.alexeyz.domain;

import com.toptal.entrance.alexeyz.util.Utils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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

    // Kilometers
    private float distance;
    // Milliseconds
    private long time;

    private Date date;

    private long userId;

    public Jog() {}

    public Jog(long userId, Date date, float distance, long time) {
        this.userId = userId;
        this.date = date;
        this.distance = distance;
        this.time = time;
    }

    public Long getId() {
        return id;
    }


    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }


    public void setTime(long time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
        if (distance == 0 || time == 0)
            return 0;

        return 3_600_000 * distance / time;
    }

    @Override
    public String toString() {
        return Utils.s(this);
    }
}

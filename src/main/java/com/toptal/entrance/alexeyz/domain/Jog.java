package com.toptal.entrance.alexeyz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    private Long userId;

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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public float getAverageSpeed() {
        if (distance == 0 || time == 0)
            return 0;

        return 3_600_000 * distance / time;
    }

    @Override
    public String toString() {
        return "Jog{" +
                "id=" + id +
                ", distance=" + distance +
                ", time=" + time +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}

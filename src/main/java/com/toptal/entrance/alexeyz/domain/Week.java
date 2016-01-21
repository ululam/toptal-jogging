package com.toptal.entrance.alexeyz.domain;

import javax.persistence.Transient;
import java.util.Calendar;
import java.util.Date;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class Week {
    private static final long DAY_MS = 24*3600*1000;

    private int distance;
    private int time;
    private int runs;
    private Date start;
    private Date end;
    private int week;
    private int year;

    public Week(Date anyDateOfWeek, long runs, long distance, long time) {
        this.distance = (int) distance;
        this.time = (int) time;
        this.runs = (int) runs;

        Calendar c = Calendar.getInstance();
        c.setTime(anyDateOfWeek);
        c.set(Calendar.DAY_OF_WEEK, 1);
        week = c.get(Calendar.WEEK_OF_YEAR);
        year = c.get(Calendar.YEAR);
        this.start = c.getTime();

        c.set(Calendar.DAY_OF_WEEK, 7);
        this.end = c.getTime();
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public int getRuns() {
        return runs;
    }

    public String getWeek() {
        return year + " - " + week;
    }

    @Transient
    public float getAverageSpeed() {
        return (float) distance / time;
    }

    @Transient
    public float getAverageDistance() {
        return (float) distance / runs;
    }

}

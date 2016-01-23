package com.toptal.entrance.alexeyz.domain;

import javax.persistence.Transient;
import java.util.Calendar;
import java.util.Date;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class Week {
    private float distance;
    // Milliseconds
    private long time;
    private int runs;
    private Date start;
    private Date end;
    private int week;
    private int year;

    public Week(Date anyDateOfWeek, long runs, double distance, long time) {
        this.distance = (float) distance;
        this.time = time;
        this.runs = (int) runs;

        Calendar c = Calendar.getInstance();
        c.setTime(anyDateOfWeek);
        c.set(Calendar.DAY_OF_WEEK, 1);
        this.start = c.getTime();

        c.set(Calendar.DAY_OF_WEEK, 7);
        this.end = c.getTime();

        week = c.get(Calendar.WEEK_OF_YEAR);
        year = c.get(Calendar.YEAR); // Get year of the last day of the week, otherwise 1 Jan will be often of the previous year
    }

    public float getDistance() {
        return distance;
    }

    public long getTime() {
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
        if (time == 0 || distance == 0)
            return 0;

        return 3_600_000 * distance / time;
    }

    @Transient
    public float getAverageDistance() {
        return distance / runs;
    }

}

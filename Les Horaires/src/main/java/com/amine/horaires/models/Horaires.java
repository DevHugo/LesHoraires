package com.amine.horaires.models;

import android.support.annotation.NonNull;

public class Horaires implements Comparable {
    /*
    periods[N][title] 	Title of N period - optional
    periods[N][T][day] 	1 (Monday) to 7 (Sunday) - Day of opening window T for period N
    periods[N][T][from_h] 	0 to 23 - Hour of beginning of opening window T for period N
    periods[N][T][from_m] 	0 to 59 (minutes) - Minute of beginning of opening window T for period N
    periods[N][T][to_h] 	0 to 32 (8am the next morning) - Hour of end of opening window T for period N
    periods[N][T][to_m] 	0 to 59 (minutes) - Minute of end of opening window T for period N
    */

    private int day;
    private int from_h;
    private int from_m;
    private int to_h;
    private int to_m;

    public Horaires(int day) {
        this.day = day;
        this.from_h = 8;
        this.from_m = 0;
        this.to_h = 18;
        this.to_m = 0;
    }

    public Horaires() {
        this.day = 1;
        this.from_h = 8;
        this.from_m = 0;
        this.to_h = 18;
        this.to_m = 0;
    }

    public Horaires(Horaires h) {
        this.day = h.getDay();
        this.from_h = h.getFrom_h();
        this.from_m = h.getFrom_m();
        this.to_h = h.getTo_h();
        this.to_m = h.getTo_m();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getFrom_h() {
        return from_h;
    }

    public void setFrom_h(int from_h) {
        this.from_h = from_h;
    }

    public int getFrom_m() {
        return from_m;
    }

    public void setFrom_m(int from_m) {
        this.from_m = from_m;
    }

    public int getTo_h() {
        return to_h;
    }

    public void setTo_h(int to_h) {
        this.to_h = to_h;
    }

    public int getTo_m() {
        return to_m;
    }

    public void setTo_m(int to_m) {
        this.to_m = to_m;
    }


    @Override
    public int compareTo(@NonNull Object another) {
        Horaires h = (Horaires) another;
        return this.day - h.day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Horaires horaires = (Horaires) o;

        return this.day == horaires.day;

    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + from_h;
        result = 31 * result + from_m;
        result = 31 * result + to_h;
        result = 31 * result + to_m;
        return result;
    }
}

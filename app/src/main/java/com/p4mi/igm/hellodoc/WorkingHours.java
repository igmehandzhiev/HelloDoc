package com.p4mi.igm.hellodoc;

import java.util.ArrayList;

/**
 * Created by IGM on 24.6.2017 г..
 */

public class WorkingHours {
    private double OPENING_TIME = 0.00;
    private double CLOSING_TIME = 24.00;
    private int workingTime = (int) (getCLOSING_TIME() - getOPENING_TIME());

    ArrayList<String> monday;
    ArrayList<String> tuesday = new ArrayList<>();
    ArrayList<String> wednesday = new ArrayList<>();
    ArrayList<String> thursday = new ArrayList<>();
    ArrayList<String> friday = new ArrayList<>();
    ArrayList<String> saturday = new ArrayList<>();
    ArrayList<String> sunday = new ArrayList<>();


    public WorkingHours() {
    }

    public WorkingHours(ArrayList<String> arrayList) {
        setMonday(arrayList);
        setTuesday(arrayList);
        setWednesday(arrayList);
        setThursday(arrayList);
        setFriday(arrayList);
        setSaturday(arrayList);
        setSunday(arrayList);

    }

    public ArrayList<String> getMonday() {
        return monday;
    }

    public void setMonday(ArrayList<String> monday) {
        this.monday = monday;
    }

    public ArrayList<String> getTuesday() {
        return tuesday;
    }

    public void setTuesday(ArrayList<String> tuesday) {
        this.tuesday = tuesday;
    }

    public ArrayList<String> getWednesday() {
        return wednesday;
    }

    public void setWednesday(ArrayList<String> wednesday) {
        this.wednesday = wednesday;
    }

    public ArrayList<String> getThursday() {
        return thursday;
    }

    public void setThursday(ArrayList<String> thursday) {
        this.thursday = thursday;
    }

    public ArrayList<String> getFriday() {
        return friday;
    }

    public void setFriday(ArrayList<String> friday) {
        this.friday = friday;
    }

    public ArrayList<String> getSaturday() {
        return saturday;
    }

    public void setSaturday(ArrayList<String> saturday) {
        this.saturday = saturday;
    }

    public ArrayList<String> getSunday() {
        return sunday;
    }

    public void setSunday(ArrayList<String> sunday) {
        this.sunday = sunday;
    }


    public double getOPENING_TIME() {
        return OPENING_TIME;
    }

    public void setOPENING_TIME(double opening_time) {
        this.OPENING_TIME = opening_time;
    }

    public double getCLOSING_TIME() {
        return CLOSING_TIME;

    }

    public void setCLOSING_TIME(double CLOSING_TIME) {
        this.CLOSING_TIME = CLOSING_TIME;
    }

}

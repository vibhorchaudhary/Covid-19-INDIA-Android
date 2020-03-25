package com.vibhor.covid19tracker.models;

import java.util.List;

public class StateWise {

    private String active;
    private String confirmed;
    private String deaths;
    private List<DeltaDataModel> delta;
    private String lastupdatedtime;
    private String recovered;
    private String state;

    public StateWise() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public List<DeltaDataModel> getDelta() {
        return delta;
    }

    public void setDelta(List<DeltaDataModel> delta) {
        this.delta = delta;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(String lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

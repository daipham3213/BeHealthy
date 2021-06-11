package com.fatguy.behealthy.Models.Covid;

import java.io.Serializable;

public class CovidV3 implements Serializable {
    private double cases;
    private double deaths;
    private double recovered;
    private double active;

    public CovidV3() {
    }

    public double getActive() {
        return active;
    }

    public void setActive(double active) {
        this.active = active;
    }

    public double getCases() {
        return cases;
    }

    public void setCases(double cases) {
        this.cases = cases;
    }

    public double getDeaths() {
        return deaths;
    }

    public void setDeaths(double deaths) {
        this.deaths = deaths;
    }

    public double getRecovered() {
        return recovered;
    }

    public void setRecovered(double recovered) {
        this.recovered = recovered;
    }

}

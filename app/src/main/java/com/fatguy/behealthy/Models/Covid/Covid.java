package com.fatguy.behealthy.Models.Covid;

import java.io.Serializable;

public class Covid implements Serializable {
    public Covid19 covid19;
    public CovidV3 covidV3;
    public CovidV3 covidV3_glo;

    public Covid() {
    }

    public CovidV3 getCovidV3_glo() {
        return covidV3_glo;
    }

    public void setCovidV3_glo(CovidV3 covidV3_glo) {
        this.covidV3_glo = covidV3_glo;
    }

    public Covid19 getCovid19() {
        return covid19;
    }

    public void setCovid19(Covid19 covid19) {
        this.covid19 = covid19;
    }

    public CovidV3 getCovidV3() {
        return covidV3;
    }

    public void setCovidV3(CovidV3 covidV3) {
        this.covidV3 = covidV3;
    }
}

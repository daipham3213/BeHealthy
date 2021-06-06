package com.fatguy.behealthy.Models.Covid;

import java.io.Serializable;
import java.util.ArrayList;

public class Covid19 implements Serializable {
    private String total;
    private String recovery;
    private String die;
    private Data[] data;
    private ArrayList<String> province;

    public Covid19() {
    }

    public ArrayList<String> getProvince() {
        return province;
    }

    public void setProvince(ArrayList<String> province) {
        this.province = province;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRecovery() {
        return recovery;
    }

    public void setRecovery(String recovery) {
        this.recovery = recovery;
    }

    public String getDie() {
        return die;
    }

    public void setDie(String die) {
        this.die = die;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

}

package com.fatguy.behealthy.Models.Covid;

import java.io.Serializable;

public class Covid19 implements Serializable {
    private String total;
    private String recovery;
    private String die;
    private Data[] data;

    public Covid19() {
    }

    public Covid19(String total, String recovery, String die) {
        this.total = total;
        this.recovery = recovery;
        this.die = die;
        this.data = new Data[Integer.parseInt(total)];

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

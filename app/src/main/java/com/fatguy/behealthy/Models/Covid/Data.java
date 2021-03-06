package com.fatguy.behealthy.Models.Covid;

import java.io.Serializable;

public class Data implements Serializable {
    private String name;
    private String age;
    private String adds;
    private String status;
    private String country;

    public Data() {
    }

    public Data(String name, String age, String adds, String status, String country) {
        this.name = name;
        this.age = age;
        this.adds = adds;
        this.status = status;
        this.country = country;
    }


    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAdds() {
        return adds;
    }

    public String getStatus() {
        return status;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAdds(String adds) {
        this.adds = adds;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

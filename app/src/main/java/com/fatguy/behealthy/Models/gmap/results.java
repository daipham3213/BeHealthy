package com.fatguy.behealthy.Models.gmap;

public class results {
    private String business_status;
    private geometry geometry;
    private String icon;
    private String name;
    private String place_id;
    private plus_code plus_code;
    private double rating;
    private String reference;
    private String scope;
    private String [] type;
    private int user_ratings_total;
    private String vicinity;
    private opening_hours opening_hours;

    public results(String business_status, com.fatguy.behealthy.Models.gmap.geometry geometry, String icon, String name, String place_id, com.fatguy.behealthy.Models.gmap.plus_code plus_code, float rating, String reference, String scope, String[] type, int user_ratings_total, String vicinity, com.fatguy.behealthy.Models.gmap.opening_hours opening_hours) {
        this.business_status = business_status;
        this.geometry = geometry;
        this.icon = icon;
        this.name = name;
        this.place_id = place_id;
        this.plus_code = plus_code;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.type = type;
        this.user_ratings_total = user_ratings_total;
        this.vicinity = vicinity;
        this.opening_hours = opening_hours;
    }

    public com.fatguy.behealthy.Models.gmap.opening_hours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(com.fatguy.behealthy.Models.gmap.opening_hours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public results() {

    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public com.fatguy.behealthy.Models.gmap.geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(com.fatguy.behealthy.Models.gmap.geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public com.fatguy.behealthy.Models.gmap.plus_code getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(com.fatguy.behealthy.Models.gmap.plus_code plus_code) {
        this.plus_code = plus_code;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public int getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(int user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}

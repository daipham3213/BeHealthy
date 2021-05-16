package com.fatguy.behealthy.gmap;

public class viewport {
    private northeast northeast;
    private northeast southwest;

    public viewport() {
    }

    public viewport(com.fatguy.behealthy.gmap.northeast northeast, com.fatguy.behealthy.gmap.northeast southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public com.fatguy.behealthy.gmap.northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(com.fatguy.behealthy.gmap.northeast northeast) {
        this.northeast = northeast;
    }

    public com.fatguy.behealthy.gmap.northeast getSouthwest() {
        return southwest;
    }

    public void setSouthwest(com.fatguy.behealthy.gmap.northeast southwest) {
        this.southwest = southwest;
    }
}

package com.fatguy.behealthy.Models.gmap;

import java.io.Serializable;

public class viewport implements Serializable {
    private northeast northeast;
    private northeast southwest;

    public viewport() {
    }

    public viewport(com.fatguy.behealthy.Models.gmap.northeast northeast, com.fatguy.behealthy.Models.gmap.northeast southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public com.fatguy.behealthy.Models.gmap.northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(com.fatguy.behealthy.Models.gmap.northeast northeast) {
        this.northeast = northeast;
    }

    public com.fatguy.behealthy.Models.gmap.northeast getSouthwest() {
        return southwest;
    }

    public void setSouthwest(com.fatguy.behealthy.Models.gmap.northeast southwest) {
        this.southwest = southwest;
    }
}

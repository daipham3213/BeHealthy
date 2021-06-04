package com.fatguy.behealthy.Models.gmap;

import java.io.Serializable;

public class geometry implements Serializable {
    private northeast location;
    private viewport viewport;

    public northeast getLocation() {
        return location;
    }

    public geometry(northeast location, com.fatguy.behealthy.Models.gmap.viewport viewport) {
        this.location = location;
        this.viewport = viewport;
    }

    public geometry() {
    }

    public com.fatguy.behealthy.Models.gmap.viewport getViewport() {
        return viewport;
    }
}

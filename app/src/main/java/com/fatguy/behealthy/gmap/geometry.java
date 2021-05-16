package com.fatguy.behealthy.gmap;

public class geometry {
    private northeast location;
    private viewport viewport;

    public northeast getLocation() {
        return location;
    }

    public com.fatguy.behealthy.gmap.viewport getViewport() {
        return viewport;
    }

    public geometry() {
    }

    public geometry(northeast location, com.fatguy.behealthy.gmap.viewport viewport) {
        this.location = location;
        this.viewport = viewport;
    }
}

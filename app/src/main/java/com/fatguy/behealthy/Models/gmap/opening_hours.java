package com.fatguy.behealthy.Models.gmap;

public class opening_hours {
    private boolean open_now;

    public opening_hours(boolean open_now) {
        this.open_now = open_now;
    }

    public opening_hours() {
    }

    public boolean isOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }
}

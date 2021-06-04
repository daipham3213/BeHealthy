package com.fatguy.behealthy.Models.C45;

import java.io.Serializable;

public class Val implements Serializable {
    public String valueName = "";
    public String itClass = "";

    public Val(String name, String inClass) {
        this.valueName = name;
        this.itClass = inClass;
    }

    public boolean isNameEqual(Val inV) {
        return this.valueName.equals(inV.valueName);
    }
}

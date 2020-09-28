package com.example;

import org.springframework.stereotype.Service;

@Service("tiger")
public class Tiger {
    private String tigerName = "DefaultTiger";

    public void setTigerName(String tigerName) {
        this.tigerName = tigerName;
    }

    public String getTigerName() {
        return tigerName;
    }

    public String toString() {
        return "TigerName: " + tigerName;
    }
}
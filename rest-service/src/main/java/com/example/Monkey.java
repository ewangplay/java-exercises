package com.example;

import org.springframework.stereotype.Service;

@Service("monkey")
public class Monkey {
    private String monkeyName = "DefaultMonkey";

    public void setMonkeyName(String monkeyName) {
        this.monkeyName = monkeyName;
    }

    public String getMonkeyName() {
        return monkeyName;
    }


    public String toString() {
        return "MonkeyName: " + monkeyName;
    }
}
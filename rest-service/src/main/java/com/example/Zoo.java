package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("zoo")
@Scope("prototype")
public class Zoo {
    @Autowired
    private Tiger tiger;

    @Autowired
    private Monkey monkey;

    public Tiger getTiger() {
        return tiger;
    }

    public Monkey getMonkey() {
        return monkey;
    }

    public String toString() {
        return tiger + ";" + monkey;
    }
}
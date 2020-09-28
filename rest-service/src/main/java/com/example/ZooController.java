package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.ApplicationContext;

@RestController
public class ZooController {
    @Autowired
    private ApplicationContext appContext;

    @GetMapping("/zoo")
    @ResponseBody
	public Zoo zoo(@RequestParam(name="tiger_name",required=false,defaultValue="KingTiger") String tigerName, @RequestParam(name="monkey_name",required=false,defaultValue="BigMonkey") String monkeyName) {
        Zoo zoo = (Zoo)appContext.getBean("zoo");
        zoo.getTiger().setTigerName(tigerName);
        zoo.getMonkey().setMonkeyName(monkeyName);
        return zoo;
	} 
}
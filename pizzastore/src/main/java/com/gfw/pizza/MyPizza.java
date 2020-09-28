package com.gfw.pizza;

public class MyPizza extends Pizza {
	MyPizza(String type) {
		this.type = type;
	}
 
	// override the cut method
	public void cut() {
		System.out.println("This is my " + type + " pizza. Please cut it into squares");
	}
}

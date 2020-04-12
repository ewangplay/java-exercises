package com.gfw.lufy.pizza;

public class SimplePizzaFactory {
	public Pizza createPizza(String type) {
		Pizza pizza = new MyPizza(type);
		return pizza;
	}
}

class MyPizza extends Pizza {
	MyPizza(String type) {
		this.type = type;
	}
 
	// override the cut method
	public void cut() {
		System.out.println("This is my " + type + " pizza. Please cut it into squares");
	}
}

package com.gfw.lufy.pizza;

public class SimplePizzaFactory {
	public Pizza createPizza(String type) {
		Pizza pizza = new MyPizza(type);
		return pizza;
	}
}


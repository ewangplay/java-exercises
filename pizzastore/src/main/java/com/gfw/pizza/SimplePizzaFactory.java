package com.gfw.pizza;

public class SimplePizzaFactory {
	public Pizza createPizza(String type) {
		Pizza pizza = new MyPizza(type);
		return pizza;
	}
}


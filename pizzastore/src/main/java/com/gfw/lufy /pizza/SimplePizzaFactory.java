package com.gfw.lufy.pizza;

public class SimplePizzaFactory {
	public Pizza createPizza(String type) {
		Pizza pizza = null;

		if (type == "cheese") {
			pizza = new CheesePizza();
		} else if (type == "greek") {
			pizza = new GreekPizza();
		} else if (type == "pepperoni") {
			pizza = new PepperoniPizza();
		}

		return pizza;
	}
}

class CheesePizza extends Pizza {
	public void prepare() {
		System.out.println("cheese pizza prepare...");
	}

	public void bake() { 
		System.out.println("cheese pizza bake...");
	}

	public void cut() {
		System.out.println("cheese pizza cut...");
	}

	public void box() {
		System.out.println("cheese pizza box...");
	}

	public String getDescription() {
		return "This is a cheese pizza. Enjoy it!";
	}
}

class GreekPizza extends Pizza {
	public void prepare() {
		System.out.println("greek pizza prepare...");
	}

	public void bake() { 
		System.out.println("greek pizza bake...");
	}

	public void cut() {
		System.out.println("greek pizza cut...");
	}

	public void box() {
		System.out.println("greek pizza box...");
	}

	public String getDescription() {
		return "This is a greek pizza. Enjoy it!";
	}
}

class PepperoniPizza extends Pizza {
	public void prepare() {
		System.out.println("pepperoni pizza prepare...");
	}

	public void bake() { 
		System.out.println("pepperoni pizza bake...");
	}

	public void cut() {
		System.out.println("pepperoni pizza cut...");
	}

	public void box() {
		System.out.println("pepperoni pizza box...");
	}

	public String getDescription() {
		return "This is a pepperoni pizza. Enjoy it!";
	}
}

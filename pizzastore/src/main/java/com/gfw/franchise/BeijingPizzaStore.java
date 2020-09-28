package com.gfw.franchise;

public class BeijingPizzaStore extends PizzaStore {
	public Pizza createPizza(String type) {
		Pizza pizza = new BeijingPizza(type);
		return pizza;
	}
}

class BeijingPizza extends Pizza {
	BeijingPizza(String type) {
		this.region = "beijing";
		this.type = type;
	}

	// override bake method
	void bake() {
		System.out.println("This is a " + region + " " + type + " pizza. Please bake it more 3 minutes");
	}

	// override cut method
	void cut() {
		System.out.println("This is a " + region + " pizza. Please cut it into squares");
	}
}


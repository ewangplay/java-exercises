package com.gfw.lufy.franchise;

public class ShanghaiPizzaStore extends PizzaStore {
	public Pizza createPizza(String type) {
		Pizza pizza = new ShanghaiPizza(type);
		return pizza;
	}
}

class ShanghaiPizza extends Pizza {
	ShanghaiPizza(String type) {
		this.region = "shanghai";
		this.type = type;
	}

	// override cut method
	void cut() {
		System.out.println("This is a " + region + " pizza. Please cut it into triangles");
	}

}


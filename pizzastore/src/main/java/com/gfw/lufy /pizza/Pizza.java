package com.gfw.lufy.pizza;

public abstract class Pizza {
	String type;

	public void prepare() {
		System.out.println(type + " pizza prepare...");
	}

	public void bake() {
		System.out.println(type + " pizza bake...");
	}

	public void cut() {
		System.out.println(type + " pizza cut...");
	}

	public void box() {
		System.out.println(type + " pizza box...");
	}

	public String getDescription() {
		return type + " pizza";
	}
}


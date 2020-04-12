package com.gfw.lufy.franchise;

public abstract class Pizza {
	String region;
	String type;

	void prepare() {
		System.out.println(region + " " + type + " pizza prepare...");
	}

	void bake() {
		System.out.println(region + " " + type + " pizza bake...");
	}

	void cut() {
		System.out.println(region + " " + type + " pizza cut...");
	}

	void box() {
		System.out.println(region + " " + type + " pizza box...");
	}

	String getDescription() {
		return region + " " + type + " pizza";
	}
}


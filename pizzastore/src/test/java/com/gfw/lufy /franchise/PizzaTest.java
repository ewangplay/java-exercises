package com.gfw.lufy.franchise;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Pizza Store
 */
public class PizzaTest extends TestCase
{
	BeijingPizzaStore bjPizzaStore = null;
	ShanghaiPizzaStore shPizzaStore = null;

	protected void setUp() {
		bjPizzaStore = new BeijingPizzaStore();
		shPizzaStore = new ShanghaiPizzaStore();
	}

	public void testBeijingCheesePizza() {
		Pizza bjCheesePizza = bjPizzaStore.orderPizza("cheese");
		assertEquals(bjCheesePizza.getDescription(), "beijing cheese pizza");
	}

	public void testShanghaiCheesePizza() {
		Pizza shCheesePizza = shPizzaStore.orderPizza("cheese");
		assertEquals(shCheesePizza.getDescription(), "shanghai cheese pizza");
	}

}

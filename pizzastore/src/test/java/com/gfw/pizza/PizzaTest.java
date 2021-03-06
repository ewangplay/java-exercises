package com.gfw.pizza;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class PizzaTest extends TestCase
{
	SimplePizzaFactory factory = null;
	PizzaStore pizzaStore = null;

	protected void setUp() {
		factory = new SimplePizzaFactory();
		pizzaStore = new PizzaStore(factory);
	}

	public void testCheesePizza() {
		Pizza pizza1 = pizzaStore.orderPizza("cheese");
		assertEquals(pizza1.getDescription(), "cheese pizza");
	}

	public void testCalmPizza() {
		Pizza pizza1 = pizzaStore.orderPizza("calm");
		assertEquals(pizza1.getDescription(), "calm pizza");
	}

}

package com.gfw.pizza;

/**
 * Pizza Store App using "Simple Factory Pattern"
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		SimplePizzaFactory factory = new SimplePizzaFactory();

		PizzaStore pizzaStore = new PizzaStore(factory);

		Pizza pizza = pizzaStore.orderPizza("cheese");

		System.out.println("This is a " + pizza.getDescription() + ". Enjoy it!");
    }
}

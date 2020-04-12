package com.gfw.lufy.franchise;

/**
 * Pizza Store Franchise App using "Factory Method Pattern"
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		BeijingPizzaStore bjPizzaStore = new BeijingPizzaStore();
		Pizza bjCheesePizza = bjPizzaStore.orderPizza("cheese");
		System.out.println("This is a " + bjCheesePizza.getDescription() + ". Enjoy it!");

		ShanghaiPizzaStore shPizzaStore = new ShanghaiPizzaStore();
		Pizza shCheesePizza = shPizzaStore.orderPizza("cheese");
		System.out.println("This is a " + shCheesePizza.getDescription() + ". Enjoy it!");

    }
}

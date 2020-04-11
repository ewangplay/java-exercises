package com.gfw.lufy;

import com.gfw.lufy.pizza.SimplePizzaFactory;
import com.gfw.lufy.pizza.PizzaStore;
import com.gfw.lufy.pizza.Pizza;

/**
 * Pizza Store App
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		SimplePizzaFactory factory = new SimplePizzaFactory();

		PizzaStore pizzaStore = new PizzaStore(factory);

		Pizza pizza = pizzaStore.orderPizza("cheese");

		System.out.println(pizza.getDescription());
    }
}

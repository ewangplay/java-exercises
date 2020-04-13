package com.gfw.lufy.pizza;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	int val1, val2;

	protected void setUp() {
		System.out.println("set up...");

		val1 = 1;
		val2 = 3;
	}
	
	protected void tearDown() {
		System.out.println("tear down...");
	}

	public void testAdd() {
		System.out.println("in testAdd");
		int result = val1 + val2;
		assertTrue(result == 4);
	}

	// 这些注释符在这里不起作用
	//execute before class
	@BeforeClass
	public static void beforeClass() {
		System.out.println("in before class");
	}

	//execute after class
	@AfterClass
	public static void  afterClass() {
		System.out.println("in after class");
	}

	//execute before test
	@Before
	public void before() {
		System.out.println("in before");
	}

	//execute after test
	@After
	public void after() {
		System.out.println("in after");
	}

	//test case
	// 每一个测试用例必须以test开头，否则不会被执行
	public void test() {
		System.out.println("in test");
	}

	// 这些注释符在这里不起作用
	//test case ignore and will not execute
	@Ignore
	public void ignoreTest() {
		System.out.println("in ignore test");
	}
}

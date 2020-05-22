package com.tutorialspoint.hellospring;

//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("//Users/wangxiaohui/gitRoot/github/exercises/java-exercises/hellospring/Beans.xml");
		try {
			HelloWorld objA = (HelloWorld) context.getBean("helloWorld");
			objA.setMessage("Hello, kitty!");
			System.out.println(objA.getMessage());

			//HelloWorld objB = (HelloWorld) context.getBean("helloWorld");
			//System.out.println(objB.getMessage());
		} finally {
			context.close();
		}

		//SpringApplication.run(DemoApplication.class, args);
	}

}

package com.todo.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.todo.springboot"})
public class ToDoServicePrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoServicePrjApplication.class, args);
	}
}

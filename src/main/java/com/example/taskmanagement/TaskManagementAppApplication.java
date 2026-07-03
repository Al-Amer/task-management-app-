package com.example.taskmanagement;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.taskmanagement")
public class TaskManagementAppApplication {

	public static void main(String[] args) {
        Application.launch(TaskManagementAppUI.class, args);
	}

}

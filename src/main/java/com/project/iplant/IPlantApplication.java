package com.project.iplant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;

@SpringBootApplication
public class IPlantApplication {

	@Autowired
	Runnable MessageListener;

	public static void main(String[] args) {
		SpringApplication.run(IPlantApplication.class, args);
	}

	@Bean
	public CommandLineRunner schedulingRunner(@Qualifier("taskExecutor") TaskExecutor executor) {
		return args -> executor.execute(MessageListener);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

}

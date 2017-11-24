package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyConfigClientApplication.class, args);
	}

	// @Autowired
	// void setEnvironment(Environment env) {
	// System.out.println("my-config.appName from env: " +
	// env.getProperty("springCloud-config.appName")+";"+env.getProperty("springCloud-config.appKey"));
	// }
}

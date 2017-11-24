package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Init extends SpringBootServletInitializer {
	// 容器启动时，自动初始化
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Init.class);
	}

	@Autowired
	void setEnvironment(Environment env) {
		System.out.println("my-config.appName from env: " + env.getProperty("springCloud-config.appName") + ";" + env.getProperty("springCloud-config.appKey"));
	}
}

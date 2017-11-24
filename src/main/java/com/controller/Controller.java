package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.User;

@RestController
public class Controller {
	@Autowired
	private User user;

	@RequestMapping("/a")
	public String getAppName() {
		return user.getAppName() + ";" + user.getAppKey();
	}
}

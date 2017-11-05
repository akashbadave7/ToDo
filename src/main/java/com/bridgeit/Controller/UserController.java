package com.bridgeit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.model.UserBean;

@RestController
public class UserController {

	@Autowired
	private UserBean user;
}

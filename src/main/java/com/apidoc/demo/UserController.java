package com.apidoc.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	
	public static final String API_USER = "/api/user";
	public static final String API_USER_BY_ID = "/api/user{id}";
	public static final String API_USERS = "/api/users";
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(API_USERS)
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping(API_USER_BY_ID)
	public User getUser(@PathVariable Long id) {
		return userRepository.findById(id).get();
	}
	
	@PostMapping(API_USER)
	public User saveUser(@RequestBody User user) {
		return userRepository.save(user);
	}
	
	@GetMapping(API_USER)
	public User getUserByEMail(@RequestParam String eMail) {
		return userRepository.findByeMail(eMail);
	}
	
	@DeleteMapping(API_USER_BY_ID)
	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}


}



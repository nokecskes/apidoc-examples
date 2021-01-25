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
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/api/users")
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/api/user/{id}")
	public User getUser(@PathVariable Long id) {
		return userRepository.findById(id).get();
	}
	
	@PostMapping("/api/user")
	public User saveUser(@RequestBody User user) {
		return userRepository.save(user);
	}
	
	@GetMapping("/api/user")
	public User getUserByEMail(@RequestParam String eMail) {
		return userRepository.findByeMail(eMail);
	}
	
	@DeleteMapping("/api/user/{id}")
	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}


}



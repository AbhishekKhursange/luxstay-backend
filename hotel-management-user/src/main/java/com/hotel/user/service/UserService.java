package com.hotel.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.user.dto.UserRequest;
import com.hotel.user.entity.User;
import com.hotel.user.repository.UserRepository;

@Service
public class UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
    private UserRepository repo;    

	public User save(UserRequest request) {
		
		log.info("Attempting to create user: {}", request.getUsername());

        if (repo.findByUsername(request.getUsername()) != null) {
        	log.warn("Username already exists: {}", request.getUsername());
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // already encoded in AUTH
        user.setRole(request.getRole());
        
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        log.info("User created successfully");
        return repo.save(user);
    }

    public User findByUsername(String username) {
    	log.info("Fetching user: {}", username);
        User user = repo.findByUsername(username);

        if (user == null) {
        	log.error("User not found: {}", username);
            throw new RuntimeException("User not found");
        }
        
        log.info("User found: {}", username);
        return user;
    }
    
    public List<User> findAll() {
        return repo.findAll();
    }

}

package com.example.exam.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exam.model.User;
import com.example.exam.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	
	@Autowired
    UserRepository userRepository;
	
    public User getUserById(int id) {
        return userRepository.findById(id);
    }
    
    public void saveUser(User user) {
        userRepository.save(user); 
    }
    
    public String getMovieThreadKey(int userId, int movieId) {
        return userId + "-" + movieId;
    } 

}

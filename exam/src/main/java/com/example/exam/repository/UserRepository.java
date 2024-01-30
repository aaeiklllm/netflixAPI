package com.example.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exam.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findById(int id);
	public User save(User user);
	
}

package com.example.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exam.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
	public Movie findById(int id);
}

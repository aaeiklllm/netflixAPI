package com.example.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exam.model.Movie;
import com.example.exam.model.User;
import com.example.exam.repository.MovieRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MovieService {
	
	@Autowired
    MovieRepository movieRepository;
	
	public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
	
	public Movie getMovieById(int id) {
        return movieRepository.findById(id);
	}
	  
	public void saveMovie(Movie movie) {
		movieRepository.save(movie); 
	}
	

}

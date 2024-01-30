package com.example.exam.controller;


import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam.model.User;
import com.example.exam.model.Movie;
import com.example.exam.repository.UserRepository;
import com.example.exam.service.MovieService;
import com.example.exam.service.UserService;

@CrossOrigin
@RestController
public class Controller {
	
	@Autowired
	UserService userService;
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	UserRepository userRepository;
	
	private Map<String, Thread> movieThreads = new HashMap<>();
	
	
	@GetMapping("/{userId}/subscription")
	public ResponseEntity<String> GetSubscriptionPlan(@PathVariable int userId){
		User user = userService.getUserById(userId);
		if (user != null) {
			String plan = user.getPlan();
			return ResponseEntity.ok(plan);
		}
		else {
			return ResponseEntity.ok("User ID doesn't exist.");
		}
	}
	
	@PostMapping("/{userId}/subscription")
	public ResponseEntity<String> SetSubscriptionPlan(@PathVariable int userId, @RequestBody String plan){
		 User user = userService.getUserById(userId);
		 
		if (user != null) {
	        user.setPlan(plan);
	        if (plan.equals("Basic")) {
	        	user.setScreen(1);
	        	userService.saveUser(user);
	        }
	        else if (plan.equals("Standard")) {
	        	user.setScreen(2);
	        	userService.saveUser(user);
	        }
	        else if (plan.equals("Premium")) {
	        	user.setScreen(3);
	        	userService.saveUser(user);
	        }
	        else {
	        	return ResponseEntity.ok("Subscription plan doesn't exist");
	        }
	        return ResponseEntity.ok("Subscription set.");
	    } else {
	        return ResponseEntity.ok("User ID doesn't exist.");
	    }
	}
	
	@GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovie(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }
	
	
	@PostMapping("/{userId}/watch/{movieId}")
	public ResponseEntity<String> UserWatchMovie(@PathVariable int userId, @PathVariable int movieId) {
	    User user = userService.getUserById(userId);
	    Movie movie = movieService.getMovieById(movieId);

	    if (user != null && movie != null) {
	        int availableScreen = user.getScreen();
	        if (availableScreen == 0) {
	            return ResponseEntity.ok("No more available screens");
	        } else {
	            String movieThreadKey = userService.getMovieThreadKey(userId, movieId);
	            if (movieThreads.containsKey(movieThreadKey)) {
	                return ResponseEntity.ok("User is already watching this movie");
	            }

	            user.setScreen(availableScreen - 1);
	            userService.saveUser(user);

	            Thread movieThread = new Thread(() -> {
	                try {
	                    Thread.sleep(movie.getDuration() * 1000);
	                    user.setScreen(user.getScreen() + 1);
	                    userService.saveUser(user);
	                    String message = "Movie ended";
	                    ResponseEntity.ok(message);
	                } catch (InterruptedException e) {
	                    user.setScreen(user.getScreen() + 1);
	                    userService.saveUser(user);
	                } finally {
	                    movieThreads.remove(movieThreadKey);
	                }
	            });

	            movieThreads.put(movieThreadKey, movieThread);

	            movieThread.start();
	            String initialMessage = "Movie started";
	            return ResponseEntity.ok(initialMessage);
	        }
	    } else {
	        return ResponseEntity.ok("User ID and/or Movie ID doesn't exist");
	    }
	}

	@PostMapping("/{userId}/stop/{movieId}")
	public ResponseEntity<String> UserStopMovie(@PathVariable int userId, @PathVariable int movieId){
		User user = userService.getUserById(userId);
		Movie movie = movieService.getMovieById(movieId);
		String movieThreadKey = userService.getMovieThreadKey(userId, movieId);
		
		if (user != null && movie != null) {
			if (movieThreads.containsKey(movieThreadKey)) {
				user.setScreen(user.getScreen() + 1);
				userService.saveUser(user);
		        Thread movieThread = movieThreads.get(movieThreadKey);
		        
		        movieThread.interrupt();	

		        return ResponseEntity.ok("Movie stopped");
		    } else {
		        return ResponseEntity.ok("Movie is not currently playing");
		    }
		}
		else {
			return ResponseEntity.ok("User ID and/or Movie ID doesn't exist");
		}
	}
	
	@PostMapping("/addUser")
	public ResponseEntity<String> addUsers(@RequestBody User userDetails){
		
		String full_name = userDetails.getFullName();
		String plan = userDetails.getPlan();
		
		User user = new User(full_name, plan);
		userService.saveUser(user);
		
		if (plan.equals("Basic")) {
			user.setScreen(1);
			userService.saveUser(user);
	    }
	    
		else if (plan.equals("Standard")) {
	        user.setScreen(2);
	       	userService.saveUser(user);
	    }
		else if (plan.equals("Premium")) {
			user.setScreen(3);
	        userService.saveUser(user);
	    }
	        
		else {
			return ResponseEntity.ok("Subscription plan doesn't exist");
	    }
		
		userService.saveUser(user);
		
		return ResponseEntity.ok("Successfully added user");
	}
	
	@PostMapping("/addMovie")
	public ResponseEntity<String> addMovies(@RequestBody Movie movieDetails){
		String title = movieDetails.getTitle();
		int duration = movieDetails.getDuration();
		
		Movie movie = new Movie(title, duration);
		movieService.saveMovie(movie);
		
		return ResponseEntity.ok("Successfully added movie");
	}
}

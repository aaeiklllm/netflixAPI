package com.example.exam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {	
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	@JsonProperty("user_id")
	int user_id;
	
	@Column(name = "full_name")
	@JsonProperty("full_name")
	String full_name;
	

	@Column(name = "plan")
	@JsonProperty("plan")
	String plan;
	
	@Column(name = "available_screen")
	@JsonProperty("available_screen")
	int available_screen;
	
	public User() {
			
		}
	
	public User(String full_name, String plan) {
		this.full_name = full_name;
		this.plan = plan;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String username) {
		this.full_name = username;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public int getScreen() {
		return available_screen;
	}

	public void setScreen(int available_screen) {
		this.available_screen = available_screen;
	}
	
}

	
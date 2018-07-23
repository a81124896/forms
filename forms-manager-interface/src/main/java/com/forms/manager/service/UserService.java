package com.forms.manager.service;

import java.util.List;

import com.forms.pojo.User;

public interface UserService {
	public List<User> getAllUsers();

	String testHashCache(String id);

	User testUserHashCacheOne(String id);
	
	
	String clearUser(String id);
}

package com.forms.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.forms.dao.UserMapper;
import com.forms.manager.service.UserService;
import com.forms.pojo.User;
import com.forms.redis.annotation.RedisCache;
import com.forms.redis.annotation.RedisHashCache;
import com.forms.redis.annotation.RedisHashDel;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

//	@Autowired
//	private RedisCacheUtil cacheUtil;

	@Override
	@RedisCache(fieldKey = "'DCgiCache_'")
	public List<User> getAllUsers() {
		return userMapper.selectAll();
	}
	
	@Override
	@RedisHashCache(fieldKey = "'ALLHASH'", fieldHashKey = "'id_'+#id")
	public String testHashCache(String id) {
		return id+"-TEST";
	}
	
	@Override
	@RedisHashCache(fieldKey = "'ALLUserHASH'", fieldHashKey = "'user_'+#id")
	public User testUserHashCacheOne(String id) {
		return userMapper.selectAll().get(Integer.valueOf(id));
	}
	
	@Override
	@RedisHashDel(fieldKey = "'ALLUserHASH'", fieldHashKey = "'user_'+#id")
	public String clearUser(String id) {
		return id+" is clear!";
	}

}

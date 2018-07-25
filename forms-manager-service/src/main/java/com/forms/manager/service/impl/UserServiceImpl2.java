package com.forms.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.forms.dao.UserMapper;
import com.forms.manager.service.UserService;

@Service(version="2.0.0")
public class UserServiceImpl2 extends UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;

//	@Autowired
//	private RedisCacheUtil cacheUtil;

	
	@Override
	//@RedisHashCache(fieldKey = "'ALLHASH'", fieldHashKey = "'id_'+#id")
	public String testHashCache(String id) {
		System.out.println("test2");
		return id+"-TEST";
	}
	

}

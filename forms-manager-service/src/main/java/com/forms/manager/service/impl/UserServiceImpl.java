package com.forms.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.forms.dao.TbUserMapper;
import com.forms.manager.service.UserService;
import com.forms.pojo.TbUser;
import com.forms.redis.annotation.RedisCache;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	@RedisCache(fieldKey = "'TbUser:All'")
	public List<TbUser> getAllUsers() {
		return userMapper.selectAll();
	}

}

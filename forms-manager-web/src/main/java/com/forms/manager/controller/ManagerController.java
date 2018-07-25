package com.forms.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.forms.manager.service.UserService;
import com.mchange.v2.log.MLog;

@Controller
public class ManagerController {

	@Reference(version="2.0.0")
	private UserService userService;
	
	@RequestMapping("/test")
	@ResponseBody
	public Object getUserList(){
		System.out.println("sssssss");
		return userService.getAllUsers();
	}
	@RequestMapping("/test2")
	@ResponseBody
	public Object testHashCache(String id){
		MLog.info("test2==>"+id);
		return userService.testHashCache(id);
	}
	@RequestMapping("/test3")
	@ResponseBody
	public Object testHashCacheOne(String id){
		MLog.info("test3==>"+id);
		return userService.testUserHashCacheOne(id);
	}
	@RequestMapping("/test4")
	@ResponseBody
	public Object clearUser(String id){
		MLog.info("test4==>"+id);
		return userService.clearUser(id);
	}
}

package com.forms.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.forms.manager.service.UserService;

@Controller
public class ManagerController {

	@Reference
	private UserService userService;
	
	@RequestMapping("/test")
	@ResponseBody
	public Object getUserList(HttpServletRequest request){
		
		Object o = request.getSession().getAttribute("USER");
		if(o==null){
			System.out.println("来了一个session");
			o = userService.getAllUsers();
			request.getSession().setAttribute("USER",o);
		}
		return o;
	}
}

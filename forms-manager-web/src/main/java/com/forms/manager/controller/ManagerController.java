package com.forms.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.forms.mail.pojo.MailParam;
import com.forms.mail.service.MailService;
import com.forms.manager.service.UserService;

@Controller
public class ManagerController {

//	@Reference(version="2.0.0")
//	private UserService userService;
	
	
	@Reference(version="1.0.0")
	private MailService mailService;
	
//	@RequestMapping("/test")
//	@ResponseBody
//	public Object getUserList(HttpServletRequest request){
//		
//		Object o = request.getSession().getAttribute("USER");
//		if(o==null){
//			System.out.println("来了一个session");
//			o = userService.getAllUsers();
//			request.getSession().setAttribute("USER",o);
//		}
//		return o;
//	}
	
	@RequestMapping("/mail")
	@ResponseBody
	public Object mail(HttpServletRequest request){
		MailParam mailParam = new MailParam();
		mailParam.setContent("test");
		mailParam.setTo("linpengs@163.com");
		mailParam.setSubject("主题");
		mailService.send(mailParam);
		return mailParam;
	}
}

package com.forms.search.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.forms.common.pojo.SearchResult;
import com.forms.search.service.SearchService;

@Controller
public class SearchController {

	@Reference
	private SearchService searchService;
	
	@RequestMapping("/test")
	@ResponseBody
	public Object getUserList(HttpServletRequest request) throws Exception{
		return searchService.setIndex();
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public SearchResult search(Integer page,@RequestParam(value="q") String queryString, Integer rows,Model model) throws Exception{
		//1.引入
		//2.注入
		//3.调用
		//处理乱码：
		queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
		
		return searchService.search(queryString, page, rows);
		//4.设置数据传递到jsp中
//		model.addAttribute("query", queryString);
//		model.addAttribute("totalPages", result.getPageCount());//总页数
//		model.addAttribute("itemList", result.getItemList());
//		model.addAttribute("page", page);
//		 "search";
	}
}

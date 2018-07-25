package com.forms.common.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/**
 * 商品搜索的分页信息结果对象
 * 
 * 
 */
public class SearchResult implements Serializable {
//	private List<Map> itemList;// 搜索结果列表
	private JSONArray itemList;// 搜索结果列表
	private long recordCount;// 总记录数
	private long pageCount;// 总页数

//	public List<Map> getItemList() {
//		return itemList;
//	}
//
//	public void setItemList(List<Map> itemList) {
//		this.itemList = itemList;
//	}

	public long getRecordCount() {
		return recordCount;
	}

	public JSONArray getItemList() {
		return itemList;
	}

	public void setItemList(JSONArray itemList) {
		this.itemList = itemList;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

}

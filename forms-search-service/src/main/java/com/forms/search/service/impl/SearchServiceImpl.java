package com.forms.search.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.forms.common.pojo.Result;
import com.forms.common.pojo.SearchResult;
import com.forms.dao.TbItemMapper;
import com.forms.pojo.TbItem;
import com.forms.search.service.SearchService;
import com.forms.search.service.util.SearchUtil;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrServer solrserver;
	@Autowired
	private SearchUtil searchUtil;

	@Override
	public Result setIndex() throws Exception {
		List<TbItem> searchItemList = itemMapper.selectAll();
		for (TbItem searchItem : searchItemList) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId().toString());// 这里是字符串需要转换
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSeller());
			document.addField("item_price", searchItem.getPrice().longValue());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory());
			document.addField("item_desc", searchItem.getSpec());
			// 添加到索引库
			solrserver.add(document);
		}
		// 提交
		solrserver.commit();
		return Result.ok();
	}

	@Override
	public SearchResult search(String queryString, Integer page, Integer rows)
			throws Exception {
		// 1.创建solrquery对象
		SolrQuery query = new SolrQuery();
		// 2.设置主查询条件
		if (StringUtils.isNotBlank(queryString)) {
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}
		// 2.1设置过滤条件 设置分页
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 60;
		query.setStart((page - 1) * rows);// page-1 * rows
		query.setRows(rows);
		// 2.2.设置默认的搜索域
		query.set("df", "item_keywords");
		// 2.3设置高亮
		// query.setHighlight(true);
		// query.setHighlightSimplePre("<em style=\"color:red\">");
		// query.setHighlightSimplePost("</em>");
		// query.addHighlightField("item_title");// 设置高亮显示的域
		// 5.返回
		return searchUtil.search(query);
	}

}

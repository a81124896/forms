package com.forms.search.service;

import com.forms.common.pojo.Result;
import com.forms.common.pojo.SearchResult;
 

public interface SearchService {

	public Result setIndex() throws Exception;

	public SearchResult search(String queryString, Integer page, Integer rows) throws Exception;
}

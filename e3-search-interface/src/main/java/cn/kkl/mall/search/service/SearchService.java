package cn.kkl.mall.search.service;


import cn.kkl.mall.pojo.SearchResult;

public interface SearchService {
	
	SearchResult search(String keyword,int page,int rows) throws Exception;

}

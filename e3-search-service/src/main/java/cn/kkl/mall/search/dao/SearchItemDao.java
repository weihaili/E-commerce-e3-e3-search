package cn.kkl.mall.search.dao;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import cn.kkl.mall.pojo.SearchResult;

public interface SearchItemDao {

	SearchResult search(SolrQuery query) throws SolrServerException;
}

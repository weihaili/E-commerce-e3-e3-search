package cn.kkl.mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kkl.mall.pojo.SearchResult;
import cn.kkl.mall.search.dao.SearchItemDao;
import cn.kkl.mall.search.service.SearchService;

/**
 * @author Admin
 * item search service
 */
@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchItemDao searchItemDao;

	/* function:
	 * 1. create solrQuery instance
	 * 2. set query conditions
	 * 3. set page parameters
	 * 4. set default search field
	 * 5. open highlight display
	 * 6. set highlight display field prefix and suffix
	 * 7. invoke dao layer execute query
	 * 9. calculate totalPages according to recordCount and rows
	 * 10. completion return value
	 */
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		SolrQuery query=new SolrQuery();
		query.setQuery(keyword);
		if (page<=0) {
			page=1;
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		SearchResult searchResult = searchItemDao.search(query);
		long recordCount=searchResult.getRecordCount();
		int totalPages=(int) Math.ceil(recordCount/rows);
		searchResult.setTotalPages(totalPages);
		return searchResult;
	}

}

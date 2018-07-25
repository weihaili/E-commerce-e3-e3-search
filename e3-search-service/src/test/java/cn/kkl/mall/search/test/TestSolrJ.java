package cn.kkl.mall.search.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Admin operate solr index step: 1. create solrServer object to create
 *         a connection.parameter is solr service url. 2. create
 *         solrInputDocument object . 3. add field to solrInputDocument
 *         instance.solrInputDocument must contain id field. all field name must
 *         definition in schema.xml. namely must follow the principle of first
 *         definition and use. 4. write the solrInputDocument instance to index
 *         library 5. commit
 *
 */
public class TestSolrJ {

	private String baseURL = "http://192.168.25.133:8080/solr/collection1";
	private SolrServer solrServer;
	private SolrInputDocument document;
	private SolrQuery solrQuery;

	@Before
	public void init() {
		solrServer = new HttpSolrServer(baseURL);
		document = new SolrInputDocument();
		solrQuery = new SolrQuery();
	}

	@Test
	public void add() {
		document.addField("id", "doc01");
		document.addField("item_title", "test solrjo1");
		document.addField("item_sell_point", "test solrJ is convenient");
		document.addField("item_price", 100);
		document.addField("item_category_name", "solrJ test");
		try {
			solrServer.add(document);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void del() {
		try {
			// solrServer.deleteById("doc01");
			solrServer.deleteByQuery("id:doc01");
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * query step: 1. create solrServer object 2. create solrQury object 3. set
	 * query condition 4. execute query then get queryResponse object 5. The
	 * queryResponse instance contain document list,get document list and query
	 * total records from the queryResponse instance 6. traversing(polling) the
	 * document list,get specific field value
	 * 
	 */
	@Test
	public void query() {
		// solrQuery.setQuery("*:*");
		solrQuery.set("q", "*:*");
		try {
			QueryResponse queryResponse = solrServer.query(solrQuery);
			SolrDocumentList documentList = queryResponse.getResults();
			System.out.println("query total records:" + documentList.getNumFound());
			for (SolrDocument solrDocument : documentList) {
				System.out.println("" + solrDocument.get("id") + solrDocument.get("item_title")
						+ solrDocument.get("item_price") + solrDocument.get("item_image")
						+ solrDocument.get("item_category_name") + solrDocument.get("item_sell_point"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryComplexCondition() {
		solrQuery.setQuery("手机");
		solrQuery.setStart(0);
		solrQuery.setRows(20);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		try {
			QueryResponse queryResponse = solrServer.query(solrQuery);
			SolrDocumentList solrDocumentList = queryResponse.getResults();
			System.out.println("query total records:" + solrDocumentList.getNumFound());
			for (SolrDocument solrDocument : solrDocumentList) {
				String id=(String) solrDocument.get("id");
				Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			    List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			    String title="";
			    if (list!=null&& list.size()>0) {
					title = list.get(0);
				}else {
					title=(String) solrDocument.get("item_title");
				}
			    System.out.println(id + title);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

}

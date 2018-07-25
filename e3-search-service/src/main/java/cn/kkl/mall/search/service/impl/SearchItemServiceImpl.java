package cn.kkl.mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kkl.mall.pojo.E3Result;
import cn.kkl.mall.pojo.SearchItem;
import cn.kkl.mall.search.mapper.ItemMapper;
import cn.kkl.mall.search.service.SearchItemService;

/**
 * @author Admin
 * index library maintain service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public E3Result importAllItem() {
		try {
			List<SearchItem> itemList = itemMapper.getItemList();
			for (SearchItem searchItem : itemList) {
				SolrInputDocument document=new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_category_name", searchItem.getCategory_name());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_image", searchItem.getImage());
				
				solrServer.add(document);
			}
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "data import exception");
		}
	}

}

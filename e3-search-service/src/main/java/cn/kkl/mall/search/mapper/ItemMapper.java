package cn.kkl.mall.search.mapper;

import java.util.List;

import cn.kkl.mall.pojo.SearchItem;

public interface ItemMapper {
	
	List<SearchItem> getItemList();
	
	SearchItem getItemById(Long itemId);

}

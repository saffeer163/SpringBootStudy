package com.cy.store.mapper;

import com.cy.store.entity.Address;
import com.cy.store.entity.Product;

import java.util.List;

public interface ProductMapper {
	/**
	 * 通过模糊查询产品
	 * @param title 产品名的模糊数据
	 * @return 产品的列表
	 */
	List<Product> searchProductByTitle(String title);
	
	/**
	 * 查询热销商品的前四名
	 * @return 热销商品前四名的集合
	 */
	List<Product> findHotList();
	
	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据则返回null
	 */
	Product findById(Integer id);
	
	List<Product> findLatelyList();
}

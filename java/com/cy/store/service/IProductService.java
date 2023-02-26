package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.entity.Product;

import java.util.List;

//产品查询接口
public interface IProductService {
	
	/**
	 * 对商品进行模糊查询
	 * @param title 用于填写的模糊数据
	 * @return
	 */
	List<Product> searchProductByTitle(String title);
	
	/**
	 * 查询热销商品的前四名(Limit 0 , 4)
	 * @return 热销商品前四名的集合
	 */
	List<Product> findHotList();
	
	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据则返回null
	 */
	Product findById(Integer id);
	
	/**
	 * 查询商品修改时间最新的前四名
	 * @return 最近修改商品前四名的集合
	 */
	List<Product> findLatelyList();
}

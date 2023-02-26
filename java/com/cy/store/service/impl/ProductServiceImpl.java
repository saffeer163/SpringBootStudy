package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.entity.Product;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.IProductService;
import com.cy.store.service.ex.AddressCountLimitException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//新增收货地址的实现类
@Service
public class ProductServiceImpl implements IProductService {
	@Autowired
	private ProductMapper productMapper;
	
	@Override
	public List<Product> searchProductByTitle(String title) {
		List<Product> list = productMapper.searchProductByTitle(title);
		if (list.size()==0){
			throw new ProductNotFoundException("未查询到该商品");
		}
		//精简数据,后续可能出错!!!!
		for (Product p : list){
			p.setNum(null);
			p.setCategoryId(null);
			p.setSellPoint(null);
			p.setCreatedTime(null);
			p.setCreatedUser(null);
			p.setModifiedTime(null);
			p.setModifiedUser(null);
			p.setPriority(null);
			p.setStatus(null);
		}
		return list;
	}
	
	@Override
	public List<Product> findHotList() {
		List<Product> list = productMapper.findHotList();
		for (Product product : list) {
			product.setPriority(null);
			product.setCreatedUser(null);
			product.setCreatedTime(null);
			product.setModifiedUser(null);
			product.setModifiedTime(null);
		}
		return list;
	}
	
	@Override
	public Product findById(Integer id) {
		// 根据参数id调用私有方法执行查询，获取商品数据
		Product product = productMapper.findById(id);
		// 判断查询结果是否为null
		if (product == null) {
			// 是：抛出ProductNotFoundException
			throw new ProductNotFoundException("尝试访问的商品数据不存在");
		}
		// 清空无关数据
		product.setPriority(null);
		product.setCreatedUser(null);
		product.setCreatedTime(null);
		product.setModifiedUser(null);
		product.setModifiedTime(null);
		// 返回查询结果
		return product;
	}
	
	@Override
	public List<Product> findLatelyList() {
		List<Product> list = productMapper.findLatelyList();
		for (Product product : list) {
			product.setPriority(null);
			product.setCreatedUser(null);
			product.setCreatedTime(null);
			product.setModifiedUser(null);
			product.setModifiedTime(null);
		}
		return list;
	}
	
	
}

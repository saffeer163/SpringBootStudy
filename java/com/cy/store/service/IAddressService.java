package com.cy.store.service;

import com.cy.store.entity.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//收货地址业务层接口
public interface IAddressService {
	
	void addNewAddress(Integer uid,
	                   String username,
	                   Address address);
	
	List<Address> getByUid(Integer uid);
	
	/*
	* 事务：基于Spring JDBC的事务（Transaction）处理，
	* 使用事务可以保证一系列的增删改操作，要么全部执行成功，要么全部执行失败。
	* @Transactional注解可以用来修饰类也可以用来修饰方法。
	* 如果添加在业务类之前，则该业务类中的方法均以事务的机制运行，
	* 但是一般并不推荐这样处理。
	* */
	@Transactional
	void setDefault(Integer aid, Integer uid, String username);
	
	@Transactional
	void delete(Integer aid, Integer uid, String username);
	
	
	/**
	 * 根据收货地址数据的id，查询收货地址详情
	 * @param aid 收货地址id
	 * @param uid 归属的用户id
	 * @return 匹配的收货地址详情
	 */
	Address getByAid(Integer aid, Integer uid);
	
	Integer update(Address address);
}

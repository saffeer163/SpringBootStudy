package com.cy.store.mapper;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;

import java.util.List;

/** 处理订单及订单商品数据的持久层接口 */
public interface OrderMapper {
	/**
	 * 插入订单数据
	 * @param order 订单数据
	 * @return 受影响的行数
	 */
	Integer insertOrder(Order order);
	
	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 * @return 受影响的行数
	 */
	Integer insertOrderItem(OrderItem orderItem);
	
	/**
	 * 查询一个订单下购买商品的数据
	 * @param oid 订单id
	 * @return 商品数据列表
	 */
	List<OrderItem> findOrderItemByOid(Integer oid);
	
	/**
	 * 通过用户id查询此用户的所有订单
	 * @param uid 用户uid
	 * @return 此用户所有订单
	 */
	List<Order> findOrderByUid(Integer uid);
}
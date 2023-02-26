package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//新增收货地址的实现类
@Service
public class AddressServiceImpl implements IAddressService {
	@Autowired
	private AddressMapper addressMapper;
//	在添加用户的收货地址的业务层依赖于 districtService的业务层接口
	@Autowired
	private IDistrictService districtService;
	
	
	
	@Override
	public void addNewAddress(Integer uid, String username, Address address) {
		//调用收货地址统计
		Integer count = addressMapper.countByUid(uid);
		if (count >= 20){
			throw new AddressCountLimitException("用户收货地址超出上限");
		}
		
		//对address对象中的数据进行补全:省 市 区
		String provinceName = districtService.getNameByCode(address.getProvinceCode());
		String cityName = districtService.getNameByCode(address.getCityCode());
		String areaName = districtService.getNameByCode(address.getAreaCode());
		
		address.setProvinceName(provinceName);
		address.setCityName(cityName);
		address.setAreaName(areaName);
		//uid,isDefault
		address.setUid(uid);
		Integer isDefault = count == 0 ? 1 : 0; //1表示默认
		address.setIsDefault(isDefault);
		//补全4项日志
		address.setCreatedUser(username);
		address.setCreatedTime(new Date());
		address.setModifiedUser(username);
		address.setModifiedTime(new Date());
		//插入收货地址
		Integer rows = addressMapper.insert(address);
		if (rows != 1){
			throw new InsertException("插入用户收货地址时产生未知的异常");
		}
	}
	
	@Override
	public List<Address> getByUid(Integer uid) {
		List<Address> list = addressMapper.findByUid(uid);
		//清空无关数据
		for (Address a : list){
			a.setTel(null);
			a.setCreatedUser(null);
			a.setCreatedTime(null);
			a.setModifiedUser(null);
			a.setModifiedTime(null);
		}
		return list;
	}
	
	/*
	* 事务：基于Spring JDBC的事务（Transaction）处理，
	* 使用事务可以保证一系列的增删改操作，要么全部执行成功，要么全部执行失败。
	* @Transactional注解可以用来修饰类也可以用来修饰方法。
	* 如果添加在业务类之前，则该业务类中的方法均以事务的机制运行，
	* 但是一般并不推荐这样处理。
	* */
	@Transactional
	@Override
	public void setDefault(Integer aid, Integer uid, String username) {
		// findByAid()查询收货地址数据,并判断是否找到
		Address address = addressMapper.findByAid(aid);
		if (address == null){
			//抛出异常
			throw new AddressNotFoundException("未找到此地址");
		}
		//判断传递来的uid与数据库此地址信息是否相等
		if (!address.getUid().equals(uid)){
			//抛出非法访问的异常
			throw new AccessDeniedException("非法访问");
		}
		//执行事务
		Integer updateRows = addressMapper.updateNonDefaultByUid(uid);
		if (updateRows < 1){
			throw  new UpdateException("修改默认地址时发生异常");
		}
		Integer rows = addressMapper.updateDefaultByAid(aid, username, new Date());
		if (rows != 1){
			throw  new UpdateException("修改默认地址时发生异常");
		}
	}
	
	@Transactional
	@Override
	public void delete(Integer aid, Integer uid, String username) {
		Address address = addressMapper.findByAid(aid);
		//判断address是否为null
		if (address==null){
			//抛出未找到此地址的异常
			throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
		}
		
		//判断非法访问
		if (!address.getUid().equals(uid)){
			throw new AccessDeniedException("非法访问");
		}
		
		//执行事务
		
		// 根据参数aid，调用deleteByAid()执行删除
		Integer rows1 = addressMapper.deleteByAid(aid);
		if (rows1 != 1) {
			throw new DeleteException("删除收货地址数据时出现未知错误，请联系系统管理员");
		}
		
		// 用户目前还有多少收货地址
		Integer count = addressMapper.countByUid(uid);
		// 用户没有地址,无法设置默认地址,返回
		if (count == 0) {
			return;
		}
		
		//判断用户是否还有默认地址
		Integer defaultAddressAid = addressMapper.findByIsDefaultAddress(uid);
		if (defaultAddressAid != null){
			//存在默认地址
			return;
		}
		
		// 调用findLastModified()找出用户最近修改的收货地址数据
		Address lastModified = addressMapper.findLastModified(uid);
		// 从以上查询结果中找出aid属性值
		Integer lastModifiedAid = lastModified.getAid();
		// 调用持久层的updateDefaultByAid()方法执行设置默认收货地址，并获取返回的受影响的行数
		Integer rows2 = addressMapper.updateDefaultByAid(lastModifiedAid, username, new Date());
		// 判断受影响的行数是否不为1
		if (rows2 != 1) {
			// 是：抛出UpdateException
			throw new UpdateException("更新收货地址数据时出现未知异常");
		}
	}
	
	
	@Override
	public Address getByAid(Integer aid, Integer uid) {
		// 根据收货地址数据id，查询收货地址详情
		Address address = addressMapper.findByAid(aid);
		
		if (address == null) {
			throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
		}
		if (!address.getUid().equals(uid)) {
			throw new AccessDeniedException("非法访问");
		}
		
		return address;
	}
	
	
	@Override
	public Integer update(Address address) {
		//对address对象中的数据进行补全:省 市 区
		String provinceName = districtService.getNameByCode(address.getProvinceCode());
		String cityName = districtService.getNameByCode(address.getCityCode());
		String areaName = districtService.getNameByCode(address.getAreaCode());
		
		address.setProvinceName(provinceName);
		address.setCityName(cityName);
		address.setAreaName(areaName);
		
		Integer update = addressMapper.update(address);
		if (update != 1){
			throw new UpdateException("修改地址时发生异常");
		}
		return null;
	}
}

package com.cy.store.service;

import com.cy.store.entity.District;

import java.util.List;

//收货地址业务层接口
public interface IDistrictService {
	
	/**
	 * 根据父区域编号来查询区域信息 (父区域编号可能是:省,市,区)
	 * @param parent 父区域编号
	 * @return 多个区域信息
	 */
	List<District> getByParent(String parent);
	
	
	String getNameByCode(String code);
}

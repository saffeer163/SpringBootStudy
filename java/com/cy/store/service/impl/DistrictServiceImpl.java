package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.mapper.DistrictMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.AddressCountLimitException;
import com.cy.store.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//新增收货地址的实现类
@Service
public class DistrictServiceImpl implements IDistrictService {
	@Autowired
	private DistrictMapper districtMapper;
	
	
	@Override
	public List<District> getByParent(String parent) {
		List<District> list = districtMapper.findByParent(parent);
		//在进行网络数据传输时,为了尽量避免无效数据的传输,可以将无效数据设置为null,这样可以节省流量并且提升效率
		
		for (District d : list){
			d.setId(null);
			d.setParent(null);
		}
		
		return list;
	}
	
	@Override
	public String getNameByCode(String code) {
		return districtMapper.findNameByCode(code);
	}
}

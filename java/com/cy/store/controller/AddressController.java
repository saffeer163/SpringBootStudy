package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.Address;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

//@Controller
@RestController //等效于@Controller+@ResponseBody
@RequestMapping("addresses")
public class AddressController extends BaseController{
	
	@Autowired
	private IAddressService addressService;
	
	@Autowired
	private IDistrictService districtService;
	
	@RequestMapping("add_new_address")
	public JsonResult<Void> addNewAddress(Address address,HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		addressService.addNewAddress(uid,username,address);
		return new JsonResult<>(OK);
	}
	
	@RequestMapping({"/",""})
	public JsonResult<List<Address>> getByUid(HttpSession session){
		Integer uid = getUidFromSession(session);
		List<Address> data = addressService.getByUid(uid);
		System.err.println(data);
		return new JsonResult<List<Address>>(OK,data);
	}
	
	@RequestMapping("{aid}/set_default")
	public JsonResult<Void> setDefault(@PathVariable("aid") Integer aid, HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		addressService.setDefault(aid, uid, username);
		return new JsonResult<Void>(OK);
	}
	
	@RequestMapping("{aid}/delete")
	public JsonResult<Void> delete(@PathVariable("aid") Integer aid, HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		addressService.delete(aid, uid, username);
		return new JsonResult<Void>(OK);
	}
	
	@RequestMapping("{aid}/get")
	public JsonResult<Address> getByAid(@PathVariable("aid") Integer aid, HttpSession session) {
		Integer uid = getUidFromSession(session);
		Address data = addressService.getByAid(aid, uid);
		return new JsonResult<Address>(OK,data);
	}
	
	@RequestMapping("{aid}/update_address")
	public JsonResult<Void> update(@PathVariable("aid") Integer aid,Address address,HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		
		address.setUid(uid);
		address.setAid(aid);
		address.setModifiedTime(new Date());
		address.setModifiedUser(username);
		addressService.update(address);
		return new JsonResult<Void>(OK);
	}
}

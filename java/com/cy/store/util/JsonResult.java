package com.cy.store.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//Json格式的数据进行相应
public class JsonResult<E> implements Serializable {
	//状态码
	private Integer state;
	//描述信息
	private String message;
	//相应数据,用泛型E表示任意类型
	private E data;
	
	public JsonResult(Throwable e) {
		this.message = e.getMessage();
	}
	
	public JsonResult(Integer state) {
		this.state = state;
	}
	
	public JsonResult() {
	}
	
	public JsonResult(Integer state, String message, E data) {
		this.state = state;
		this.message = message;
		this.data = data;
	}
	
	public JsonResult(Integer state, E data) {
		this.state = state;
		this.data = data;
	}
	
	
	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public E getData() {
		return data;
	}
	
	public void setData(E data) {
		this.data = data;
	}
}

package com.cy.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/** 商品数据的实体类 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity implements Serializable {
	private Integer id;
	private Integer categoryId;
	private String itemType;
	private String title;
	private String sellPoint;
	private Long price;
	private Integer num;
	private String image;
	private Integer status;
	private Integer priority;
	
}

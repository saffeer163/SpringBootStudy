package com.cy.store.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 订单中的商品数据 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity implements Serializable {
	private Integer id;
	private Integer oid;
	private Integer pid;
	private String title;
	private String image;
	private Long price;
	private Integer num;
	
	// Generate: Getter and Setter、Generate hashCode() and equals()、toString()
}
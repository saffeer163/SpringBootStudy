package com.cy.store.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



/** 购物车数据的Value Object类 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVO implements Serializable {
	private Integer cid;
	private Integer uid;
	private Integer pid;
	private Long price;
	private Integer num;
	private String title;
	private Long realPrice;
	private String image;
	
	// Generate: Getter and Setter、Generate hashCode() and equals()、toString()
}

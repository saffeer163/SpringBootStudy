package com.cy.store.entity;

import com.cy.store.controller.BaseController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class District extends BaseController implements Serializable {
	private Integer id;
	private String parent;
	private String code;
	private String name;
}

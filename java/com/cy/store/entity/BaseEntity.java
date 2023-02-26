package com.cy.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//作为实体类的基类
@Data
@NoArgsConstructor
@AllArgsConstructor
// @Data注解包含了@Getter @Setter
// @RequiredArgsConstructor @ToString
// @EqualsAndHashCode5个注解。
//@EqualsAndHashCode通过对象属性的计算hashCode和equals方法，与对象内存地址无关，因此具有相同属性的两个对象会被判断为相等，但是equals判断相等的两个对象并不一定指向同一内存地址。
//在继承关系中，父类的hashCode针对父类的所有属性进行运算，而子类的hashCode却只是针对子类才有的属性进行运算，那么就会存在部分对象在比较时，它们并不相等，却因为lombok自动生成的equals(Object other) 和 hashCode()方法判定为相等，从而导致出错。。
public class BaseEntity implements Serializable {
	private String createdUser;
	private Date createdTime;
	private String modifiedUser;
	private Date modifiedTime;
	
}

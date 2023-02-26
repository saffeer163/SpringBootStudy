package com.cy.store.service;

import com.cy.store.entity.User;

//用户模块业务接口
public interface IUserService {
	//用户注册
	void reg(User user);
	
	/**
	 * 用户登陆功能
	 * @param username 用户名
	 * @param password 密码
	 * @return 当前匹配的用户数据,没有则返回null
	 */
	User login(String username, String password);
	
	//session存于浏览器 哪怕数据库已经删除session对象依然存在,所以要验证用户是否存在
	void changePassword(Integer uid,
	                    String username,
	                    String oldPassword,
	                    String newPassword);
	
	
	/**
	 *根据用户的id查询用户的数据
	 * @param uid 用户uid
	 * @return 用户的数据
	 */
	User getByUid (Integer uid);
	
	/**
	 * 更新用户的数据
	 * @param uid
	 * @param username
	 * @param user 用户对象的数据
	 */
	void changeInfo(Integer uid, String username,User user);
	
	/**
	 * 修改用户头像
	 * @param uid 用户uid
	 * @param avatar 用户头像路径
	 * @param username 用户名
	 */
	void changeAvatar(Integer uid, String avatar, String username);
}

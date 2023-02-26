package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface UserMapper {
	//定义sql语句的抽象方法
	
	/*
	* 插入用户的数据
	* @param user 用户的数据
	* @return 受影响的行数(增,删改,都受影响的行数作为返回值,可以根据返回值来判断是否成功)
	* */
	Integer insert(User user);
	
	/*
	 * 根据用户名查询用户的数据
	 * @param username 用户名
	 * @return 如果找到对应用户则返回用户的数据,没有找到则返回null
	 * */
	User findByUsername(String username);
	
	
	/**
	 * 根据用户uid来修改用户密码
	 * @param uid 用户的uid
	 * @param password 用户输入的新密码
	 * @param modifiedUser 修改的执行者
	 * @param modifiedTime 修改时间
	 * @return 返回受影响行数
	 */
	Integer updatePasswordByUid(Integer uid,
	                            String password,
	                            String modifiedUser,
	                            Date modifiedTime
	                            );
	
	/**
	 * 根据用户uid查询用户数据
	 * @param uid 用户的uid
	 * @return 如果找到则返回对象,反之则返回null值
	 */
	User findByUid(Integer uid);
	
	/**
	 * 更新用户的数据信息
	 * @param user 用户的数据
	 * @return 返回受影响行数
	 */
	Integer updateInfoByUid(User user);
	
	

	
	 /**根据用户uid修改用户头像
	 * @param uid
	 * @param avatar 用户头像路径
	 * @param modifiedUser
	 * @param modifiedTime
	 * @return
	 */
	 //	 @Param("sql映射文件中#{}占位符中的变量名"): 解决sql语句占位符和映射的接口方法参数名不一致时,
	//	 需要将某个参数强项注入到某个占位符变量上时,需要用到@Param("")
	Integer updateAvatarByUid(@Param("uid") Integer uid,
	                          @Param("avatar") String avatar,
	                          @Param("modifiedUser") String modifiedUser,
	                          @Param("modifiedTime") Date modifiedTime);
	
}

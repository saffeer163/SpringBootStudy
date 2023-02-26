package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.UUID;

//用户模块业务层的实现类
@Service//@Service: 将当前类的对象交给Spring来管理,能够起到自动创建对象以及对象的维护的作用
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public void reg(User user) {
		//通过user参数来获取传递过来的username
		String username = user.getUsername();
		//调用Mapper的findByUsername(username)判断用户是否被注册过
		User result = userMapper.findByUsername(username);
		//判断结果集是否为null
		if (result != null){
			//不为null则抛出用户名占用的异常
			throw new UsernameDuplicatedException("用户名被占用");
		}
		//密码加密处理的实现:MD5算法的形式
		//串 + password + 串  ---- MDS算法进行加密
		//串: 通常被称为盐值(一个随机的字符串)
		String oldPassword = user.getPassword();
		//获取盐值(随机生成一个盐值)
		String salt = UUID.randomUUID().toString().toUpperCase();
		//将密码和盐值进行加密处理,忽略原有密码强度提升了数据安全性
		String md5Password = getMD5Password(oldPassword,salt);
		//将加密之后的密码重新补全到user对象中
		user.setPassword(md5Password);
		
		//数据补全操作
		//记录盐值
		user.setSalt(salt);
		//is_delete设置为0
		user.setIsDelete(0);
		//4个日志字段信息
		user.setCreatedUser(user.getUsername());
		user.setModifiedUser(user.getUsername());
		Date date = new Date();
		user.setCreatedTime(date);
		user.setModifiedTime(date);
		
		//执行注册业务功能的实现(rows==1)
		Integer rows = userMapper.insert(user);
		if (rows !=1){
			throw new InsertException("在用户注册过程中产生了未知的异常");
		}
	}
	
	@Override
	public User login(String username, String password) {
		User result = userMapper.findByUsername(username);
		if (result == null){
			throw new UserNotFoundException("用户数据不存在");
		}
		//检测用户密码是否匹配
		//1. 获取加密后密码
		String md5Password = getMD5Password(password,result.getSalt());
		
		if (!md5Password.equals(result.getPassword())){
			throw new PasswordNotMatchException("用户密码错误");
		}
		//判断isDelete只是否为1(被注销)
		if (result.getIsDelete()==1){
			throw new UserNotFoundException("用户数据不存在");
		}
		
		//封装User对象,前后端传输数据体量更小,提升系统的性能
		User user = new User();
		user.setUid(result.getUid());
		user.setUsername(result.getUsername());
		user.setAvatar(result.getAvatar());
		
		//返回的User对象是为了辅助其他页面做数据展示使用的(uid,username.avatar)
		return user;
	}
	
	@Override
	public void changePassword(Integer uid,
	                           String username,
	                           String oldPassword,
	                           String newPassword) {
		User result = userMapper.findByUid(uid);
		if (result == null || result.getIsDelete() == 1){
			throw new UserNotFoundException("用户数据不存在");
		}
		//原始密码和数据库密码进行比较
		String oldMd5Password = getMD5Password(oldPassword,result.getSalt());
		if (!result.getPassword().equals(oldMd5Password)){
			throw new PasswordNotMatchException("密码错误");
		}
		//将新的密码是设置数据库中,将新的密码加密再去更新
		String newMd5Password = getMD5Password(newPassword, result.getSalt());
		Integer rows = userMapper.updatePasswordByUid(uid,newMd5Password,username,new Date());
		if (rows != 1){
			throw new UpdateException("更新数据时产生未知的异常");
		}
	}
	
	@Override
	public User getByUid(Integer uid) {
		User result = userMapper.findByUid(uid);
		if (result==null || result.getIsDelete()==1){
			throw new UserNotFoundException("用户数据不存在");
		}
		//封装数据
		User user = new User();
		user.setUsername(result.getUsername());
		user.setPhone(result.getPhone());
		user.setEmail(result.getEmail());
		user.setGender(result.getGender());
		
		return user;
	}
	
	/**
	 * User对象中的数据只有从前端传递过来的phone,email和gender.需要手动讲uid和username封装到对象中
	 */
	@Override
	public void changeInfo(Integer uid, String username, User user) {
		User result = userMapper.findByUid(uid);
		if (result==null || result.getIsDelete()==1){
			throw new UserNotFoundException("用户数据不存在");
		}
		user.setUid(uid);
		//user.setUsername(username);(可省)
		user.setModifiedUser(username);
		user.setModifiedTime(new Date());
		
		Integer rows = userMapper.updateInfoByUid(user);
		if (rows != 1){
			throw new UpdateException("更新数据时产生未知的异常");
		}
	}
	
	
	@Override
	public void changeAvatar(Integer uid, String avatar, String username) {
		User result = userMapper.findByUid(uid);
		if (result==null || result.getIsDelete()==1){
			throw new UserNotFoundException("用户数据不存在");
		}
		Integer rows = userMapper.updateAvatarByUid(uid, avatar, username, new Date());
		if (rows != 1){
			throw new UpdateException("修改用户头像时产生未知的异常");
		}
	}
	
	//定义一个MD5算法的加密
	private String getMD5Password(String password, String salt){
		for (int i = 0; i < 3; i++){
			//md5加密算法方法的调用
			password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
		}
		//返回加密之后的密码
		return password;
	}
}

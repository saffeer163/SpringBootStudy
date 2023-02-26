package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Controller
@RestController //等效于@Controller+@ResponseBody
@RequestMapping("users")
public class UserController extends BaseController{
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 约定大于配置:开发思想来完成,省略的大量的配置甚至注解来编写
	 * 1. 接收数据方式: 请求处理方法的参数列表设置为pojo类型来接受前端的数据.
	 * Springboot会将前端中的url的之中的参数名和pojo类中的属性名进行比较,
	 * 如果这两个名称相同,则将值注入到pojo类中对应的属性上
	 */
	@RequestMapping("reg")
	//@ResponseBody //表示此方法得相应结果以json格式进行数据的响应
	public JsonResult<Void> reg(User user){
		userService.reg(user);
		return new JsonResult<Void>(OK);
	}
	
	@RequestMapping("change_password")
	public JsonResult<Void> changePassword(String oldPassword,
	                                       String newPassword,
	                                       HttpSession session){
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		userService.changePassword(uid, username, oldPassword, newPassword);
		System.out.println("更新执行成功");
		//清空session,预防非法操作
		session.setAttribute("uid",0);
		session.setAttribute("username",null);
		
		return new JsonResult<Void>(OK);
	}
	/**
	 * 2. 接收数据方式: 请求处理方法的参数列表设置为非pojo类型.
	 * Springboot会直接将请求的参数名和方法的参数名直接进行比较
	 * 如果这两个名称相同,则自动完成值的依赖注入
	 */
	//session对象的获取:SpringBoot能够直接使用session对象,只需要将HttpSession类型的对象作为请求处理方法的参数
	//就会自动将全局session对象注入到请求处理方法的session形参上
	@RequestMapping("login")
	public JsonResult<User> login(String username, String password, HttpSession session){
		User data = userService.login(username,password);
//		将用户数据绑定到session对象
		session.setAttribute("uid",data.getUid());
		session.setAttribute("username",data.getUsername());
		
		//test:获取session中绑定的数据
//		System.out.println(getUidFromSession(session));
//		System.out.println(getUsernameFromSession(session));
		return new JsonResult<User>(OK,data);
	}
	
	@RequestMapping("get_by_uid")
	public JsonResult<User> getByUid(HttpSession session){
		User data = userService.getByUid(getUidFromSession(session));
		return new JsonResult<>(OK,data);
	}
	
	@RequestMapping("change_info")
	public JsonResult<Void> changeInfo(User user,
	                                   HttpSession session){
		//user对象中有三部分数据:username,phone,email,gender
		//uid数据需要再次封装到user对象中
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		userService.changeInfo(uid, username,user);
		return new JsonResult<>(OK);
	}
	
	//设置上传文件的最大值
	public static final int AVATAR_MAX_SIZE = 10*1024*1024;
	//限制上传文件类型
	public static final List<String> AVATAR_TYPE = new ArrayList<>();
	static{
		AVATAR_TYPE.add("image/jpeg");
		AVATAR_TYPE.add("image/png");
		AVATAR_TYPE.add("image/bmp");
		AVATAR_TYPE.add("image/gif");
	}
	/**
	 * MultipartFile接口是SpringMVC提供的一个接口,
	 * 这个接口为我们包装了获取文件类型的数据(任何类型的file都可以接收)
	 * Springboot又整合了SpringMVC,只需要在处理请求方法的参数列表上声明一个参数类型为MultipartFile的参数
	 * 然后SpringBoot会自动将传递给服务的文件数据赋值,赋值给这个参数
	 *
	 *  @RequestParam(""): 表示请求中的参数,将这个请求的参数注入请求处理方法的某个参数上.
	 *  如果名称不一致,可以使用 @RequestParam注解进行标记和映射
	 * @param session
	 * @param file
	 * @return
	 */
	@RequestMapping("change_avatar")
	public JsonResult<String> changeAvatar(HttpSession session,
	                                       @RequestParam("file") MultipartFile file){
		//判断文件是否为空
		if (file.isEmpty()){
			throw new FileEmptyException("文件为空");
		}
		//判断文件大小
		if (file.getSize() > AVATAR_MAX_SIZE){
			throw new FileSizeException("文件大小超出限制");
		}
		//判断文件类型
		String contentType = file.getContentType();
		if (!AVATAR_TYPE.contains(contentType)){
			throw new FileTypeException("不支持的文件类型");
		}
		
		//存放文件
		String parent = "E:/StoreProject/upload/";
		//File对象指向这个路径,File是否存在
		File dir = new File(parent);
		if (!dir.exists()){//检测目录是否存在
			dir.mkdirs();//创建当前的目录
		}
		//获取到这个文件的名称,UUID工具将随机生成一个字符串作为文件名
		String originalFilename = file.getOriginalFilename();
		//test
		System.out.println("原文件名:"+originalFilename);
		//获取文件后缀
		int index = originalFilename.indexOf(".");
		String suffix = originalFilename.substring(index);
		//拼接出新的文件名
		String filename = UUID.randomUUID().toString().toUpperCase() + suffix;
		
		//创建空文件
		File dest = new File(dir, filename);
		//将参数file中的数据写入空文件中
		try {
			file.transferTo(dest);
		} catch (FileStateException e) {
			throw new FileStateException("文件状态异常");
		} catch (IOException e) {
			throw new FileUploadIOException("文件读写异常");
		}
		System.out.println("生成文件名:"+filename);
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		//返回头像路径 /upload/test.png
		String avatar = "/upload/"+ filename;
		userService.changeAvatar(uid,avatar,username);
		//返回用户头像路径给前端将来用于头像的展示
		return new JsonResult<>(OK,avatar);
	}
	
//	@Autowired
//	private IUserService userService;
//
//	@RequestMapping("reg")
//	//@ResponseBody //表示此方法得相应结果以json格式进行数据的响应
//	public JsonResult<Void> reg(User user){
//		//创建相应结果对象
//		JsonResult<Void> result = new JsonResult<>();
//		try {
//			userService.reg(user);
//			result.setState(200);
//			result.setMessage("用户注册成功");
//		} catch (UsernameDuplicatedException e) {
//			result.setState(4000);
//			result.setMessage("用户名被占用");
//		}catch (InsertException e) {
//			result.setState(5000);
//			result.setMessage("注册时产生未知的异常");
//		}
//		return result;
//	}
}

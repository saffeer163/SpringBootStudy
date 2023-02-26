package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;


//控制层类的基类
public class BaseController {
	//操作成功的状态码
	public static final int OK = 200;
	
	//请求处理方法,这个方法的返回值就是需要传递给前端的数据
	//@ExceptionHandler:用于统一处理抛出的异常,会自动将异常对象传递给此方法的参数列表上
	//当项目中产生了异常,会被统一拦截到此方法中,这个方法此时就充当的是请求处理方法,方法的返回值直接给到前端
	@ExceptionHandler({ServiceException.class,FileUploadException.class})
	public JsonResult<Void> handleException(Throwable e){
		JsonResult<Void> result = new JsonResult<>(e);
		if (e instanceof UsernameDuplicatedException){
			result.setState(4000);
			result.setMessage("用户名被占用");
		} else if (e instanceof UserNotFoundException) {
			result.setState(4001);
			result.setMessage("用户数据不存在");
		} else if (e instanceof PasswordNotMatchException) {
			result.setState(4002);
			result.setMessage("用户名的密码错误");
		} else if (e instanceof AddressCountLimitException) {
			result.setState(4003);
			result.setMessage("用户收货地址超出上限");
		} else if (e instanceof AddressNotFoundException) {
			result.setState(4004);
			result.setMessage("未查询到此地址");
		} else if (e instanceof AccessDeniedException) {
			result.setState(4005);
			result.setMessage("非法访问");
		} else if (e instanceof ProductNotFoundException) {
			result.setState(4006);
			result.setMessage("未查询到商品");
		} else if (e instanceof CartNotFoundException) {
			result.setState(4007);
			result.setMessage("未查询到此购物车信息");
		}else if (e instanceof InsertException) {
			result.setState(5000);
			result.setMessage("注册时产生未知的异常");
		} else if (e instanceof UpdateException) {
			result.setState(5001);
			result.setMessage("修改时产生未知的异常");
		} else if (e instanceof DeleteException) {
			result.setState(5002);
			result.setMessage("删除时产生未知的异常");
		}else if (e instanceof FileEmptyException) {
			result.setState(6000);
			result.setMessage("文件为空的异常");
		}else if (e instanceof FileSizeException) {
			result.setState(6001);
			result.setMessage("文件大小的异常");
		}else if (e instanceof FileTypeException) {
			result.setState(6002);
			result.setMessage("文件为空的异常");
		}else if (e instanceof FileStateException) {
			result.setState(6003);
			result.setMessage("文件状态的异常");
		}else if (e instanceof FileUploadIOException) {
			result.setState(6004);
			result.setMessage("文件上传时IO的异常");
		}
		return result;
	}
	
	//封装session对象中数据的获取和数据的设置(当用户登录成功时设置session对象的数据,设置到全局session数据)
	
	/**
	 * 获取session中的uid
	 * @param session session对象
	 * @return 当前登陆用户的uid值
	 */
	protected final Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf(session.getAttribute("uid")
				.toString());
		
	}
	
	/**
	 * 获取当前登陆用户的username
	 * @param session session对象
	 * @return 当前登录用户的用户名
	 *HttpSession 在实现类中重写了父类中的toString(),不是句柄信息的输出
	 *
	 */
	protected final String getUsernameFromSession(HttpSession session){
		return session.getAttribute("username").toString();
	}
}

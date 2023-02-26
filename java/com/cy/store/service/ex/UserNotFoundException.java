package com.cy.store.service.ex;

//密码与用户名不匹配的异常
public class UserNotFoundException extends ServiceException{
	public UserNotFoundException() {
		super();
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UserNotFoundException(Throwable cause) {
		super(cause);
	}
	
	protected UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

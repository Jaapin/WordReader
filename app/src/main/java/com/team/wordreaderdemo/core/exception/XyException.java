package com.team.wordreaderdemo.core.exception;

/**
 * 所有自定义异常的超类,xy是xiao yun的的首字母
 */
public class XyException extends RuntimeException {

	private static final long serialVersionUID = -2097465532154739597L;

	/**
	 *
	 * @param message 错误信息
     */
	public XyException(String message){
		super(message);
	}

	/**
	 *
	 * @param message 错误信息
	 * @param e 原始异常
     */
	public XyException(String message, Exception e){
		super(message,e);
	}

}

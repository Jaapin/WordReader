package com.team.wordreaderdemo.core.exception;

/**
 * 方法未实现时抛出该异常
 */
public class NotImplementedException extends XyException {
	private static final long serialVersionUID = -6384191391592436024L;
	private String message;

    public NotImplementedException() {
        super("方法未实现");
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        message = "方法未实现:"+stackTraces[2].getClassName()+"#"+stackTraces[2].getMethodName();
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }

}

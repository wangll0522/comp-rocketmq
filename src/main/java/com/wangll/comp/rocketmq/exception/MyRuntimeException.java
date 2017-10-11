package com.wangll.comp.rocketmq.exception;


public class MyRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -4138013499105708265L;
    protected SystemExceptionType exceptionType;

    public MyRuntimeException() {
        this.exceptionType = SystemExceptionType.UNKNOWN;
    }

    public MyRuntimeException(SystemExceptionType exceptionType) {
        this.exceptionType = SystemExceptionType.UNKNOWN;
        this.exceptionType = exceptionType;
    }

    public MyRuntimeException(String msg) {
        super(msg);
        this.exceptionType = SystemExceptionType.UNKNOWN;
    }

    public MyRuntimeException(String msg, SystemExceptionType exceptionType) {
        super(msg);
        this.exceptionType = SystemExceptionType.UNKNOWN;
        this.exceptionType = exceptionType;
    }

    public MyRuntimeException(Throwable cause) {
        super(cause);
        this.exceptionType = SystemExceptionType.UNKNOWN;
    }

    public MyRuntimeException(Throwable cause, SystemExceptionType exceptionType) {
        super(cause);
        this.exceptionType = SystemExceptionType.UNKNOWN;
        this.exceptionType = exceptionType;
    }

    public MyRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
        this.exceptionType = SystemExceptionType.UNKNOWN;
    }

    public MyRuntimeException(String msg, Throwable cause, SystemExceptionType exceptionType) {
        super(msg, cause);
        this.exceptionType = SystemExceptionType.UNKNOWN;
        this.exceptionType = exceptionType;
    }

    protected SystemExceptionType getExceptionType() {
        return this.exceptionType;
    }

    protected void setExceptionType(SystemExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}

package com.wangll.comp.rocketmq.exception;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.enums.ValuedEnum;

public class SystemExceptionType extends ValuedEnum {
    private static final long serialVersionUID = 6838092903039365633L;
    public static final SystemExceptionType UNKNOWN = new SystemExceptionType("UNKNOWN", 1);
    public static final SystemExceptionType SERVICE_EXCEPTION = new SystemExceptionType("SERVICE_EXCEPTION", 2);
    public static final SystemExceptionType CACHE_EXCEPTION = new SystemExceptionType("CACHE_EXCEPTION", 3);
    public static final SystemExceptionType WEB_EXCEPTION = new SystemExceptionType("WEB_EXCEPTION", 4);
    public static final SystemExceptionType SYSTEM_EXCEPTION = new SystemExceptionType("SYSTEM_EXCEPTION", 5);
    public static final SystemExceptionType Argument = new SystemExceptionType("Argument", 6);
    public static final SystemExceptionType TOKEN_EXCEPTION = new SystemExceptionType("TOKEN_EXCEPTION", 7);

    protected SystemExceptionType(String name, int value) {
        super(name, value);
    }

    public static SystemExceptionType getEnum(int value) {
        return (SystemExceptionType)getEnum(SystemExceptionType.class, value);
    }

    public static SystemExceptionType getEnum(String name) {
        return (SystemExceptionType)getEnum(SystemExceptionType.class, name);
    }

    public static Map getEnumMap() {
        return getEnumMap(SystemExceptionType.class);
    }

    public static List getEnumList() {
        return getEnumList(SystemExceptionType.class);
    }

    public static Iterator iterator() {
        return iterator(SystemExceptionType.class);
    }
}

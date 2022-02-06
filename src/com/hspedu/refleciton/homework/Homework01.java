package com.hspedu.refleciton.homework;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Zhang Yu
 * @version 1.0
 */
public class Homework01 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //1. 创建类
        Class<PrivateTest> privateTestCls = PrivateTest.class;
        Object o = privateTestCls.newInstance();
        Field name = privateTestCls.getDeclaredField("name");
        //暴破
        name.setAccessible(true);
        name.set(o, "helloKitty2");
        Method getName = privateTestCls.getMethod("getName");
        System.out.println(getName.invoke(o));




    }
}

class PrivateTest {
    private String name = "hellokitty";
    public String getName() {
        return name;
    }
}
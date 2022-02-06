package com.hspedu.refleciton;

import com.hspedu.Cat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Zhang Yu
 * @version 1.0
 */
public class Reflection02 {
    public static void main(String[] args) throws Exception{
        // Field
        // Method
        // Constructor
        m1();
        m2();
        m3();
    }

    //传统方法调用hi
    public static void m1() {
        Cat cat = new Cat();
        long start = System.currentTimeMillis();
        for(int i = 0; i < 900000000; i++) {
            cat.hi();
        }
        long end = System.currentTimeMillis();
        System.out.println("m1 耗时=" + (end - start));
    }

    //反射机制调用方法hi
    public static void m2() throws Exception {
        Class cls = Class.forName("com.hspedu.Cat");
        Object o = cls.newInstance();
        Method hi = cls.getMethod("hi");

        long start = System.currentTimeMillis();
        for(int i = 0; i < 900000000; i++) {
            hi.invoke(o);
        }
        long end = System.currentTimeMillis();
        System.out.println("m2 耗时=" + (end - start));
    }

    //反射调用优化：关闭访问检查
    public static void m3() throws Exception {
        Class cls = Class.forName("com.hspedu.Cat");
        Object o = cls.newInstance();
        Method hi = cls.getMethod("hi");
        hi.setAccessible(true);//在反射调用方法时，取消访问检查

        long start = System.currentTimeMillis();
        for(int i = 0; i < 900000000; i++) {
            hi.invoke(o);
        }
        long end = System.currentTimeMillis();
        System.out.println("m3 耗时=" + (end - start));
    }

}

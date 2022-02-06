package com.hspedu.refleciton.class_;

/**
 * @author Zhang Yu
 * @version 1.0
 *
 */

import java.util.*;
import java.lang.reflect.*;

public class ClassLoader_ {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入key");
        String key = scanner.next();
        switch (key) {
            case "1":
                //静态加载，在编译时就会检查，如果没有该类，则会报错
                Dog dog = new Dog();
                dog.cry();
                break;
            case "2":
                //反射-动态加载，在运行时运行到该代码，才会检查
                Class cls = Class.forName("Person");
                Object o = cls.newInstance();
                Method m = cls.getMethod("hi");
                m.invoke(o);
                System.out.println("ok");
                break;
            default:
                System.out.println("do nothing..");
        }
    }
}

class Dog {
    public void cry() {

    }
}




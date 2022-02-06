package com.hspedu.refleciton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Zhang Yu
 * @version 1.0
 * 通过反射机制创建实例
 */
public class ReflecCreateInstance {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //1. 先获取User类的Class对象
        Class<?> userClass = Class.forName("com.hspedu.refleciton.User");
        //2. 通过public的无参构造器创建实例
        Object o = userClass.newInstance();
        System.out.println(o);
        //3. 通过public的有参构造器创建实例
        Constructor<?> constructor = userClass.getConstructor(String.class);
        Object hsp = constructor.newInstance("hsp");
        System.out.println("hsp=" + hsp);
        //4. 通过非public的有参构造器创建实例
        //4.1 得到private的构造器对象
        Constructor<?> constructor1 = userClass.getDeclaredConstructor(int.class, String.class);
        //4.2 创建实例
        //暴破【暴力破解】，使用反射可以访问 private 构造器/方法/属性，反射面前，全部都是纸老虎
        constructor1.setAccessible(true);
        Object user2 = constructor1.newInstance(28, "张三丰");
        System.out.println("user2="+user2);

    }
}
class User {
    private int age = 100;
    private String name = "韩顺平教育";

    public User() {}//无参public

    public User(String name) {//有参public

    }

    private User(int age, String name) {//有参private
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
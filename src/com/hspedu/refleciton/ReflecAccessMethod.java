package com.hspedu.refleciton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Zhang Yu
 * @version 1.0
 * 通过反射调用方法
 */
public class ReflecAccessMethod {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //1. 得到Boss类对应的Class对象
        Class<?> bossCls = Class.forName("com.hspedu.refleciton.Boss");
        //2. 创建对象
        Object o = bossCls.newInstance();
        //3. 调用public方法
        //Method hi = bossCls.getMethod("hi", String.class);//OK
        //3.1 得到hi方法对象
        Method hi = bossCls.getDeclaredMethod("hi", String.class);//OK
        //3.2 调用
        hi.invoke(o, "韩顺平教育~");
        //4. 调用private static 方法
        //4.1 得到 say 方法对象
        Method say = bossCls.getDeclaredMethod("say", int.class, String.class, char.class);
        //4.2 因为 say 方法是 private，所以需要暴破，原理和前面讲的构造器和属性一样
        say.setAccessible(true);
        System.out.println(say.invoke(o, 100, "张三", '男'));
        //4.3 因为say方法是 static的，还可以这样调用
        System.out.println(say.invoke(null, 200, "李四", '女'));

        //5. 在反射中，如果方法有返回值，统一返回Object，但是他运行类型和方法定义的返回类型一致
        Object reVal = say.invoke(null, 300, "王五", '男');
        System.out.println("reVal 的运行类型="+ reVal.getClass());

    }
}

class Boss {
    public int age;
    private static String name;

    public Boss() {//构造器}
    }

    //静态方法
    private static String say(int n, String s, char c) {
        return n + " " + s + " " + c;
    }

    //普通方法
    public void hi(String s) {
        System.out.println("hi" + s);
    }
}

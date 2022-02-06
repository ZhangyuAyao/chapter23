package com.hspedu.refleciton.question;

import com.hspedu.Cat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Zhang Yu
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class ReflectionQuestion {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //根据配置文件 re.properties 指定信息，创建Cat对象并调用hi

        //传统的方式 new 对象 -》 调用方法
        Cat cat = new Cat();
        cat.hi();

        //我们尝试做一做 -》 明白反射的价值

        //1. 使用 Properties 类，可以读写配置文件
        Properties properties = new Properties();
        properties.load(new FileInputStream("src\\re.properties"));
        String classfullpath = properties.get("classfullpath").toString();//"com.hspedu.Cat"
        String methodName = properties.get("method").toString();//"hi"
        System.out.println("classfullpath=" + classfullpath);
        System.out.println("method=" + methodName);

        //2. 创建对象，传统的方法，行不通 =》 使用反射机制
        //new classfullpath
        //cat.hi() ====> cat.cry() 如果要改成cry需要修改源码
        //如果使用反射则不需要修改源码，只需要修改配置文件

        //3. 使用反射机制解决问题
        //(1)加载类，返回Class类型的对象
        Class cls = Class.forName(classfullpath);
        //(2)通过 cls 得到你加载的类 com.hspedu.Cat 的对象实例
        Object o = cls.newInstance();
        System.out.println("o的运行类型=" + o.getClass()); //运行类型
        //(3)通过 cls 得到你加载的类 com.hspedu.Cat 的 methodName"hi" 的方法对象
        // 即：在反射中，可以把方法视为对象（万物皆对象）
        Method method1 = cls.getMethod(methodName);
        //(4)通过method1 调用方法：即通过方法对象来实现调用方法
        System.out.println("===================================");
        method1.invoke(o);//传统方法 对象.方法()，反射机制 方法.invoke(对象)



    }
}

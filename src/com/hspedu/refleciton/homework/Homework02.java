package com.hspedu.refleciton.homework;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Zhang Yu
 * @version 1.0
 */
public class Homework02 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        //得到File的Class对象
        Class<?> fileClass = Class.forName("java.io.File");
        //遍历构造器
        Constructor<?>[] declaredConstructors = fileClass.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }
        //新建File对象
        Constructor<?> constructor = fileClass.getConstructor(String.class);
        Object file = constructor.newInstance("c:\\ioTest\\reflection.txt");
        //调用createNewFile方法
        Method createNewFile = fileClass.getMethod("createNewFile");
        createNewFile.invoke(file);

        System.out.println(file.getClass());
        System.out.println("创建文件成功 c:\\ioTest\\reflection.txt");

    }
}

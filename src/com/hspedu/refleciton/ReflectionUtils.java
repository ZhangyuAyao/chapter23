package com.hspedu.refleciton;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Zhang Yu
 * @version 1.0
 */
public class ReflectionUtils {
    public static void main(String[] args) throws ClassNotFoundException {
        //得到class对象
        Class<?> personCls = Class.forName("com.hspedu.refleciton.Person");
        //1. getName:获取全类名
        System.out.println(personCls.getName());//com.hspedu.refleciton.Person
        //2. getSimpleName：获取简单类名
        System.out.println(personCls.getSimpleName());//Person
        //3. getFields：获取所有 public 修饰的属性，包含本类以及父类的
        Field[] fields = personCls.getFields();
        for (Field field : fields) {
            System.out.println("本类以及父类的属性"+field.getName());
        }
        //4. getDeclaredFields：获取本类中所有属性
        Field[] declaredFields = personCls.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("本类中所有属性"+declaredField.getName());
        }
        //5. getMethods:获取所有 public 修饰的方法，包含本类以及父类的
        Method[] methods = personCls.getMethods();
        for (Method method : methods) {
            System.out.println("本类以及父类的method="+method.getName());
        }
        //6. getDeclaredMethods:获取本类中所有方法
        Method[] declaredMethods = personCls.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println("本类中所有方法="+declaredMethod.getName());
        }
        //7. getConstructors:获取所有public 修饰的构造器，包含本类以及父类的
        Constructor<?>[] constructors = personCls.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("本类以及父类的构造器="+constructor.getName());
        }
        //8. getDeclaredConstructors 获取本类中所有构造器
        Constructor<?>[] declaredConstructors = personCls.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println("本类中所有构造器="+declaredConstructor.getName());
        }
        //9. getPackage:以 Package 形式返回 包信息
        System.out.println(personCls.getPackage());//package com.hspedu.refleciton
        //10. getSuperClass: 以 Class 形式返回父类信息
        Class<?> superclass = personCls.getSuperclass();
        System.out.println("父类的class对象="+superclass);
        //11. getSuperClass: 以 Class[] 形式返回接口信息
        Class<?>[] interfaces = personCls.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println("接口信息=" + anInterface);
        }
        //12. getAnnotations: 以 Annotation[] 形式返回注解信息
        Annotation[] annotations = personCls.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("注解信息="+annotation);//注解
        }
        Person person = new Person();
        person.m3();


    }

    @Test
    public void api_02() throws ClassNotFoundException {
        //得到class对象
        Class<?> personCls = Class.forName("com.hspedu.refleciton.Person");
        //4. getDeclaredFields：获取本类中所有属性
        //规定 说明：默认修饰符 是0，public 是 1， private 是 2， protected 是 4， static 是8
        //组合的话就是加起来
        Field[] declaredFields = personCls.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("本类中所有属性"+declaredField.getName()
            + " 该属性的修饰符值="+declaredField.getModifiers()
            + " 该属性的类型="+ declaredField.getType());
        }

        //6. getDeclaredMethods:获取本类中所有方法
        Method[] declaredMethods = personCls.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println("本类中所有方法="+declaredMethod.getName()
            + " 该方法的访问修饰符值=" + declaredMethod.getModifiers()
            + " 该方法返回类型" + declaredMethod.getReturnType());

            //输出当前这个方法的形参数组情况
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.println("该方法的形参类型="+parameterType);
            }
        }

        //8. getDeclaredConstructors 获取本类中所有构造器
        Constructor<?>[] declaredConstructors = personCls.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println("======================");
            System.out.println("本类中所有构造器="+declaredConstructor.getName());

            Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.println("该构造器的形参类型="+parameterType);
            }

        }



    }
}
class A {
    public String hobby;
    public void hi() {

    }

    public A(){}

}

interface IA {

}

interface IB {

}

@Deprecated
class Person extends A implements IA, IB{
    //属性
    public String name;
    protected static int age;
    String job;
    private double sal;

    //构造器
    public Person(){}

    public Person(String name){}
    //私有
    private Person(String name, int age){}

    //方法
    public void m1() {

    }

    protected void m2() {

    }

    void m3() {

    }


}
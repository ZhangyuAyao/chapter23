# 反射
![image.png](https://note.youdao.com/yws/res/a/WEBRESOURCE19df2c69c7283951fc334733b116d50a)
## 一个需求引出反射
1. 根据配置文件 re.properties 指定信息，创建 Cat 对象并调用方法 hi
   classfullpath=com.hspedu.Cat
   method=hi

- 思考：根据现有技术，能做到吗？

```
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
```

2. 这样的需求在学习框架时特别多，==即通过外部文件配置，在不修改源码情况下来控制程序==，也符合设计模式的OCP原则（开闭原则：不修改源码，扩容功能）

## 反射机制 Java Reflection
1. 反射机制允许程序在执行期借助于Reflection API 取得任何类的内部信息（比如成员变量，构造器，成员方法等等），并能操作对象的属性及方法。反射在设计模式和框架底层都会用到

2. 加载完类之后，在堆中就产生了一个 Class 类型的对象（一个类只有一个 Class 对象），这个对象包含了类的完整结构信息。通过这个对象得到类的结构。这个 Class 对象就像是一面镜子，透过这个镜子看到类的结构，所以，形象的称之为：反射

### Java 程序在计算机有三个阶段
1. 代码阶段/编译阶段
2. Class 类阶段（加载阶段）
3. Runtime 运行阶段
- **Java反射机制原理图**（==重要==）
  ![image.png](https://note.youdao.com/yws/res/9/WEBRESOURCEf539ff89dc0029fd66eb334985972df9)

### java反射机制可以完成
1. 在运行时判断任意一个对象所属的类
2. 在运行时构造任意一个类的对象
3. 在运行时得到任意一个类所具有的成员变量和方法
4. 在运行时调用任意一个对象的成员变量和方法
5. 生成动态代理

### 反射相关的主要类
1. java.lang.Class: 代表一个类，Class对象表示某个类加载后在堆中的对象
2. java.lang.reflect.Method:代表累的方法，Method对象表示某个类的方法
3. java.lang.reflect.Field:代表类的成员变量，Field对象表示某个类的成员变量
4. java.lang.reflect.Constructor:代表类的构造方法，Constructor对象表示构造器

### 反射的优点和缺点
1. 优点：可以动态的创建和使用对象（也是框架底层核心），使用灵活，没有反射机制，框架技术就失去底层支撑
2. 缺点：使用反射基本是解释执行，对执行速度有影响

> 计算程序耗时：System.currentTimeMillis()方法得到当前系统时间，得到程序的start 和 end 的时间相减即可

### 反射调用优化-关闭访问检查
1. Method 和 Field、Constructor对象都有 setAccessible()方法
2. setAccessible作用是启动和禁用访问安全检查的开关
3. 参数值为 true 表示反射的对象在使用时取消访问检查，提高反射的效率。参数值为 false 则表示反射的对象执行执行访问检查

```
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
```

## Class类
![image.png](https://note.youdao.com/yws/res/6/WEBRESOURCE128e2b54db6e76a7d53858041bf51fa6)
1. Class 也是类，因此也继承 Object 类 [类图]

2. Class 类对象不是 new 出来的，而是系统创建的

3. 对于某个类的 Class 类对象，在内存中只有一份，因为类只加载一次

4. 每个类的实例都会记得自己是由哪个 Class 实例所生成

5. 通过 Class 可以完整地得到一个类的完整结构，通过一系列API

6. Class对象是存放在堆的

7. 类的字节码二进制数据，是放在方法区的，有的地方称为类的元数据（包括 方法代码，变量名，方法名，访问权限等等）
   ![image.png](https://note.youdao.com/yws/res/9/WEBRESOURCEa93d0bd61cc84e158d0774c5f90ad649)

### Class类常用方法
1. 获取 Car 类的 Class对象：Class.forName(classAllPath)

2. 输出cls类型

3. 得到包名：cls.getPackage().getName()

4. 得到全类名：cls.getName()

5. 通过cls创建对象实例：cls.newInstance()

6. 通过反射获取属性：
   Field brand = cls.getField("brand");
   brand.get(car);

7. 通过反射给属性赋值
   brand.set(car, "奔驰");
   brand.get(car);

8. 得到所有的属性
   Field[] fields = cls.getFields();
   for (Field f : fields) {
   f.getName();
   }



```
    String classAllPath = "com.hspedu.Car";
    //1. 获取Car类 对应的 Class对象
    //<?>表示不确定的Java类型
    Class<?> cls = Class.forName(classAllPath);
    //2. 输出cls
    System.out.println(cls); //显示 cls 对象，是哪个类的Class对象 com.hspedu.Car
    System.out.println(cls.getClass());//输出cls运行类型 java.lang.Class
    //3. 得到包名
    System.out.println(cls.getPackage().getName());//包名
    //4. 得到全类名
    System.out.println(cls.getName());//com.hspedu.Car
    //5. 通过cls创建对象实例
    Car car = (Car) cls.newInstance();
    System.out.println(car);//car.toString
    //6. 通过反射获取属性
    Field brand = cls.getField("brand");
    System.out.println(brand.get(car));//宝马，如果是私有属性则会报错
    //7. 通过反射给属性赋值
    brand.set(car, "奔驰");
    System.out.println(brand.get(car));//奔驰
    //8. 我希望大家可以得到所有的属性（字段）
    Field[] fields = cls.getFields();
    for (Field f : fields) {
        System.out.println(f.getName());//名称
    }
```
### 获取Class类对象的六种方式
![image.png](https://note.youdao.com/yws/res/f/WEBRESOURCEeaa46f2f312ad7cad4cee6284f038fbf)
1. 前提：已知一个类的全类名，且该类在类路径下，可通过 Class 类的静态方法 forName() 获取，可能抛出 ClassNotFoundException, 实例：Class cls1 = Class.forName("java.lang.Cat");
   ==应用场景==：多用于配置文件，读取类全路径，加载类

2. 前提：若已知具体的类，通过类的 class 获取，该方式最为安全可靠，程序性能最高. 实例：Class cls2 = Cat.class
   ==应用场景==：多用于参数传递，比如通过反射得到对应构造器对象

3. 前提：已知某个类的实例，调用该实例的getClass()方法获取Class对象，实例：Class cls3 = 对象.getClass();
   ==应用场景==：通过创建好的对象，获取 Class 对象

4. 其它方式
   ClassLoader cl = 对象.getClass().getClassLoader();
   Class cls4 = cl.loadClass("类的全类名");

5. 基本数据(int, char, boolean, float, double, byte, long, short) 按如下方式得到 Class 类对象
   Class cls = 基本数据类型.class
6. 基本数据类型对应的包装类，可以通过 .type 得到 Class 类对象
   Class cls = 包装类.TYPE

```
    //1. Class.forName()
    String classAllPath = "com.hspedu.Cat";
    Class cls1 = Class.forName(classAllPath);
    System.out.println(cls1);

    //2. 类名.class，应用场景：用于参数传递
    Class cls2 = Car.class;
    System.out.println(cls2);

    //3. 对象.getClass()，应用场景，有对象实例
    Car car = new Car();
    Class cls3 = car.getClass();
    System.out.println(cls3);

    //4. 通过类加载器[4种]来获取到类的 Class 对象
    //(1)先得到类加载器 car
    ClassLoader classLoader = car.getClass().getClassLoader();
    //(2)通过类加载器得到Class 对象
    Class cls4 = classLoader.loadClass(classAllPath);
    System.out.println(cls4);

    //cls1,cls2,cls3,cls4 其实是同一个对象
    System.out.println(cls1.hashCode());
    System.out.println(cls2.hashCode());
    System.out.println(cls3.hashCode());
    System.out.println(cls4.hashCode());

    //5. 基本数据(int, char, boolean, float, double, byte, long, short)按入下方式获取
    Class<Integer> integerClass = int.class;
    Class<Character> characterClass = char.class;
    Class<Boolean> booleanClass = boolean.class;
    System.out.println(integerClass);//int

    //6. 基本数据类型对应的包装类 可以通过.TYPE获取Class 类对象
    Class<Integer> type1 = Integer.TYPE;
    Class<Character> type2 = Character.TYPE;//其他包装类BOOLEAN,DOUBLE,lONG
    System.out.println(type1);

    //基本数据类型 和 包装类的 Class 对象为同一个
    System.out.println(integerClass.hashCode());//?
    System.out.println(type1.hashCode());//?
```
### 哪些类型有 Class 对象
- 如下类型有 Class 对象
1. 外部类，成员内部类，静态内部类，局部内部类，匿名内部类
2. interface：接口
3. 数组
4. enum：枚举
5. annotation：注解
6. 基本数据类型
7. void
8. Class类

## 类加载
### 基本说明
反射机制是 java 实现动态语言的关键，也就是通过反射实现类动态加载
1. 静态加载：编译时加载相关的类，如果没有则报错，依赖性太强
2. 动态加载：运行时加载需要的类，如果运行时不用该类，则不报错，降低了依赖性
3. 举例说明

```
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
```
### 类加载时机
1. 当创建对象时（new) //静态加载
2. 当子类被加载时，父类也加载 //静态加载
3. 调用类中的静态成员时 //静态加载
4. 通过反射 //动态加载

### 类加载流程
![image.png](https://note.youdao.com/yws/res/b/WEBRESOURCE3b1d60b99f1bf933dc6b12f03dbd2e5b)

### 类加载
![image.png](https://note.youdao.com/yws/res/7/WEBRESOURCEaf50653233d710f13af04fea0f6542b7)
初始化阶段
#### 加载阶段：
JVM 在该阶段的主要目的是将字节码从不同的数据源（可能是 class 文件、也可能是 jar 包，甚至网络）转化为==二进制字节流加载到内存中==，并生成一个代表该类的java.lang.Class 对象
#### 连接阶段-Linking
- **验证**：
1. 目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。
2. 包含：文件格式验证（是否以魔数 oxcafebabe开头）、元数据验证、字节码验证和符号引用验证
3. 可以考虑使用 -Xverify:none 参数来关闭大部分的类验证措施，缩短虚拟机类加载的时间。

- **准备**
1. JVM 会在该阶段对静态变量，分配内存并默认初始化（对应数据类型的默认初始值，如0、0L、null、false等）。这些变量所使用的内存都将在方法去中进行分配

```
class A {
    //属性-成员变量-字段
    //老韩分析类加载的链接阶段-准备，属性时如何处理
    //1. n1 是实例属性，不是静态变量，因此在准备阶段，是不会分配内存
    //2. n2 是静态变量，分配内存 n2 是默认初始化 0 ，而不是20
    //3. n3 是static final 是常量，他和静态变量不一样，因为一旦赋值就不变了，n3 = 30
    public int n1 = 10;
    public static int n2 = 20;
    public static final int n3 = 30;
}
```
- **解析**
1. 虚拟机将常量池内的符号引用替换为直接引用的过程

#### Initialization 初始化
1. 到初始化阶段，才真正开始执行类中定义的 Java 程序代码，此阶段是执行 <clinit>() 方法的过程
2. <clinit>() 方法是由编译器按语句在源文件中出现的顺序，依次自动收集类中的所有==静态变量==的赋值动作和==静态代码块==中的语句，并进行合并。
3. 虚拟机会保证一个类的 <clinit>() 方法在多线程环境中正确地加锁、同步，如果多个线程同时去初始化一个类，那么只有一个线程去执行这个类的 <clinit>() 方法，其他线程都需要阻塞等待，直到活动线程执行 <clinit>() 方法完毕

```
public class ClassLoad03 {
    public static void main(String[] args) throws ClassNotFoundException {
        //老韩分析
        //1. 加载B类，并生成 B 的 class对象
        //2. 链接 num = 0
        //3. 初始化阶段
        // 依次自动收集类中的所有==静态变量==的赋值动作和静态代码块中的语句
        /*
         clinit() {
            System.out.println("B 静态代码块被执行");
            num = 300;
            num = 100;
         }
         合并：num = 100
         */

        //new B();//类加载
        //System.out.println(B.num);//100, 如果直接使用类的静态属性，也会导致类的加载

        //看看加载类的时候，是否有同步机制控制
        /*
        protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
        {
        //正因为有这个机制，才能保证某个类在内存中只有一份 class 对象
        synchronized (getClassLoadingLock(name)) {
        //...
        }
         */
        B b = new B();
    }
}

class B {
    static {
        System.out.println("B 静态代码块被执行");
        num = 300;
    }

    static int num = 100;
    public B() {//构造器
        System.out.println("B() 构造器被执行");
    }
}
```

代码分析：
1. 加载B类，并生成 B 的 class对象
2. 链接 num = 0
3. 初始化阶段 num = 100

## 通过反射获取类的结构信息
### 第一组：java.lang.Class类
1. getName:获取全类名
2. getSimpleName：获取简单类名
3. getField：获取所有 public 修饰的属性，包含本类以及父类的
4. getDeclaredFields：获取本类中所有属性
5. getMethod:获取所有 public 修饰的方法，包含本类以及父类的
6. getDeclaredMethods:获取本类中所有方法
7. getConstructors:获取本类中所有构造器
8. getPackage:以 Package 形式返回 包信息
9. getSuperClass: 以 Class 形式返回父类信息
10. getSuperClass: 以 Class[] 形式返回接口信息
11. getAnnotations: 以 Annotation[] 形式返回注解信息

```
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
        //7. getConstructors:获取本类的所有 public 修饰的构造器
        Constructor<?>[] constructors = personCls.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("本类的构造器="+constructor.getName());
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

    }
}
class A {
    public String hobby;
    
    public void hi() {}

    public A(){}

}
interface IA {}

interface IB {}

@Deprecated
class Person extends A implements IA, IB{
    //属性
    public String name;
    protected int age;
    String job;
    private double sal;

    //构造器
    public Person(){}

    public Person(String name){}
    //私有
    private Person(String name, int age){}

    //方法
    public void m1() {}

    protected void m2() {}

    void m3() {}
}
```
### 第二组：java.lang.reflect.Field类
1. getModifiers:以 int 形式返回修饰符
   [说明：默认修饰符是 0， public 是1， private是2，protected是4，static是8，final是16]
   如果是 public static 类型,则为public(1) + static(9) = 10
2. getType：以 Class 形式返回类型
3. getName：返回属性名


```
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
```


### 第三组：java.lang.reflect.Method类
1. getModifiers:以 int 形式返回修饰符
   [说明：默认修饰符是 0， public 是1， private是2，protected是4，static是8，final是16]
2. getReturnType：以 Class 形式返回 返回类型
3. getName：返回方法名
4. getParameterTypes: 以 Class[] 返回参数类型数组

```
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
```

### 第四组：java.lang.reflect.Constructor类
1. getModifiers: 以int形式返回修饰符
2. getName：返回构造器名（全类名）
3. getParameterTypes：以 Class[] 返回参数类型数组

### 通过反射创建对象
1. 方式一：调用类中的public修饰的无参构造器
2. 方式二：调用类中的指定构造器
3. Class类相关方法
- newInstance：调用类中的无参构造器，获取对应类的对象
- getConstructor(Class...clazz): 根据参数列表，获取对应的public构造器对象
- getDecalaredConstructor(Class...clazz): 根据参数列表，获取对应的所有构造器对象
4. Constructor 类相关方法
- setAccessible：暴破
- newInstance（Object...obj): 调用构造器


```
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
```
### 通过反射访问类中的成员
- **访问属性**
1. 根据属性名获取Field对象
   Field f = clazz对象.getDeclaredField(属性名);

2. 暴破：f.setAccessible(true); //f是Field
3. 访问
   f.set(o, 值); //o表示对象
   syso(f.get(o); //o表示对象
4. 注意：如果是静态属性，则 set 和 get 中的参数 o，可以写成 null

```
public class ReflecAccessProperty {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //1. 得到Student类对应的Class对象
        Class<?> stuClass = Class.forName("com.hspedu.refleciton.Student");
        //2. 创建对象
        Object o = stuClass.newInstance();//o 的运行类型就是Student
        System.out.println(o.getClass());//Student
        //3. 使用反射得到age 属性对象
        Field age = stuClass.getField("age");
        age.set(o, 88);//通过反射来操作属性
        System.out.println(o);//
        System.out.println(age.get(o));//返回age属性的值

        //4. 使用反射操作name 属性
        Field name = stuClass.getDeclaredField("name");
        //对name 进行暴破，可以操作private 属性
        name.setAccessible(true);
        //name.set(o, "老韩");
        name.set(null, "老韩~");//因为name是static属性，因此 o 也可以写成null
        System.out.println(o);
        System.out.println(name.get(o));//获取属性值
        System.out.println(name.get(null));//获取属性值，要求name是static
    }
}

class Student {
    public int age;
    private static String name;//静态私有

    public Student(){//构造器

    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
```

- **访问方法**
1. 根据方法名和参数列表获取 Method 方法对象：Method m = clazz.getDeclaredMethod(方法名，XX.class);

2. 获取对象：Object o = clazz.newInstance();

3. 暴破：m.setAccessible(true);

4. 访问：Object returnValue = m.invoke(o, 实参列表)；

5. 注意：如果是静态方法，则invoke的参数o，可以写成null！

> getMethod只能获取public方法，getDeclaredMethod能获取所有的方法

```
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

```

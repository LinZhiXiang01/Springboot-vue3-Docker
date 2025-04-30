package com.mocha.springboot.MyTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {
    public String name;
    private int price;

    public ReflectionTest(String str) {
        System.out.println("公开构造函数:" + str);
    }
    public void sayHello(){
        System.out.println("hello");
    }
    public int getPrice(){
        return price;
    }
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<ReflectionTest> clazz = ReflectionTest.class;

        //获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        //打印属性信息
        System.out.println(fields);

        //打印所有属性名
        for(Field field:fields){
            System.out.println(field.getName());
        }

        // 获取构造函数,此处为有参构造函数
        Constructor constructor = clazz.getDeclaredConstructor(String.class);

        //打印构造函数信息
        System.out.println(constructor);
        Object obj = constructor.newInstance("hello我是构造函数");


        Method[] methods = clazz.getMethods();
        for(Method method:methods){
            System.out.println(method.getName());
        }
        methods[1].invoke(obj);
        System.out.println(methods[2].invoke(obj));
    }
}

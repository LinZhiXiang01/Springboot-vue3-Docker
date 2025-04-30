package com.mocha.springboot.MyTest;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings(value = {"unused", "rawtypes"})
public class StandardDemo extends Parent{

    @Override
    public void sayHello() {
        // unused 声明了list却没有使用
        // rawtypes 创建了泛型类却没有指定元素类型
        List list = new ArrayList();
    }

    @Deprecated
    public void walk() {

    }
    public static void main(String[] args) {
        StandardDemo standardDemo = new StandardDemo();
        standardDemo.walk();
    }
}

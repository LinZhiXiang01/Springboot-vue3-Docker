package com.mocha.springboot.MyTest;

import java.lang.annotation.Annotation;

public class Children extends Parent{

    public static void main(String[] args) {
        Class<Children> children = Children.class;
        Annotation[] annotations = children.getAnnotations();
        for(Annotation annotation:annotations){
            System.out.println(annotation);
        }
    }

    @Override
    public void sayHello() {

    }
}

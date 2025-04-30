package com.mocha.springboot.MyAnnotationsReflectionTest;
import java.lang.annotation.Annotation;
import java.util.Arrays;

@AnyAnnotation(order = 1, description = "我是类上的注释")
public class ReflectAnnotationTest {
    public static void main(String[] args) throws Exception {

        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

        Class<ReflectAnnotationTest> clz = ReflectAnnotationTest.class;

        //获取clz的 注释
        Annotation[] annotations = clz.getAnnotations();
        Arrays.stream(annotations).forEach(System.out::println);

        //获取package 注释
        Class<?> packageInfo = Class.forName("com.mocha.springboot.MyAnnotationsReflectionTest.package-info");
        Annotation[] packageAnnotations = packageInfo.getAnnotations();
        Arrays.stream(packageAnnotations).forEach(System.out::println);

        //通过Class对象，获取指定注解
        AnyAnnotation anyAnnotation = clz.getAnnotation(AnyAnnotation.class);
        System.out.println(anyAnnotation.description());
        System.out.println(anyAnnotation.order());

        System.out.println(anyAnnotation.getClass());
//        while(true){
//
//        }
    }
}

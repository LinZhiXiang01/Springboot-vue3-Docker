package com.mocha.springboot.MyAnnotationsReflectionTest;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE,ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,
        ElementType.CONSTRUCTOR,ElementType.LOCAL_VARIABLE,ElementType.PARAMETER })
public @interface AnyAnnotation {
    int order() default 0;
    String description() default "description";
}

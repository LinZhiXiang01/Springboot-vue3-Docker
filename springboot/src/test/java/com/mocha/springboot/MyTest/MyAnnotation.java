package com.mocha.springboot.MyTest;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD })
@Inherited
public @interface MyAnnotation {
    String value() default "default";

}

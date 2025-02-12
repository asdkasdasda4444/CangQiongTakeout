package com.sky.annotation;

import  com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行自动填充处理
 */
//指定注解可以应用于的目标元素类型
@Target(ElementType.METHOD)
//指定注解的保留策略，即注解在什么级别保留，是源代码级别、类文件级别还是运行时级别
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

//    在mapper层：@AutoFill(value= OperationType.INSERT)
//               value可以自定义但是value只能等于OperationType中的枚举
    OperationType value();
}

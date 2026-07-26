package com.asa.anno;

import com.asa.vaildation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented //元注解
@Target(FIELD) //元注解，自定义注解可以使用的范围
@Retention(RUNTIME) //元注解，自定义注解在哪个阶段被保留
@Constraint(validatedBy = {StateValidation.class}) //指定提供校验规则的类
public @interface State {
    //提供校验失败后的提示信息
    String message() default "State参数的值只能是‘已发布’或‘草稿’";

    //指定分组
    Class<?>[] groups() default { };

    //负载，提供自定义注解的附加信息
    Class<? extends Payload>[] payload() default { };
}

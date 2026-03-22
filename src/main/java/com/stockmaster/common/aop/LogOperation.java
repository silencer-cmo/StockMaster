package com.stockmaster.common.aop;

import com.stockmaster.common.enums.OperationType;
import com.stockmaster.common.security.SecurityUtils;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    OperationType value();
    String module() default "";
    String description() default "";
}

package com.asa.vaildation;

import com.asa.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State, String> { //<起作用的注解，起作用注解的数据类型>
    //提供校验规则
    /**
    *
    * @param value 将来要校验的数据
     *
     * @return 如果返回false则校验不通过，如果返回true则校验通过
    * */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }

        if(value.equals("已发布") || value.equals("草稿")) {
            return true;
        }

        return false;
    }
}

package org.mju_likelion.festival.common.util.annotaion.page_size;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageSize {

  String message() default "페이지 크기는 1보다 크거나 같아야 합니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
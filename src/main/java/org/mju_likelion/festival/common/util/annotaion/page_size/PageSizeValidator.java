package org.mju_likelion.festival.common.util.annotaion.page_size;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageSizeValidator implements ConstraintValidator<PageSize, Integer> {

  @Override
  public void initialize(PageSize constraintAnnotation) {
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return value >= 1;
  }
}

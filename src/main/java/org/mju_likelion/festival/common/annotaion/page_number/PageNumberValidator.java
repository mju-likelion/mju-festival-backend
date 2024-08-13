package org.mju_likelion.festival.common.annotaion.page_number;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageNumberValidator implements ConstraintValidator<PageNumber, Integer> {

  @Override
  public void initialize(PageNumber constraintAnnotation) {
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return value >= 0;
  }
}

package org.fuelteam.watt.lucky.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
public @interface NotNull {}
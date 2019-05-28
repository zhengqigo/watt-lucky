package org.fuelteam.watt.lucky.validate;

import org.apache.commons.lang3.StringUtils;

public interface Validator<T> {

    boolean validate(T value);

    Validator<Integer> INTEGER_GT_ZERO_VALIDATOR = new Validator<Integer>() {
        @Override
        public boolean validate(Integer value) {
            return (value != null && value > 0);
        }
    };

    Validator<String> STRING_EMPTY_VALUE_VALIDATOR = new Validator<String>() {
        @Override
        public boolean validate(String value) {
            return StringUtils.isNotEmpty(value);
        }
    };

    Validator<String> STRICT_BOOL_VALUE_VALIDATOR = new Validator<String>() {
        @Override
        public boolean validate(String value) {
            return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
        }
    };
}
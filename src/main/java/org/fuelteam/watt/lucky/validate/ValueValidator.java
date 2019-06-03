package org.fuelteam.watt.lucky.validate;

public class ValueValidator {

    /**
     * 校验目标值, 并根据校验结果取值, 使用示例(校验目标值是否大于0, 如果小于0则取值1)
     * <pre>
     * ValueValidator.checkAndGet(idleTime, 1, Validator.INTEGER_GT_ZERO_VALIDATOR)
     * <pre>
     * @param value 校验值
     * @param defaultValue 校验失败默认值
     * @param v 校验器
     * @return 校验后的返回值, 校验成功返回value, 校验失败返回defaultValue
     */
    public static <T> T checkAndGet(T value, T defaultValue, Validator<T> validator) {
        if (validator.validate(value)) return value;
        return defaultValue;
    }
}
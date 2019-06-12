package org.fuelteam.watt.lucky.text;

/**
  *  @see StringBuilder在高性能场景下的正确用法http://calvin1978.blogcn.com/articles/stringbuilder.html
  */
public class StringBuilderHolder {

    // 全局公共的ThreadLocal StringBuilder
    private static ThreadLocal<StringBuilder> globalStringBuilder = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(512);
        }
    };

    // 独立创建的ThreadLocal的StringBuilder
    private ThreadLocal<StringBuilder> stringBuilder = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(initSize);
        }
    };

    private int initSize;

    public StringBuilderHolder(int initSize) {
        this.initSize = initSize;
    }

    public static StringBuilder getGlobal() {
        StringBuilder sb = globalStringBuilder.get();
        sb.setLength(0);
        return sb;
    }

    public StringBuilder get() {
        StringBuilder sb = stringBuilder.get();
        sb.setLength(0);
        return sb;
    }
}
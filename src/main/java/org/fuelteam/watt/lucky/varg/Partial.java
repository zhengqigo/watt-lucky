package org.fuelteam.watt.lucky.varg;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public interface Partial {

    static <R, T> VArgFunction<R, T> vargFunction(VArgFunction<R, T> function) {

        return new VArgFunction<R, T>() {

            private final List<T> ARGS = Lists.newLinkedList();

            @SuppressWarnings("unchecked")
            @Override
            public R apply(T... args) {
                ARGS.addAll(Arrays.asList(args));
                final T[] argsArray = (T[]) Array.newInstance(args.getClass().getComponentType(), args.length);
                return function.apply(ARGS.toArray(argsArray));
            }

            @Override
            public VArgFunction<R, T> arg(T arg) {
                ARGS.add(arg);
                return this;
            }
        };
    }

    static <T> VArgConsumer<T> vargConsumer(VArgConsumer<T> consumer) {

        return new VArgConsumer<T>() {

            private final List<T> ARGS = Lists.newLinkedList();

            @SuppressWarnings("unchecked")
            @Override
            public void apply(T... args) {
                ARGS.addAll(Arrays.asList(args));
                final T[] argsArray = (T[]) Array.newInstance(args.getClass().getComponentType(), args.length);
                consumer.apply(ARGS.toArray(argsArray));
            }

            @Override
            public VArgConsumer<T> arg(T arg) {
                ARGS.add(arg);
                return this;
            }
        };
    }
}
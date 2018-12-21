package org.fuelteam.watt.lucky.varg;

@FunctionalInterface
public interface VArgFunction<R, T> {

    @SuppressWarnings("unchecked")
    R apply(T... args);

    @SuppressWarnings("unchecked")
    default VArgFunction<R, T> arg(T arg) {
        return (T... args) -> {
            final T[] arguments = (T[]) new Object[args.length + 1];
            arguments[0] = arg;
            System.arraycopy(args, 0, arguments, 1, args.length);
            return apply(arguments);
        };
    }
}
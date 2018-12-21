package org.fuelteam.watt.lucky.varg;

@FunctionalInterface
public interface VArgConsumer<T> {

    @SuppressWarnings("unchecked")
    void apply(T... args);

    @SuppressWarnings("unchecked")
    default VArgConsumer<T> arg(T arg) {
        return (T... args) -> {
            final T[] arguments = (T[]) new Object[args.length + 1];
            arguments[0] = arg;
            System.arraycopy(args, 0, arguments, 1, args.length);
            apply(arguments);
        };
    }
}
package org.fuelteam.watt.lucky.jsr166e;

import java.util.Random;

/**
 * @see http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/Striped64.java Revision 1.10
 */
@SuppressWarnings("restriction")
public abstract class Striped64 extends Number {
    private static final long serialVersionUID = 305432553211711227L;

    static final class Cell {
        volatile long p0, p1, p2, p3, p4, p5, p6;
        volatile long value;
        volatile long q0, q1, q2, q3, q4, q5, q6;

        Cell(long x) {
            value = x;
        }

        final boolean cas(long cmp, long val) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
        }

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long valueOffset;
        static {
            try {
                UNSAFE = getUnsafe();
                Class<?> ak = Cell.class;
                valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }

    }

    static final ThreadLocal<int[]> threadHashCode = new ThreadLocal<int[]>();

    static final Random rng = new Random();

    static final int NCPU = Runtime.getRuntime().availableProcessors();

    transient volatile Cell[] cells;

    transient volatile long base;

    transient volatile int busy;

    Striped64() {}

    final boolean casBase(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    abstract long fn(long currentValue, long newValue);

    final void retryUpdate(long x, int[] hc, boolean wasUncontended) {
        int h;
        if (hc == null) {
            threadHashCode.set(hc = new int[1]); // Initialize randomly
            int r = rng.nextInt(); // Avoid zero to allow xorShift rehash
            h = hc[0] = (r == 0) ? 1 : r;
        } else
            h = hc[0];
        boolean collide = false; // True if last slot nonempty
        for (;;) {
            Cell[] as;
            Cell a;
            int n;
            long v;
            if ((as = cells) != null && (n = as.length) > 0) {
                if ((a = as[(n - 1) & h]) == null) {
                    if (busy == 0) { // Try to attach new Cell
                        Cell r = new Cell(x); // Optimistically create
                        if (busy == 0 && casBusy()) {
                            boolean created = false;
                            try { // Recheck under lock
                                Cell[] rs;
                                int m, j;
                                if ((rs = cells) != null && (m = rs.length) > 0 && rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;
                                    created = true;
                                }
                            } finally {
                                busy = 0;
                            }
                            if (created) break;
                            continue; // Slot is now non-empty
                        }
                    }
                    collide = false;
                } else if (!wasUncontended) // CAS already known to fail
                    wasUncontended = true; // Continue after rehash
                else if (a.cas(v = a.value, fn(v, x)))
                    break;
                else if (n >= NCPU || cells != as)
                    collide = false; // At max size or stale
                else if (!collide)
                    collide = true;
                else if (busy == 0 && casBusy()) {
                    try {
                        if (cells == as) { // Expand table unless stale
                            Cell[] rs = new Cell[n << 1];
                            for (int i = 0; i < n; ++i)
                                rs[i] = as[i];
                            cells = rs;
                        }
                    } finally {
                        busy = 0;
                    }
                    collide = false;
                    continue; // Retry with expanded table
                }
                h ^= h << 13; // Rehash
                h ^= h >>> 17;
                h ^= h << 5;
                hc[0] = h; // Record index for next time
            } else if (busy == 0 && cells == as && casBusy()) {
                boolean init = false;
                try { // Initialize table
                    if (cells == as) {
                        Cell[] rs = new Cell[2];
                        rs[h & 1] = new Cell(x);
                        cells = rs;
                        init = true;
                    }
                } finally {
                    busy = 0;
                }
                if (init) break;
            } else if (casBase(v = base, fn(v, x))) break; // Fall back on using base
        }
    }

    final void internalReset(long initialValue) {
        Cell[] as = cells;
        base = initialValue;
        if (as != null) {
            int n = as.length;
            for (int i = 0; i < n; ++i) {
                Cell a = as[i];
                if (a != null) a.value = initialValue;
            }
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static {
        try {
            UNSAFE = getUnsafe();
            Class<?> sk = Striped64.class;
            baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static sun.misc.Unsafe getUnsafe() {
        try {
            return sun.misc.Unsafe.getUnsafe();
        } catch (SecurityException tryReflectionInstead) {}
        try {
            return java.security.AccessController.doPrivileged(new java.security.PrivilegedExceptionAction<sun.misc.Unsafe>() {
                public sun.misc.Unsafe run() throws Exception {
                    Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;
                    for (java.lang.reflect.Field f : k.getDeclaredFields()) {
                        f.setAccessible(true);
                        Object x = f.get(null);
                        if (k.isInstance(x)) return k.cast(x);
                    }
                    throw new NoSuchFieldError("the Unsafe");
                }
            });
        } catch (java.security.PrivilegedActionException e) {
            throw new RuntimeException("Could not initialize intrinsics", e.getCause());
        }
    }
}
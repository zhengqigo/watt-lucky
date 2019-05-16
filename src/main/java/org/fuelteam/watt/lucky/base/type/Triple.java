package org.fuelteam.watt.lucky.base.type;

import java.io.Serializable;

import org.fuelteam.watt.lucky.base.annotation.Nullable;

/**
 * 从Twitter Common移植的简单的Triple，用于返回值返回三个元素。
 */
public class Triple<L, M, R> implements Serializable {

    private static final long serialVersionUID = -4187201641839757403L;

    @Nullable
    private final L left;

    @Nullable
    private final M middle;

    @Nullable
    private final R right;

    public Triple(@Nullable L left, @Nullable M middle, @Nullable R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Nullable
    public L getLeft() {
        return left;
    }

    @Nullable
    public M getMiddle() {
        return middle;
    }

    @Nullable
    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((middle == null) ? 0 : middle.hashCode());
        return prime * result + ((right == null) ? 0 : right.hashCode());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Triple other = (Triple) obj;
        if (left == null && other.left != null) return false;
        if (left != null && !left.equals(other.left)) return false;
        if (middle == null && other.middle != null) return false;
        if (middle != null && !middle.equals(other.middle)) return false;
        if (right == null && other.right != null) return false;
        if (right != null && !right.equals(other.right)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Triple [left=" + left + ", middle=" + middle + ", right=" + right + ']';
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Triple
     */
    public static <L, M, R> Triple<L, M, R> of(@Nullable L left, @Nullable M middle, @Nullable R right) {
        return new Triple<L, M, R>(left, middle, right);
    }
}
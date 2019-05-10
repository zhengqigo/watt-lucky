package org.fuelteam.watt.lucky.base.type;

import java.io.Serializable;

import org.fuelteam.watt.lucky.base.annotation.Nullable;

/**
 * 引入一个简单的Pair，用于返回值返回两个元素
 * <BR>
 * copy from Twitter Common
 */
public class Pair<L, R> implements Serializable{

    private static final long serialVersionUID = 988170472238117297L;

    @Nullable
    private final L left;

    @Nullable
    private final R right;

    public Pair(@Nullable L left, @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    @Nullable
    public L getLeft() {
        return left;
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
        return prime * result + ((right == null) ? 0 : right.hashCode());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair other = (Pair) obj;
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }
        if (right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!right.equals(other.right)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + ']';
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Pair
     */
    public static <L, R> Pair<L, R> of(@Nullable L left, @Nullable R right) {
        return new Pair<L, R>(left, right);
    }
}
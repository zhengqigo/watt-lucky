package org.fuelteam.watt.lucky.collection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue和Deque工具类
 */
public class QueueUtil {

    /**
     * @see ArrayDeque
     */
    public static <E> ArrayDeque<E> newArrayDeque(int initSize) {
        return new ArrayDeque<E>(initSize);
    }

    /**
     * @see LinkedList
     */
    public static <E> LinkedList<E> newLinkedDeque() {
        return new LinkedList<E>();
    }

    /**
     * 无阻塞性能最优的并发队列
     * @see ConcurrentLinkedQueue
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentNonBlockingQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    /**
     * 无阻塞性能最优的并发双端队列
     * @see ConcurrentLinkedDeque
     */
    public static <E> Deque<E> newConcurrentNonBlockingDeque() {
        return new ConcurrentLinkedDeque<E>();
    }

    /**
     * 并发阻塞、长度不受限队列，即生产者不会因为满而阻塞，但消费者会因为空而阻塞
     * @see LinkedBlockingQueue
     */
    public static <E> LinkedBlockingQueue<E> newBlockingUnlimitQueue() {
        return new LinkedBlockingQueue<E>();
    }

    /**
     * 并发阻塞、长度不受限的双端队列，即生产者不会因为满而阻塞，但消费者会因为空而阻塞
     * @see LinkedBlockingDeque
     */
    public static <E> LinkedBlockingDeque<E> newBlockingUnlimitDeque() {
        return new LinkedBlockingDeque<E>();
    }

    /**
     * 并发阻塞、长度受限、共用一把锁的队列(无双端队列实现)，节约内存
     * @see ArrayBlockingQueue
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue<E>(capacity);
    }

    /**
     * 并发阻塞、长度受限、头队尾两把锁的队列，使用内存较多
     * @see LinkedBlockingQueue
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
        return new LinkedBlockingQueue<E>(capacity);
    }

    /**
     * 并发阻塞、长度受限、头队尾两把锁的双端队列，使用内存较多
     * @see LinkedBlockingDeque
     */
    public static <E> LinkedBlockingDeque<E> newBlockingDeque(int capacity) {
        return new LinkedBlockingDeque<E>(capacity);
    }
}
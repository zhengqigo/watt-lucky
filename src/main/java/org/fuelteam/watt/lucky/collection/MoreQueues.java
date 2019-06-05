package org.fuelteam.watt.lucky.collection;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

import org.fuelteam.watt.lucky.utils.QueueUtil;

import com.google.common.collect.EvictingQueue;

public class MoreQueues {

	/**
	 * 基于ArrayDeque的Stack, 初始默认16满时成倍扩容
	 * 
	 * @see Collections#asLifoQueue()
	 */
	public static <E> Queue<E> createStack(int initSize) {
		return Collections.asLifoQueue(new ArrayDeque<E>(initSize));
	}

	/**
	 * 基于ConcurrentLinkedDeque的无阻塞并发Stack 
	 * 
	 * @see Collections#asLifoQueue()
	 */
	@SuppressWarnings("unchecked")
    public static <E> Queue<E> createConcurrentStack() {
		return (Queue<E>) Collections.asLifoQueue(QueueUtil.newConcurrentNonBlockingDeque());
	}

	/**
	 * 基于ArrayDeque的LRUQueue, 若Queue已满则删除最旧的元素
	 */
	public static <E> EvictingQueue<E> createLRUQueue(int maxSize) {
		return EvictingQueue.create(maxSize);
	}
}
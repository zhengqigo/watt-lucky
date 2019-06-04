package org.fuelteam.watt.lucky.collection;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

import org.fuelteam.watt.lucky.utils.QueueUtil;

import com.google.common.collect.EvictingQueue;

public class MoreQueues {

	/**
	 * ArrayDeque实现的后进先出栈, 经过Collections#asLifoQueue()转换顺序, 需设置初始长度(默认16), 数组满时成倍扩容
	 * 
	 * @see Collections#asLifoQueue()
	 */
	public static <E> Queue<E> createStack(int initSize) {
		return Collections.asLifoQueue(new ArrayDeque<E>(initSize));
	}

	/**
	 * ConcurrentLinkedDeque实现的后进先出的无阻塞并发栈, 经过Collections#asLifoQueue()转换顺序, 对于BlockingQueue接口, 
	 * JDK无LIFO倒转实现, 只能直接使用未调转顺序的LinkedBlockingDeque
	 * 
	 * @see Collections#asLifoQueue()
	 */
	@SuppressWarnings("unchecked")
    public static <E> Queue<E> createConcurrentStack() {
		return (Queue<E>) Collections.asLifoQueue(QueueUtil.newConcurrentNonBlockingDeque());
	}

	/**
	 * LRUQueue, 如果Queue已满则删除最旧的元素, 内部实现是ArrayDeque
	 */
	public static <E> EvictingQueue<E> createLRUQueue(int maxSize) {
		return EvictingQueue.create(maxSize);
	}
}
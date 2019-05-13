package org.fuelteam.watt.lucky.collection.type;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

import org.fuelteam.watt.lucky.collection.QueueUtil;

import com.google.common.collect.EvictingQueue;

/**
 * LIFO的Stack和LRU的Queue
 */
public class MoreQueues {

	/**
	 * 支持后进先出的栈，用ArrayDeque实现，经过Collections#asLifoQueue()转换顺序，需设置初始长度，默认为16，数组满时成倍扩容
	 * 
	 * @see Collections#asLifoQueue()
	 */
	public static <E> Queue<E> createStack(int initSize) {
		return Collections.asLifoQueue(new ArrayDeque<E>(initSize));
	}

	/**
	 * 支持后进先出的无阻塞的并发栈，用ConcurrentLinkedDeque实现，经过Collections#asLifoQueue()转换顺序
	 * 
	 * 另对于BlockingQueue接口，JDK暂无Lifo倒转实现，因此只能直接使用未调转顺序的LinkedBlockingDeque
	 * 
	 * @see Collections#asLifoQueue()
	 */
	@SuppressWarnings("unchecked")
    public static <E> Queue<E> createConcurrentStack() {
		return (Queue<E>) Collections.asLifoQueue(QueueUtil.newConcurrentNonBlockingDeque());
	}

	/**
	 * LRUQueue, 如果Queue已满，则删除最旧的元素，内部实现是ArrayDeque
	 */
	public static <E> EvictingQueue<E> createLRUQueue(int maxSize) {
		return EvictingQueue.create(maxSize);
	}
}
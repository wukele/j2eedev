package com.starit.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 类说明：利用LinkedHashMap实现简单的缓存， 必须实现removeEldestEntry方法
 * 
 * 通过双向链表来实现，当某个位置被命中，通过调整链表的指向将该位置调整到头位置，新加入的内容直接放在链表头，
 * 如此一来，最近被命中的内容就向链表头移动，需要替换时，链表最后的位置就是最近最少使用的位置。
 * 
 * @author bsli
 * 
 * @param <K>
 * @param <V>
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxCapacity;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public LRULinkedHashMap(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }
    @Override
    public boolean containsKey(Object key) {
        try {
        	readLock.lock();
            return super.containsKey(key);
        } finally {
        	readLock.unlock();
        }
    }

    
    @Override
    public V get(Object key) {
        try {
        	readLock.lock();
            return super.get(key);
        } finally {
        	readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            writeLock.lock();
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        try {
        	readLock.lock();
            return super.size();
        } finally {
        	readLock.unlock();
        }
    }

    public void clear() {
        try {
        	writeLock.lock();
            super.clear();
        } finally {
        	writeLock.unlock();
        }
    }

    public Collection<Map.Entry<K, V>> getAll() {
        try {
        	readLock.lock();
            return new ArrayList<Map.Entry<K, V>>(super.entrySet());
        } finally {
        	readLock.unlock();
        }
    }
}
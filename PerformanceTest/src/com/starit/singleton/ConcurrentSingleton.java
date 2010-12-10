package com.starit.singleton;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ����putIfAbsent�̰߳�ȫ������ʵ�ֵ���ģʽ
 * 
 * @author Administrator
 * 
 */
public class ConcurrentSingleton {
	private static final ConcurrentMap<String, ConcurrentSingleton> map = new ConcurrentHashMap<String, ConcurrentSingleton>();
	private static ConcurrentSingleton instance;

	public static ConcurrentSingleton getInstance() {
		if (instance == null) {
			instance = map.putIfAbsent("INSTANCE", new ConcurrentSingleton());
		}
		return instance;
	}

	private ConcurrentSingleton() {
	}
}

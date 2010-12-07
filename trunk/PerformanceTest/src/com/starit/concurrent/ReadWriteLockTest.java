package com.starit.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试读写锁分离的性能
 * 测试结果大致相差24倍
 * 
 * @author bsli 2010-12-07
 *
 */
public class ReadWriteLockTest {
	private static Map<String, String> dataMap = new HashMap<String, String>();
	private static CyclicBarrier barrier = new CyclicBarrier(102);
	private static CountDownLatch downLatch = new CountDownLatch(102);
	
	//读写锁分离
//	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//	private static Lock read = readWriteLock.readLock();   
//	private static Lock write = readWriteLock.writeLock();   
	
	private static ReentrantLock read = new ReentrantLock();   
	private static ReentrantLock write = read;  
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int i=0; i<2; i++) {
			Writer writer = new Writer();
			writer.start();
		}
		
		for(int i=0; i<100; i++) {
			Reader reader = new Reader();
			reader.start();
		}
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Total times: " + (System.currentTimeMillis() - start));
	}
	
	private static class Reader extends Thread {

		public void run() {
			try {
				barrier.await();
			}catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				read.lock();
				dataMap.get("key");
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				read.unlock();
			}
			
			downLatch.countDown();
		}
	}
	
	private static class Writer extends Thread {

		public void run() {
			try {
				barrier.await();
			}catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				write.lock();
				dataMap.put("key", "value");
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				write.unlock();
			}
			
			downLatch.countDown();
		}
	}
}

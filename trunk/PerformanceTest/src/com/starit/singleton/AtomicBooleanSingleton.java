package com.starit.singleton;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanSingleton {
	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static AtomicBooleanSingleton instance;
	
	public static AtomicBooleanSingleton getInstantce() {   
		checkInitialized();
        return instance;   
    }
	
	private static void checkInitialized() {
		if(instance == null && initialized.compareAndSet(false, true)) {
			instance = new AtomicBooleanSingleton();
		}
	}
}

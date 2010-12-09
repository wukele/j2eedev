package com.starit.concurrent;

/**
 * DCL 减少锁的次数
 * 
 * @author Administrator
 *
 */
public class LazySingleton {   
    private static volatile LazySingleton instance;   
       
    public static LazySingleton getInstantce() {   
        if (instance == null) {   
            synchronized (LazySingleton.class) {   
                if (instance == null) {   
                    instance = new LazySingleton();   
                }   
            }   
        }   
        return instance;   
    }   
}  
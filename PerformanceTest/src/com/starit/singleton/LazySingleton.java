package com.starit.singleton;

/**
 * DCL �������Ĵ���
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
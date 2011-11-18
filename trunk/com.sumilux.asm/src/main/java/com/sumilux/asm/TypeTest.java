package com.sumilux.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

public class TypeTest {
	public static void main(String[] args) {
		System.out.println(Type.INT_TYPE.getDescriptor());
		
		System.out.println(Type.getType(String.class).getInternalName());
		
		System.out.println(Type.getType(String.class).getDescriptor());
		
		System.out.println(Type.getArgumentTypes("(I)V"));
		
	}
}

package com.sumilux.asm.test;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifierClassVisitor;

public class ASMifierClassVisitorTest {
	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader(User.class.getName());
		
		reader.accept(new ASMifierClassVisitor(new PrintWriter(System.out)), 0);
	}
}	

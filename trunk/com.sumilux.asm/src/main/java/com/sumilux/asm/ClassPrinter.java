package com.sumilux.asm;

import java.io.IOException;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassPrinter implements ClassVisitor {
	
	public static void main(String[] args) throws IOException {
		ClassReader classReader = new ClassReader("java.lang.Runnable");
		
		classReader.accept(new ClassPrinter(), 0);
	}

	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		System.out.println(name + " extends " + superName + " {");
	}

	public void visitSource(String source, String debug) {
		// TODO Auto-generated method stub
		
	}

	public void visitOuterClass(String owner, String name, String desc) {
		// TODO Auto-generated method stub
		
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		// TODO Auto-generated method stub
		return null;
	}

	public void visitAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		
	}

	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		// TODO Auto-generated method stub
		
	}

	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.println("  " + name + " " + desc);
		return null;
	}

	public void visitEnd() {
		System.out.println("}");
	}

}

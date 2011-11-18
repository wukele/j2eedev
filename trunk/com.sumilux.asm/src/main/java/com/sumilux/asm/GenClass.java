package com.sumilux.asm;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassWriter;

public class GenClass {
	public static void main(String[] args) throws IOException {
		ClassWriter classWriter = new ClassWriter(0);

		classWriter.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, 
				"com/sumilux/GenClassInterface", null, "java/lang/Object", null);
		
		classWriter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, 1).visitEnd();
		
		classWriter.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
		
		FileUtils.writeByteArrayToFile(new File("GenClassInterface.class"), classWriter.toByteArray());
	}
}

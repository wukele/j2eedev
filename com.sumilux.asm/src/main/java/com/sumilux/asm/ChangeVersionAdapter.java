package com.sumilux.asm;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class ChangeVersionAdapter extends ClassAdapter {
	public static void main(String[] args) throws IOException {
		ClassWriter classWriter = new ClassWriter(0);
		ChangeVersionAdapter adapter = new ChangeVersionAdapter(classWriter);
		
		ClassReader classReader = new ClassReader(FileUtils.readFileToByteArray(new File("GenClassInterface.class")));
		
		classReader.accept(adapter, 0);
		
		FileUtils.writeByteArrayToFile(new File("GenClassInterface1.class"), classWriter.toByteArray());
	}

	public ChangeVersionAdapter(ClassVisitor cv) {
		super(cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		cv.visit(V1_6, access, name + "1", signature, superName, interfaces);
	}
}

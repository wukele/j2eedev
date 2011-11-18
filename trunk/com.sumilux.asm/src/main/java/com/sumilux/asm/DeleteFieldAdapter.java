package com.sumilux.asm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

public class DeleteFieldAdapter extends ClassAdapter {
	public static void main(String[] args) throws IOException {
		ClassReader classReader = new ClassReader(FileUtils.readFileToByteArray(new File("GenClassInterface.class")));
		
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		DeleteFieldAdapter adapter = new DeleteFieldAdapter(classWriter);
		
		classReader.accept(adapter, 0);
		
		FileUtils.writeByteArrayToFile(new File("GenClassInterface1.class"), classWriter.toByteArray());
	}

	public DeleteFieldAdapter(ClassVisitor cv) {
		super(cv);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		if("LESS".equals(name))
			return null;
		else
			return super.visitField(access, name, desc, signature, value);
	}
}

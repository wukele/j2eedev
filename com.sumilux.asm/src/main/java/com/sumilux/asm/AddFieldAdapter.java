package com.sumilux.asm;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.sun.org.apache.bcel.internal.generic.CHECKCAST;

public class AddFieldAdapter extends ClassAdapter {
	private int fAcc;
	private String fName;
	private String fDesc;
	private boolean isFieldPresent;

	public AddFieldAdapter(ClassVisitor cv, int fAcc, String fName, String fDesc) {
		super(cv);
		this.fAcc = fAcc;
		this.fName = fName;
		this.fDesc = fDesc;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		if(fName.equals(name))
			isFieldPresent = true;

		return super.visitField(access, name, desc, signature, value);
	}
	
	@Override
	public void visitEnd() {
		if(!isFieldPresent) {
			FieldVisitor fieldVisitor = cv.visitField(fAcc, fName, fDesc, null, null);
			
			if(fieldVisitor != null)
				fieldVisitor.visitEnd();
		}
		
		super.visitEnd();
	} 
	
	public static void main(String[] args) throws IOException {
		ClassReader classReader = new ClassReader(FileUtils.readFileToByteArray(new File("GenClassInterface.class")));
		
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		AddFieldAdapter adapter = new AddFieldAdapter(classWriter, ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "NAME", "Ljava/lang/String;");
		
		TraceClassVisitor traceClassVisitor = new TraceClassVisitor(adapter, new PrintWriter(System.out));
		CheckClassAdapter checkClassAdapter = new CheckClassAdapter(traceClassVisitor);
		classReader.accept(checkClassAdapter, 0);
		
		FileUtils.writeByteArrayToFile(new File("GenClassInterface.class"), classWriter.toByteArray());
	}
}

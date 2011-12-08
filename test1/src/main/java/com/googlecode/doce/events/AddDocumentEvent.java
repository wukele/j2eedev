package com.googlecode.doce.events;

import org.springframework.context.ApplicationEvent;

import com.googlecode.doce.model.Node;

@SuppressWarnings("serial")
public class AddDocumentEvent extends ApplicationEvent {
	private String fileCode;
	
	public AddDocumentEvent(Node node, String fileCode) {
		super(node);
		
		this.fileCode = fileCode;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

}

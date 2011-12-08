package com.googlecode.doce.events;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DeleteDocumentEvent extends ApplicationEvent {
	private String fileCode;

	public DeleteDocumentEvent(String nodeId, String fileCode) {
		super(nodeId);
		
		this.fileCode = fileCode;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
}

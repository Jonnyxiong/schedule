package com.ucpaas.sms.exception;

public class SmspBusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -8957766121835731531L;

	public SmspBusinessException() {  
		super();  
	} 
	
	public SmspBusinessException(String msg) {
		super(msg);
	}

	public SmspBusinessException(String msg, Throwable cause) {  
		super(msg, cause);  
	} 
}

package edu.wpi.cs.heineman.calculator.http;

/** Sends back the name of the constant deleted -- easier to handle on client-side. */
public class DeleteConstantResponse {
	public final String name;
	public final int statusCode;
	public final String error;
	
	public DeleteConstantResponse (String name, int statusCode) {
		this.name = name;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	// 200 means success
	public DeleteConstantResponse (String name, int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.error = errorMessage;
		this.name = name;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "DeleteResponse(" + name + ")";
		} else {
			return "ErrorResult(" + name + ", statusCode=" + statusCode + ", err=" + error + ")";
		}
	}
}

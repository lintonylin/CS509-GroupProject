package edu.wpi.cs.heineman.calculator.http;

public class CreateConstantRequest {
	public final String name;
	public final String base64EncodedValue;
	
	public CreateConstantRequest (String n, String encoding) {
		this.name = n;
		this.base64EncodedValue = encoding;
	}
	
	public String toString() {
		return "CreateConstant(" + name + "," + base64EncodedValue + ")";
	}
}

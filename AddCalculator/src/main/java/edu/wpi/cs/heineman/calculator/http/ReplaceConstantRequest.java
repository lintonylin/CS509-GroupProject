package edu.wpi.cs.heineman.calculator.http;

public class ReplaceConstantRequest {
	public final String name;
	public final String value;
	
	public ReplaceConstantRequest (String n, String value) {
		this.name = n;
		this.value = value;
	}
	
	public String toString() {
		return "ReplaceConstant(" + name + "," + value + ")";
	}
}

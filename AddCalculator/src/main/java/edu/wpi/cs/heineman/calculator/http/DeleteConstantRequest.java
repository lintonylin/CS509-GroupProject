package edu.wpi.cs.heineman.calculator.http;

public class DeleteConstantRequest {
	public final String name;
	
	public DeleteConstantRequest (String n) {
		this.name = n;
	}
	
	public String toString() {
		return "Delete(" + name + ")";
	}
}

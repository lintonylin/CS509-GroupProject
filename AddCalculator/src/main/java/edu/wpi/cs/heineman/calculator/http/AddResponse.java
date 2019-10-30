package edu.wpi.cs.heineman.calculator.http;

/**
 * Arbitrary decision to make this a String and not a native double.
 * @author heineman
 */
public class AddResponse {
	public final String result;
	public final int statusCode;
	public final String error;
	
	public AddResponse (double value, int statusCode) {
		this.result = "" + value; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddResponse (int statusCode, String errorMessage) {
		this.result = ""; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (statusCode / 100 == 2) {  // too cute?
			return "Result(" + result + ")";
		} else {
			return "ErrorResult(" + statusCode + ", err=" + error + ")";
		}
	}
}

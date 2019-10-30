package edu.wpi.cs.heineman.calculator.http;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.heineman.calculator.model.Constant;

public class AllConstantsResponse {
	public final List<Constant> list;
	public final int statusCode;
	public final String error;
	
	public AllConstantsResponse (List<Constant> list, int code) {
		this.list = list;
		this.statusCode = code;
		this.error = "";
	}
	
	public AllConstantsResponse (int code, String errorMessage) {
		this.list = new ArrayList<Constant>();
		this.statusCode = code;
		this.error = errorMessage;
	}
	
	public String toString() {
		if (list == null) { return "EmptyConstants"; }
		return "AllConstants(" + list.size() + ")";
	}
}

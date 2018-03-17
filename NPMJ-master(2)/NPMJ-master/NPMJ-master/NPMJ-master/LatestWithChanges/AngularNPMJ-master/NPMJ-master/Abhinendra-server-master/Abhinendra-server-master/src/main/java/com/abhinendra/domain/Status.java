package com.abhinendra.domain;

public class Status {
	public String fieldValidFail; 
	public String fieldValidPass;
	public String statusValidFail;
	public String statusValidPass; 
	public String uploadPass;
	public String uploadFail;
	public Status()
	{
		fieldValidFail = "field_validation_fail";
		fieldValidPass = "field_validation_pass";
		statusValidFail = "screen_validation_fail";
		statusValidPass = "screen_validation_pass";		
		uploadPass = "upload_pass";
		uploadFail = "upload_fail";
	}
}
